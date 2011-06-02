package nescent;

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
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

import nescent.phylogeoref.geodata.ParseCoords;
import nescent.phylogeoref.tree.GetTree;
import nescent.phylogeoref.Calc3DTree;
import nescent.phylogeoref.kml.KmlWriter;
import nescent.phylogeoref.tree.NeXMLtoPhyObj;

import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.data.NodeData;
import org.forester.phylogeny.data.Taxonomy;
import org.forester.phylogeny.iterators.PhylogenyNodeIterator;
import org.forester.phylogeny.iterators.ExternalForwardIterator;
import org.forester.phylogeny.PhylogenyMethods;


/**
 *
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class testMain {   
    
    static ArrayList coordList;
    static ParseCoords pc = new ParseCoords();
    static GetTree gt = new GetTree();
    static Phylogeny my_phy = new Phylogeny();
    static Calc3DTree c3dt = new Calc3DTree();
    static KmlWriter kmlw = new KmlWriter();
    static NeXMLtoPhyObj ntp = new NeXMLtoPhyObj();
    
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

        String intreeFile ="src\\testTree.nwk"; //args[0];//c.readLine("Enter tree File: ");
        String coordFile = "src\\testCoords.csv";//args[1];//c.readLine("Enter coordinate File: ");
        String metadata = "n";//args[2];//c.readLine("Does this file have metadata (y/n): ");

        File treeFile = new File(intreeFile);
//        try {
//            my_phy = ntp.parseFloatTree(treeFile);
//        } catch (Throwable ex) {
//            Logger.getLogger(testMain.class.getName()).log(Level.SEVERE, null, ex);
//        }

        try {
           my_phy = gt.openTree(treeFile);
       } catch (Exception e) {
           System.out.println("Error: " + e.toString() );
       }

        if (metadata.equals("y")) coordList = pc.parseCSVwithMetadata(coordFile);

        else coordList = pc.parseCSV(coordFile);

       //c3dt.lazyAssignNodeCoords(my_phy, coordList);
       //c3dt.assignBinaryNodes(my_phy, coordList);
       //c3dt.assignNodeCoords(my_phy, coordList);
       c3dt.assignMeridianCoords(my_phy, coordList);
       //c3dt.assignMultipleObservations(my_phy, coordList);

//        try {
//            ntp.parseFloatTree(treeFile);
//        } catch (Throwable ex) {
//            Logger.getLogger(testMain.class.getName()).log(Level.SEVERE, null, ex);
//        }


        //my_phy.printExtNodes();
        //System.out.println(my_phy.toString());

//        for( PhylogenyNodeIterator ext_it = my_phy.iteratorExternalForward(); ext_it.hasNext();) {
//            PhylogenyNode node = ext_it.next();//ext_it.next();
//            NodeData data = node.getNodeData();
//            Distribution dist = data.getDistribution();
//            //Taxonomy tax = data.getTaxonomy();
//            //String name = tax.getScientificName();
//
//            BigDecimal alt = dist.getAltitude();
//            BigDecimal lat = dist.getLatitude();
//            BigDecimal lng = dist.getLongitude();
//
//            System.out.println(alt.toString());
//            System.out.println(lat.toString());
//            System.out.println(lng.toString());
//        }

        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPreorder(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();
            Distribution dist = data.getDistribution();

            BigDecimal alt = dist.getAltitude();
            BigDecimal lat = dist.getLatitude();
            BigDecimal lng = dist.getLongitude();

            System.out.println(node.getNodeName());
            System.out.println(alt.toString());
            System.out.println(lat.toString());
            System.out.println(lng.toString());

            System.out.println(dist.toString());
        }
        try {
            //testAssignCoords(treeFile, coordFile, metadata);
            
            kmlw.setTesselate(false).setExtruded(false).setStyleID("test").createKML(my_phy, "testfile.kml");
            //kmlw.createKML(my_phy, "testfile.kml");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(testMain.class.getName()).log(Level.SEVERE, null, ex);
        }

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
