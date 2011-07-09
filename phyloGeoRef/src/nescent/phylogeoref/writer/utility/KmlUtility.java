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
import de.micromata.opengis.kml.v_2_2_0.ColorMode;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.IconStyle;
import de.micromata.opengis.kml.v_2_2_0.LatLonAltBox;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.Lod;
import de.micromata.opengis.kml.v_2_2_0.Pair;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.Region;
import de.micromata.opengis.kml.v_2_2_0.Style;
import de.micromata.opengis.kml.v_2_2_0.StyleMap;
import de.micromata.opengis.kml.v_2_2_0.StyleState;
import de.micromata.opengis.kml.v_2_2_0.ViewRefreshMode;
import static java.lang.System.out;
import java.awt.Color;
import nescent.phylogeoref.reader.PhylogenyMould;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.BranchColor;
import org.forester.phylogeny.data.BranchData;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.data.NodeData;

/**
 * Provides the necessary utility functions while painting a kml.
 * @author apurv
 */
public class KmlUtility {

    private final static double UNDEFINED = -1.0d;

    private final static double MIN_LOD_PIXELS_LEVEL_OUTER = 0;
    private final static double MAX_LOD_PIXELS_LEVEL_OUTER = 127;
    
    private final static double MIN_LOD_PIXELS_LEVEL_MIDDLE = 128;
    private final static double MAX_LOD_PIXELS_LEVEL_MIDDLE = 1024;

    private final static double MIN_LOD_PIXELS_LEVEL_INTERNAL = 1025;
    private final static double MAX_LOD_PIXELS_LEVEL_INTERNAL = -1;
    
    private final static double MIN_FADE_EXTENT = 0;
    private final static double MAX_FADE_EXTENT = 0;

    private final static double DELTA_L = 2.5;
    private final static double ELEVATION = 0;

    //The color of the hypothetical taxonomic unit placemarks.
    private final static String HTU_COLOR="ff0ff9ff";

