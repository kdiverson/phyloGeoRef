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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Reads metadata from a text file.
 * @author apurv
 */
public class TextMetadataReader implements MetadataReader{

    private final static Logger LOGGER = Logger.getLogger("nescent");

    
    private char delim;
    private File metaFile;
    private BufferedReader in;

    
    public TextMetadataReader(File metaFile, char delim){
        this.delim = delim;
        this.metaFile = metaFile;
        try {
            in = new BufferedReader(new FileReader(metaFile));
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Override
    public String[] readLine() {
        String line = null;
        try {
            line = in.readLine();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }

        if(line == null){
            return null;
        }
        return line.split(Character.toString(delim));
    }

}
