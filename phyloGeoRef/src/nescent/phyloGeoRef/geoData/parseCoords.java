/*
 *  Copyright (C) 2010 Kathryn Iverson <kd.iverson at gmail.com>
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

package nescent.phyloGeoRef.geoData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.math.BigDecimal;

import au.com.bytecode.opencsv.CSVReader;

import nescent.phyloGeoRef.Triple;
import nescent.phyloGeoRef.Quad;

/**
 *coords should be stored as big decimals in an array of big decimals
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class parseCoords {

    //static List coordList;
    static String leafName;
    static String leafLat;
    static String leafLong;
    static String metadata;

    ArrayList latLongArr = new ArrayList();

    public ArrayList parseCSV(File infile) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile));
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = nextLine[1];
                leafLong = nextLine[2];
                Triple latLong = new Triple(leafName, leafLat, leafLong);
                latLongArr.add(latLong);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArr;
    }

    public ArrayList parseCSV(File infile, int skipLines) {

        try {
            CSVReader reader = new CSVReader(new FileReader(infile), ',', '\'', skipLines);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = nextLine[1];
                leafLong = nextLine[2];
                Triple latLong = new Triple(leafName, leafLat, leafLong);
                latLongArr.add(latLong);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArr;
    }


    public List parseTSV(File infile) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), '\t', '\'');
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = nextLine[1];
                leafLong = nextLine[2];
                Triple latLong = new Triple(leafName, leafLat, leafLong);
                latLongArr.add(latLong);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArr;
    }


    /**
     *
     * @param infile
     * @param skipLines
     * @return
     */
    public ArrayList parseTSV(File infile, int skipLines) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), '\t', '\'', skipLines);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                //do something with
                leafName = nextLine[0];
                leafLat = nextLine[1];
                leafLong = nextLine[2];
                Triple latLong = new Triple(leafName, leafLat, leafLong);
                latLongArr.add(latLong);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArr;
    }

    
    /**
     * 
     * @param infile
     * @param delim
     * @return
     */
    public ArrayList parseDelim(File infile, char delim) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), delim, '\'');
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = nextLine[1];
                leafLong = nextLine[2];
                Triple latLong = new Triple(leafName, leafLat, leafLong);
                latLongArr.add(latLong);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArr;
    }


    /**
     *
     * @param infile
     * @param delim
     * @param skipLines
     * @return
     */
    public ArrayList parseDelim(File infile, char delim, int skipLines) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), delim, '\'', skipLines);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = nextLine[1];
                leafLong = nextLine[2];
                Triple latLong = new Triple(leafName, leafLat, leafLong);
                latLongArr.add(latLong);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArr;
    }


    public ArrayList parseCSVwithMetadata(File infile) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile));
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = nextLine[1];
                leafLong = nextLine[2];
                metadata = nextLine[3];
                Quad latLong = new Quad(leafName, leafLat, leafLong, metadata);
                latLongArr.add(latLong);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArr;
    }

    public ArrayList parseCSVwithMetadata(File infile, int skipLines) {

        try {
            CSVReader reader = new CSVReader(new FileReader(infile), ',', '\'', skipLines);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = nextLine[1];
                leafLong = nextLine[2];
                metadata = nextLine[3];
                Quad latLong = new Quad(leafName, leafLat, leafLong, metadata);
                latLongArr.add(latLong);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArr;
    }


    public List parseTSVwithMetadata(File infile) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), '\t', '\'');
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = nextLine[1];
                leafLong = nextLine[2];
                metadata = nextLine[3];
                Quad latLong = new Quad(leafName, leafLat, leafLong, metadata);
                latLongArr.add(latLong);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArr;
    }


    /**
     *
     * @param infile
     * @param skipLines
     * @return
     */
    public ArrayList parseTSVwithMetadata(File infile, int skipLines) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), '\t', '\'', skipLines);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                //do something with
                leafName = nextLine[0];
                leafLat = nextLine[1];
                leafLong = nextLine[2];
                metadata = nextLine[3];
                Quad latLong = new Quad(leafName, leafLat, leafLong, metadata);
                latLongArr.add(latLong);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArr;
    }


    /**
     *
     * @param infile
     * @param delim
     * @return
     */
    public ArrayList parseDelimWithMetadata(File infile, char delim) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), delim, '\'');
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = nextLine[1];
                leafLong = nextLine[2];
                metadata = nextLine[3];
                Quad latLong = new Quad(leafName, leafLat, leafLong, metadata);
                latLongArr.add(latLong);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArr;
    }


    /**
     *
     * @param infile
     * @param delim
     * @param skipLines
     * @return
     */
    public ArrayList parseDelimWithMetadata(File infile, char delim, int skipLines) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), delim, '\'', skipLines);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = nextLine[1];
                leafLong = nextLine[2];
                metadata = nextLine[3];
                Quad latLong = new Quad(leafName, leafLat, leafLong, metadata);
                latLongArr.add(latLong);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArr;
    }
    
}
