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

    private String fileName;
    private final Kml kml;
    private Document document;
    private boolean isAnimated;     //true value draws an animated kml.
    

    public AdvancedKmlWriter(String fileName){
        kml = new Kml();
        document = kml.createAndSetDocument().withName("Phylogeny").withOpen(true);
        this.fileName = fileName;
    }

    public AdvancedKmlWriter(String fileName, boolean isAnimated){
        this(fileName);
        setAnimated(isAnimated);
    }

    public void setAnimated(boolean isAnimated){
        this.isAnimated = isAnimated;
    }

    public void createKML(Phylogeny phy, Map mouldMap){
        try {
            if(isAnimated){
                DynamicKmlPainter dynamicPainter = new DynamicKmlPainter(phy, mouldMap, document);
                dynamicPainter.paintPhylogeny();
            }else{
                StaticKmlPainter staticPainter = new StaticKmlPainter(phy, mouldMap, document);
                staticPainter.paintPhylogeny();
            }

            //The last thing you need to do it is marhsal the graph object to a file object.
            kml.marshal(new File(fileName));
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
    


}
