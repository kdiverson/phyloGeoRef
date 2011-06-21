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

package nescent.phylogeoref.nexml.utility;

import java.math.BigDecimal;
import static java.lang.System.out;
import java.net.URI;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.BranchData;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.data.Identifier;
import org.forester.phylogeny.data.NodeData;
import org.forester.phylogeny.data.PropertiesMap;
import org.forester.phylogeny.data.Property;
import org.forester.phylogeny.data.Property.AppliesTo;
import org.nexml.model.Annotation;
import org.nexml.model.Edge;
import org.nexml.model.FloatEdge;
import org.nexml.model.Network;
import org.nexml.model.Node;
import org.nexml.model.OTU;

/**
 * Provides the necessary utility functions while transforming a parsed NeXML document
 * into a Phylogeny tree.<br>
 * It is important to note that there are two nodes and edges used in this class.<br>
 * (1) org.forester.phylogeny.PhylogenyNode<br>
 * (2) org.nexml.model.Node<br><br>
 *
 * A successful transformation will grab all the data from a Node and put it into the
 * PhylogenyNode.<br><br>
 *
 * Similarly there are two edges.<br>
 * (1) org.nexml.model.Edge<br>
 * (2) org.forester.phylogeny.Edge<br>
 *
 * @author apurv
 */
public class PhyloUtility {

    /**
     * Finds and returns the root node of the tree.
     * @param network
     * @return the root node
     */
    public static Node getRootNode(Network<FloatEdge> network){
        Node rootNode=null;
        
        for(Node node:network.getNodes()){
            if(node.isRoot()){
                rootNode=node;
            }
        }
        return rootNode;
    }

    /**
     * Transforms Node to a PhylogenyNode.<br>
     * Does not attach the metadata.<br><br>
     * 
     * @param fromNode the node from which data is to be extracted.
     * @param toNode the node to which data has to be copied.
     * @return a PhylogenyNode analogous to node.
     */
    public static PhylogenyNode toPhylogenyNode(Node node){
        PhylogenyNode phyNode = new PhylogenyNode();
        phyNode.setName(node.getLabel());
        
        int id = getNumberFromMetaId(node.getId());

        //TODO: This can't be uncommented because setNodeId() method is protected.
        //phyNode.setNodeId(id);
     
        return phyNode;
    }

    /**
     * Attaches latitude and longitudes to each node.<br>
     * The latitude/longitude metadata can be attached to the node in two ways.<br>
     * 1) Either in the OTU which is referenced by this node.<br>
     * 2) Or directly within this node.<br><br>
     * 
     * Caveat: Should be called only for external nodes.<br>
     * @see http://rs.tdwg.org/dwc/2009-07-06/terms/simple/index.htm <br><br>
     *
     * @param node the node in nexml document
     * @param phyNode the phylogeny tree node    
     */
    public static void attachLocationMetadata(Node node, PhylogenyNode phyNode){

        //TODO: Check this 
        URI namespaceURI = URI.create("http://rs.tdwg.org/dwc/terms/");
        //Case 1: Location metadata is stored in an OTU.
        OTU otu = node.getOTU();

        if(otu!=null){
            Set<Object> latitudes = otu.getAnnotationValues("dwc:decimalLatitude");
            Set<Object> longitudes = otu.getAnnotationValues("dwc:decimalLongitude");
            
            if(!latitudes.isEmpty() && !longitudes.isEmpty()){
                Double lat = (Double)latitudes.iterator().next();
                Double lon = (Double)longitudes.iterator().next();
                
                Distribution dist = new Distribution(phyNode.getNodeName());
                phyNode.getNodeData().setDistribution(dist);

                phyNode.getNodeData().getDistribution().setLatitude(new BigDecimal(lat));
                phyNode.getNodeData().getDistribution().setLongitude(new BigDecimal(lon));
                phyNode.getNodeData().getDistribution().setAltitude(BigDecimal.ZERO);
                
            }
        }

        //Case 2: Location metadata is attached to the node itself.
        Set<Object> latitudes = node.getAnnotationValues("dwc:decimalLatitude");
        Set<Object> longitudes = node.getAnnotationValues("dwc:decimalLongitude");
        
        if(!latitudes.isEmpty() && !longitudes.isEmpty()){
            Double lat = (Double)latitudes.iterator().next();
            Double lon = (Double)longitudes.iterator().next();

            Distribution dist = new Distribution(phyNode.getNodeName());
            phyNode.getNodeData().setDistribution(dist);

            phyNode.getNodeData().getDistribution().setLatitude(new BigDecimal(lat));
            phyNode.getNodeData().getDistribution().setLongitude(new BigDecimal(lon));
            phyNode.getNodeData().getDistribution().setAltitude(BigDecimal.ZERO);          

        }
        //throw new LocationNotFoundException(node.getId(),node.getLabel());
    }

