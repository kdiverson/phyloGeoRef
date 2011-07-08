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

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import static java.lang.Math.*;
import static java.lang.System.out;

/**
 * Provides the various aviation formulae.
 * @author apurv
 */
public class AMath {

    /**
     * Converts angle to radians from degrees.
     * @param angle_degree
     * @return
     */
    public static double getAngleInRadians(double angle_degree){
        double angle_radian = angle_degree * Math.PI/180;
        return angle_radian;
    }


    /**
     * Converts angle to degrees from radians.
     * @param angle_radian
     * @return
     */
    public static double getAngleInDegrees(double angle_radian){
        double angle_degree = angle_radian * 180/Math.PI;
        return angle_degree;
    }


    /**
     * Returns the great circle distance between the points (lat1, long1) and point (lat2, long2)<br>
     * It is actually an angle between 0 and pie.
     * @param fromCoord
     * @param toCoord
     * @return
     */
    public static double getGreatCircleDistance(Coordinate fromCoord, Coordinate toCoord){

        double lat1 = AMath.getAngleInRadians(fromCoord.getLatitude());
        double lon1 = AMath.getAngleInRadians(fromCoord.getLongitude());        

        double lat2 = AMath.getAngleInRadians(toCoord.getLatitude());
        double lon2 = AMath.getAngleInRadians(toCoord.getLongitude());
       
        double d =0.0;
        double term1 = sin(lat1) * sin(lat2);
        double term2 = cos(lat1) * cos(lat2) * cos(lon1-lon2);
        double sum = term1 + term2;
        
        d = acos(sum);
        return Math.abs(d);
    }

    /**
     * Gets the coordinate corresponding to the fraction f between the source and destination coordinates.
     * <br>
     * @see http://iphylo.blogspot.com/2007/06/earth-not-flat-official.html
     * @param fromCoord
     * @param toCoord
     * @param d the great circle distance.
     * @param f the fraction. 0 represents the fromPoint and 1 the toPoint
     * @return
     */
    public static Coordinate getIntermediateCoordinate(Coordinate fromCoord, Coordinate toCoord, double d, double f){
        Coordinate coord = null;

        double lat1 = AMath.getAngleInRadians(fromCoord.getLatitude());
        double lon1 = AMath.getAngleInRadians(fromCoord.getLongitude());
        double alt1 = fromCoord.getAltitude();

        double lat2 = AMath.getAngleInRadians(toCoord.getLatitude());
        double lon2 = AMath.getAngleInRadians(toCoord.getLongitude());
        double alt2 = toCoord.getAltitude();
        
        double A = getA(f, d);
        double B = getB(f, d);
        double x = getX(A, B, lat1, lon1, lat2, lon2);
        double y = getY(A, B, lat1, lon1, lat2, lon2);
        double z = getZ(A, B, lat1, lon1, lat2, lon2);

        double lat = atan2(z, sqrt( pow(x,2) + pow(y,2)));
        double lon = atan2(y, x);
        double alt = alt1;

        lat = getAngleInDegrees(lat);
        lon = getAngleInDegrees(lon);

        coord = new Coordinate(lon, lat, alt);
        return coord;
    }

    /**
     * Returns the A value used in the formula
     * @param f
     * @param d
     * @return
     */
    private static double getA(double f, double d){
        double A = sin((1-f)*d) / sin(d);
        return A;
    }

    /**
     * Returns the B value to be used in the formula.
     * @param f
     * @param d
     * @return
     */
    private static double getB(double f, double d){
        double B = sin(f*d) / sin(d);
        return B;
    }

    /**
     * Returns the x value to be used in the formula.
     * @param A
     * @param B
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    private static double getX(double A, double B, double lat1, double lon1, double lat2, double lon2){
        double x = 0.0;
        x = A*cos(lat1)*cos(lon1) + B*cos(lat2)*cos(lon2);
        return x;
    }

    /**
     * Returns the y value to be used in the formula.
     * @param A
     * @param B
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    private static double getY(double A, double B, double lat1, double lon1, double lat2, double lon2){
        double y = 0.0;
        y = A*cos(lat1)*sin(lon1) + B*cos(lat2)*sin(lon2);
        return y;
    }

    /**
     * Returns the z value to be used in the formula.
     * @param A
     * @param B
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    private static double getZ(double A, double B, double lat1, double lon1, double lat2, double lon2){
        double z = 0.0;
        z = A*sin(lat1) + B*sin(lat2);
        return z;
    }

}
