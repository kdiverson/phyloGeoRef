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

package nescent;

import java.io.File;
import nescent.phylogeoref.reader.GrandUnifiedReader;
import nescent.phylogeoref.reader.MultiFormatReader;
import nescent.phylogeoref.reader.NeXMLReader;
import nescent.phylogeoref.reader.UniversalTreeReader;

/**
 * Main class
 * @author apurv
 */
public class Phylogeoref {

    /**
     * Utility main method, used for testing this file during development.
     * @param args
     * @throws Throwable
     */
    public static void main(String...args) throws Throwable{

        //File treeFile = new File("samples\\treeExperimental\\testTree.xml");
        //File inTree=new File("samples\\tree3\\T1092.xml");
        //File inTree=new File("samples\\tree1\\testTree.xml");
    
        File treeFile = new File("samples\\mammals\\mammalsTree.xml");
        File metaFile = new File("samples\\mammals\\mammals_in_tree.txt");        

        File[] metaFiles = new File[]{metaFile};

        GrandUnifiedReader gur = new GrandUnifiedReader();
        gur.setTreeFile(treeFile).setMetaFile(metaFiles).setDelim('\t').setCladeDiv(0);
        gur.setArgs(5,3,4,1,2);

        gur.buildUnifiedPhylogeny();


        

    }    
}
