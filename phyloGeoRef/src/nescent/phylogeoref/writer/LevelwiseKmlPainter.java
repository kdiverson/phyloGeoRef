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
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import static java.lang.System.out;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import nescent.phylogeoref.reader.PhylogenyMould;
import nescent.phylogeoref.writer.utility.KmlUtility;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyMethods;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.iterators.PhylogenyNodeIterator;

/**
 * Paints a static kml file with no animations.
 * @author apurv
 */
public class LevelwiseKmlPainter implements KmlPainter{

    private Phylogeny phylogeny;    //the phylogeny currently being drawn.
    private HashMap<String,PhylogenyMould> mouldMap;//the mould map associated with the currently drawn phylogeny.
    private Document document;

    private HashMap<Integer,Folder> folderMap;

    public LevelwiseKmlPainter(Phylogeny phylogeny, Map mouldMap, Document document) {
        this.phylogeny = phylogeny;
        this.mouldMap = (HashMap) mouldMap;
        this.document = document;
        folderMap = new HashMap<Integer, Folder>();
    }


    @Override
    public void paintPhylogeny() {

        for( PhylogenyNodeIterator it = phylogeny.iteratorLevelOrder(); it.hasNext();) {
            
            PhylogenyNode node = it.next();
            
            //If the node is an external node, we'll draw it later.
            if(node.isExternal()){
                continue;
            }

            //Find the folder in which the current level is to be drawn.
            Integer level = PhylogenyMethods.calculateDepth(node);
            Folder folder = null;
            if(folderMap.containsKey(level)){
                folder = folderMap.get(level);
            }else{
                folder = KmlUtility.createFolder(document, "Level-"+level.toString(), level.toString());
                folderMap.put(level, folder);
            }

            String name = node.getNodeName();
            PhylogenyMould mould = mouldMap.get(name);

            //If it's an internal node, create a placemark for it.
            KmlUtility.createHTUPlacemark(folder, node, mould);
            drawEdges(folder, node);
        }
        putExternalNodes();
    }

    /**
     * Puts the placemarks for external nodes in a separate folder named "Taxon Label".
     * Places the members of different clades in separate folder.
     */
    private void putExternalNodes(){      
        //Create a folder to put all the tip nodes.
        Folder folder = KmlUtility.createFolder(document, "Taxon Label", "Contains the leaf obeservations");
        Set<PhylogenyNode> extNodeSet = phylogeny.getExternalNodes();
        HashMap<String, Folder> cladeFolderMap = new HashMap<String, Folder>();

        for(PhylogenyNode node:extNodeSet){
            
            String name = node.getNodeName();
            PhylogenyMould mould = mouldMap.get(name);
            
            Folder cladeFolder = null;
            String clade = mould.getClade();
            
            if(cladeFolderMap.containsKey(clade)){
                cladeFolder = cladeFolderMap.get(clade);
                
            }else{
                cladeFolder = KmlUtility.createFolder(folder, clade, "Contains members of the clade "+clade);
                cladeFolderMap.put(clade, cladeFolder);
            }
            
            //Create a placemark in folder for node having mould with it.
            KmlUtility.createExternalPlacemark(cladeFolder, node, mould);
        }
    }

    /**
     * Draws all the edges from the node to its children.
     * @param folder the folder in which everything at this level is to be drawn.
     * @param node
     * @param mould
     */
    private void drawEdges(Folder folder, PhylogenyNode node){

        for (int i=0; i < node.getNumberOfDescendants(); i++){
            
            PhylogenyNode childNode = node.getChildNode(i);           
            
            KmlUtility.createBranch(folder, node, childNode);
            
        }
    }   

}
