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

package nescent.phylogeoref;

import java.io.File;
import java.util.ArrayList;

import nescent.phylogeoref.geoData.parseCoords;
import nescent.phylogeoref.tree.getTree;

import org.forester.phylogeny.Phylogeny;

/**
 *
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class magic {

    ArrayList coordList;
    parseCoords pc;
    getTree gt;
    Phylogeny my_phy;
    calc3Dtree c3dt;

    public void toKML(File treeFile, String coordFile, String fileType, String metadata) {
       
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
