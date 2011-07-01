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

package nescent.phylogeoref.reader;

import static java.lang.System.out;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.forester.phylogeny.Phylogeny;
import org.nexml.model.Document;
import org.nexml.model.DocumentFactory;
import org.nexml.model.FloatEdge;
import org.nexml.model.Network;
import org.nexml.model.TreeBlock;
import org.xml.sax.SAXException;


/**
 * Takes an NeXML file. Parses it using the org.nexml library and constructs a Phylogeny object from
 * the parsed tree.
 * 
 * @author apurv
 */
public class NeXMLReader implements TreeReader{

    private final static Logger LOGGER = Logger.getLogger("nescent");

    
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
    public Phylogeny[] parseNetwork(File networkFile)throws SAXException, IOException, ParserConfigurationException{

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
     * Reads the nexml file and returns the array of phylogenies.<br>
     * If the coordinate metadata is present in the nexml file, it is attached to the node,
     * @param xmlFile
     * @return the array of Phylogenies
     */
    public Phylogeny[] readPhylogenyArray(File xmlFile){
        Phylogeny[] phylogenies = null;
        try{
            phylogenies = parseNetwork(xmlFile);
        }
        catch(SAXException e){
            LOGGER.log(Level.SEVERE,e.getMessage(),e);
        }
        catch(IOException e){
            LOGGER.log(Level.SEVERE,e.getMessage(),e);
        }
        catch(ParserConfigurationException e){
            LOGGER.log(Level.SEVERE,e.getMessage(),e);
        }
        return phylogenies;
    }

    /**
     * Reads and returns the first phylogeny.
     * To be used if the input file contains only a single phylogeny.
     * @param xmlFile
     * @return
     */
    public Phylogeny readPhylogeny(File xmlFile){
        return this.readPhylogenyArray(xmlFile)[0];
    }

    


    

}
