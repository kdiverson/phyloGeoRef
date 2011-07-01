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

import java.awt.Color;
import static java.lang.System.out;
import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.BranchColor;
import org.forester.phylogeny.data.BranchData;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.data.NodeData;
import org.forester.phylogeny.data.Taxonomy;

/**
 * Takes a raw, crude phylogenetic tree and metadata from a secondary file
 * and converts it into a cooked, full-fledged phylogenetic tree.
 * 
 * @author apurv
 */
public class PhylogenyKitchen {

    private final static Logger LOGGER = Logger.getLogger("nescent");
   
       

    private File metaFile;
    private Phylogeny phy;
    private int numCol; // number of columns in the input text file.
    private int numMaxCol; //maximum number of columns in var args.
    private char delim; //Delimiter character

    private int cladeDiv; // clade classifier.
                          // The values in this column will be used in classification in clades.
    
    private UniversalMetadataReader in;  //Reader for the metadata file.
    private Map<String,PhylogenyMould> map;
    private Map<String,BranchColor> cladeColorMap;
    private int index[];
    private String[] properties = null;
    private String[] propertyNames = null;
    private boolean[] attendanceInMould = null;

    private Random r;
    private Random g;
    private Random b;

    private final static int NaN = -1;

    private final static String[] PROPERTIES = {"label",
                                                "latitude",
                                                "longitude",
                                                "id",
                                                "sname",    //Scientific Name
                                                "cname",    //Common Name
                                               };


    public PhylogenyKitchen(File metaFile, Phylogeny phy, Map<String,PhylogenyMould> map, char delim, int cladeDiv) {
        this.metaFile = metaFile;
        this.phy = phy;
        this.map = map;
        this.delim = delim;
        this.cladeDiv = cladeDiv-1; //Subtracted 1 because 1st column means 0th index in an array.
        numCol = 0;

        if(this.cladeDiv !=NaN){
            cladeColorMap = new HashMap<String, BranchColor>();
        }

        r = new Random();
        g = new Random();
        b = new Random();


        in = new UniversalMetadataReader(metaFile,  delim);
    }

        

    /**
     * 
     * @param args
     * @see NeXMLReader readPhylogeny()
     */
    public void setRecipe(Integer...args){
        int l = PROPERTIES.length;
        numMaxCol = l;
        index = new int[l];
        Arrays.fill(index,0, l,NaN);

        for(int i=0;i<args.length;i++){
            if(args[i]!=0){
                numCol++;
                index[i]=args[i]-1;
            }
        }
    }

    /**
     * Cooks the raw data and prepare a cooked Phylogeny with all the metadata.
     */
    public void cookDish(){
        //Store the names of the properties which occur in the first row.
        propertyNames = in.readLine();                

        setAttendanceInMould();
        displayRecipe(propertyNames);

        while ((properties = in.readLine()) != null) {

            String label = get("label");

            PhylogenyMould mould = map.get(label);

            if(mould!=null){
                PhylogenyNode node = mould.getAssociatedNode();
                mixIngredients(node);
                fillMould(mould);

            }
            else {
                //Ignore any node whose details are given in the text file but not in the phylogeny.
                LOGGER.log(Level.CONFIG, "Ignored node named "+label+" in the metaFile");
            }
        }
    }

    /**
     * Sets the values of the array attendanceInMould[].<br>
     * attendaceInMould[i] = true denotes that value in 'i'th column will be stored in the external mould
     * attached with the phylogeny node.
     */
    private void setAttendanceInMould(){
        int l = propertyNames.length;
        attendanceInMould = new boolean[l];
        
        Arrays.fill(attendanceInMould,0, l,true);

        for(int i=0;i<index.length;i++){
            if(index[i]>=0){
                attendanceInMould[index[i]] = false;
            }
        }
    }

