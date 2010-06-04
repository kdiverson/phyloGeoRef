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
import java.util.List;

import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.data.NodeData;
import org.forester.phylogeny.iterators.PhylogenyNodeIterator;
import org.forester.phylogeny.iterators.ExternalForwardIterator;

/**
 *
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class calc3Dtree {
//Don't need this function
    //    private void assignExternalNodeAltitude(Phylogeny my_phy) {
//        //BigDecimal a;
//        //calc altitude for external leafs -> should always be zero, leafs always on the ground
//        //a = BigDecimal.ZERO;
//        //BigDecimal [] alt = new BigDecimal [countNodes(my_phy)];
//        for( PhylogenyNodeIterator ext_it = my_phy.iteratorExternalForward(); ext_it.hasNext();) {
//            PhylogenyNode node = ext_it.next();
//            NodeData data = node.getNodeData();
//            Distribution dist = data.getDistribution();
//            dist.setAltitude(BigDecimal.ZERO); //a from nodeAltitude = a + ((n-1)*b)
//        }
//
//    }


    //function to assign tuples of lat/long

    private void assignExtenalNodeDistribution (Phylogeny my_phy, List coordList) {
        //calc lat long
        BigDecimal [] latLong = new BigDecimal [countNodes(my_phy)];
        //function that determins the lat and long of each node
        //assigns lat and long to the leafs from the coord file
        //external nodes are leafs
        int x = 0;
        int y = 1;
        ListIterator li = coordList.listIterator();
        for( PhylogenyNodeIterator ext_it = my_phy.iteratorExternalForward(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();
            Distribution dist = data.getDistribution();
            //hack, replace with listIterator?
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

    private BigDecimal [] calcNodeAltitude(Phylogeny my_phy) {
        //calc altitude for leafs, nodeAltitude = a + ((n-1)*b)
        BigDecimal [] alt = new BigDecimal [countNodes(my_phy)];

        return alt;
    }

    private void assignNodeCoords (Phylogeny my_phy, BigDecimal [] alt, BigDecimal [] latLong) {
        //This loop assigns
        int x = 0;
        int y = 0;
        int z = 1;
        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPostorder(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();
            Distribution dist = data.getDistribution();
            if ( !data.isHasDistribution() ) {
                dist.setAltitude(alt[x]);
                dist.setLatitude(latLong[y]);
                dist.setLongitude(latLong[z]); 
                
                x++;
                y++;
                z++;    
            }

        }
    }

    public int countNodes(Phylogeny my_phy) {
        int c = 0;
        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPostorder(); ext_it.hasNext();) {
            c++;
        }
        return c;
    }

}