    /**
     * Creates a new folder inside document with given folderName and description.
     * @param document
     * @param folderName
     * @param description
     * @return
     */
    public static Folder createFolder(Document document, String folderName, String description){
        Folder folder = document.createAndAddFolder();
        folder.withName(folderName);
        folder.withDescription(description);
        return folder;
    }

    
    /**
     * Creates a placemark for the external node, node<br>
     * More about regions can be found on this page.<br>
     * @see http://code.google.com/apis/kml/documentation/kml_21tutorial.html#workingregions<br>
     *
     * There will be three nested regions created, one will be most distant with just the color.<br>
     * Second will be with the condensed name.
     * Third will be for the complete information.
     * All the three will have the complete balloon information associated. So the user can view it
     * from any level.
     * @param folder
     * @param node
     * @param mould
     */
    public static void createExternalPlacemark(Folder folder, PhylogenyNode node, PhylogenyMould mould){

        if(!hasValidLocation(node)){
            return;
        }

        //Specify the position of the node.
        double latitude = getLatitude(node);
        double longitude = getLongitude(node);
        double altitude = getAltitude(node);


        //Create the level 1 folder.
        Folder outerFolder = folder.createAndAddFolder();
        outerFolder.withName(node.getNodeName());

        //Create the level 1 placemark. (outermost placemark)
        Placemark outerPlacemark = outerFolder.createAndAddPlacemark();
        outerPlacemark.setDescription(node.getNodeName());

        Point p = outerPlacemark.createAndSetPoint();
        p.setAltitudeMode(AltitudeMode.ABSOLUTE);
        p.addToCoordinates(longitude, latitude, altitude);

        //Specify a region inside this placemark.
        Region outerRegion = outerFolder.createAndSetRegion();

        //Specify the color of this placemark.
        Style style = outerPlacemark.createAndAddStyle();
        IconStyle iconStyle = style.createAndSetIconStyle();
        String color = getColor(node);
        iconStyle.setColor(color);
        iconStyle.setColorMode(ColorMode.NORMAL);
        iconStyle.setScale(0.80);

        //Specify the icon with this placemark.
        Icon icon = iconStyle.createAndSetIcon();
        icon.setHref("http://maps.google.com/mapfiles/kml/paddle/wht-blank.png");

        LatLonAltBox latlonBox = outerRegion.createAndSetLatLonAltBox();
        latlonBox.setEast(longitude + DELTA_L);
        latlonBox.setWest(longitude - DELTA_L);
        latlonBox.setNorth(latitude + DELTA_L);
        latlonBox.setSouth(latitude - DELTA_L);
        latlonBox.setMinAltitude(altitude);
        latlonBox.setMaxAltitude(altitude+DELTA_L);
        latlonBox.setAltitudeMode(AltitudeMode.ABSOLUTE);

        Lod lod = outerRegion.createAndSetLod();
        lod.setMinLodPixels(MIN_LOD_PIXELS_LEVEL_OUTER);
        lod.setMaxLodPixels(MAX_LOD_PIXELS_LEVEL_OUTER);
        lod.setMinFadeExtent(MIN_FADE_EXTENT);
        lod.setMaxFadeExtent(MAX_FADE_EXTENT);

        createMiddlePlacemark(outerFolder, node, mould);     
    }

    
    /**
     * Creates the middle level.
     * @param outerFolder
     * @param node
     * @param mould
     */
    private static void createMiddlePlacemark(Folder outerFolder, PhylogenyNode node, PhylogenyMould mould){

        //Specify the position of the node.
        double latitude = getLatitude(node);
        double longitude = getLongitude(node);
        double altitude = getAltitude(node);

        //Create the level 2 folder.
        Folder middleFolder = outerFolder.createAndAddFolder();

        //Create the level 2 placemark. (middle placemark)
        Placemark middlePlacemark = middleFolder.createAndAddPlacemark();
        middlePlacemark.setName(getCondensedName(node.getNodeName()));
        middlePlacemark.setDescription(node.getNodeName());

        Point p = middlePlacemark.createAndSetPoint();
        p.setAltitudeMode(AltitudeMode.ABSOLUTE);
        p.addToCoordinates(longitude, latitude, altitude);

        //Specify a region inside this placemark.
        Region middleRegion = middleFolder.createAndSetRegion();

        //Specify the color of this placemark.
        Style style = middlePlacemark.createAndAddStyle();
        IconStyle iconStyle = style.createAndSetIconStyle();
        String color = getColor(node);
        iconStyle.setColor(color);
        iconStyle.setColorMode(ColorMode.NORMAL);
        iconStyle.setScale(0.90);

        //Specify the icon with this placemark.
        Icon icon = iconStyle.createAndSetIcon();
        icon.setHref("http://maps.google.com/mapfiles/kml/paddle/wht-blank.png");
        icon.setRefreshInterval(4);

        LatLonAltBox latlonBox = middleRegion.createAndSetLatLonAltBox();
        latlonBox.setEast(longitude + DELTA_L);
        latlonBox.setWest(longitude - DELTA_L);
        latlonBox.setNorth(latitude + DELTA_L);
        latlonBox.setSouth(latitude - DELTA_L);
        latlonBox.setMinAltitude(altitude);
        latlonBox.setMaxAltitude(altitude+DELTA_L);
        latlonBox.setAltitudeMode(AltitudeMode.ABSOLUTE);

        Lod lod = middleRegion.createAndSetLod();
        lod.setMinLodPixels(MIN_LOD_PIXELS_LEVEL_MIDDLE);
        lod.setMaxLodPixels(MAX_LOD_PIXELS_LEVEL_MIDDLE);
        lod.setMinFadeExtent(MIN_FADE_EXTENT);
        lod.setMaxFadeExtent(MAX_FADE_EXTENT);
        
        createInternalPlacemark(middleFolder, node, mould);
    }


