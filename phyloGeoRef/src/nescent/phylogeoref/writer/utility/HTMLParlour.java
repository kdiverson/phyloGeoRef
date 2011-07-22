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

package nescent.phylogeoref.writer.utility;

import static java.lang.System.out;
import nescent.phylogeoref.reader.PhylogenyMould;
import org.forester.phylogeny.PhylogenyNode;

/**
 * Prepares a well formed HTML content for the given PhylogenyNode which has the given PhylogenyMould
 * associated with it.<br>
 * 
 * @author apurv
 */
public class HTMLParlour {

    /**
     * Prepares the html content.
     * @param node
     * @param mould
     * @return
     */
    public String prepareHTMLContent(PhylogenyNode node, PhylogenyMould mould){
        StringBuilder content = new StringBuilder("");
        addHeading(node, content);

        out.println(content);
        return encloseInCDATA(content);
    }

    /**
     * Encloses the string in CDATA.
     * @param innerContent
     * @return
     */
    private String encloseInCDATA(StringBuilder innerContent){
        String content = "<![CDATA[ " + innerContent + " ]] >";
        return content;
    }

    private void addHeading(PhylogenyNode node, StringBuilder content){
        content = content.append("<h1>");
        content = content.append(node.getNodeName());
        content = content.append("</h1>");
    }

}
