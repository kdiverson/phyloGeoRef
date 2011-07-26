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

package nescent.phylogeoref.writer.utility;

import de.micromata.opengis.kml.v_2_2_0.AltitudeMode;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.Pair;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.StyleMap;
import de.micromata.opengis.kml.v_2_2_0.StyleState;
import static java.lang.System.out;

/**
 * Contains pure kml drawing functions that draw complex imagery in kml from simple types.<br>
 * This class strictly contains methods that draw generic kml features. No types defined in the
 * forester library are to be used here.
 * <br>
 * @author apurv
 */
public class KmlToolkit {

    private final static double LINE_WIDTH = 1.00d;
    private final static double LINE_WIDTH_MULTIPLY_FACTOR = 3.00d;

    private final static int N = 72;
    private final static double ARC_QUANTUM = Math.PI/N;


    /**
     * Prepares a new LineString inside this folder with the specified color.
     * @param folder
     * @param color
     * @return
     */
    public static LineString drawNewStyledLine(Folder folder, String color){

        Placemark placemark = folder.createAndAddPlacemark();

        //Create a style map.
        StyleMap styleMap = placemark.createAndAddStyleMap();

        //Create the style for normal edge.
        Pair pNormal = styleMap.createAndAddPair();
        Style normalStyle = pNormal.createAndSetStyle();
        pNormal.setKey(StyleState.NORMAL);

        LineStyle normalLineStyle = normalStyle.createAndSetLineStyle();
        normalLineStyle.setColor(color);
        normalLineStyle.setWidth(LINE_WIDTH);


        //Create the style for highlighted edge.
        Pair pHighlight = styleMap.createAndAddPair();
        Style highlightStyle = pHighlight.createAndSetStyle();
        pHighlight.setKey(StyleState.HIGHLIGHT);

        LineStyle highlightLineStyle = highlightStyle.createAndSetLineStyle();
        highlightLineStyle.setColor(color);
        highlightLineStyle.setWidth(LINE_WIDTH_MULTIPLY_FACTOR*LINE_WIDTH);

        //Create the line.
        LineString line = placemark.createAndSetLineString();
        line.setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);

        return line;
    }


    /**
     * Extends the line along a tessellated curved path from fromCoord to destCoord.
     * @param fromCoord
     * @param toCoord
     */
    public static void drawCurvedLine(LineString line, Coordinate fromCoord, Coordinate toCoord){

        
        double d = AMath.getGreatCircleDistance(fromCoord, toCoord);
        double n = getNumberOfSegments(d);
        double q = 1.0/n;

        //Assumption: Both lat and lon are zero together when d=0.
        if( d==0){
            return;
        }

        for(double f = 0; f< 1.0; f+=q){

            Coordinate coordinate = AMath.getIntermediateCoordinate(fromCoord,toCoord,d, f);
            double lat = coordinate.getLatitude();
            double lon = coordinate.getLongitude();
            double alt = coordinate.getAltitude();            

            line.addToCoordinates(lon, lat, alt);
        }

        //Add the final point. Keeping it in the loop does not achieve the value f=1.0 always.
        double f = 1.0;
        Coordinate coordinate = AMath.getIntermediateCoordinate(fromCoord,toCoord,d, f);
        double lat = coordinate.getLatitude();
        double lon = coordinate.getLongitude();
        double alt = coordinate.getAltitude();

        line.addToCoordinates(lon, lat, alt);

    }

    /**
     * Returns the number of line segments to be drawn in a greatCircleDistance.
     * @param greatCircleDistance
     * @return
     */
    private static double getNumberOfSegments(double greatCircleDistance){
        double n = Math.ceil( greatCircleDistance / ARC_QUANTUM);
        return n;
    }


}
