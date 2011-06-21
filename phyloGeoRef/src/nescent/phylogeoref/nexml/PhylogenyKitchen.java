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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
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

    private final static Logger LOGGER = Logger.getLogger(PhylogenyKitchen.class.getName());

    static{
        setupLogger(); //Setup the logger at class load
    }

    private File metaFile;
    private Phylogeny phy;
    private int numCol; // number of columns in the input text file.
    private int numMaxCol; //maximum number of columns in var args.
    private char delim; //Delimiter character
    private char cladeDiv; //clade classifier
    private BufferedReader in;  //Reader for the text file.
    private Map<String,PhylogenyNode> map;
    private int index[];
    private String[] properties = null;

    private final static int NaN = -1;

    private final static String[] PROPERTIES = {"label",
                                                "latitude",
                                                "longitude",
                                                "id",
                                                "sname",    //Scientific Name
                                                "cname",    //Common Name
                                                "species",
                                                "genus",
                                                "family",
                                                "order",
                                                "class",
                                                "phylum"
                                               };


    public PhylogenyKitchen(File metaFile, Phylogeny phy, Map<String,PhylogenyNode> map, char delim, char cladeDiv) {
        this.metaFile = metaFile;
        this.phy = phy;
        this.map = map;
        this.delim = delim;
        this.cladeDiv = cladeDiv;
        numCol = 0;
        FileReader fin;

        try {
            fin = new FileReader(this.metaFile);
            in = new BufferedReader(fin);           
        }
        catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            System.exit(-1);
        }
        catch (IOException ex){
            LOGGER.log(Level.SEVERE, null, ex);
            System.err.println(ex.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Sets up the LOGGER.
     */
    private static void setupLogger(){
        LOGGER.setLevel(Level.ALL);
        try {

            FileHandler fhandler = new FileHandler("Logfile.txt");
            SimpleFormatter sformatter = new SimpleFormatter();
            fhandler.setFormatter(sformatter);
            LOGGER.addHandler(fhandler);
            
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param args
     * @see NeXMLReader readPhylogeny()
     */
    public void setRecipe(Integer...args){
        int l = args.length;
        numMaxCol = l;
        index = new int[l];
        Arrays.fill(index,0, l,NaN);

        for(int i=0;i<l;i++){
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
        String line = null;
        try {
            line = in.readLine();
            displayRecipe(line.split(Character.toString(delim)));

            while ((line = in.readLine()) != null) {
                properties = line.split(Character.toString(delim));

                String label = get("label");
                PhylogenyNode node = map.get(label);
                if(node!=null){
                    mixIngredients(node);
                }
                else {
                    //Ignore any node whose details are given in the text file but not in the phylogeny.
                    LOGGER.log(Level.CONFIG, "Ignored node named "+label+" in the metaFile");
                }
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * displays the recipe.
     * @param headerLine
     */
    private void displayRecipe(String[] headerLine){
        LOGGER.log(Level.FINEST, "The recipe you specified is as follows");
        
        for(int i=0;i<numMaxCol;i++){
            if( index[i] != NaN){
                String part1 = String.format("%18s", headerLine[index[i]]);
                String part2 = String.format("%18s", PROPERTIES[i]);
                
                LOGGER.log(Level.FINEST, part1 + " maps to: "+part2);
            }
        }
    }

    /**
     * Gets the value of the property as a String. Null if the property is not present.
     * @param propName
     * @return
     */
    private String get(String propName){
        int colNo = index(propName);
        String value = null;
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
        for(int i=0; i<numMaxCol;i++){

            if(property.equalsIgnoreCase(PROPERTIES[i])){
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
    }

    /**
     * Sets the branch data for this node.
     * @param nodeData
     */
    private void setBranchData(BranchData branchData){
        
    }
        
    

}