    /**
     * Created the innermost detailed level.
     * @param middleFolder
     * @param node
     * @param mould
     */
    private static void createInternalPlacemark(Folder middleFolder, PhylogenyNode node, PhylogenyMould mould){

        //Specify the position of the node.
        double latitude = getLatitude(node);
        double longitude = getLongitude(node);
        double altitude = getAltitude(node);

        //Create the level 2 folder.
        Folder innerFolder = middleFolder.createAndAddFolder();

        //Create the level 2 placemark. (middle placemark)
        Placemark innerPlacemark = innerFolder.createAndAddPlacemark();
        innerPlacemark.setName(node.getNodeName());
        innerPlacemark.setDescription(node.getNodeName());

        Point p = innerPlacemark.createAndSetPoint();
        p.setAltitudeMode(AltitudeMode.ABSOLUTE);
        p.addToCoordinates(longitude, latitude, altitude);

        //Specify a region inside this placemark.
        Region innerRegion = innerFolder.createAndSetRegion();     

        //Create a style map.
        StyleMap styleMap = innerPlacemark.createAndAddStyleMap();

        //Create the style for normal edge.
        Pair pNormal = styleMap.createAndAddPair();
        Style normalStyle = pNormal.createAndSetStyle();
        pNormal.setKey(StyleState.NORMAL);
        IconStyle normalIconStyle = normalStyle.createAndSetIconStyle();
        String normalColor = getColor(node);
        normalIconStyle.setColor(normalColor);
        normalIconStyle.setColorMode(ColorMode.NORMAL);
        normalIconStyle.setScale(1.00);
        //Specify the icon with this placemark.
        Icon normalIcon = normalIconStyle.createAndSetIcon();
        normalIcon.setHref("http://maps.google.com/mapfiles/kml/paddle/wht-blank.png");
        
        //Create the style for highlighted edge.
        Pair pHighlight = styleMap.createAndAddPair();
        Style highlightStyle = pHighlight.createAndSetStyle();
        pHighlight.setKey(StyleState.HIGHLIGHT);
        IconStyle highlightIconStyle = highlightStyle.createAndSetIconStyle();
        String highlightColor = getColor(node);
        highlightIconStyle.setColor(highlightColor);
        highlightIconStyle.setColorMode(ColorMode.NORMAL);
        highlightIconStyle.setScale(2.00);
        //Specify the icon with this placemark.
        Icon highlightIcon = highlightIconStyle.createAndSetIcon();
        highlightIcon.setHref("http://maps.google.com/mapfiles/kml/paddle/wht-blank.png");

        LatLonAltBox latlonBox = innerRegion.createAndSetLatLonAltBox();
        latlonBox.setEast(longitude + DELTA_L);
        latlonBox.setWest(longitude - DELTA_L);
        latlonBox.setNorth(latitude + DELTA_L);
        latlonBox.setSouth(latitude - DELTA_L);
        latlonBox.setMinAltitude(altitude);
        latlonBox.setMaxAltitude(altitude+DELTA_L);
        latlonBox.setAltitudeMode(AltitudeMode.ABSOLUTE);

        Lod lod = innerRegion.createAndSetLod();
        lod.setMinLodPixels(MIN_LOD_PIXELS_LEVEL_INTERNAL);
        lod.setMaxLodPixels(MAX_LOD_PIXELS_LEVEL_INTERNAL);
        lod.setMinFadeExtent(MIN_FADE_EXTENT);
        lod.setMaxFadeExtent(MAX_FADE_EXTENT);


    }

    /**
     * Returns true is the node has a valid location else returns false.
     * @param node
     * @return
     */
    public static boolean hasValidLocation(PhylogenyNode node){

        double latitude = getLatitude(node);
        double longitude = getLongitude(node);
        double altitude = getAltitude(node);

        if(latitude !=UNDEFINED && longitude!=UNDEFINED && altitude!=UNDEFINED){
            return true;
        }else{
            return false;
        }        
    }


