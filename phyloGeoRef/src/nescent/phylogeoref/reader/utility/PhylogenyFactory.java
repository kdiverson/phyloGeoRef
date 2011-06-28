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

package nescent.phylogeoref.reader.utility;

import static java.lang.System.out;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Identifier;
import org.nexml.model.FloatEdge;
import org.nexml.model.Network;
import org.nexml.model.Node;

/**
 * Instantiates a new Phylogeny object.
 * @author apurv
 */
public class PhylogenyFactory {

    /**
     * Instantiates a new phylogeny object with its attributes set to appropriate initial value.
     * @param network The network from which the new Phylogeny Object is to be constructed.
     * @return A new Phylogeny object.
     */
    public static Phylogeny newInstance(Network<FloatEdge> network){

        Phylogeny phylogeny = new Phylogeny();

        phylogeny.setName(network.getLabel());

        String phyId = network.getId(); 
        //TODO: This is unmapped to since there is no id attribute in a phylogeny.
        
        phylogeny.setRooted(true);

        Node rootNode = PhyloUtility.getRootNode(network);
        PhylogenyNode phyRootNode = PhyloUtility.toPhylogenyNode(rootNode);
        phylogeny.setRoot(phyRootNode);

        //TODO: This too needs to be set. This is the xsi:type value
        //phylogeny.setType();

        //TODO: This needs to be set to true because in a Network one node can have multiple parent nodes.
        //phylogeny.setAllowMultipleParents(true);             

        return phylogeny;

    }

}
