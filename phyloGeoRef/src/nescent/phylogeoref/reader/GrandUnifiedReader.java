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

import java.io.File;
import static java.lang.System.out;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.iterators.PhylogenyNodeIterator;

/**
 * Reads the raw, skeletal tree structure from a treeFile.
 * Cooks it along with metadata from metaFile 
 *
 * @author apurv
 */
public class GrandUnifiedReader {


    private final static Logger LOGGER = Logger.getLogger("nescent");

   

    private File treeFile;
    private File[] metaFile;
    
    private char delim;     // Delimiter character.    
    private int cladeDiv;  // Clade Classifier.

    private Integer[] args;
    private Map mouldMapArray[] = null;

    //Values to be returned by this class.
    Phylogeny[] phylogenies = null;    

    public GrandUnifiedReader() {
    }

    /**
     * 
     * @param treeFile
     * @param metaFile
     * @param delim delimiter character e.g. '\t ',  '\s '
     * @param cladeDiv  The column whose values have to be taken as the clade.
     */
    public GrandUnifiedReader(File treeFile, File[] metaFile, char delim, int cladeDiv) {
        this.treeFile = treeFile;
        this.metaFile = metaFile;
        this.delim = delim;
        this.cladeDiv = cladeDiv;
    }

    public Phylogeny[] getPhylogenyArray()throws NullPointerException {
        if (phylogenies !=null)
                return phylogenies;
        else
            throw new NullPointerException("getPhylogenyArray() should be called after buildUnifiedPhylogeny()");
    }

    public Phylogeny getPhylogeny()throws NullPointerException {
        if (phylogenies !=null)
                return phylogenies[0];
        else
            throw new NullPointerException("getPhylogeny() should be called after buildUnifiedPhylogeny()");
    }

    /**
     *
     * @return the array of mould maps which contain label, mould binding for each labeled node.
     * @throws NullPointerException
     */
    public Map[] getMouldMaps()throws NullPointerException{
        if(mouldMapArray != null){
            return mouldMapArray;
        }else{
            throw new NullPointerException("getMouldMaps() should be called after buildUnifiedPhylogeny()");
        }
    }

    /**
     * 
     * @return the first map in the array of mould maps.
     * @throws NullPointerException
     */
    public Map getMouldMap()throws NullPointerException{
        if(mouldMapArray != null){
            return mouldMapArray[0];
        }else{
            throw new NullPointerException("getMouldMaps() should be called after buildUnifiedPhylogeny()");
        }
    }

    /**
     * Sets the column whose values will be takes as the clade division.<br>
     * Specify 0, if no clade division is to be specified.
     * @param cladeDiv
     * @return
     */
    public GrandUnifiedReader setCladeDiv(int cladeDiv) {
        this.cladeDiv = cladeDiv;
        return this;
    }

    public GrandUnifiedReader setDelim(char delim) {
        this.delim = delim;
        return this;
    }

    public GrandUnifiedReader setMetaFile(File[] metaFile) {
        this.metaFile = metaFile;
        return this;
    }

    public GrandUnifiedReader setTreeFile(File treeFile) {
        this.treeFile = treeFile;
        return this;
    }



    /**
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
     *      <td> 4, third column is to be read as latitude      </td>
     *  </tr>
     *  <tr>
     *      <td> args[2] </td>
     *      <td> Longitude </td>
     *      <td> 3, fourth column to be read as longitude </td>
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
     * </table>
     * <br>
     * The trailing unused values can be left unspecified.
     */

    public GrandUnifiedReader setArgs(Integer...args) {
        //TODO: Add checks here whether or not user is specifying the arguments correctly.
        this.args = args;
        return this;
    }



    

    /**
     * Builds the full fledged Phylogeny.
     */
    public void buildUnifiedPhylogeny(){
        
        //Get an array of raw phylogenies.
        UniversalTreeReader utr = new UniversalTreeReader();
        phylogenies = utr.readPhylogenyArray(treeFile);

        mouldMapArray = new HashMap[phylogenies.length];
        int i = 0;
        //Iterate over each phylogeny and cook it up in the kitchen to get
        //full fledged phylogenies.
        for(Phylogeny phylogeny:phylogenies){

            mouldMapArray[i] = buildMouldMap(phylogeny);

            // Prepare a new kitchen
            PhylogenyKitchen kitchen = new PhylogenyKitchen(metaFile[i], phylogeny, mouldMapArray[i], delim, cladeDiv);

            // Set the recipe by matching the column values.
            kitchen.setRecipe(args);

            // Cook the tree
            kitchen.cookDish();
            i++;

        }
    }

    /**
     * Builds the mouldMap corresponding to the PhylogenyNode phy.
     * @param phy
     * @return
     */
    private Map<String,PhylogenyMould> buildMouldMap(Phylogeny phy){
        Map<String,PhylogenyMould> mouldMap = new HashMap<String, PhylogenyMould>();
        
        for( PhylogenyNodeIterator it = phy.iteratorPreorder(); it.hasNext();){

            PhylogenyNode node = it.next();
            String label = node.getNodeName();

            if(!label.equals("") && label != null){
                PhylogenyMould mould = new PhylogenyMould();
                mould.setAssociatedNode(node);
                mouldMap.put(label, mould);
            }
        }
        
        return mouldMap;
    }                   


}
