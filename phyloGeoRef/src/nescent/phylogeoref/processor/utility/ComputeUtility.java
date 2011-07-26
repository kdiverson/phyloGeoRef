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
package nescent.phylogeoref.processor.utility;

import static java.lang.System.out;
import java.util.Arrays;
import java.util.Vector;

/**
 * Provides utility methods for geospatial mean.
 * @author apurv
 */
public class ComputeUtility {
    
    private static Double angleZero = Double.NaN;
    private static Double angleMax = Double.NaN;
    
    /**
     * Finds the mean coordinate of the vector posVector.
     * @param posVector
     * @return 
     */
    public static double findMeanCoordinate(Vector<Double> posVector){
        double meanPos = 0.0;
        
        //Create 4 buckets, 1 for each quadrant of the circle.
        
        Vector<Double> bucket1 = new Vector<Double>();      //    0  <= x  <  90
        Vector<Double> bucket2 = new Vector<Double>();      //  -90  <= x  <   0
        Vector<Double> bucket3 = new Vector<Double>();      // -180  <= x  < -90
        Vector<Double> bucket4 = new Vector<Double>();      //   90  <= x  < 180
        
        //Iterate over the posVector and classify each of the positions in respective buckets.
        
        for(Double position:posVector){
            
            if(position>=0){
                
                if(position > 90){
                    bucket4.add(position);
                    
                }else{
                    bucket1.add(position);
                }                
                
            }else if(position<0){
                
                if(position < -90){
                    bucket3.add(position);
                    
                }else{
                    bucket2.add(position);
                }
            }
        }
        
        
        //Create arrays out of the 4 buckets.
        
        Double[] quad1 = new Double[bucket1.size()];
        Double[] quad2 = new Double[bucket2.size()];
        Double[] quad3 = new Double[bucket3.size()];
        Double[] quad4 = new Double[bucket4.size()];
        
        bucket1.toArray(quad1);
        bucket2.toArray(quad2);
        bucket3.toArray(quad3);
        bucket4.toArray(quad4);
        
        
        // Sort the arrays.
        Arrays.sort(quad1);
        Arrays.sort(quad2);
        Arrays.sort(quad3);
        Arrays.sort(quad4);
        
        out.println(Arrays.deepToString(quad1));///////////////////////
        out.println(Arrays.deepToString(quad2));///////////////////////
        out.println(Arrays.deepToString(quad3));///////////////////////
        out.println(Arrays.deepToString(quad4));///////////////////////
                
        
        //Find the two points that are maximally separated.
        Double maxDistance = Double.MIN_VALUE;
        Double angularDistance = -1.0;
        
        //The angle of two points that are maximally separated.
        Double localAngleZero = 0.0;
        Double localAngleMax  = 0.0;
        
        Double globalAngleZero = 0.0;
        Double globalAngleMax  = 0.0;
        
        angularDistance = findMaxDistance12(quad1, quad2);
        
        if(angularDistance > maxDistance){
            maxDistance = angularDistance;
            globalAngleMax = angleMax;
            globalAngleZero = angleZero;
            
        }
        
        angularDistance = findMaxDistance14(quad1, quad4);

        if(angularDistance > maxDistance){
            maxDistance = angularDistance;
            globalAngleMax = angleMax;
            globalAngleZero = angleZero;
            
        }
        
        angularDistance = findMaxDistance23(quad2, quad3);

        if(angularDistance > maxDistance){
            maxDistance = angularDistance;
            globalAngleMax = angleMax;
            globalAngleZero = angleZero;
            
        }
        
        angularDistance = findMaxDistance34(quad3, quad4);

        if(angularDistance > maxDistance){
            maxDistance = angularDistance;
            globalAngleMax = angleMax;
            globalAngleZero = angleZero;
            
        }
        
        angularDistance = findMaxDistance13(quad1, quad3);

        if(angularDistance > maxDistance){
            maxDistance = angularDistance;
            globalAngleMax = angleMax;
            globalAngleZero = angleZero;
            
        }
        
        angularDistance = findMaxDistance24(quad2, quad4);

        if(angularDistance > maxDistance){
            maxDistance = angularDistance;
            globalAngleMax = angleMax;
            globalAngleZero = angleZero;
            
        }
        
        angularDistance = findMaxDistanceAA(quad1);

        if(angularDistance > maxDistance){
            maxDistance = angularDistance;
            globalAngleMax = angleMax;
            globalAngleZero = angleZero;
            
        }
        
        angularDistance = findMaxDistanceAA(quad2);

        if(angularDistance > maxDistance){
            maxDistance = angularDistance;
            globalAngleMax = localAngleMax;
            globalAngleZero = localAngleZero;
            
        }
        
        angularDistance = findMaxDistanceAA(quad3);

        if(angularDistance > maxDistance){
            maxDistance = angularDistance;
            globalAngleMax = angleMax;
            globalAngleZero = angleZero;
            
        }
        
        angularDistance = findMaxDistanceAA(quad4);

        if(angularDistance > maxDistance){
            maxDistance = angularDistance;
            globalAngleMax = angleMax;
            globalAngleZero = angleZero;
            
        }
        
        //Transform the coordinates such that globalAngleMax is transformed to 0 with positive direction
        //in the direction of least distance of globalAngleMax.
        Vector<Double> transformedPosVector = getTransformedPosVector(posVector, angleZero, angleMax);                        
        
        
        return meanPos;
    }
    
