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

package nescent.phylogeoref.nexml.utility;

import static java.lang.System.out;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.BranchData;
import org.forester.phylogeny.data.Identifier;
import org.forester.phylogeny.data.NodeData;
import org.nexml.model.Edge;
import org.nexml.model.FloatEdge;
import org.nexml.model.Network;
import org.nexml.model.Node;

/**
 * Provides the necessary utility functions while transforming a parsed NeXML document
 * into a Phylogeny tree.
 * It is important to note that there are two nodes and edges used in this class.
 * (1) org.forester.phylogeny.PhylogenyNode
 * (2) org.nexml.model.Node
 *
 * A successful transformation will grab all the data from a Node and put it into the
 * PhylogenyNode.
 *
 * Similarly there are two edges.
 * (1) org.nexml.model.Edge
 * (2) org.forester.phylogeny.Edge
 *
 * @author apurv
 */
public class PhyloUtility {

    /**
     * Finds and returns the root node of the tree.
     * @param network
     * @return the root node
     */
    public static Node getRootNode(Network<FloatEdge> network){
        Node rootNode=null;
        
        for(Node node:network.getNodes()){
            if(node.isRoot()){
                rootNode=node;
            }
        }
        return rootNode;
    }

    /**
     * Transforms Node to a PhylogenyNode.
     * @param fromNode the node from which data is to be extracted.
     * @param toNode the node to which data has to be copied.
     */
    public static PhylogenyNode toPhylogenyNode(Node node){

        PhylogenyNode phyNode = new PhylogenyNode();
        phyNode.setName(node.getLabel());

        phyNode.getNodeData().setNodeIdentifier(new Identifier(node.getId()));      

        return phyNode;
    }

    /**
     * Sets the distance of PhylogenyNode node from its parent.
     * @param edge
     * @param node
     */
    public static void setDistanceToParent(Edge edge, PhylogenyNode node){
        
        Number length = edge.getLength();
        if(length!=null){
            node.setDistanceToParent(length.doubleValue());
        }
        else{
            node.setDistanceToParent(0.0d);
        }                

    }

    

}
