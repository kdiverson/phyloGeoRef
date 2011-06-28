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

package nescent.phylogeoref.reader.exception;

/**
 * Signals that the coordinates of an external node have not been specified in the input file.
 * @author apurv
 */
public class LocationNotFoundException extends Exception{

    public LocationNotFoundException(String id, String label){
        this( "Location metadata not found for node (id = "+id+", label = "+label+")" );
    }

    public LocationNotFoundException(String msg){
        super(msg);
    }

}
