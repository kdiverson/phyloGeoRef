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

package nescent.phylogeoref.writer;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static java.lang.System.out;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.forester.phylogeny.Phylogeny;

/**
 * Prepares a rich kml from the phylogeny.
 * @author apurv
 */
public class AdvancedKmlWriter {

    private final static Logger LOGGER = Logger.getLogger("nescent");

    private final Kml kml;
    private Document document;
    private PaintStyle style;
    

    public AdvancedKmlWriter(){
        kml = new Kml();
        document = kml.createAndSetDocument().withName("Phylogeny").withOpen(true);
        this.style = PaintStyle.LEVELWISE;
    }

    public AdvancedKmlWriter( PaintStyle style){
        this();
        this.style = style;
    }

    public void setStyle(PaintStyle style){
        this.style = style;
    }

    /**
     * Outputs a normal .kml file visualizing the full phylogeny.
     * @param phy
     * @param mouldMap 
     * @param fileName
     */
    public void createKML(Phylogeny phy, Map mouldMap, String fileName){
        try {
            
            createGraphObject(phy, mouldMap);

            //The last thing you need to do it is marhsal the graph object to a file object.
            kml.marshal(new File(fileName+".kml"));            
            
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    /**
     * Outputs a compressed .kmz file visualizing the full phylogeny.
     * @param phy
     * @param mouldMap 
     * @param fileName
     */
    public void createKMZ(Phylogeny phy, Map mouldMap, String fileName){
        try {
            
            createGraphObject(phy, mouldMap);

            //The last thing you need to do it is marhsal the graph object to a file object.            
            kml.marshalAsKmz( fileName+".kmz", kml);
            
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
    
    
    
    /**
     * Creates the object representation of the tree.
     * Also called as the graph object.
     * 
     * @param phy the phylogeny which is to be drawn.
     * @param mouldMap a map which contains a mapping of nodes and their moulds in this phylogeny.
     */
    private void createGraphObject(Phylogeny phy, Map mouldMap){
        
        if(style == PaintStyle.HIERARCHICAL){                
            HierarchicalKmlPainter hPainter = new HierarchicalKmlPainter(phy, mouldMap, document);
            hPainter.paintPhylogeny();
                
        }else if(style == PaintStyle.LEVELWISE){
            LevelwiseKmlPainter lPainter = new LevelwiseKmlPainter(phy, mouldMap, document);
            lPainter.paintPhylogeny();
                
        }else if(style == PaintStyle.ANIMATED){
                
        }        
    }
    


}
