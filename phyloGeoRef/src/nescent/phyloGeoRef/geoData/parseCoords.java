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
    static BigDecimal leafLat;
    static BigDecimal leafLong;
    static String metadata;

    ArrayList latLongArrLst = new ArrayList();

    public ArrayList<Triple> parseCSV(File infile) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile));
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = new BigDecimal(nextLine[1]);
                leafLong = new BigDecimal(nextLine[2]);
                Triple latLong = new Triple(leafName, leafLat, leafLong);
                latLongArrLst.add(latLong);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArrLst;
    }

    public ArrayList<Triple> parseCSV(File infile, int skipLines) {

        try {
            CSVReader reader = new CSVReader(new FileReader(infile), ',', '\'', skipLines);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = new BigDecimal(nextLine[1]);
                leafLong = new BigDecimal(nextLine[2]);
                Triple latLong = new Triple(leafName, leafLat, leafLong);
                latLongArrLst.add(latLong);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArrLst;
    }


    public ArrayList<Triple> parseTSV(File infile) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), '\t', '\'');
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = new BigDecimal(nextLine[1]);
                leafLong = new BigDecimal(nextLine[2]);
                Triple latLong = new Triple(leafName, leafLat, leafLong);
                latLongArrLst.add(latLong);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArrLst;
    }


    /**
     *
     * @param infile
     * @param skipLines
     * @return
     */
    public ArrayList<Triple> parseTSV(File infile, int skipLines) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), '\t', '\'', skipLines);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                //do something with
                leafName = nextLine[0];
                leafLat = new BigDecimal(nextLine[1]);
                leafLong = new BigDecimal(nextLine[2]);
                Triple latLong = new Triple(leafName, leafLat, leafLong);
                latLongArrLst.add(latLong);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArrLst;
    }

    
    /**
     * 
     * @param infile
     * @param delim
     * @return
     */
    public ArrayList<Triple> parseDelim(File infile, char delim) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), delim, '\'');
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = new BigDecimal(nextLine[1]);
                leafLong = new BigDecimal(nextLine[2]);
                Triple latLong = new Triple(leafName, leafLat, leafLong);
                latLongArrLst.add(latLong);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArrLst;
    }


    /**
     *
     * @param infile
     * @param delim
     * @param skipLines
     * @return
     */
    public ArrayList<Triple> parseDelim(File infile, char delim, int skipLines) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), delim, '\'', skipLines);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = new BigDecimal(nextLine[1]);
                leafLong = new BigDecimal(nextLine[2]);
                Triple latLong = new Triple(leafName, leafLat, leafLong);
                latLongArrLst.add(latLong);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArrLst;
    }


    public ArrayList<Quad> parseCSVwithMetadata(File infile) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile));
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = new BigDecimal(nextLine[1]);
                leafLong = new BigDecimal(nextLine[2]);
                metadata = nextLine[3];
                Quad latLong = new Quad(leafName, leafLat, leafLong, metadata);
                latLongArrLst.add(latLong);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArrLst;
    }

    public ArrayList<Quad> parseCSVwithMetadata(File infile, int skipLines) {

        try {
            CSVReader reader = new CSVReader(new FileReader(infile), ',', '\'', skipLines);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = new BigDecimal(nextLine[1]);
                leafLong = new BigDecimal(nextLine[2]);
                metadata = nextLine[3];
                Quad latLong = new Quad(leafName, leafLat, leafLong, metadata);
                latLongArrLst.add(latLong);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArrLst;
    }


    public List<Quad> parseTSVwithMetadata(File infile) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), '\t', '\'');
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = new BigDecimal(nextLine[1]);
                leafLong = new BigDecimal(nextLine[2]);
                metadata = nextLine[3];
                Quad latLong = new Quad(leafName, leafLat, leafLong, metadata);
                latLongArrLst.add(latLong);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArrLst;
    }


    /**
     *
     * @param infile
     * @param skipLines
     * @return
     */
    public ArrayList<Quad> parseTSVwithMetadata(File infile, int skipLines) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), '\t', '\'', skipLines);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                //do something with
                leafName = nextLine[0];
                leafLat = new BigDecimal(nextLine[1]);
                leafLong = new BigDecimal(nextLine[2]);
                metadata = nextLine[3];
                Quad latLong = new Quad(leafName, leafLat, leafLong, metadata);
                latLongArrLst.add(latLong);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArrLst;
    }


    /**
     *
     * @param infile
     * @param delim
     * @return
     */
    public ArrayList<Quad> parseDelimWithMetadata(File infile, char delim) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), delim, '\'');
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = new BigDecimal(nextLine[1]);
                leafLong = new BigDecimal(nextLine[2]);
                metadata = nextLine[3];
                Quad latLong = new Quad(leafName, leafLat, leafLong, metadata);
                latLongArrLst.add(latLong);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArrLst;
    }


    /**
     *
     * @param infile
     * @param delim
     * @param skipLines
     * @return
     */
    public ArrayList<Quad> parseDelimWithMetadata(File infile, char delim, int skipLines) {
        try {
            CSVReader reader = new CSVReader(new FileReader(infile), delim, '\'', skipLines);
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                leafName = nextLine[0];
                leafLat = new BigDecimal(nextLine[1]);
                leafLong = new BigDecimal(nextLine[2]);
                metadata = nextLine[3];
                Quad latLong = new Quad(leafName, leafLat, leafLong, metadata);
                latLongArrLst.add(latLong);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.toString());
        }
        return latLongArrLst;
    }
    
}
