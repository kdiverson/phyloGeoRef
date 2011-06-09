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
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import org.forester.phylogeny.Phylogeny;
import org.nexml.model.Document;
import org.nexml.model.DocumentFactory;
import org.nexml.model.Edge;
import org.nexml.model.FloatEdge;
import org.nexml.model.IntEdge;
import org.nexml.model.Network;
import org.nexml.model.Node;
import org.nexml.model.Tree;
import org.nexml.model.TreeBlock;
import org.xml.sax.SAXException;


/**
 * Takes an NeXML file. Parses it using the org.nexml library and constructs a Phylogeny object from
 * the parsed tree.
 * 
 * @author apurv
 */
public class NeXMLReader {

    private NeXMLEngine engine;

    public NeXMLReader(){
        engine = new NeXMLEngine();
    }

    /**
     * Parses the nexml file wrapped as a File object in treeFile and constructs the Phylogeny object.
     * Operable on trees having edges with integer lengths.
     * 
     * @param treeFile  the file which wraps the input the nexml file.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private void parseIntTree(File treeFile)throws SAXException, IOException,ParserConfigurationException{

        Document document = DocumentFactory.parse(treeFile);
        
        List<TreeBlock> treeList = document.getTreeBlockList();

        for(TreeBlock treeBlock:treeList){
            
            Tree<IntEdge> tree = (Tree<IntEdge>)treeBlock.iterator().next();
            for(Node node:tree.getNodes()){
                out.println(node.getId()+" "+node.getLabel());
            }
        }

    }

    /**
     * Parses the nexml file wrapped as a File object in treeFile and constructs the Phylogeny object.
     * Operable on trees having edges with real number/integer lengths.
     *
     * @param treeFile  the file which wraps the input the nexml file.
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private void parseFloatTree(File treeFile)throws SAXException, IOException,ParserConfigurationException{

        Document document = DocumentFactory.parse(treeFile);

        List<TreeBlock> treeList = document.getTreeBlockList();

        for(TreeBlock treeBlock:treeList){
            Tree<FloatEdge> tree = (Tree<FloatEdge>)treeBlock.iterator().next();
            for(Node node:tree.getNodes()){
                out.println(node.getId()+" "+node.getLabel());
            }
        }
        
    }

    /**
     * Parses the nexml file wrapped as a File object in networkFile and constructs the Phylogeny object.
     * Operable on network having edges with real number/integer lengths.
     * 
     * @param treeFile
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    private Phylogeny[] parseNetwork(File networkFile)throws SAXException, IOException, ParserConfigurationException{

        Document document = DocumentFactory.parse(networkFile);

        List<TreeBlock> treeList = document.getTreeBlockList();

        int index=0;
        int n = treeList.size();
        Phylogeny[] phylogenies = new Phylogeny[n];

        for(TreeBlock treeBlock:treeList){
            
            Network<FloatEdge> network = (Network<FloatEdge>)treeBlock.iterator().next();
            phylogenies[index] = engine.constructPhylogenyFromNetwork(network);            
                      
            out.println(phylogenies[index].toNexus());
            
            index++;
        }

        return phylogenies;
    }


    /**
     * Utility main method, used for testing this file during development.
     * @param args
     * @throws Throwable
     */
    public static void main(String...args) throws Throwable{

        NeXMLReader in = new NeXMLReader();
        
        File inTree=new File("samples\\tree2\\intTestTree.xml");
        //in.parseIntTree(inTree);

        //File inTree=new File("samples\\tree1\\testTree.xml");
        //in.parseFloatTree(inTree);
        in.parseNetwork(inTree);
        
        
        
    }
    

}
