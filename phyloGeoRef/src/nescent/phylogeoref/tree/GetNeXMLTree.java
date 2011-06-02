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

package nescent.phylogeoref.tree;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import org.nexml.model.Document;

import org.nexml.model.DocumentFactory;

/**
 *This class defines functions to parse NeXML trees. These have been merged with
 * NeXMLtoPhyObj so this class can probably be deleted.
 * @author Kathryn Iverson <kd.iverson at gmail.com>
 */
public class GetNeXMLTree {

    //read NeXML file in as [object]
    //parse until treeblock
    //call iterator on treeblock object
    //assign each node to a phylogeny object

    // if filetype is xml
    //File file = new File(intreeFile);
	
    /**
     * 
     * @param intreeFile
     * @return Document
     */
    public Document parseNeXML(File intreeFile) {
//		String nexmlRoot = System.getenv("NEXML_ROOT");
//		if ( nexmlRoot == null ) {
//			nexmlRoot = "/Users/rvosa/Documents/workspace/nexml/trunk/nexml";
//		}
		//File file = new File(intreeFile);
		Document doc = null;
		try {
			doc = DocumentFactory.parse(intreeFile);
		} catch (SAXException e) {
			//Assert.assertTrue(e.getMessage(), false);
			e.printStackTrace();
		} catch (IOException e) {
			//Assert.assertTrue(e.getMessage(), false);
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			//Assert.assertTrue(e.getMessage(), false);
			e.printStackTrace();
		}
		//System.out.println(doc.getXmlString());

        return doc;
	}


}
