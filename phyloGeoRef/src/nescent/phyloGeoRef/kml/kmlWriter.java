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

package nescent.phyloGeoRef.kml;

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
 *
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class kmlWriter {
    public void createKML(Phylogeny my_phy, String fileName) throws FileNotFoundException {
        
        final Kml kml = new Kml();

        Document doc = kml.createAndSetDocument().withName("Test Example1").withOpen(true);

        // create a Folder
        Folder folder = doc.createAndAddFolder();
        folder.withName("test tree").withOpen(true);

        final Style style = doc.createAndAddStyle().withId("linestyleExample");

        style.createAndSetLineStyle().withColor("7f0000ff").withWidth(7.0d);
        
        for( PhylogenyNodeIterator ext_it = my_phy.iteratorPostorder(); ext_it.hasNext();) {
            PhylogenyNode node = ext_it.next();
            NodeData data = node.getNodeData();
            Distribution dist = data.getDistribution();

            Placemark placemarkLines = folder.createAndAddPlacemark();
            Placemark placemarkPoints = folder.createAndAddPlacemark();
            Placemark placemarkRoot = folder.createAndAddPlacemark();

            String coords = dist.getLatitude().toString() + ", " + dist.getLongitude().toString() + ", " + dist.getAltitude().toString();
            
            if (!node.isRoot()) {
                PhylogenyNode parentNode = node.getParent();
                NodeData parentData = parentNode.getNodeData();
                Distribution parentDist = parentData.getDistribution();
                String parentCoord = parentDist.getLatitude().toString() + ", " + parentDist.getLongitude().toString() + ", " + parentDist.getAltitude().toString();



                placemarkLines.withStyleUrl("#linestyleExample").createAndSetLineString().withExtrude(false).withTessellate(false).withAltitudeMode(AltitudeMode.ABSOLUTE)
                        .addToCoordinates(coords).addToCoordinates(parentCoord);

            }

            if (node.isExternal()) {
                placemarkPoints.withName(node.getNodeName()).createAndSetPoint().addToCoordinates(dist.getLatitude().doubleValue(),
                        dist.getLongitude().doubleValue(),dist.getAltitude().doubleValue());
            }

//            if (node.isRoot() ){
//                BigDecimal newLat = new BigDecimal("1000");
//                BigDecimal newLong = new BigDecimal("1000");
//                BigDecimal newAlt = new BigDecimal("1000");
//                String rootCoords = (dist.getLatitude().add(newLat).toString() + ", " + dist.getLongitude().add(newLong).toString() + ", " + dist.getAltitude().add(newAlt).toString());
//                placemarkRoot.createAndSetLineString().withExtrude(false).withTessellate(false).withAltitudeMode(AltitudeMode.ABSOLUTE)
//                        .addToCoordinates(coords).addToCoordinates(rootCoords);
//            }

        }        

        kml.marshal(new File(fileName));
    }
    
}





            //need start and end coords

            //placemarkLines.withName(node.getNodeName()).createAndSetPoint().addToCoordinates(dist.getLatitude().doubleValue(), dist.getLongitude().doubleValue());
            //.createAndSetLookAt().withLongitude(dist.getLatitude().doubleValue())
//                    .withLatitude(dist.getLongitude().doubleValue())
//                    .withAltitude(dist.getAltitude().doubleValue());

            // set coordinates
            //placemarkLines.createAndSetPoint().addToCoordinates(dist.getLatitude().doubleValue(), dist.getLongitude().doubleValue());

            //System.out.println(kml.toString());