    /**
     * Returns the latitude of the PhylogenyNode node
     * @param node
     * @return
     */
    public static double getLatitude(PhylogenyNode node){
        double latitude = 0.0;
        NodeData nodeData = node.getNodeData();
        Distribution dist = nodeData.getDistribution();
        if(dist == null){
            latitude = UNDEFINED;
        }else{
            latitude = dist.getLatitude().doubleValue();
        }        
        return latitude;
    }

    /**
     * Returns the longitude of the PhylogenyNode node
     * @param node
     * @return
     */
    public static double getLongitude(PhylogenyNode node){
        double longitude = 0.0;
        NodeData nodeData = node.getNodeData();
        Distribution dist = nodeData.getDistribution();
        if(dist == null){
            longitude = UNDEFINED;
        }else{
            longitude = dist.getLongitude().doubleValue();
        }    
        return longitude;
    }

    /**
     * Returns the altitude of the PhylogenyNode node
     * @param node
     * @return
     */
    public static double getAltitude(PhylogenyNode node){
        double altitude = 0.0;
        NodeData nodeData = node.getNodeData();
        Distribution dist = nodeData.getDistribution();
        if(dist == null){
            altitude = UNDEFINED;
        }else{
            altitude = dist.getAltitude().doubleValue();
        }

        double elevation = getElevation(node);

        return (altitude+elevation);
    }

    public static double getElevation(PhylogenyNode node){
        double elevation = 0.0;
        elevation = ELEVATION;
        return elevation;
    }

    /**
     * Gets the color associated with node as hex string.
     * @param node
     * @return
     */
    public static String getColor(PhylogenyNode node){
        BranchData branchData = node.getBranchData();
        BranchColor branchColor = branchData.getBranchColor();
        Color c = branchColor.getValue();
        String hexColor = rgbToHex(c);
        return hexColor;
    }

    /**
     * Converts a RGB color to a hex color.
     * @param c
     * @return
     */
    private static String rgbToHex(Color c){
        String color = null;
        color = decimalToHex(c.getAlpha()) + decimalToHex(c.getBlue()) + decimalToHex(c.getGreen())+
                decimalToHex(c.getRed());
        return color;
    }


    /**
     * Returns the hexadecimal representation of the positive decimal number n.<br>
     * A two digit representation is provided by this function always.
     * @param n
     * @return
     */
    private static String decimalToHex(int n){
        assert(n>=0);
        
        String invHexRepr = "";
        String hexRepr = "";
        String[] digits = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        while(n>=16){
            int remainder = n%16;
            invHexRepr = invHexRepr+digits[remainder];
            n=n/16;
        }
        invHexRepr = invHexRepr+digits[n];
        
        for(int i=invHexRepr.length()-1;i>=0;i--){
            hexRepr = hexRepr + invHexRepr.substring(i, i+1);
        }

        if(hexRepr.length() < 2){
            hexRepr = "0"+hexRepr;
        }
        return hexRepr;
    }

    /**
     * Gets the condensed name of a taxonomic unit.<br>
     * e.g. Homo Sapiens as H.Sapiens
     * e.g. Homo_Sapiens as H.Sapiens
     * @param name
     * @return
     */
    public static String getCondensedName(String name){
        String condensedName = null;
        
        String nameCopy = name.trim();
        condensedName = Character.toString(nameCopy.charAt(0));
        condensedName = condensedName+".";
        
        int i = nameCopy.indexOf(' ');

        if(i==-1){
            i = nameCopy.indexOf('_');
        }
        
        if(i==-1){
            condensedName = nameCopy.substring(i+1);
        }else{
            condensedName = condensedName + nameCopy.substring(i+1);
        }

        return condensedName;
    }