    /**
     * displays the recipe.
     * @param headerLine
     */
    private void displayRecipe(String[] headerLine){
        //LOGGER.log(Level.FINEST, "The recipe you specified is as follows");
        out.println("The recipe you specified is as follows");
        
        for(int i=0;i<numMaxCol;i++){
            if( index[i] != NaN){
                String part1 = String.format("%18s", headerLine[index[i]]);
                String part2 = String.format("%18s", PROPERTIES[i]);
                
                //LOGGER.log(Level.FINEST, part1 + " maps to: "+part2);
                out.println(part1 + " maps to: "+part2);
            }
        }
    }

    /**
     * Fills the extraneous values in the mould.
     * @param mould
     */
    private void fillMould(PhylogenyMould mould){

        for(int i=0;i<attendanceInMould.length;i++){
            if(attendanceInMould[i]){
                mould.storeValue(propertyNames[i], properties[i]);
            }
        }
    }

    private String getCladeValue(){
        String cladeValue = null;
        if(cladeDiv == NaN)
            cladeValue = null;
        else
            cladeValue = properties[cladeDiv];
        return cladeValue;
    }

    /**
     * Generates and returns a random color.
     * @return
     */
    private Color getRandomColor(){
        float red = r.nextFloat();
        float green = g.nextFloat();
        float blue = b.nextFloat();
        Color c = new Color(red, green, blue);
        return c;
    }

    /**
     * Gets the value of the property as a String. Null if the property is not present.
     * @param propName
     * @return "" if the index for that property was not specified or was zero.
     */
    private String get(String propName){
        int colNo = index(propName);
        String value = "";
        if(colNo != NaN){
            value = properties[colNo];
        }
        return value;
    }

    /**
     *
     * @param property
     * @return the column number of property.
     */
    private int index(String property){
        int ordinal = NaN;
        for(int i=0; i<PROPERTIES.length;i++){

            if(property.equalsIgnoreCase(PROPERTIES[i])){
                // index[i] is the column number of the ith property, i.e. PROPERTY[i].
                ordinal = index[i];
            }
        }
        return ordinal;
    }

    /**
     * Put your logic for patching values on the node here.<br><br>
     *
     * Set all the values for node.<br>
     * To get the value of any property use get(property name as specified in the string PROPERTIES)
     * @param node
     */
    private void mixIngredients(PhylogenyNode node)throws NullPointerException{

        setNodeData(node.getNodeData());

        // If the user has specified a colums to be taken as clade.
        setBranchData(node.getBranchData());
        
    }

    /**
     * Sets node data for this node.
     * @param nodeData
     */
    private void setNodeData(NodeData nodeData){

        Taxonomy taxo = new Taxonomy();
        nodeData.setTaxonomy(taxo);
        taxo.setScientificName(get("sname"));
        taxo.setCommonName(get("cname"));

        Distribution dist = new Distribution(get("label"));
        nodeData.setDistribution(dist);
        Float lat = Float.parseFloat(get("latitude"));
        Float lon = Float.parseFloat(get("longitude"));
        dist.setLatitude(new BigDecimal(lat));
        dist.setLongitude(new BigDecimal(lon));
        dist.setAltitude(BigDecimal.ZERO);
    }

    /**
     * Sets the branch data for this node.
     * @param nodeData
     */
    private void setBranchData(BranchData branchData){

        String clade = getCladeValue();

        //If no clade has been specfied then assign the same color to all nodes.
        if(cladeColorMap == null){
            Color constantColor = Color.ORANGE;
            BranchColor bc = new BranchColor(constantColor);
            branchData.setBranchColor(bc);
            return;
        }

        if(cladeColorMap.containsKey(clade)){
            BranchColor bc = cladeColorMap.get(clade);
            branchData.setBranchColor(bc);
        }
        else{
            Color newRandomColor = getRandomColor();
            BranchColor bc = new BranchColor(newRandomColor);
            branchData.setBranchColor(bc);
            
            cladeColorMap.put(clade, bc);
            out.println("Putting clade "+clade+ " in colorMap");
        }
    }
        
    

}
