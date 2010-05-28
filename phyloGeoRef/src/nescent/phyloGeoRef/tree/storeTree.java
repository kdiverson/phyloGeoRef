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

package nescent.phyloGeoRef.tree;

import nescent.phyloGeoRef.tree.getTree;

import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;

/**
 * This class stores trees. It uses functions from getTree to get a tree
 * from a file. A parser from biojava is called to parse the tree as a
 * Phylogeny object. Then various functions from the Phylogeny class are
 * called to extract information about the tree. That information is stored
 * as a storeTree object.
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class storeTree {
    //Phylogeny tree = new Phylogeny;
    Phylogeny phy = null;
    //Phylogeny [] phyArr = null;
    void treeStore (Phylogeny [] phyArr) {
        for (int i = 0; i < phyArr.length; i++)
            phy = phyArr[i];

        double height = phy.getHeight();
        int numBr = phy.getNumberOfBranches();
        PhylogenyNode root =  phy.getRoot();
        int en = phy.getNumberOfExternalNodes();
    }
    

    

    

}