    /**
     * Creates a placemark for an internal node.<br>
     * These nodes represent the hypothetical taxonomic units.<br>
     * @param folder
     * @param node
     * @param mould
     */
    public static void createHTUPlacemark(Folder folder, PhylogenyNode node, PhylogenyMould mould){

        if(!hasValidLocation(node)){
            return;
        }

        //Specify the position of the node.
        double latitude = getLatitude(node);
        double longitude = getLongitude(node);
        double altitude = getAltitude(node);

        //Create the level 1 folder.
        Folder outerFolder = folder.createAndAddFolder();
        outerFolder.withName(node.getNodeName());

        //Create the level 1 placemark. (outermost placemark)
        Placemark outerPlacemark = outerFolder.createAndAddPlacemark();
        outerPlacemark.setName(node.getNodeName());

        Point p = outerPlacemark.createAndSetPoint();
        p.addToCoordinates(longitude, latitude, altitude);
        p.setAltitudeMode(AltitudeMode.RELATIVE_TO_GROUND);

        //TODO: No regions have been added to Hypothetical Taxa placemarks.
        //      Can be added in future; at present, the HTU's dont have any names.

        //Specify the color of this placemark.
        Style style = outerPlacemark.createAndAddStyle();
        IconStyle iconStyle = style.createAndSetIconStyle();
        String color = HTU_COLOR;
        iconStyle.setColor(color);
        iconStyle.setColorMode(ColorMode.NORMAL);
        iconStyle.setScale(0.50);

        //Specify the icon with this placemark.
        Icon icon = iconStyle.createAndSetIcon();
        icon.setHref("http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png");
        icon.setRefreshInterval(4);
        icon.setViewRefreshMode(ViewRefreshMode.NEVER);
        icon.setViewRefreshTime(4);
        icon.setViewBoundScale(1);
    }


    /**
     * Creates the 3 edges from the parentNode to the childNode.
     * @param folder
     * @param parentNode
     * @param childNode
     */
    public static void createBranch(Folder folder, PhylogenyNode parentNode, PhylogenyNode childNode){

        String childColor = getColor(childNode);
        LineString line = KmlToolkit.drawNewStyledLine(folder, childColor);
        
        createVerticalEdge(line, parentNode, childNode);
        createCurvedEdge(line, parentNode, childNode);
        //createVerticalEdgeFromParent(line, parentNode, childNode);
    }

    

    /**
     * Creates the vertical edge joining each child node to the curved edge connected to its parent.
     * @param folder
     * @param parentNode
     * @param childNode
     */
    private static void createVerticalEdge(LineString line, PhylogenyNode parentNode, PhylogenyNode childNode){

        double parentAlt = getAltitude(parentNode);
        
        double childLat = getLatitude(childNode);
        double childLong = getLongitude(childNode);
        double childAlt = getAltitude(childNode);

        double sourceLat = childLat;
        double sourceLong = childLong;
        double sourceAlt = childAlt;

        double destLat = childLat;
        double destLong = childLong;//TODO:
        double destAlt = parentAlt;//FRACTION*(parentAlt-childAlt) + childAlt;
       
        line.addToCoordinates(sourceLong, sourceLat, sourceAlt);
        line.addToCoordinates(destLong, destLat, destAlt);
    }


    /**
     * Appends the curved portion of the edge between the child and parent node to line.
     * @param line
     * @param parentNode
     * @param childNode
     */
    private static void createCurvedEdge(LineString line, PhylogenyNode parentNode, PhylogenyNode childNode){

        double childLat = getLatitude(childNode);
        double childLong = getLongitude(childNode);
        double childAlt = getAltitude(childNode);

        double parentLat = getLatitude(parentNode);
        double parentLong = getLongitude(parentNode);
        double parentAlt = getAltitude(parentNode);

        double sourceLat = childLat;
        double sourceLong = childLong;//TODO:
        double sourceAlt = parentAlt;//FRACTION*(parentAlt-childAlt) + childAlt;

        double destLat = parentLat;
        double destLong = parentLong;
        double destAlt = parentAlt;//FRACTION*(parentAlt-childAlt) + childAlt;     

        Coordinate fromCoord = new Coordinate(sourceLong, sourceLat, sourceAlt);
        Coordinate toCoord = new Coordinate(destLong, destLat, destAlt);
        
        KmlToolkit.drawCurvedLine(line, fromCoord, toCoord);
    }    

}
