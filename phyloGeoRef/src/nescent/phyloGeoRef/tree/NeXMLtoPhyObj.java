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

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import java.util.Iterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.nexml.model.Document;

import org.nexml.model.DocumentFactory;
import org.nexml.model.TreeBlock;
import org.nexml.model.Tree;
import org.nexml.model.Edge;
import org.nexml.model.Network;
import org.nexml.model.Node;
import org.nexml.model.IntEdge;
import org.nexml.model.FloatEdge;

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
public class NeXMLtoPhyObj {

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

	public void parseIntTree(File intreeFile) throws Throwable {
//		String nexmlRoot = System.getenv("NEXML_ROOT");
//		if ( nexmlRoot == null ) {
//			nexmlRoot = "/Users/rvosa/Documents/workspace/nexml/trunk/nexml";
//		}
		Document document = DocumentFactory.parse(intreeFile);
		//Assert.assertEquals("should be one tree", 1, document
				//.getTreeBlockList().size());
		//Assert.assertEquals("should be an int tree", "'the tree'", document
				//.getTreeBlockList().get(0).iterator().next().getLabel());
		TreeBlock treeBlock = document.getTreeBlockList().get(0);
		//Assert.assertEquals("should have same OTU in both", document
				//.getOTUsList().get(0), treeBlock.getOTUs());
		@SuppressWarnings("unchecked")
		Tree<IntEdge> tree = (Tree<IntEdge>) treeBlock.iterator().next();
		boolean foundRoot = false;
		for (Node node : tree.getNodes()) {
			if (node.isRoot()) {
				foundRoot = true;
			}
			//Assert.assertNotNull(node.getLabel());
			// System.out.println("edge.getLabel(): " + node.getLabel());
		}
		//Assert.assertTrue("should have found the root", foundRoot);

//		for (Edge edge : tree.getEdges()) {
//			//Assert.assertTrue("length should be >=1", ((IntEdge) edge)
//					//.getLength() >= 1);
//		}

		Map<Node, List<IntEdge>> nodeToEdge = new HashMap<Node, List<IntEdge>>();
		for (Node node : tree.getNodes()) {
			findEdges(node, tree, nodeToEdge);
			// System.out.println("node: " + node);
			// for (IntEdge edge : nodeToEdge.get(node)) {
			// System.out.println("    edge: " + edge);
			// }
		}
		Node brevParent = null;

		for (IntEdge edge : tree.getEdges()) {
			if (edge.getTarget().getLabel().equals("S. brevirostris")) {
				brevParent = edge.getSource();
			}
		}

		for (Node node : tree.getNodes()) {

			if ("S. brevirostris".equals(node.getLabel())) {
				tree.removeNode(node);
			}

			if ("S. megalops".equals(node.getLabel())) {
				node.setLabel("S. megalopsTESTMODIFYLABEL");
			}
		}

		Node newNode = tree.createNode();
		newNode.setLabel("TEST_NEW_NODE");
		IntEdge newEdge = tree.createEdge(brevParent, newNode);
		newEdge.setLabel("TEST_NEW_EDGE");
		newEdge.setLength(33);
		//Assert.assertEquals("length should be 33", Integer.valueOf(33), newEdge
				//.getLength());
		//System.out.println("xmlString: " + document.getXmlString());
	}

	void findEdges(Node node, Tree<IntEdge> tree,
			Map<Node, List<IntEdge>> nodeToEdge) {
		if (!nodeToEdge.containsKey(node)) {
			nodeToEdge.put(node, new ArrayList<IntEdge>());
		}
		for (IntEdge edge : tree.getEdges()) {
			if (edge.getSource().equals(node) || edge.getTarget().equals(node)) {
				nodeToEdge.get(node).add(edge);
			}
		}
	}

    public Document parseNeXML(File intreeFile) {
//		String nexmlRoot = System.getenv("NEXML_ROOT");
//		if ( nexmlRoot == null ) {
//			nexmlRoot = "/Users/rvosa/Documents/workspace/nexml/trunk/nexml";
//		}
    //File file = new File(intreeFile);
    Document doc = null;
    try {
        doc = DocumentFactory.parse(intreeFile);
    } catch (SAXException e) {
        //Assert.assertTrue(e.getMessage(), false);
        e.printStackTrace();
    } catch (IOException e) {
        //Assert.assertTrue(e.getMessage(), false);
        e.printStackTrace();
    } catch (ParserConfigurationException e) {
        //Assert.assertTrue(e.getMessage(), false);
        e.printStackTrace();
    }
    //System.out.println(doc.getXmlString());

    return doc;
	}
}



//        for ( Iterator li = treeBlock.iterator(); li.hasNext();) {
//
//            Tree<FloatEdge> tree = (Tree<FloatEdge>) treeBlock.iterator().next(); //this is for the NeXML treeblock
//
//            for (Node node : tree.getNodes()) { //this is an NeXML tree node
//			if (node.isRoot()) {
//                PhylogenyNode rootPhyNode = new PhylogenyNode(node.toString());
//                my_phy.setRoot(rootPhyNode);
//            }
//
//			else {
//
//                PhylogenyNode phyNode = new PhylogenyNode(node.toString());
//                my_phy.addThisNode(node);
//            }
//
//
//        }
//        }

