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

package nescent.phylogeoref.geoData;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

import au.com.bytecode.opencsv.CSVReader;

import nescent.phylogeoref.Triple;
import nescent.phylogeoref.Quad;

/**
 * coords should be stored as big decimals in an array of big decimals
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class parseCoords {

    //static List coordList;
    static String leafName;
    static BigDecimal leafLat;
    static BigDecimal leafLong;
    static String metadata;

    ArrayList latLongArrLst = new ArrayList();

    /**
     * Parse a csv file with no header or metadata
     * @param infile
     * @return
     */

    public ArrayList<Triple> parseCSV(String infile) {
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

    /**
     * Parse a csv file skipping x lines, where x is parameter skiplines and no metadata
     * @param infile
     * @param skipLines
     * @return
     */

    public ArrayList<Triple> parseCSV(String infile, int skipLines) {

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


    /**
     * parse a tsv file with no header lines or metadata.
     * @param infile
     * @return
     */

    public ArrayList<Triple> parseTSV(String infile) {
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
     * prase a tsv file skipping x lines where x is parameter skiplines and no metadata
     * @param infile
     * @param skipLines
     * @return
     */
    public ArrayList<Triple> parseTSV(String infile, int skipLines) {
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
     * parse a file that is deliminated by "delim" ie a semicolon deliminated file
     * with no metadata or headers
     * @param infile
     * @param delim
     * @return
     */
    public ArrayList<Triple> parseDelim(String infile, char delim) {
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
     * parse a deliminated file with a header and no metadata
     * @param infile
     * @param delim
     * @param skipLines
     * @return
     */
    public ArrayList<Triple> parseDelim(String infile, char delim, int skipLines) {
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


    /**
     * parse a csv file with metadata and no header
     * @param infile
     * @return
     */

    public ArrayList<Quad> parseCSVwithMetadata(String infile) {
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

    /**
     * parse a csv file with metadata and header line(s)
     * @param infile
     * @param skipLines
     * @return
     */

    public ArrayList<Quad> parseCSVwithMetadata(String infile, int skipLines) {

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


    /**
     * parse a tsv file with metadata and no header lines
     * @param infile
     * @return
     */

    public List<Quad> parseTSVwithMetadata(String infile) {
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
     * parse a tsv file with metadata and header lines
     * @param infile
     * @param skipLines
     * @return
     */
    public ArrayList<Quad> parseTSVwithMetadata(String infile, int skipLines) {
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
     * parse a deliminated file with metadata
     * @param infile
     * @param delim
     * @return
     */
    public ArrayList<Quad> parseDelimWithMetadata(String infile, char delim) {
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
     * parse a deliminated file with metadata and header line(s)
     * @param infile
     * @param delim
     * @param skipLines
     * @return
     */
    public ArrayList<Quad> parseDelimWithMetadata(String infile, char delim, int skipLines) {
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
