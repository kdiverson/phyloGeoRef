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

import java.awt.Color;
import static java.lang.System.out;
import java.util.Set;
import nescent.phylogeoref.reader.PhylogenyMould;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.BranchColor;
import org.forester.phylogeny.data.BranchData;

/**
 * Prepares a well formed HTML content for the given PhylogenyNode which has the given PhylogenyMould
 * associated with it.<br>
 * 
 * @author apurv
 */
public class HTMLParlour {
    
    /**
     * Used in HTML table to differentiate consecutive lines.
     */
    boolean lineFlag = true;

    /**
     * Prepares the html content.
     * @param node
     * @param mould
     * @return
     */
    public String prepareHTMLContent(PhylogenyNode node, PhylogenyMould mould){
        StringBuilder content = new StringBuilder("");
        addHeading(node, content);
        addId(node, content);
        
        if(mould != null){
            addPropertiesTable(node, mould, content);
        }
        
        addLineBreak(content);
        addPropertiesTable(node, content);
        
        return encloseInCDATA(content);
    }

    /**
     * Encloses the string in CDATA.
     * @param innerContent
     * @return
     */
    private String encloseInCDATA(StringBuilder innerContent){
        String content = "<![CDATA[\n" + innerContent + "";
        return content;
    }

    /**
     * Puts the name of the taxonomic unit in the content.
     * @param node
     * @param content 
     */
    private void addHeading(PhylogenyNode node, StringBuilder content){
        BranchData bData = node.getBranchData();
        BranchColor bColor = bData.getBranchColor();
        Color c = bColor.getValue();        
        String kmlColor = KmlUtility.rgbToHex(c);
        String rgbColor = getRGBColor(kmlColor);
        
        content = content.append("<h2>");
        content = content.append("<font color=\"#"+rgbColor+"\"  align=\"right\">");
        content = content.append(getFormattedBiologicalName(node.getNodeName()));
        content = content.append("</font>");
        content = content.append("</h2>");
    }
    
    /**
     * Puts the id of the taxonomic unit in the content.
     * @param node
     * @param builder 
     */
    private void addId(PhylogenyNode node, StringBuilder content){
        Integer id = node.getNodeId();
        content = content.append("(");
        content = content.append(id.toString());
        content = content.append(")");
    }
    
    /**
     * Puts a table with all the attributes and their values in the content.
     * @param node
     * @param mould
     * @param content 
     */
    private void addPropertiesTable(PhylogenyNode node, StringBuilder content){
        
        content = content.append("<table bgcolor=\"#000000\"  border=\"1\"  >");
        
        Double latitude = node.getNodeData().getDistribution().getLatitude().doubleValue();
        addRow("Latitude",latitude.toString(), content );
        
        Double longitude = node.getNodeData().getDistribution().getLongitude().doubleValue();
        addRow("Longitude",longitude.toString(), content );
        
        BranchData bData= node.getBranchData();
        BranchColor bColor = bData.getBranchColor();
        if(bColor!=null){
            Color c = bColor.getValue();
            String color = getStringRepresentation(c);
            addRow("Color", color, content);
        }
        
        Double distParent = node.getDistanceToParent();
        if(distParent != 0.0){
            addRow("Distance To Parent", distParent.toString(), content);
        }
        
        //TODO: Add the confidence value to the table.
        
                
        content = content.append("</table>");
    }
    
    /**
     * Extracts all the properties from the phylogeny node and its mould and places it in the html table.
     * @param node
     * @param mould
     * @param content 
     */
    private void addPropertiesTable(PhylogenyNode node, PhylogenyMould mould, StringBuilder content){
        
        content = content.append("<table bgcolor=\"#000000\">");
        Set<String> propertyNames = mould.getAllPropertyNames();        
        
        for(String propertyName:propertyNames){
            String propertyValue = mould.accessValue(propertyName);
            addRow(propertyName, propertyValue, content);
            
        }        
        content = content.append("</table>");
    }
    
    /**
     * Adds a row in the html table.
     * @param key
     * @param value 
     */
    private void addRow(String key, String value, StringBuilder content){
        
        if(lineFlag){
            content = content.append("<tr>");
            
            content = content.append("<td bgcolor=\"#009900\">");
            content = content.append("<font color=\"ffff00\" align=\"right\" >");
            content = content.append(key);
            content = content.append("</font>");
            content = content.append("</td>");
            
            content = content.append("<td>");
            content = content.append("<font color=\"ffffff\" align=\"right\">");
            content = content.append(value);
            content = content.append("</font>");
            content = content.append("</td>");
            
            content = content.append("</tr>");
            
            lineFlag = false;
            
        }else{
            content = content.append("<tr>");
            
            content = content.append("<td bgcolor=\"#0000ff\">");
            content = content.append("<font color=\"dddddd\" align=\"right\" >");
            content = content.append(key);
            content = content.append("</font>");
            content = content.append("</td>");
            
            content = content.append("<td>");
            content = content.append("<font color=\"ffffff\" align=\"right\">");
            content = content.append(value);
            content = content.append("</font>");
            content = content.append("</td>");
            
            content = content.append("</tr>");
            
            lineFlag = true;
        }
    }
    
    /**
     * Adds an html line break to content.
     * @param content 
     */
    private void addLineBreak(StringBuilder content){
        content = content.append("<br>");
    }
    
    /**
     * Returns the formatted biological name of a taxonomic unit.
     * @param name
     * @return 
     */
    private String getFormattedBiologicalName(String name){
        String fName = null;
        int i = name.indexOf(' ');
        
        //Find the separator.
        if(i == -1){
            i = name.indexOf('_');
        }
        
        if(i == -1){
            i = name.indexOf('.');
        }
        
        //If the separator is not among '.' or '_' or ' ' then take the entire name as it is.
        if(i == -1){
            fName = name.substring(0);
            
        }else{
            String sFirst = name.substring(i+1,i+2);            
            fName = name.substring(0,i) +" "+ sFirst.toUpperCase() +name.substring(i+2, name.length());
            
        }
        
        return fName;
    }
    
    /**
     * Finds the rgb color equivalent of a kml color which is in bgr format.
     * @param color
     * @return 
     */
    private String getRGBColor(String color){
        String rgbColor = "";        
        String blue = color.substring(2,4);
        String green = color.substring(4,6);
        String red = color.substring(6,8);
        rgbColor = red + green + blue;
        return rgbColor;
    }
    
    /**
     * Finds and returns the string representation of the given color.
     * @param c
     * @return 
     */
    private String getStringRepresentation(Color c){
        String color = null;
        Integer r = c.getRed();
        Integer g = c.getBlue();
        Integer b = c.getBlue();
        color = "(r="+r.toString()+", g="+g.toString()+", b="+b.toString()+")";
        return color;
    }

}
