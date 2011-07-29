/*
 *  Copyright (C) 2011 apurv
 * 
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package nescent.phylogeoref.processor;

import java.awt.Color;
import static java.lang.System.out;
import java.math.BigDecimal;
import java.util.Vector;
import nescent.phylogeoref.processor.utility.ComputeUtility;
import nescent.phylogeoref.validator.PhylogenyValidator;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyMethods;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.BranchColor;
import org.forester.phylogeny.data.BranchData;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.data.NodeData;
import org.forester.phylogeny.iterators.PhylogenyNodeIterator;

/**
 * Processes this phylogeny and assigns color and lat/long values to internal nodes.
 * Also Validates the phylogeny.<br>
 * @author apurv
 */
public class PhylogenyProcessor {

    //To be used in the formula HTU height = a +(n-1)*b
    private final static long a = 198000;
    private final static long b =  66000;
    
    //Do not change this value, same value has also been used in KmlUtility.
    private final static double UNDEFINED = -1.0d;

    private PhylogenyValidator validator;
    private boolean weightedAvg;

    public PhylogenyProcessor(){
        this.weightedAvg = false;
        validator = new PhylogenyValidator(this.weightedAvg);        
    }
    
    public PhylogenyProcessor(boolean weightedAvg){
        this();
        this.weightedAvg = weightedAvg;
        validator = new PhylogenyValidator(this.weightedAvg);
    }


    /**
     * Phylogenifies the phylogeny
     * @param phylogeny
     */
    public void phylogenify(Phylogeny phylogeny){

        validator.validatePhylogeny(phylogeny);
        
        assignCoordinatesToInternalNodes(phylogeny);
        assignColorToInternalNodes(phylogeny);
    }

    /**
     * Assigns coordinates to internal nodes of the phylogeny.
     *
     * Most of this code is a copy of Kathryn's code from last year.(Calc3DTree.java)
     * @param phylogeny
     */
    private void assignCoordinatesToInternalNodes(Phylogeny phylogeny){

        int heightOfTree = PhylogenyMethods.calculateMaxDepth(phylogeny);

        for( PhylogenyNodeIterator it = phylogeny.iteratorPostorder(); it.hasNext();) {

            
            PhylogenyNode node = it.next();
            NodeData data = node.getNodeData();

            if ( node.isInternal() ) {

                data.setDistribution(new Distribution(node.getNodeName()));
                Distribution dist = data.getDistribution();

                double meanLat = findMeanChildLatitude(node);
                double meanLong = findMeanChildLongitude(node);
                                                
                dist.setLatitude(new BigDecimal(meanLat));
                dist.setLongitude(new BigDecimal(meanLong));

                int nodeDepth = PhylogenyMethods.calculateDepth(node);
                int n = heightOfTree - nodeDepth; //the height/level of node.

                long altitude = a + (n-1)*b;
                BigDecimal alt = new BigDecimal(altitude);
                dist.setAltitude(alt);                
            }
        }
    }


    /**
     * Finds the mean latitude of the children of this node.
     * Should be called only for internal nodes.
     * @param node
     * @return
     */
    private double findMeanChildLatitude(PhylogenyNode node){

        double meanLat = 0.0;
        int numValidChildren = 0;
        
        int numChildren = node.getNumberOfDescendants();
        
        Vector<Double> latVector = new Vector<Double>();

        for (int i=0; i < numChildren ; i++){
            PhylogenyNode childNode = node.getChildNode(i);
            NodeData childData = childNode.getNodeData();
            Distribution childDist = childData.getDistribution();

            BigDecimal childLat = BigDecimal.ZERO;

            if(childDist != null){
                childLat = childDist.getLatitude();
                
                if(childLat.doubleValue() != UNDEFINED){
                    numValidChildren++;
                    latVector.add(childLat.doubleValue());
                }
            }

        }
        
        if(numValidChildren == 0){
            meanLat = UNDEFINED;
        }else{
            meanLat = ComputeUtility.findMeanPosition(latVector);
        }

        return meanLat;
    }



    /**
     * Finds the mean longitude of the children of this node.
     * Should be called only for internal nodes.
     * @param node
     * @return
     */
    private double findMeanChildLongitude(PhylogenyNode node){

        double meanLon = 0.0;
        int numValidChildren = 0;
        
        int numChildren = node.getNumberOfDescendants();
        
        Vector<Double> lonVector = new Vector<Double>();

        for (int i=0; i < numChildren ; i++){
            PhylogenyNode childNode = node.getChildNode(i);
            NodeData childData = childNode.getNodeData();
            Distribution childDist = childData.getDistribution();

            BigDecimal childLon = BigDecimal.ZERO;

            if(childDist != null){
                childLon = childDist.getLongitude();
                
                if(childLon.doubleValue() != UNDEFINED){
                    numValidChildren++;
                    lonVector.add(childLon.doubleValue());
                }
            }

        }
        
        if(numValidChildren == 0){
            meanLon = UNDEFINED;
        }else{
            meanLon = ComputeUtility.findMeanPosition(lonVector);
        }

        return meanLon;
    }

    

    
    /**
     * Assigns color to internal edges of the phylogeny.
     * Color of parent branch is taken to be the arithmetic mean of colors of child branches.
     * @param phylogeny
     */
    private void assignColorToInternalNodes(Phylogeny phylogeny){
        
        for( PhylogenyNodeIterator it = phylogeny.iteratorPostorder(); it.hasNext();) {

            PhylogenyNode node = it.next();

            int numChildren = 0;
            int rSum = 0;
            int gSum = 0;
            int bSum = 0;

            if ( node.isInternal() ) {

                for (int i=0; i < node.getNumberOfDescendants(); i++){

                    PhylogenyNode childNode = node.getChildNode(i);
                    BranchColor childBranchColor = childNode.getBranchData().getBranchColor();

                    if(childBranchColor !=null){
                        Color childColor = childBranchColor.getValue();

                        rSum+=childColor.getRed();
                        gSum+=childColor.getGreen();
                        bSum+=childColor.getBlue();

                        numChildren++;
                    }                    
                }

                //TODO: Handle the case if num of children is zero.
                int meanR = rSum/numChildren;
                int meanG = gSum/numChildren;
                int meanB = bSum/numChildren;

                Color meanColor = new Color(meanR,meanG,meanB);
                BranchData branchData = node.getBranchData();
                branchData.setBranchColor(new BranchColor(meanColor));
            }   
        }
    }

}
