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
 * @author apurv
 */
public class PhylogenyProcessor {

    //To be used in the formula HTU height = a +(n-1)*b
    private final static long a = 792000;
    private final static long b = 264000;


    /**
     * Phylogenifies the phylogeny
     * @param phylogeny
     */
    public void phylogenify(Phylogeny phylogeny){

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

                double meanLat = 0.0;
                double meanLong = 0.0;

                double meanLat1 = findMeanChildLatitude(node);
                double meanLat2 = findDiametricallyOppositeAngle(meanLat1);

                double sumLat1 = findSumOfAngularDistancesToMeanLat(node, meanLat1);
                double sumLat2 = findSumOfAngularDistancesToMeanLat(node, meanLat2);

                if(sumLat1<sumLat2){
                    meanLat = meanLat1;
                }else{
                    meanLat = meanLat2;
                }             
                
                double meanLong1 = findMeanChildLongitude(node);
                double meanLong2 = findDiametricallyOppositeAngle(meanLong1);

                double sumLong1 = findSumOfAngularDistancesToMeanLong(node, meanLong1);
                double sumLong2 = findSumOfAngularDistancesToMeanLong(node, meanLong2);

                if(sumLong1<sumLong2){
                    meanLong = meanLong1;
                }else{
                    meanLong = meanLong2;
                }

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
        double latSum = 0;
        int numChildren = 0;

        for (int i=0; i < node.getNumberOfDescendants(); i++){
            PhylogenyNode childNode = node.getChildNode(i);
            NodeData childData = childNode.getNodeData();
            Distribution childDist = childData.getDistribution();

            BigDecimal childLat = BigDecimal.ZERO;

            if(childDist != null){
                childLat = childDist.getLatitude();
                numChildren++;
            }
            latSum+= childLat.doubleValue();

        }

        //TODO: Handle case for childCount=0 maybe because all children have missing location.
        meanLat = latSum/numChildren;

        return meanLat;
    }



    /**
     * Finds the mean longitude of the children of this node.
     * Should be called only for internal nodes.
     * @param node
     * @return
     */
    private double findMeanChildLongitude(PhylogenyNode node){

        double meanLong = 0.0;
        double longSum = 0;
        int numChildren = 0;

        for (int i=0; i < node.getNumberOfDescendants(); i++){
            PhylogenyNode childNode = node.getChildNode(i);
            NodeData childData = childNode.getNodeData();
            Distribution childDist = childData.getDistribution();

            BigDecimal childLong = BigDecimal.ZERO;

            if(childDist != null){
                childLong = childDist.getLongitude();
                numChildren++;
            }
            longSum+= childLong.doubleValue();

        }

        //TODO: Handle case for childCount=0 maybe because all children have missing location.
        meanLong = longSum/numChildren;
        return meanLong;
    }

    /**
     * Finds the sum of minimum angular latitude distances to all child nodes from their mean.
     * @param node
     * @param meanLat
     * @return
     */
    private double findSumOfAngularDistancesToMeanLat(PhylogenyNode node, double meanLat){
        double sum =0.0;

        for (int i=0; i < node.getNumberOfDescendants(); i++){
            PhylogenyNode childNode = node.getChildNode(i);
            NodeData childData = childNode.getNodeData();
            Distribution childDist = childData.getDistribution();

            BigDecimal childLat = BigDecimal.ZERO;

            if(childDist != null){
                childLat = childDist.getLatitude();
            }
            sum+= findMinimumAngularDistance(childLat.doubleValue(), meanLat);
        }
        return sum;        
    }



    /**
     * Finds the sum of minimum angular longitude distances to all child nodes from their mean.
     * @param node
     * @param meanLong
     * @return
     */
    private double findSumOfAngularDistancesToMeanLong(PhylogenyNode node, double meanLong){
        double sum =0.0;

        for (int i=0; i < node.getNumberOfDescendants(); i++){
            PhylogenyNode childNode = node.getChildNode(i);
            NodeData childData = childNode.getNodeData();
            Distribution childDist = childData.getDistribution();

            BigDecimal childLong = BigDecimal.ZERO;

            if(childDist != null){
                childLong = childDist.getLongitude();
            }
            sum+= findMinimumAngularDistance(childLong.doubleValue(), meanLong);
        }
        return sum;
    }


    
    /**
     * Finds the diametrically opposite angle to this angle.<br>
     * The diametrically opposite angle to 2.5 is -177.5
     * @param angle
     * @return
     */
    private double findDiametricallyOppositeAngle(double angle){
        double oppAngle = 0.0;
        oppAngle = -1*Math.signum(angle)*(180-Math.abs(angle));
        return oppAngle;
    }

    
    /**
     * Finds the minimum angular distance between two angles.
     * @param angle1
     * @param angle2
     * @return
     */
    private double findMinimumAngularDistance(double angle1, double angle2){
        double angDistance = 0.0;
        if(Math.signum(angle2) == Math.signum(angle1)){
            angDistance = Math.abs(angle2-angle1);
        }else{
            double sumAngle = Math.abs(angle1)+Math.abs(angle2);
            angDistance = Math.min(sumAngle, 360-sumAngle);
        }
        return angDistance;
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
                        gSum+=childColor.getBlue();
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
