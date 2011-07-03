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

package nescent.phylogeoref.writer;

import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.MathContext;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.data.NodeData;
import org.forester.phylogeny.iterators.PhylogenyNodeIterator;

/**
 * This class defines a function to write the tree to a kml file for Google Earth.
 * Here style elements can be defined.
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class SimpleKmlWriter {

    private String fileName;

    boolean tesselate = false;
    String color = "7f0000ff";
    String folderName = "folder";
    String documentName = "document";
    String styleID = "lineStyle";
    boolean extruded = false;

    public SimpleKmlWriter(String fileName){
        this.fileName = fileName;
    }

    public SimpleKmlWriter setTesselate(boolean val) {
        this.tesselate = val;
        return this;
    }

    public SimpleKmlWriter setColor(String val){
        this.color = val;
        return this;
    }

    public SimpleKmlWriter setFolderName(String val) {
        this.folderName = val;
        return this;
    }

    public SimpleKmlWriter setDocumentName (String val) {
        this.documentName = val;
        return this;
    }

    public SimpleKmlWriter setStyleID (String val) {
        this.styleID = val;
        return this;
    }

    public SimpleKmlWriter setExtruded (boolean val) {
        this.extruded = val;
        return this;
    }

    /**
     * This is the main create kml function. It takes the phylogeny object and the name
     * of the kml file to be created.
     * 
     * @param my_phy
     * @throws FileNotFoundException
     */

    public void createKML(Phylogeny my_phy) throws FileNotFoundException {
        
        final Kml kml = new Kml();
        
        AltitudeMode altMode = AltitudeMode.ABSOLUTE;

        if (tesselate) altMode = AltitudeMode.CLAMP_TO_GROUND;

        Document doc = kml.createAndSetDocument().withName(documentName).withOpen(true);

        // create a Folder
        Folder folder = doc.createAndAddFolder();
        folder.withName(folderName).withOpen(true);

        final Style style = doc.createAndAddStyle().withId(styleID);

        style.createAndSetLineStyle().withColor(color).withWidth(3.0d);
        
        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPostorder(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();
            Distribution dist = data.getDistribution();

            Placemark placemarkLines = folder.createAndAddPlacemark();
            Placemark placemarkPoints = folder.createAndAddPlacemark();
            Placemark placemarkRoot = folder.createAndAddPlacemark();

            String coords = dist.getLongitude().toString() + ", " + dist.getLatitude().toString() + ", " + dist.getAltitude().toString();
            
            if (!node.isRoot()) {
                PhylogenyNode parentNode = node.getParent();
                NodeData parentData = parentNode.getNodeData();
                Distribution parentDist = parentData.getDistribution();
                String parentCoord = parentDist.getLongitude().toString() + ", " + parentDist.getLatitude().toString() + ", " + parentDist.getAltitude().toString();

                placemarkLines.withStyleUrl("#" + styleID).createAndSetLineString().withExtrude(extruded).withTessellate(tesselate).withAltitudeMode(altMode)
                        .addToCoordinates(coords).addToCoordinates(parentCoord);

            }

            if (node.isExternal()) {
                placemarkPoints.withName(node.getNodeName()).withDescription(node.getNodeName()).createAndSetPoint().addToCoordinates(dist.getLongitude().doubleValue(),
                        dist.getLatitude().doubleValue(),dist.getAltitude().doubleValue());
            }

        }        

        kml.marshal(new File(fileName));
    }
    
}


//            if (node.isRoot() ){
//                BigDecimal newLat = new BigDecimal("1000");
//                BigDecimal newLong = new BigDecimal("1000");
//                BigDecimal newAlt = new BigDecimal("1000");
//                String rootCoords = (dist.getLatitude().add(newLat).toString() + ", " + dist.getLongitude().add(newLong).toString() + ", " + dist.getAltitude().add(newAlt).toString());
//                placemarkRoot.createAndSetLineString().withExtrude(false).withTessellate(false).withAltitudeMode(AltitudeMode.ABSOLUTE)
//                        .addToCoordinates(coords).addToCoordinates(rootCoords);
//            }


            //need start and end coords

            //placemarkLines.withName(node.getNodeName()).createAndSetPoint().addToCoordinates(dist.getLatitude().doubleValue(), dist.getLongitude().doubleValue());
            //.createAndSetLookAt().withLongitude(dist.getLatitude().doubleValue())
//                    .withLatitude(dist.getLongitude().doubleValue())
//                    .withAltitude(dist.getAltitude().doubleValue());

            // set coordinates
            //placemarkLines.createAndSetPoint().addToCoordinates(dist.getLatitude().doubleValue(), dist.getLongitude().doubleValue());

            //System.out.println(kml.toString());