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

import java.io.File;
import java.io.Console;
import java.util.ArrayList;

import nescent.phyloGeoRef.geoData.parseCoords;
import nescent.phyloGeoRef.tree.getTree;
import nescent.phyloGeoRef.calc3Dtree;

import org.forester.phylogeny.Phylogeny;

/**
 *
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class testMain {   
    
    static ArrayList coordList;
    static parseCoords pc = new parseCoords();
    static getTree gt = new getTree();
    static Phylogeny my_phy = new Phylogeny();
    static calc3Dtree c3dt = new calc3Dtree();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

//        Console c = System.console();
//        if (c == null) {
//            System.err.println("No console.");
//            System.exit(1);
//        }

        String intreeFile = args[0];//c.readLine("Enter tree File: ");
        String coordFile = args[1];//c.readLine("Enter coordinate File: ");
        String metadata = args[2];//c.readLine("Does this file have metadata (y/n): ");

        File treeFile = new File(intreeFile);
        //File coordFile = new File(incoordFile);

        try {
           my_phy = gt.openTree(treeFile);
       } catch (Exception e) {
           System.out.println("Error: " + e.toString() );
       }

        coordList = pc.parseCSV(coordFile);
        
        c3dt.lazyAssignNodeCoords(my_phy, coordList);

        my_phy.printExtNodes();

        //testAssignCoords(treeFile, coordFile, metadata);

    }

    public static void testAssignCoords(File treeFile, String coordFile, String metadata) {

       if ( metadata.equalsIgnoreCase("y") ) {
           coordList = pc.parseCSVwithMetadata(coordFile);

       }
       else {
           coordList = pc.parseCSV(coordFile);
       }

       try {
           my_phy = gt.openTree(treeFile);
       } catch (Exception e) {
           System.out.println("Error: " + e.toString() );
       }

       c3dt.assignNodeCoords(my_phy, coordList);

       //Write KML!!
    }

}
