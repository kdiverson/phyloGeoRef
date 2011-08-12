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
import de.micromata.opengis.kml.v_2_2_0.Folder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nescent.phylogeoref.reader.PhylogenyMould;
import nescent.phylogeoref.writer.utility.KmlUtility;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.iterators.PhylogenyNodeIterator;

/**
 * Paints a dynamic kml file with animations.
 * @author apurv
 */
public class HierarchicalKmlPainter extends KmlPainter{
    
    private HashMap<PhylogenyNode,Folder> folderMap;   //Contains the mapping from the node to the kml folder corresponding to it.

    public HierarchicalKmlPainter(Phylogeny phylogeny, Map mouldMap, Document document) {
        super(phylogeny, (HashMap) mouldMap, document);        
        folderMap = new HashMap<PhylogenyNode, Folder>();
    }

    
    
    @Override
    public void paintPhylogeny() {
        
        for( PhylogenyNodeIterator it = phylogeny.iteratorPreorder(); it.hasNext();) {
            
            PhylogenyNode node = it.next();
            
            //If the node is an external node, we'll draw it later.
            if(node.isExternal()){
                continue;
            }
            
            Folder folder = null;
            
            if(node.isRoot()){
                Integer id = node.getNodeId();
                folder = KmlUtility.createFolder(document, node.getNodeName()+" HTU-"+id.toString(), "Root Node");
                
                String name = node.getNodeName();
                PhylogenyMould mould = mouldMap.get(name);
                KmlUtility.createHTUPlacemark(folder, node, mould);
                drawEdges(folder, node);
                
                folderMap.put(node, folder);
                
            }else{
                PhylogenyNode parentNode = node.getParent();
                Folder parentFolder = folderMap.get(parentNode);
                
                String name = node.getNodeName();
                Integer id = node.getNodeId();
                Folder childFolder = KmlUtility.createFolder(parentFolder, name+" HTU-"+id.toString(), name);
                
                PhylogenyMould mould = mouldMap.get(name);
                KmlUtility.createHTUPlacemark(childFolder, node, mould);
                drawEdges(childFolder, node);
                
                folderMap.put(node, childFolder);
            }
        }
        putExternalNodes();        
    }                       

    
}
