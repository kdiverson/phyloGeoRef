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

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.nexml.model.Document;
import org.nexml.model.DocumentFactory;
import org.nexml.model.FloatEdge;
import org.nexml.model.Network;
import org.nexml.model.Node;
import org.nexml.model.Tree;
import org.nexml.model.TreeBlock;

/**
 *
 * @author apurv
 */
public class NeXMLEngine {

    public Phylogeny parseFloatTree(File intreeFile) throws Throwable {

		Phylogeny my_phy = new Phylogeny();

        Document document = DocumentFactory.parse(intreeFile);
		System.out.println(document.getXmlString());

        TreeBlock treeBlock = document.getTreeBlockList().get(0);
	Tree<?> floatTree = null;

        for (Network<?> networkObject : treeBlock) {
			if ("tree1".equals(networkObject.getLabel())) {
				floatTree = (Tree<?>) networkObject;
				break;
			}

		}

		Node rootNode = floatTree.getRoot();

        PhylogenyNode rootPhyNode = new PhylogenyNode(rootNode.toString());
        my_phy.setRoot(rootPhyNode);

        Set<Node> rootChildren = floatTree.getOutNodes(rootNode);

        for (Node rootChild : rootChildren) {
            PhylogenyNode phyRootChild = new PhylogenyNode(rootChild.toString());
            my_phy.getRoot().addAsChild(phyRootChild);
        }

        //should replace this with if statement so treesblocks with multiple trees can be put into a phylogeny array
        for ( Iterator li = treeBlock.iterator(); li.hasNext();) {

            Tree<FloatEdge> tree = (Tree<FloatEdge>) treeBlock.iterator().next(); //every tree in the treeblock

            //for every node in the tree
            for (Node node : tree.getNodes()) {

                //if (node.isRoot()) continue;

                //create a new phylogeny node for each node in the tree
                PhylogenyNode phyNode = new PhylogenyNode(node.toString());

                //get all nodes that are a decendet of node and put them in the set decendentNodes
                //decendentNodes is a set of all the nodes that are decendent of node
                Set<Node> decendentNodes = tree.getOutNodes(node);

                //for every node that is a decendnt of node
                for (Node treeNode : decendentNodes) {

                    //for (PhylogenyNode phyNode : my_phy.getNodes(null))
                    PhylogenyNode phyChild = new PhylogenyNode(treeNode.toString());

                    //if phyphyNode != terminal

                    if (node.isRoot()) {
                        phyChild.setParent(my_phy.getRoot());
                    }

                    else phyChild.setParent(phyNode);

                    phyChild.setName(treeNode.getLabel());

                    //phyChild.getFirstChildNode();

                    //my_phy.addAsSibling(phyChild);

                }
                //lastNode = ;
            }
        }

        return my_phy;
	}


    public static void main(String...args) throws Throwable{
        String inTreeFile ="src\\testTree.xml";
        File treeFile = new File(inTreeFile);
        NeXMLEngine engine=new NeXMLEngine();
        engine.parseFloatTree(treeFile);
    }

}
