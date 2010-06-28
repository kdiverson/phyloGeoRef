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

import java.lang.Math;
import java.math.BigDecimal;
import java.util.ListIterator;
import java.util.ArrayList;

import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.data.NodeData;
import org.forester.phylogeny.data.Taxonomy;
import org.forester.phylogeny.iterators.PhylogenyNodeIterator;
import org.forester.phylogeny.iterators.ExternalForwardIterator;
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

                    PhylogenyNode node = my_phy.getNode(species);//ext_it.next();
                    NodeData data = node.getNodeData();
                    node.getNodeData().setDistribution(new Distribution(""));
                    Distribution dist = data.getDistribution();

                    //String desc = new String();

                    //Distribution dist = new Distribution(desc);
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

                }
            }

            else{
                System.out.println("Something wrong with coordList: didn't find type Triple or Quad");
            }

    }

    private BigDecimal [] calcNodeLatLong (Phylogeny my_phy) {
        //calc lat long latLong[lat,long,lat,long...]
        BigDecimal [] latLong = new BigDecimal [countNodes(my_phy)];
        //function that determins the lat and long of each node
        
        return latLong;
    }

    private void assignLatLong(Phylogeny my_phy, PhylogenyNode node) {
        
        BigDecimal lat = BigDecimal.TEN;
        BigDecimal lng = BigDecimal.TEN;
        
        //linear strech algorithm from GeoPhyloBuilder 1.1
        //converts a non-ultrametric tree to an ultrametric tree
        //right now all we can do is ignore branch lengths
        short nLtip = PhylogenyMethods.calculateMaxBranchesToLeaf(node);
        double nLroot = PhylogenyMethods.calculateDistanceToRoot(node);
        int maxLtree = PhylogenyMethods.calculateMaxDepth(my_phy);
        short zMult = 1;
        
        double nodeZ = (nLtip/(nLroot/nLtip)*maxLtree)*zMult;

        

        NodeData data = node.getNodeData();
        node.getNodeData().setDistribution(new Distribution(""));
        Distribution dist = data.getDistribution();
        dist.setLatitude(lat);
        dist.setLongitude(lng);

    }

    private void assignNodeAltitude(PhylogenyNode node) {
        //calc altitude for leafs, nodeAltitude = a + ((n-1)*b)
        //BigDecimal [] alt = new BigDecimal [countNodes(my_phy)];
        double n = PhylogenyMethods.calculateDistanceToRoot(node);
        int a = 198000; //from Janies et al. 2007
        int b = 66000; //from Janies et al. 2007
        double theAlt = a + ((n-1)*b);
        //alt = BigDecimal.valueOf(theAlt);
        BigDecimal alt = BigDecimal.valueOf(theAlt);//new BigDecimal(theAlt);

        NodeData data = node.getNodeData();
        //Don't need this if alt has already been assigned
        //node.getNodeData().setDistribution(new Distribution(""));
        Distribution dist = data.getDistribution();
        dist.setAltitude(alt);

        //return alt;
    }

    private void assignBinaryNodes (Phylogeny my_phy, ArrayList coordList) {
        
        assignExtenalNodeDistribution(my_phy, coordList);

        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPreorder(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();
            //node.getNodeData().setDistribution(new Distribution(""));
            //Distribution dist = data.getDistribution();

            //assignNodeAltitude(node);

            //Algorithm: Each external node, "leaf" is assigned a lat/long from the coordlist
            //all subsequent nodes are then placed in the middle of the child nodes at altitude alt.

            //if my_phy.isRooted() do this else root then do this

            //int c = 0;

            if ( !node.isExternal() ) {
                node.getNodeData().setDistribution(new Distribution(""));
                Distribution dist = data.getDistribution();

                PhylogenyNode firstChild = node.getFirstChildNode();
                NodeData fcn = firstChild.getNodeData();
                //firstChild.getNodeData().setDistribution(new Distribution(""));
                Distribution fcd = fcn.getDistribution();
                BigDecimal firstChildLat = fcd.getLatitude();
                BigDecimal firstChildLong = fcd.getLongitude();

                PhylogenyNode lastChild = node.getLastChildNode();
                NodeData lcn = lastChild.getNodeData();
                //lastChild.getNodeData().setDistribution(new Distribution(""));
                Distribution lcd = lcn.getDistribution();
                BigDecimal lastChildLat = lcd.getLatitude();
                BigDecimal lastChildLong = lcd.getLongitude();

                BigDecimal two = new BigDecimal("2");

                //Assigns the node a lat/long pair that is in the midpoint of the two children
                dist.setLatitude(firstChildLat.add(lastChildLat).divide(two));
                dist.setLongitude(firstChildLong.add(lastChildLong).divide(two));

                //c++;
                //System.out.print(c);

                //System.out.println(dist.toString());
            }

            assignNodeAltitude(node);
        }


    }


    public void lazyAssignNodeCoords (Phylogeny my_phy, ArrayList coordList) {
        assignBinaryNodes(my_phy, coordList);
    }


    public void assignMultipleObservations (Phylogeny my_phy, ArrayList coordList) {
        //similar to other assignNode functions but altitude needs to change; leaves should be at
        //some nonzero altitude, then assignment can continue as normal.

        //
        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPostorder(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();
            node.getNodeData().setDistribution(new Distribution(""));
            Distribution dist = data.getDistribution();

            if (node.isExternal() )
                dist.setAltitude(new BigDecimal("198000"));
            
            else{
                assignNodeAltitude(node);
                assignLatLong(my_phy, node);
            }


        }
        
    }

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

            //
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
                BigDecimal meanLat = latSum.divide(count,BigDecimal.ROUND_CEILING);
                BigDecimal meanLong = longSum.divide(count,BigDecimal.ROUND_CEILING);

                dist.setLatitude(meanLat);
                dist.setLongitude(meanLong);

            }
            assignNodeAltitude(node);

        }
    }

    private int countNodes(Phylogeny my_phy) {
        int c = 0;
        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPostorder(); ext_it.hasNext();) {
            c++;
        }
        return c;
    }

}


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