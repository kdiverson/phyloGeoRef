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
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
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
     * Parses the nexml file wrapped as a File object in networkFile and constructs the Phylogeny object.
     * Operable on network having edges with real number/integer lengths.
     * 
     * @param networkFile the file which wraps the input the nexml file.
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
            
            index++;
        }

        return phylogenies;
    }
   

    /**
     * Reads the nexml file and returns the list of phylogenies.<br>
     * Must be called if the nexml file has coordinate metadata is present in the nexml file.<br>
     * @param xmlFile
     * @return the array of Phylogenies
     */
    public Phylogeny[] readPhylogeny(File xmlFile){
        Phylogeny[] phylogenies = null;
        try{
            phylogenies = parseNetwork(xmlFile);
        }
        catch(SAXException e){
            System.err.println(e.getMessage());
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        }
        catch(ParserConfigurationException e){
            System.err.println(e.getMessage());
        }
        return phylogenies;
    }

    /**
     * Reads the nexml file and returns the list of phylogenies.<br>
     * Must be called if the nexml file has coordinate metadata is present in the nexml file.<br>
     *
     * args[0] should be the column number of Label in the input file. <br>
     * args[1] should be the column number of Latitude in the input file <br>
     * and so on...<br>
     * args[i] = 0 implies that the property is not being given in the input file.<br>
     * The values in the example here have been fed according to the file,
     * samples/mammals/mammals_in_tree.txt<br>
     * If you are using this file pass the example values.
     * 
     * <table border="1">
     *  <tr>
     *  <th> args[i]  </th>
     *  <th> Property </th>
     *  <th> Example  </th>
     *  </tr>
     *  <tr>
     *      <td> args[0]</td>
     *      <td> Label  </td>
     *      <td> 5, if the fifth column has to be read as the label      </td>
     *  </tr>
     *  <tr>
     *      <td> args[1] </td>
     *      <td> Latitude </td>
     *      <td> 3, third column is to be read as latitude      </td>
     *  </tr>
     *  <tr>
     *      <td> args[2] </td>
     *      <td> Longitude </td>
     *      <td> 4, fourth column to be read as longitude </td>
     *  </tr>
     *  <tr>
     *      <td> args[3] </td>
     *      <td> Id </td>
     *      <td> 1  </td>
     *  </tr>
     *  <tr>
     *      <td> args[4] </td>
     *      <td> Scientific Name </td>
     *      <td> 2      </td>
     *  </tr>
     *  <tr>
     *      <td> args[5] </td>
     *      <td> Common Name </td>
     *      <td> 0, since this value is not being given. </td>
     *  </tr>
     * <tr>
     *      <td> args[7] </td>
     *      <td> Species </td>
     *      <td> 9 </td>
     * </tr>
     * <tr>
     *      <td> args[8] </td>
     *      <td> Genus </td>
     *      <td> 8 </td>
     * </tr>
     * <tr>
     *      <td> args[9] </td>
     *      <td> Family </td>
     *      <td> 7 </td>
     * </tr>
     * <tr>
     *      <td> args[10] </td>
     *      <td> Order </td>
     *      <td> 0, not being used </td>
     * </tr>
     * <tr>
     *      <td> args[11] </td>
     *      <td> Class </td>
     *      <td> 6 </td>
     * </tr>
     * <tr>
     *      <td> args[12] </td>
     *      <td> Phylum </td>
     *      <td> 0,  or can be left unspecified. </td>
     * </tr>
     * </table>
     * <br>
     * The trailing unused values can be left unspecified.
     * @param xmlFile
     * @param metaFile
     * @param delim the delimiter character
     * @param cladeDiv 'f', use family as the clade division. Allowed values {'g','f','o','c','p'}
     * @param args
     * @return the full fledged array of phylogenies.
     */
    public Phylogeny[] readPhylogeny(File xmlFile, File metaFile,char delim,char cladeDiv, Integer...args){
        Phylogeny[] phylogenies = null;
        try{
            //Get an array of raw phylogenies.
            phylogenies = parseNetwork(xmlFile);

            //Iterate over each phylogeny and cook it up in the kitchen to get
            //full fledged phylogenies.
            for(Phylogeny phylogeny:phylogenies){

                Map<String,PhylogenyNode> map = engine.getMap();
                //Prepare a new kitchen
                PhylogenyKitchen kitchen = new PhylogenyKitchen(metaFile, phylogeny, map, delim, cladeDiv);
                
                //Set the recipe by matching the column values.
                kitchen.setRecipe(args);

                //Cook the tree
                kitchen.cookDish();
            }            
        }
        catch(SAXException e){
            System.err.println(e.getMessage());
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        }
        catch(ParserConfigurationException e){
            System.err.println(e.getMessage());
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

        File inTree = new File("samples\\mammals\\mammalsTree.xml");
        File metaFile = new File("samples\\mammals\\mammals_in_tree.txt");
        //File inTree = new File("samples\\treeExperimental\\testTree.xml");
        //File inTree=new File("samples\\tree3\\T1092.xml");
        //in.parseIntTree(inTree);

        //File inTree=new File("samples\\tree1\\testTree.xml");
        //in.parseFloatTree(inTree);
        in.readPhylogeny(inTree, metaFile,'\t','f',5,3,4,1,2,0,9,8,7,0,6,0);
                        
    }
    

}
