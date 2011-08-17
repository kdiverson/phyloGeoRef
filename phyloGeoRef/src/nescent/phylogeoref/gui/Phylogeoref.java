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
    public static void main(String...args) throws Throwable{

        File treeFile = new File("samples\\treeHypothetical\\tree.nwk");
        File metaFile=new File("samples\\treeHypothetical\\test.csv");        
        
        File[] metaFiles = new File[]{metaFile};


        GrandUnifiedReader gur = new GrandUnifiedReader();
        
        gur.setTreeFile(treeFile).setMetaFile(metaFiles).setDelim(',').setCladeDiv(4);
        
        gur.setArgs(1,2,3);

        gur.buildUnifiedPhylogeny();
                
        Phylogeny phyArray[] =  gur.getPhylogenyArray();
        Map mouldMapArray[] = gur.getMouldMaps();
        

        PhylogenyProcessor processor = ProcessorFactory.getInstance(false);
        AdvancedKmlWriter kmlw = new AdvancedKmlWriter(PaintStyle.HIERARCHICAL);
        
        for(int i=0; i<phyArray.length; i++){

            processor.phylogenify(phyArray[i]);

            kmlw.createKMZ(phyArray[i], mouldMapArray[i], "mojo");
            
        }
    }
}