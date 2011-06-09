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

package nescent.phylogeoref.nexml;

import static java.lang.System.out;
import java.util.HashMap;
import java.util.Map;
import nescent.phylogeoref.nexml.utility.PhyloUtility;
import nescent.phylogeoref.nexml.utility.PhylogenyFactory;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.nexml.model.Edge;
import org.nexml.model.FloatEdge;
import org.nexml.model.Network;
import org.nexml.model.Node;
import org.nexml.model.Tree;

/**
 * Provides methods that can construct a Phylogeny object from a Tree or Network.
 * @author apurv
 */
public class NeXMLEngine {


    
    /**
     * Constructs the Phylogeny object representation of the network
     * @param network represents the tree from which the Phylogeny object is to be constructed.
     * @return the fully built Phylogeny object representing this tree.
     */
    public Phylogeny constructPhylogenyFromNetwork(Network<FloatEdge> network){

        Phylogeny phy = PhylogenyFactory.newPhylogeny(network);
        Map<Node,PhylogenyNode> map= new HashMap<Node,PhylogenyNode>();

        map.put(PhyloUtility.getRootNode(network), phy.getRoot());
        
        for(Node node:network.getNodes()){
            if(node.isRoot()){
                continue;
            }
            PhylogenyNode phyNode = PhyloUtility.toPhylogenyNode(node);
            map.put(node, phyNode);
        }

        for(Edge edge:network.getEdges()){
            Node sourceNode = edge.getSource();
            Node targetNode = edge.getTarget();
            
            PhylogenyNode phySourceNode = map.get(sourceNode);
            PhylogenyNode phyTargetNode = map.get(targetNode);

            phySourceNode.addAsChild(phyTargetNode);

            PhyloUtility.setDistanceToParent(edge, phyTargetNode);
        }

        return phy;
        
    }



}
