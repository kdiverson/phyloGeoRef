/*
 *  Copyright (C) 2010 Kathryn Iverson <kd.iverson at gmail.com>
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

package nescent.phyloGeoRef.tree;

import java.io.*;

import org.forester.io.parsers.nhx.NHXParser;
import org.forester.io.parsers.phyloxml.PhyloXmlParser;
import org.forester.io.parsers.tol.TolParser;
import org.forester.io.parsers.nexus.NexusPhylogeniesParser;
import org.forester.io.parsers.PhylogenyParser;
import org.forester.io.parsers.PhylogenyParserException;
import org.forester.phylogeny.Phylogeny;
import org.forester.phylogeny.factories.PhylogenyFactory;
import org.forester.phylogeny.factories.ParserBasedPhylogenyFactory;
import org.forester.util.ForesterUtil;

/**
 *
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 *
 * This class gets a tree
 *
 */
public class getTree {

    Phylogeny [] phy = null;

    /**
     * openTree (File tree_file, String fileType) takes 2 parameters, a file
     * and a string. The file is a tree file in nhx, nexus, phylxml or itol
     * format. The second parameter is a string indicating the
     * filetype: NHX, NEXUS, XML or ITOL. It returns an array of phylogeny
     * objects.
     * 
     * @param tree_file
     * @param fileType
     * @return Phylogeny []
     * @throws Exception
     */
    public Phylogeny [] openTree (File tree_file, String fileType) throws Exception {
        if (fileType.equalsIgnoreCase("NHX"))
            try {
                PhylogenyFactory factory = ParserBasedPhylogenyFactory.getInstance();
                phy = factory.create( tree_file, new NHXParser() );
            } catch ( Exception e ) {
                    System.out.println("Error: " + e.toString());
                }
        else if (fileType.equalsIgnoreCase("XML"))
            try {
                PhylogenyFactory factory = ParserBasedPhylogenyFactory.getInstance();
                phy = factory.create( new PhyloXmlParser(), tree_file );
            } catch ( Exception e ) {
                    System.out.println("Error: " + e.toString());
               }
        else if (fileType.equalsIgnoreCase("TOL"))
            try {
                PhylogenyFactory factory = ParserBasedPhylogenyFactory.getInstance();
                phy = factory.create( new TolParser(), tree_file );
            } catch ( Exception e ) {
                System.out.println("Error: " + e.toString());
            }
        else if (fileType.equalsIgnoreCase("NEXUS"))
            try {
                PhylogenyFactory factory = ParserBasedPhylogenyFactory.getInstance();
                phy = factory.create( new NexusPhylogeniesParser(), tree_file );
            } catch ( Exception e ) {
                System.out.println("Error: " + e.toString());
            }
        else {
            System.out.println("No/wrong fileType specified for file: " + tree_file +" if you don't know the filetype, use openTree(tree_file)");
        }
        return phy;
        }

    /**
     * openTree (File tree_file) takes one parameter, a file object containing
     * a phylogenetic tree file. It will try to guess what format the tree is
     * in. Use this if you don't know ahead of time what the tree format is.
     * 
     * @param tree_file
     * @return
     * @throws Exception
     */
    public Phylogeny [] openTree (File tree_file) throws Exception {
        try {
            PhylogenyFactory factory = ParserBasedPhylogenyFactory.getInstance();
            phy = factory.create( tree_file, ForesterUtil.createParserDependingOnFileType( tree_file, true ) );
        } catch ( Exception e ) {
            System.out.println("Error: " + e.toString());
        }
        return phy;
    }


}
