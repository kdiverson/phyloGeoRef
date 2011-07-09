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

package nescent.phylogeoref.validator.exception;

/**
 * Signals that longitude of this node is invalid. -180 <= Valid longitude <=180
 * @author apurv
 */
public class InvalidLongitudeException extends Exception{

    public InvalidLongitudeException(String id, String label, double lon){
        this( "Invalid latitude: "+lon+" for node (id = "+id+", label = "+label+")" );
    }

    public InvalidLongitudeException(String msg){
        super(msg);
    }

}
