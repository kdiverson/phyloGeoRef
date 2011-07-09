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

package nescent.phylogeoref.validator;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import nescent.phylogeoref.validator.exception.LocationNotFoundException;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.PhylogenyNode;
import org.forester.phylogeny.data.Distribution;
import org.forester.phylogeny.data.NodeData;

/**
 * Validates a phylogeny.
 * @author apurv
 */
public class PhylogenyValidator {

    private final static Logger LOGGER = Logger.getLogger("nescent");

    /**
     * Validates the phylogeny by checking that all external nodes have been assigned coordinates.
     * @param phy
     */
    private void validatePhylogeny(Phylogeny phy){

        Set<PhylogenyNode> extNodeSet = phy.getExternalNodes();

        for(PhylogenyNode node:extNodeSet){
            try{
                NodeData nodeData = node.getNodeData();
                Distribution dist = nodeData.getDistribution();
                if(dist != null){
                    if(dist.getLatitude()==null || dist.getLongitude()==null ||dist.getAltitude()==null){
                        Integer id = node.getNodeId();
                        throw new LocationNotFoundException(id.toString(), node.getNodeName());
                    }
                }
                else{
                    Integer id = node.getNodeId();
                    throw new LocationNotFoundException(id.toString(), node.getNodeName());
                }
            }
            catch(LocationNotFoundException ex){
                LOGGER.log(Level.INFO, ex.getMessage(), ex);
            }
        }
    }

}
