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
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Reads metadata from all types of files.
 * @author apurv
 */
public class UniversalMetadataReader implements MetadataReader{


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

    

    private char delim;
    private File metaFile;
    private MetadataReader in;

    public UniversalMetadataReader(File metaFile, char delim){
        this.metaFile = metaFile;
        this.delim = delim;

        String extension = getFileExtension(metaFile);

        if(extension.equalsIgnoreCase(".csv")){
            in = new CSVMetadataReader(metaFile, delim);
        }
        else if(extension.equalsIgnoreCase(".txt")){
            in = new TextMetadataReader(metaFile, delim);
        }
        else{
            LOGGER.log(Level.WARNING, "Only .csv and .txt files are supported. Exiting ...");
            System.exit(-1);
        }
    }

    @Override
    public String[] readLine() {
        return in.readLine();
    }

    /**
     * Returns the file extension for file.
     * @param file
     * @return
     */
    private String getFileExtension(File file){
        String extension = null;
        String filename = file.getName();

        int dotPos = filename.lastIndexOf(".");
        extension = filename.substring(dotPos);
        
        return extension;
    }

    

}
