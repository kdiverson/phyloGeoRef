/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nescent.phyloGeoRef.geoData;

import java.io.*;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
/**
 *
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class parseCoords {

    List coordList = null;

    List parseCSV(File infile) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile));
            coordList = reader.readAll();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return coordList;
    }

    List parseTSV(File infile) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), '\t');
            coordList = reader.readAll();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return coordList;
    }

    List parseDelim(File infile, char delim) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), delim);
            coordList = reader.readAll();
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return coordList;
    }
    
}
