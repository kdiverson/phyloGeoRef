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
import java.util.Map;
import org.forester.phylogeny.Phylogeny;

/**
 * Paints a dynamic kml file with animations.
 * @author apurv
 */
public class DynamicKmlPainter implements KmlPainter{

    private Phylogeny phylogeny;    //the phylogeny currently being drawn.
    private Map mouldMap;           //the mould map with the currently drawn phylogeny.
    private Document document;

    public DynamicKmlPainter(Phylogeny phylogeny, Map mouldMap, Document document) {
        this.phylogeny = phylogeny;
        this.mouldMap = mouldMap;
        this.document = document;
    }

    @Override
    public void paintPhylogeny() {
        
    }

    
}
