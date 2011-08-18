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

package nescent.phylogeoref.gui;

import java.io.File;
import java.io.IOException;
import static java.lang.System.out;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import nescent.phylogeoref.processor.PhylogenyProcessor;
import nescent.phylogeoref.processor.ProcessorFactory;
import nescent.phylogeoref.reader.GrandUnifiedReader;
import nescent.phylogeoref.writer.AdvancedKmlWriter;
import nescent.phylogeoref.writer.PaintStyle;
import org.forester.phylogeny.Phylogeny;

/**
 * Main class
 * @author apurv
 */
public class Phylogeoref {

    private final static Logger LOGGER = Logger.getLogger("nescent");

    static{
        setupLogger(); //Setup the logger at class load
    }

    /**
     * Sets up the logger.
     */
    private static void setupLogger(){
        LOGGER.setLevel(Level.ALL);
        try {
                FileHandler fhandler = new FileHandler("Logfile.txt");
                SimpleFormatter sformatter = new SimpleFormatter();
                fhandler.setFormatter(sformatter);
                LOGGER.addHandler(fhandler);

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (SecurityException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    


    /**
     * Utility main method, used for testing this file during development.
     * @param args
     * @throws Throwable
     */
    public void generate(PhyloGeoRefWidget phyWidget, boolean compress){
        
        //Get all the values.

        //Get the files.
        File treeFile = phyWidget.getTreeFile();
        File metaFile = phyWidget.getMetaFile();
        
        //TODO: This needs to be modfied for other type of delimeters.
        //TODO: Currently supports any normal char, tab and space.
        //Get the delimiter character.
        String delimText = phyWidget.getDelim().getText();
        char delim = ',';
        if(delimText !=null && !delimText.equals("")){
            
            if(delimText.equals("\\t"))
                delim = '\t';                       
            
        }
        
        
        //Get which column is to be used as the clade.
        int cladeDiv = 0;
        String cladeDivText = phyWidget.getCladeDiv().getText();
        if(cladeDivText != null && !cladeDivText.equals("")){
            cladeDiv = Integer.parseInt(cladeDivText);            
        }
        
        //Get which column is to be used as the label.
        int label = 0;
        String labelText = phyWidget.getLabel().getText();
        try{
            label = Integer.parseInt(labelText);
            
        }catch(NumberFormatException ex){
            phyWidget.showMessageDialog("Invalid value for label column");
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        
        
        //Get the latitude column.
        int lat=2;
        String latText = phyWidget.getLatitude().getText();
        try{
        lat = Integer.parseInt(latText);
        
        }catch(NumberFormatException ex){
            phyWidget.showMessageDialog("Invalid value for latitude column");
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
        
        //Get the longitude column.
        int lon=3;
        String lonText = phyWidget.getLongitude().getText();
        try{
        lon = Integer.parseInt(lonText);
        
        }catch(NumberFormatException ex){
            phyWidget.showMessageDialog("Invalid value for longitude column");
            LOGGER.log(Level.SEVERE, ex.getMessage());
        }
                 
        //Get the id column.
        int id = 0;
        String idText = phyWidget.getId().getText();
        if(idText!=null && !idText.equals("")){
            id = Integer.parseInt(idText);
        }
        
        //Get the column for scientific name.
        int sname = 0;
        String snameText = phyWidget.getSname().getText();
        if(snameText!=null && !snameText.equals("")){
            sname = Integer.parseInt(snameText);
        }
        
        //Get the column for common name.
        int cname = 0;
        String cnameText = phyWidget.getCname().getText();
        if(cnameText!=null && !cnameText.equals("")){
            cname = Integer.parseInt(cnameText);
        }
        
        
        File[] metaFiles = new File[]{metaFile};

        GrandUnifiedReader gur = new GrandUnifiedReader();        
        gur.setTreeFile(treeFile).setMetaFile(metaFiles).setDelim(delim).setCladeDiv(cladeDiv);        
        gur.setArgs(label, lat, lon, id, sname, cname);
        
        System.out.println("\n Building the phylogeny ... ");
        
        gur.buildUnifiedPhylogeny();
                
        Phylogeny phyArray[] =  gur.getPhylogenyArray();
        Map mouldMapArray[] = gur.getMouldMaps();
        
        int pStyleNum = phyWidget.getPaintStyle().getSelectedIndex();
        int hasWeightNum = phyWidget.getWeighted().getSelectedIndex();
        
        //TODO: This needs to be modified when more styles are added into phlyogeoref.
        PaintStyle pStyle = null;
        if(pStyleNum == 0){
            pStyle = PaintStyle.HIERARCHICAL;
            
        }else if(pStyleNum == 1){
            pStyle = PaintStyle.LEVELWISE;
            
        }
        
        boolean hasWeight = false;
        if(hasWeightNum == 0){
            hasWeight = true;
                    
        }else{
            hasWeight = false;
                    
        }

        PhylogenyProcessor processor = ProcessorFactory.getInstance(hasWeight);
        AdvancedKmlWriter kmlw = new AdvancedKmlWriter(pStyle);
        
        for(Integer i=0; i<phyArray.length; i++){

            processor.phylogenify(phyArray[i]);

            if(compress){
                System.out.println("\n Writing the kml, compressing to kmz ...");
                kmlw.createKMZ(phyArray[i], mouldMapArray[i], "outputTree-"+i.toString());
                
            }else{
                System.out.println("\n Writing the kml ...");
                kmlw.createKML(phyArray[i], mouldMapArray[i], "outputTree-"+i.toString());
            }
            
        }
        System.out.println("\n Open the output file in google earth browser to view the tree.");
        System.out.println("\n You can close the window to exit.");
    }
}