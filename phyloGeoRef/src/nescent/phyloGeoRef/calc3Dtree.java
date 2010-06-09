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
import org.forester.phylogeny.iterators.PhylogenyNodeIterator;
import org.forester.phylogeny.iterators.ExternalForwardIterator;
import org.forester.phylogeny.PhylogenyMethods;

/**
 *
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class calc3Dtree {

    private void assignExtenalNodeDistribution (Phylogeny my_phy, ArrayList coordList) {
        //calc lat long
        BigDecimal [] latLong = new BigDecimal [countNodes(my_phy)];
        //function that determins the lat and long of each node
        //assigns lat and long to the leafs from the coord file
        //external nodes are leafs
        int x = 0;
        int y = 1;
        //Triple speciesCoord;
        //ListIterator li = coordList.listIterator();
        for( PhylogenyNodeIterator ext_it = my_phy.iteratorExternalForward(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();
            Distribution dist = data.getDistribution();
            //hack, replace with listIterator?
            //for item in coordList
            for(ListIterator li = coordList.listIterator(); li.hasNext();) {
                dist.setAltitude(BigDecimal.ZERO);
                speciesCoord = li.next();

                if (data.getTaxonomy() == coordList) {

                }
            }
            
            dist.setLatitude(); //from geo coord file
            dist.setLongitude(); //from geo coord file
            dist.setAltitude(BigDecimal.ZERO);//always 0
            x++;
            y++;
        }

    }

    private BigDecimal [] calcNodeLatLong (Phylogeny my_phy) {
        //calc lat long latLong[lat,long,lat,long...]
        BigDecimal [] latLong = new BigDecimal [countNodes(my_phy)];
        //function that determins the lat and long of each node
        
        return latLong;
    }

    private void calcNodeAltitude(PhylogenyNode node) {
        //calc altitude for leafs, nodeAltitude = a + ((n-1)*b)
        //BigDecimal [] alt = new BigDecimal [countNodes(my_phy)];
        //int n = countNodes(my_phy);//FIX -> SHOULD BE DISTANCE TO ROOT NOT TOTAL NODES!
        //PhylogenyMethods pm = new PhylogenyMethods();
        double n = PhylogenyMethods.calculateDistanceToRoot(node);
        int a = 198000; //from Janies et al. 2007
        int b = 66000; //from Janies et al. 2007
        double theAlt = a + ((n-1)*b);
        //alt = BigDecimal.valueOf(theAlt);
        BigDecimal alt = BigDecimal.valueOf(theAlt);//new BigDecimal(theAlt);

        NodeData data = node.getNodeData();
        Distribution dist = data.getDistribution();
        dist.setAltitude(alt);

        //return alt;
    }

    private void assignBinaryNodes (Phylogeny my_phy, ArrayList coordList) {
        
        assignExtenalNodeDistribution(my_phy, coordList);

        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPostorder(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();
            Distribution dist = data.getDistribution();
            //Algorithm: Each external node, "leaf" is assigned a lat/long from the coordlist
            //all subsequent nodes are then placed in the middle of the child nodes at altitude alt.

            //if my_phy.isRooted() do this else root then do this

            if ( !data.isHasDistribution() ) {
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

            calcNodeAltitude(node);
        }


    }


    public void lazyAssignNodeCoords (Phylogeny my_phy, ArrayList coordList) {
        assignBinaryNodes(my_phy, coordList);
    }


    public void assignNodeCoords(Phylogeny my_phy, ArrayList coordList) {
        if (my_phy.isCompletelyBinary()) {
            assignBinaryNodes(my_phy, coordList);
        }
        
        int x = 0;
        int y = 0;
        int z = 1;

        //calc3Dtree c3dt;
        //this loop iterated through each node
        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPostorder(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();
            Distribution dist = data.getDistribution();

            //my_phy.getNode(nameFromCoordListFile) -> get the node with the name in coordList[0[0]]

            //ONLY RELEVANT TO LEAFS IE EXTERNAL NODES!!!
            //coordlist is an array of triples (or quads) like this: [(species,lat,long,metadata), (species2, lat, long, metadata)]
            //iterate through coordList [(species,lat,long,metadata), (species2, lat, long, metadata)]

            //this assumes we already assigned external nodes
            if ( !data.isHasDistribution() ) {
                //alt =
                lat
                longt
                
                //calcNodeAltitude(my_phy, node);
                //calcLat(my_phy, node)
                dist.setLatitude(coordList[y]);
                dist.setLongitude(latLong[z]);

                x++;
                y++;
                z++;
            }

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