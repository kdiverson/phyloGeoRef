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

package nescent.phylogeoref.tree;

import java.math.BigDecimal;
import java.util.ListIterator;
import java.util.List;
//import nescent.phyloGeoRef.tree.getTree;
import nescent.phylogeoref.geodata.ParseCoords;

import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.data.NodeData;
import org.forester.phylogeny.iterators.PhylogenyNodeIterator;
import org.forester.phylogeny.iterators.ExternalForwardIterator;

/**
 * This class stores trees. It uses functions from getTree to get a tree
 * from a file. A parser from biojava is called to parse the tree as a
 * Phylogeny object. Then various functions from the Phylogeny class are
 * called to extract information about the tree. That information is stored
 * as a storeTree object.
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class StoreTree {

    Phylogeny my_phy;
    List geoCoords; //from parseCoords.java -> returned coordList
    ListIterator li = geoCoords.listIterator();
    BigDecimal lat;
    BigDecimal tude;

    /**
     * 
     * @param phyArr
     */
    public void treeStore (Phylogeny [] phyArr) {
        for (int i = 0; i < phyArr.length; i++)
            my_phy = phyArr[i];
            double height = my_phy.getHeight();
            int numBr = my_phy.getNumberOfBranches();
            PhylogenyNode root =  my_phy.getRoot();
            int en = my_phy.getNumberOfExternalNodes();

            for( PhylogenyNodeIterator it = my_phy.iteratorPostorder(); it.hasNext(); ) {


        }

    }


    
}
