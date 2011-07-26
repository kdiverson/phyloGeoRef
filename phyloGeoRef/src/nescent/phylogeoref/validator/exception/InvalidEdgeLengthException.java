/*
 * Copyright (C) 2011 apurv
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package nescent.phylogeoref.validator.exception;

/**
 * Signals that edge length values were invalid for the phylogeny node.
 * @author apurv
 */
public class InvalidEdgeLengthException extends Exception{
    
    public InvalidEdgeLengthException(String id, String label){
        this( "Invalid Edge length for node (id = "+id+", label = "+label+")" );
    }

    public InvalidEdgeLengthException(String msg){
        super(msg);
    }
    
}
