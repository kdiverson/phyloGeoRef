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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import org.forester.phylogeny.Phylogeny;
import org.forester.util.ForesterUtil;

/**
 * Reads all kinds of input tree files.
 * @author apurv
 */
public class UniversalTreeReader implements TreeReader{

    private final static Logger LOGGER = Logger.getLogger(PhylogenyKitchen.class.getName());

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
     * Reads and returns the first phylogeny.
     * To be used if the input file contains only a single phylogeny.
     * @param treeFile
     * @return
     */
    @Override
    public Phylogeny readPhylogeny(File treeFile) {
        return readPhylogenyArray(treeFile)[0];
    }


    /**
     * Reads the tree file and returns the array of phylogenies.<br>
     * @param treeFile
     * @return the array of Phylogenies
     */
    @Override
    public Phylogeny[] readPhylogenyArray(File treeFile) {
        Phylogeny[] phylogenies = null;
        try {
            String first_line = ForesterUtil.getFirstLine(treeFile).trim().toLowerCase();
            if ( first_line.startsWith( "<nex:" ) ) {
                TreeReader nexmlReader = new NeXMLReader();
                phylogenies = nexmlReader.readPhylogenyArray(treeFile);                
            }
            else{
                TreeReader multiFormatReader = new MultiFormatReader();
                phylogenies = multiFormatReader.readPhylogenyArray(treeFile);
            }
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }

        return phylogenies;
    }    

}
