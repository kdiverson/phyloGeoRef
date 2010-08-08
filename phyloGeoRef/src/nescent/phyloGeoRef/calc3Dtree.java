/*
 *  Copyright (C) 2010 Kathryn Iverson <kd.iverson at gmail.com>
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

package nescent.phyloGeoRef;

import java.math.BigDecimal;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.List;

import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.data.NodeData;
import org.forester.phylogeny.iterators.PhylogenyNodeIterator;
import org.forester.phylogeny.PhylogenyMethods;

/**
 *
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class calc3Dtree {

    private void assignExtenalNodeDistribution (Phylogeny my_phy, ArrayList coordList) {

            if (coordList.get(0) instanceof Triple) {
                for(ListIterator<Triple> li = coordList.listIterator(); li.hasNext();) {
                    Triple item = li.next();

                    String species = (String) item.getFirst();
                    BigDecimal lat = (BigDecimal) item.getSecond();
                    BigDecimal lng = (BigDecimal) item.getThird();

                    PhylogenyNode node = my_phy.getNode(species);//ext_it.next(); look into fuzzy matching
                    NodeData data = node.getNodeData();
                    node.getNodeData().setDistribution(new Distribution(""));
                    Distribution dist = data.getDistribution();

                    dist.setLatitude(lat); //from geo coord file
                    dist.setLongitude(lng); //from geo coord file
                    dist.setAltitude(BigDecimal.ZERO);//always 0

                }
            }

            else if (coordList.get(0) instanceof Quad){
                for(ListIterator<Quad> li = coordList.listIterator(); li.hasNext();) {
                    Quad item = li.next();

                    String species = (String) item.getFirst();
                    BigDecimal lat = (BigDecimal) item.getSecond();
                    BigDecimal lng = (BigDecimal) item.getThird();
                    //Should decide what to do with this
                    String metadata = (String) item.getFourth();

                    PhylogenyNode node = my_phy.getNode(species);//ext_it.next();
                    NodeData data = node.getNodeData();
                    node.getNodeData().setDistribution(new Distribution(""));
                    Distribution dist = data.getDistribution();

                    dist.setLatitude(lat); //from geo coord file
                    dist.setLongitude(lng); //from geo coord file
                    dist.setAltitude(BigDecimal.ZERO);//always 0
                    dist.setDescription(metadata);

                }
            }

            else{
                System.out.println("Something wrong with coordList: didn't find type Triple or Quad");
            }

    }

//    private BigDecimal [] calcNodeLatLong (Phylogeny my_phy) {
//        //calc lat long latLong[lat,long,lat,long...]
//        BigDecimal [] latLong = new BigDecimal [countNodes(my_phy)];
//        //function that determins the lat and long of each node
//
//        return latLong;
//    }

//    private void assignLatLong(Phylogeny my_phy, PhylogenyNode node) {
//
//        BigDecimal lat = BigDecimal.TEN;
//        BigDecimal lng = BigDecimal.TEN;
//
//        //linear strech algorithm from GeoPhyloBuilder 1.1
//        //converts a non-ultrametric tree to an ultrametric tree
//        //right now all we can do is ignore branch lengths
//        short nLtip = PhylogenyMethods.calculateMaxBranchesToLeaf(node);
//        double nLroot = PhylogenyMethods.calculateDistanceToRoot(node);
//        int maxLtree = PhylogenyMethods.calculateMaxDepth(my_phy);
//        short zMult = 1;
//
//        double nodeZ = (nLtip/(nLroot/nLtip)*maxLtree)*zMult;
//
//        NodeData data = node.getNodeData();
//        node.getNodeData().setDistribution(new Distribution(""));
//        Distribution dist = data.getDistribution();
//        dist.setLatitude(lat);
//        dist.setLongitude(lng);
//
//    }

    private void assignNodeAltitude(PhylogenyNode node) {

        NodeData data = node.getNodeData();
        //Don't need this if alt has already been assigned
        //node.getNodeData().setDistribution(new Distribution(""));
        Distribution dist = data.getDistribution();

        if (node.isInternal()) {
            //calc altitude for leafs, nodeAltitude = a + ((n-1)*b)
            //BigDecimal [] alt = new BigDecimal [countNodes(my_phy)];
            //double n = PhylogenyMethods.calculateDistanceToRoot(node);
            double n = node.getNumberOfParents();
            int a = 198000; //from Janies et al. 2007
            int b = 66000; //from Janies et al. 2007
            double theAlt = a + ((n-1)*b);
            //alt = BigDecimal.valueOf(theAlt);
            BigDecimal alt = BigDecimal.valueOf(theAlt);//new BigDecimal(theAlt);

            dist.setAltitude(alt);
            System.out.println("number of patents: ");
            System.out.println(n);
        }
        else if (node.isExternal() ) dist.setAltitude(BigDecimal.ZERO);
        //else if (node.isRoot()) dist.setAltitude(BigDecimal.valueOf(1000000));

    }

    /**
     * linear stretch algorithm from GeoPhyloBuilder 1.1
     * converts a non-ultrametric tree to an ultrametric tree
     * @param node
     * @param maxLtree
     */
    private void assignNodeAltitude(PhylogenyNode node, int maxLtree){

        NodeData data = node.getNodeData();
        Distribution dist = data.getDistribution();

        if (node.isInternal()) {
            //right now all we can do is ignore branch lengths
            //short nLtip = PhylogenyMethods.calculateMaxBranchesToLeaf(node);
            //double nLtip = (double) nLt;
            int nLtip = node.getNumberOfDescendants();
            double nLroot = node.getNumberOfParents();
            //double nLroot = PhylogenyMethods.calculateDistanceToRoot(node);

            short zMult = 1;

            System.out.println(nLtip);
            System.out.println(nLroot);
            System.out.println((nLtip/(nLroot/nLtip)*maxLtree)*zMult);

            double nodeZ = (nLtip/(nLroot/nLtip)*maxLtree)*zMult;

            //double test = nodeZ;
            //BigDecimal t = new BigDecimal("123");
            System.out.println(nodeZ);
            BigDecimal alt = BigDecimal.valueOf(nodeZ*100000);
            //BigDecimal big = new BigDecimal(1000000000);
            
            dist.setAltitude(alt);
        }

        else dist.setAltitude(BigDecimal.ZERO);

    }

    /**
     * Algorithm: Each external node, "leaf" is assigned a lat/long from the coordlist
     * all subsequent nodes are then placed in the middle of the child nodes at altitude alt.
     * TODO: still need to add xmeridian check
     * This is probably not a necessary function, I think the regular assignNodeCoords will do
     * the same thing as this function.
     * 
     * @param my_phy
     * @param coordList
     */

    public void assignBinaryNodes (Phylogeny my_phy, ArrayList coordList) {
        
        assignExtenalNodeDistribution(my_phy, coordList);

        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPreorder(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();

            if ( !node.isExternal() ) {
                node.getNodeData().setDistribution(new Distribution(""));
                Distribution dist = data.getDistribution();

                PhylogenyNode firstChild = node.getFirstChildNode();
                NodeData fcn = firstChild.getNodeData();
                Distribution fcd = fcn.getDistribution();
                BigDecimal firstChildLat = fcd.getLatitude();
                BigDecimal firstChildLong = fcd.getLongitude();

                PhylogenyNode lastChild = node.getLastChildNode();
                NodeData lcn = lastChild.getNodeData();
                Distribution lcd = lcn.getDistribution();
                BigDecimal lastChildLat = lcd.getLatitude();
                BigDecimal lastChildLong = lcd.getLongitude();

                BigDecimal two = new BigDecimal("2");

                //Assigns the node a lat/long pair that is in the midpoint of the two children
                dist.setLatitude(firstChildLat.add(lastChildLat).divide(two));
                dist.setLongitude(firstChildLong.add(lastChildLong).divide(two));

            }

            //assignNodeAltitude(node, maxLtree);
            //uncoment the following line for the Janis et al tree height calculation
            assignNodeAltitude(node);
        }

    }

    /**
     * This just calls assignBinaryNodes -- it was mostly used for testing and
     * can probably be removed
     * 
     * @param my_phy
     * @param coordList
     */

    public void lazyAssignNodeCoords (Phylogeny my_phy, ArrayList coordList) {
        assignBinaryNodes(my_phy, coordList);
    }


    /**
     * This doesn't quite work yet -- don't use it unless you fix it
     * The best way I can think to do this is create a polygon in KML that
     * is bound by the points and put the leaf of the tree in the center of the
     * polygon.
     *
     * @param my_phy
     * @param coordList
     */

    public void assignMultipleObservations (Phylogeny my_phy, ArrayList coordList) {
        //similar to other assignNode functions but altitude needs to change; leaves should be at
        //some nonzero altitude, then assignment can continue as normal.

        //
        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPostorder(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();
            Distribution dist = data.getDistribution();

            //coordlist is an array of triples (or quads) like this: [(species,lat,long,metadata), (species2, lat, long, metadata)]
            //iterate through coordList [(species,lat,long,metadata), (species2, lat, long, metadata)]

            ArrayList childCoordsLat = new ArrayList();
            ArrayList childCoordsLong = new ArrayList();
            BigDecimal latSum = BigDecimal.ZERO;
            BigDecimal longSum = BigDecimal.ZERO;
            int c = 1;

            for (int i=0; i < node.getNumberOfDescendants(); i++){
                //do mean calcs
                PhylogenyNode childNode = node.getChildNode(i);
                NodeData childData = childNode.getNodeData();
                Distribution childDist = childData.getDistribution();
                BigDecimal childLat = childDist.getLatitude();
                BigDecimal childLong = childDist.getLongitude();

                childCoordsLat.add(childLat);
                childCoordsLong.add(childLong);

                latSum = latSum.add(childLat);
                longSum = longSum.add(childLong);

                c++;

            }

        BigDecimal count = new BigDecimal(c);
        BigDecimal meanLat = latSum.divide(count,BigDecimal.ROUND_CEILING);
        BigDecimal meanLong = longSum.divide(count,BigDecimal.ROUND_CEILING);


        dist.setLatitude(meanLat);
        dist.setLongitude(meanLong);
        
        int maxLtree = PhylogenyMethods.calculateMaxDepth(my_phy);
        System.out.println(maxLtree);

        if (node.isExternal() ) dist.setAltitude(new BigDecimal("198000"));

        else assignNodeAltitude(node, maxLtree);

        assignNodeAltitude(node, maxLtree);
        //uncoment the following line for the Janis et al tree height calculation
        //assignNodeAltitude(node);
        }
        
}


    /**
     * This function assigned coordinates to the nodes of the tree.
     * 
     * @param my_phy
     * @param coordList
     */

    public void assignNodeCoords(Phylogeny my_phy, ArrayList coordList) {
        if (my_phy.isCompletelyBinary()) {
            assignBinaryNodes(my_phy, coordList);
        }

        assignExtenalNodeDistribution(my_phy, coordList);

        //IMPLEMNTS MEAN POSITION ALGORITHM
        //Basic idea: get the mean of the lat and long of all the decendents of each node
        //then assigns the mean to the node

        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPostorder(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();

            //coordlist is an array of triples (or quads) like this: [(species,lat,long,metadata), (species2, lat, long, metadata)]
            //iterate through coordList [(species,lat,long,metadata), (species2, lat, long, metadata)]

            ArrayList childCoordsLat = new ArrayList();
            ArrayList childCoordsLong = new ArrayList();
            BigDecimal latSum = BigDecimal.ZERO;
            BigDecimal longSum = BigDecimal.ZERO;
            int c = 0;

            //this assumes we already assigned external nodes
            if ( !node.isExternal() ) {
                node.getNodeData().setDistribution(new Distribution(""));
                Distribution dist = data.getDistribution();

                for (int i=0; i < node.getNumberOfDescendants(); i++){
                    //do mean calcs
                    PhylogenyNode childNode = node.getChildNode(i);
                    NodeData childData = childNode.getNodeData();
                    Distribution childDist = childData.getDistribution();
                    BigDecimal childLat = childDist.getLatitude();
                    BigDecimal childLong = childDist.getLongitude();

                    childCoordsLat.add(childLat);
                    childCoordsLong.add(childLong);

                    latSum = latSum.add(childLat);
                    longSum = longSum.add(childLong);

                    c++;

                }


                BigDecimal count = new BigDecimal(c);
                //BigDecimal latAbs = latSum.abs();
                //BigDecimal longAbs = longSum.abs();
                BigDecimal meanLat = latSum.divide(count,BigDecimal.ROUND_CEILING);
                BigDecimal meanLong = longSum.divide(count,BigDecimal.ROUND_CEILING);


                dist.setLatitude(meanLat);
                dist.setLongitude(meanLong);

            }
            int maxLtree = PhylogenyMethods.calculateMaxDepth(my_phy);
            System.out.println(maxLtree);
            assignNodeAltitude(node, maxLtree);
            //assignNodeAltitude(node);

        }
    }

    public void assignMaridianCoords (Phylogeny my_phy, ArrayList coordList) {
        if (my_phy.isCompletelyBinary()) {
            assignBinaryNodes(my_phy, coordList);
        }

        assignExtenalNodeDistribution(my_phy, coordList);

        //IMPLEMNTS MEAN POSITION ALGORITHM
        //Basic idea: get the mean of the lat and long of all the decendents of each node
        //then assigns the mean to the node

        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPostorder(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();

            //coordlist is an array of triples (or quads) like this: [(species,lat,long,metadata), (species2, lat, long, metadata)]
            //iterate through coordList [(species,lat,long,metadata), (species2, lat, long, metadata)]

            ArrayList childCoordsLat = new ArrayList();
            ArrayList childCoordsLong = new ArrayList();
            BigDecimal latSum = BigDecimal.ZERO;
            BigDecimal longSum = BigDecimal.ZERO;
            int c = 0;

            //this assumes we already assigned external nodes
            if ( !node.isExternal() ) {
                node.getNodeData().setDistribution(new Distribution(""));
                Distribution dist = data.getDistribution();

                boolean xmeridian = false;
                int neg = 0;

                for (int i=0; i < node.getNumberOfDescendants(); i++){

                    //do mean calcs
                    PhylogenyNode childNode = node.getChildNode(i);
                    NodeData childData = childNode.getNodeData();
                    Distribution childDist = childData.getDistribution();
                    BigDecimal childLat = childDist.getLatitude();
                    BigDecimal childLong = childDist.getLongitude();

//                    if (i+1 < node.getNumberOfDescendants()) {
//                        PhylogenyNode nextChild = node.getChildNode(i+1);
//                        NodeData nextChildData = nextChild.getNodeData();
//                        Distribution nextChildDist = nextChildData.getDistribution();
//
//                        if (childLong.floatValue() < 0 && nextChildDist.getLongitude().floatValue() > 0) {
//                            xmeridian = true;
//                            System.out.println("crossing the xmeridian...");
//                        }
//                    }

                    List<PhylogenyNode> decendents = node.getDescendants();
                    //int neg = 0;

                    for (PhylogenyNode decendent : decendents) {
                        NodeData decendentData = decendent.getNodeData();
                        Distribution decendentDist = decendentData.getDistribution();

                        if (decendentDist.getLongitude().floatValue() < 0 && decendentDist.getLongitude().floatValue() < -90) {
                            neg++;
                            System.out.println("crossing the meridian...");
                            System.out.println(neg);
                        }
                    }

                    if (neg != decendents.size() && neg != 0) xmeridian = true;

                    childCoordsLat.add(childLat);
                    childCoordsLong.add(childLong);

                    latSum = latSum.add(childLat);
                    longSum = longSum.add(childLong);

                    c++;

                }

                BigDecimal count = new BigDecimal(c);

                BigDecimal meanLat = latSum.divide(count,BigDecimal.ROUND_CEILING);
                BigDecimal meanLong = longSum.divide(count,BigDecimal.ROUND_CEILING);

                if (xmeridian == true) {
                    //if meanLong is negative
                    int midPtSign;
                    if (meanLong.compareTo(BigDecimal.ZERO) == -1) {
                        //if midPtSign is 1 then the midpoint value should be positive
                        midPtSign = 1;
                        meanLong = meanLong.abs();
                    }
                    else midPtSign = -1;
                    
                    BigDecimal oneEighty = new BigDecimal("180");
                    //meanLat = (oneEighty.subtract(meanLat)).multiply(BigDecimal.ONE.negate());
                    meanLong = oneEighty.subtract(meanLong);
                    //switch the sign if the origional meanLong was negative
                    if (midPtSign == -1) meanLong = meanLong.negate();

                }


                dist.setLatitude(meanLat);
                dist.setLongitude(meanLong);

            }
            int maxLtree = PhylogenyMethods.calculateMaxDepth(my_phy);
            System.out.println(maxLtree);
            assignNodeAltitude(node, maxLtree);
            //assignNodeAltitude(node);
        }
    }
}