    private static Vector<Double> getTransformedPosVector(Vector<Double> posVector, Double globalAngleZero, Double globalAngleMax){
        Vector<Double> transformedPosVector = new Vector<Double>();
        
        return transformedPosVector;
    }
    
    
    
    /**
     * Finds the minimum angular distance between two angles.
     * @param angle1
     * @param angle2
     * @return
     */
    private static double findMinAngularDistance(double angle1, double angle2){
        double angDistance = 0.0;
        if(Math.signum(angle2) == Math.signum(angle1)){
            angDistance = Math.abs(angle2-angle1);
        }else{
            double sumAngle = Math.abs(angle1)+Math.abs(angle2);
            angDistance = Math.min(sumAngle, 360-sumAngle);
        }
        return angDistance;
    }
    
    /**
     * Finds the maximum separation between two points that are in quadrant1 and quadrant2.
     * @param quad1
     * @param quad2
     * @param angleZero
     * @param angleMax
     * @return 
     */
    private static Double findMaxDistance12(Double[] quad1, Double[] quad2){
        
        if(quad1.length == 0 || quad2.length == 0){
            angleZero = Double.NaN;
            angleMax = Double.NaN;
            return Double.MIN_VALUE;
        }
        
        Double maxDistance = Double.MIN_VALUE;
        angleZero = quad1[quad1.length-1];
        angleMax = quad2[0];
        
        maxDistance = findMinAngularDistance(angleZero, angleMax);
        
        return maxDistance;
    }
    
    
    /**
     * Finds the maximum separation between two points that are in quadrant1 and quadrant4.
     * @param quad1
     * @param quad4
     * @param angleZero
     * @param angleMax
     * @return 
     */
    private static Double findMaxDistance14(Double[] quad1, Double[] quad4){
        
        if(quad1.length == 0 || quad4.length == 0){
            angleZero = Double.NaN;
            angleMax = Double.NaN;
            return Double.MIN_VALUE;
        }
        
        Double maxDistance = Double.MIN_VALUE;
        angleZero = quad1[0];
        angleMax = quad4[quad4.length-1];
        
        maxDistance = findMinAngularDistance(angleZero, angleMax);
        
        return maxDistance;
    }
    
    /**
     * Finds the maximum separation between two points that are in quadrant3 and quadrant4.
     * @param quad3
     * @param quad4
     * @param angleZero
     * @param angleMax
     * @return 
     */
    private static Double findMaxDistance34(Double[] quad3, Double[] quad4){
        
        if(quad3.length == 0 || quad4.length == 0){
            angleZero = Double.NaN;
            angleMax = Double.NaN;
            return Double.MIN_VALUE;
        }
        
        Double maxDistance = Double.MIN_VALUE;
        angleZero = quad3[quad3.length-1];
        angleMax = quad4[0];
        
        maxDistance = findMinAngularDistance(angleZero, angleMax);
        
        return maxDistance;
    }
    
    /**
     * Finds the maximum separation between two points that are in quadrant2 and quadrant3.
     * @param quad2
     * @param quad3
     * @param angleZero
     * @param angleMax
     * @return 
     */
    private static Double findMaxDistance23(Double[] quad2, Double[] quad3){
        
        if(quad2.length == 0 || quad3.length == 0){
            angleZero = Double.NaN;
            angleMax = Double.NaN;
            return Double.MIN_VALUE;
        }
        
        Double maxDistance = Double.MIN_VALUE;
        angleZero = quad2[quad2.length-1];
        angleMax = quad3[0];
        
        maxDistance = findMinAngularDistance(angleZero, angleMax);
        
        return maxDistance;
    }
    
