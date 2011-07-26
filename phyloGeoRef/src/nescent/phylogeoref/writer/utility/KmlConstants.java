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
package nescent.phylogeoref.writer.utility;

/**
 * Contains the various constants that are used by KmlUtility.
 * @author apurv
 */
public interface KmlConstants {
    
    final static double UNDEFINED = -1.0d;

    /**
     * Each tip node placemark has 3 levels of detail associated with it.
     * Denotes the minimum pixels after which the outer lod should become conspicuous.
     */
    final static double MIN_LOD_PIXELS_LEVEL_OUTER = 0;
    
    /**
     * Denoted the maximum pixels after which the outer lod should become invisible.
     */
    final static double MAX_LOD_PIXELS_LEVEL_OUTER = 127;
    
    /**
     * Denotes the minimum pixels after which the middle lod should become conspicuous.
     */
    final static double MIN_LOD_PIXELS_LEVEL_MIDDLE = 128;
    
    /**
     * Denotes the maximum pixels after which the outer lod should become invisible.
     */
    final static double MAX_LOD_PIXELS_LEVEL_MIDDLE = 1024;

    /**
     * Denotes the minimum pixels after which the innermost lod should become conspicuous.
     */
    final static double MIN_LOD_PIXELS_LEVEL_INTERNAL = 1025;
    
    /**
     * Denotes the maximum pixels after which the outer lod should become invisible.
     * A value of -1 denotes that it remains visible till infinity.
     */
    final static double MAX_LOD_PIXELS_LEVEL_INTERNAL = -1;
    
    /**
     * Minimum fade extent. See fading in kml to understand its effect.
     * Provides a kind of graceful disappearance. A higher value denotes a more graceful fade.
     * Should be chosen properly as it is an expensive operation.
     * The current implementation requires fade extent to be 0 so don't change it.
     */
    final static double MIN_FADE_EXTENT = 0;
    
    /**
     * Minimum fade extent. See fading in kml to understand its effect.
     * Provides a kind of graceful disappearance. A higher value denotes a more graceful fade.
     * Should be chosen properly as it is an expensive operation.
     * The current implementation requires fade extent to be 0 so don't change it.
     */
    final static double MAX_FADE_EXTENT = 0;

    /**
     * A constant to denote the region warped around a placemark.
     */
    final static double DELTA_L = 2.5;
    
    /**
     * 
     */
    final static double ELEVATION = 0;

    /**
     * The color of the hypothetical taxonomic unit placemarks.
     */
    final static String HTU_COLOR="ff0ff9ff";
    
    /**
     * The icon image for the tip node placemarks.
     */
    final static String PLACEMARK_ICON_HREF = "http://bioguid.info/images/whiteBall.png";
    
    /**
     * The icon image for the hypothetical taxonomic unit placemarks.
     */
    final static String HTU_ICON_HREF = "http://maps.google.com/mapfiles/kml/shapes/shaded_dot.png";

    
}