//    private int countNodes(Phylogeny my_phy) {
//        int c = 0;
//        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPostorder(); ext_it.hasNext();) {
//            c++;
//        }
//        return c;
//    }


//This is SUPER ugly but here's how it works:
                //we need to get the distance between the two child nodes
                //so this is an inplementation of the distance formula.
                //BigDecimal doesn't have square root or powers so it has
                //to be done the old fashioned way.
//                double distance = Math.sqrt( ( firstChildLat.subtract(lastChildLat)
//                        ).multiply(firstChildLat.subtract(lastChildLat) ).add(
//                        ( firstChildLong.subtract(lastChildLong)
//                        ).multiply(firstChildLong.subtract(lastChildLong)) ).doubleValue() );

        //calc lat long
        //BigDecimal [] latLong = new BigDecimal [countNodes(my_phy)];
        //function that determins the lat and long of each node
        //assigns lat and long to the leafs from the coord file
        //external nodes are leafs

        //Triple speciesCoord;
        //ListIterator li = coordList.listIterator();
        //MAY NOT NEED TO ITERATE THROGUH NODES. CAN JUST CALL THE NODE BY SPECIES NAME!!!
        //for( PhylogenyNodeIterator ext_it = my_phy.iteratorExternalForward(); ext_it.hasNext();) {
//            PhylogenyNode node = my_phy.getNode(null);//ext_it.next();
//            NodeData data = node.getNodeData();
//            Distribution dist = data.getDistribution();
//            Taxonomy tax = data.getTaxonomy();
//            String name = tax.getScientificName();