    /**
     * Maps the various metadata properties to a phylogeny node.<br>
     * This function may require a revision because a perfect mapping between
     * NeXML and Phylogeny object doesn't exist. Since the forester library
     * was made keeping the PhyloXML format in mind.<br><br>
     *
     * TODO: This URI needs to be checked,however it's not used because of the leniency of the api.
     *
     * @param node
     * @param phyNode
     */
    public static void attachOtherMetadata(Node node, PhylogenyNode phyNode){
        
        URI namespaceURI = URI.create("http://purl.org/PHYLO/TREEBASE/PHYLOWS/study/TB2:");
        OTU otu= node.getOTU();

        NodeData nodeData = phyNode.getNodeData();
        PropertiesMap pMap = new PropertiesMap();
        nodeData.setProperties(pMap);
        
        if(otu!=null){
            Set<Annotation> set = null;
            String pName = null; //Property name

            pName = "tb:identifier.taxon";
            set = otu.getAnnotations(pName);
            for(Annotation a:set){
                String value = a.getValue().toString();
                String unit = null; //This is not applicable.
                String datatype = a.getXsdType().toString();
                String id_ref = a.getId().toString();

                Property p = new Property( pName, value, unit, datatype, AppliesTo.NODE, id_ref);
                pMap.addProperty(p);

                //TODO: Currently the properties map can only contain unique properties.
                break;
            }

            pName = "tb:identifier.taxonVariant";
            set = otu.getAnnotations(pName);
            for(Annotation a:set){                
                String value = a.getValue().toString();
                String unit = null; //This is not applicable.
                String datatype = a.getXsdType().toString();
                String id_ref = a.getId().toString();

                Property p = new Property( pName, value, unit, datatype, AppliesTo.NODE, id_ref);
                pMap.addProperty(p);

                //TODO: Currently the properties map can only contain unique properties.
                break;
            }

            pName = "skos:prefLabel";
            set = otu.getAnnotations(pName);
            for(Annotation a:set){
                String value = a.getValue().toString();
                String unit = null; //This is not applicable.
                String datatype = a.getXsdType().toString();
                String id_ref = a.getId();

                Property p = new Property( pName, value, unit, datatype, AppliesTo.NODE, id_ref);
                pMap.addProperty(p);

                //TODO: Currently the properties map can only contain unique properties.
                break;
            }         

            pName = "skos:closeMatch";
            set = otu.getAnnotations(pName);
            for(Annotation a:set){
                String value = a.getValue().toString();
                String unit = null; //This is not applicable.
                String datatype = a.getXsdType().toString();
                String id_ref = a.getId();
               
                Property p = new Property( pName, value, unit, datatype, AppliesTo.NODE, id_ref);
                pMap.addProperty(p);

                //TODO: Currently the properties map can only contain unique properties.
                break;
            }

            pName = "rdfs:isDefinedBy";
            set = otu.getAnnotations(pName);
            for(Annotation a:set){
                String value = a.getValue().toString();
                String unit = null; //This is not applicable.
                String datatype = a.getXsdType().toString();
                String id_ref = a.getId();

                Property p = new Property( pName, value, unit, datatype, AppliesTo.NODE, id_ref);
                pMap.addProperty(p);

                //TODO: Currently the properties map can only contain unique properties.
                break;
            }
        }
    }

    /**
     * Sets the distance of PhylogenyNode node from its parent.
     * @param edge
     * @param node
     */
    public static void setDistanceToParent(Edge edge, PhylogenyNode node){        
        Number length = edge.getLength();
        if(length!=null){
            node.setDistanceToParent(length.doubleValue());
        }
        else{
            node.setDistanceToParent(0.0d);
        }                
    }


    /**
     * Returns the number part of a meta id<br>
     * For example if a node has id = "node234"<br>
     * It returns 234
     * 
     * @param metaId
     * @return the number id of this meta id.
     */
    private static int getNumberFromMetaId(String metaId){
        int n;
        Pattern numberRegex = Pattern.compile("(\\D*)(\\d*)");
        Matcher numberMatcher = numberRegex.matcher(metaId);
        String number = "-1";

        if(numberMatcher.find()){
            number = numberMatcher.group(2);
        }
        n = Integer.parseInt(number);
        return n;
    }
    
}