    /**
     * Finds the maximum separation between two points that are in quadrant1 and quadrant3.
     * Employs a search method similar to binary search. O(n log n) algorithm.
     * @param quad1
     * @param quad3
     * @param angleZero
     * @param angleMax
     * @return 
     */
    private static Double findMaxDistance13(Double[] quad1, Double[] quad3){
        
        
        if(quad1.length == 0 || quad3.length == 0){
            angleZero = Double.NaN;
            angleMax = Double.NaN;
            return Double.MIN_VALUE;
        }
        
        Double maxDistance = Double.MIN_VALUE;
        angleZero = quad1[0];
        angleMax = quad3[0];                
        
        for(Double x:quad1){
            Double y = findDiametricallyOppositeAngle(x);
            Double z = findClosestAngle(y, quad3);
            Double angularDistance = findMinAngularDistance(z, x);
            
            if(angularDistance > maxDistance){
                maxDistance = angularDistance;
                angleZero = x;
                angleMax = z;
            }                    
        }
        
        maxDistance = findMinAngularDistance(angleZero, angleMax);
        
        
        return maxDistance;
    }
    
    
    /**
     * Finds the maximum separation between two points that are in quadrant2 and quadrant4.
     * @param quad2
     * @param quad4
     * @param angleZero
     * @param angleMax
     * @return 
     */
    private static Double findMaxDistance24(Double[] quad2, Double[] quad4){
        
        if(quad2.length == 0 || quad4.length == 0){
            angleZero = Double.NaN;
            angleMax = Double.NaN;
            return Double.MIN_VALUE;
        }
        
        return findMaxDistance13(quad2, quad4);
    }        
    
    /**
     * Finds the closest value in quad that is closest in distance to angle.
     * @param angle
     * @param quad
     * @return 
     */
    private static double findClosestAngle(double angle, Double[] quad){
        double closestAngle = 0;
        int l = 0;
        int u = quad.length-1;
        int mid = (l+u)/2;
        while(l<=u){
            
            mid = (l+u)/2;
            if(angle> quad[mid]){
                l=mid+1;
                
            }else if(angle < quad[mid]){
                u=mid-1;
                
            }else{
                break;                
            }
        }
        mid = (l+u)/2;
        closestAngle = quad[mid];
        
        
        //IMP: The closestAngle obtained above is the angle just smaller than angle in the array quad.
        //     As an additional correction we need to check which among quad[mid] and quad[mid+1] is closer to angle.
        //     Also the case when size of quad is 1 is also handled.
        //Begin Correction
        if(mid+1 < quad.length){
            Double closestAngle1 = quad[mid];
            Double closestAngle2 = quad[mid+1];
            Double distance1 = findMinAngularDistance(closestAngle1, angle);
            Double distance2 = findMinAngularDistance(closestAngle2, angle);
            if(distance1 < distance2){
                closestAngle = closestAngle1;
                
            }else{
                closestAngle = closestAngle2;
                
            }
        }        
        //End Correction
        
        
        return closestAngle;
    }
    
    
    /**
     * Finds the diametrically opposite angle to this angle.<br>
     * The diametrically opposite angle to 2.5 is -177.5
     * @param angle
     * @return
     */
    private static double findDiametricallyOppositeAngle(double angle){
        double oppAngle = 0.0;
        oppAngle = -1*Math.signum(angle)*(180-Math.abs(angle));
        return oppAngle;
    }
    
    /**
     * Finds the maximum separation between two points that are in the same quadrant.
     * @param quad1
     * @param angleZero
     * @param angleMax
     * @return 
     */
    private static Double findMaxDistanceAA(Double[] quadA){
        
        if(quadA.length == 0){
            angleZero = Double.NaN;
            angleMax = Double.NaN;
            return Double.MIN_VALUE;
        }
        
        Double maxDistance = 0.0;
        angleZero = quadA[0].doubleValue();
        angleMax = quadA[quadA.length-1].doubleValue();
        
        maxDistance = findMinAngularDistance(angleZero, angleMax);
        
        return maxDistance;
    }


    
}
