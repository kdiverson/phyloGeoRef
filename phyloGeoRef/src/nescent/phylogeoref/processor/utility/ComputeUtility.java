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
    /**
     * Temporary variable for storing the immediate result of calculations.
     */
    private static Double angleZero = Double.NaN;
    
    /**
     * Temporary variable for storing the immediate result of calculations.
     */
    private static Double angleMax = Double.NaN;
    
    /**
     * The permissible error in equality comparisons.
     */
    private static double ERROR = 0.0000000000001;
    
    /**
     * Finds the mean position.
     * @param posVector
     * @return 
     */
    public static double findMeanPosition(Vector<Double> posVector){
        return computeMeanPosition(posVector, null);
    }
    
    /**
     * Finds the weighted mean position.
     * @param posVector
     * @param wVector the weight vector.
     * @return 
     */
    public static double findMeanPosition(Vector<Double> posVector, Vector<Double> timeVector){
        double meanPos = 0.0;
        Vector<Double> wtVector = getWeightVector(timeVector);
        meanPos = computeMeanPosition(posVector, wtVector);
        return meanPos;
    }
              
    
    /**
     * Computes the mean coordinate of the vector posVector.
     * @param posVector
     * @return 
     */
    private static double computeMeanPosition(Vector<Double> posVector, Vector<Double> wVector){
        double meanPos = 0.0;
        
        //Create 4 buckets, 1 for each quadrant of the circle.
        
        Vector bucket[] = new Vector[5];
        
        bucket[0] = null;                      //This is a dummy bucket,just to match the index with the exact quad. No.
        bucket[1] = new Vector<Double>();      //    0  <= x  <  90
        bucket[2] = new Vector<Double>();      //   90  <= x  < 180
        bucket[3] = new Vector<Double>();      // -180  <= x  < -90
        bucket[4] = new Vector<Double>();      //  -90  <= x  <   0
        
        //Iterate over the posVector and classify each of the positions in respective buckets.
        
        for(Double position:posVector){
            
            //Change 180.0 to -180.0 so that there is no ambiguity.
            if(position == 180){
                posVector.remove(position);
                posVector.add(-180.0);
            }
            
            int qNo = getQuadNumber(position);
            bucket[qNo].add(position);            
            
        }        
        
        //Create arrays out of the 4 buckets.
        
        Double[] quad1 = new Double[bucket[1].size()];
        Double[] quad2 = new Double[bucket[2].size()];
        Double[] quad3 = new Double[bucket[3].size()];
        Double[] quad4 = new Double[bucket[4].size()];
        
        bucket[1].toArray(quad1);
        bucket[2].toArray(quad2);
        bucket[3].toArray(quad3);
        bucket[4].toArray(quad4);
        
        
        // Sort the arrays.
        Arrays.sort(quad1);
        Arrays.sort(quad2);
        Arrays.sort(quad3);
        Arrays.sort(quad4);                
                
        
        //Find the two points that are maximally separated.
        Double maxDistance = Double.MIN_VALUE;
        Double angularDistance = -1.0;                
        
        //The angle of two points that are maximally separated across all comparison.
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
            globalAngleMax = angleMax;
            globalAngleZero = angleZero;
            
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
        
        Vector<Double> tPosVector = null;
        
        if (maxDistance == 180.0){
            tPosVector = transformPosVector(posVector, globalAngleZero, globalAngleMax);
            
        }else{
            tPosVector = transformPosVector(posVector, globalAngleZero, globalAngleMax, maxDistance);
            
        }
        
        double transMeanPos = 0.0;
        //Compute the mean in the transformed frame of reference.
        if(wVector == null){
            transMeanPos = computeMean(tPosVector);            
            
        }else{
            transMeanPos = computeWeightedMean(tPosVector, wVector);
            
        }
        
        //Transform back
        meanPos = unTransform(transMeanPos, globalAngleZero, globalAngleMax, maxDistance);
        
        out.println(Arrays.deepToString(posVector.toArray()));
        out.println(Arrays.deepToString(tPosVector.toArray()));
        out.println("mean position is "+meanPos);
        return meanPos;
    }
    
    
    /**
     * Transforms the positions in posVector by placing the origin at gAngleZero with
     * positive direction towards gAngleMax.
     * @param posVector
     * @param gAngleZero
     * @param gAngleMax
     * @param maxDistance should be < 180.0
     * @return 
     */
    private static Vector<Double> transformPosVector(Vector<Double> posVector, Double gAngleZero, Double gAngleMax,
                                                        Double maxDistance){
        Vector<Double> tPosVector = new Vector<Double>();
        
        for(Double position:posVector){
            double d1 = findMinAngularDistance(gAngleZero, position);
            double d2 = findMinAngularDistance(gAngleMax, position);
            
            if(((d1+d2)-maxDistance) <= ERROR){
                tPosVector.add(d1);
                
            }else{
                tPosVector.add(-1.0*d1);
            }
        }
        
        return tPosVector;
    }
    
    /**
     * Transforms the positions in posVector by placing the origin at gAngleZero with
     * positive direction towards gAngleMax.
     * 
     * This assumes maxDistance = 180.0
     * 
     * @param posVector
     * @param gAngleZero
     * @param gAngleMax
     * @return 
     */
    private static Vector<Double> transformPosVector(Vector<Double> posVector, Double gAngleZero, Double gAngleMax){
        
        Vector<Double> tPosVector = new Vector<Double>();
        
        posVector.remove(-10.0);
        
        int sgn = getSignOf180(posVector, gAngleZero, gAngleMax);
        
        for(Double position:posVector){
            double d = findMinAngularDistance(gAngleZero, position);            
            
            if(position.compareTo(gAngleMax) == 0){
                tPosVector.add(sgn*Math.abs(d));
            }
            else if(position >= gAngleZero || position < gAngleMax){
                tPosVector.add(d);
                
            }else if(position < gAngleZero || position > gAngleMax){
                tPosVector.add(-1.0 * d);
                
            }
        }
        
        return tPosVector;
    }
    
    /**
     * Finds the sign of gAngleMax in transformed coordinates when maxDistance = 180.0
     * @param posVector
     * @param gAngleZero
     * @param gAngleMax
     * @return 
     */
    private static int getSignOf180(Vector<Double> posVector, Double gAngleZero, Double gAngleMax){
        int sgn = 1;
        int positives = 0;
        int negatives = 0;
        
        for(Double position:posVector){
            if(position.compareTo(gAngleZero)==0 || position.compareTo(gAngleMax)==0){
                continue;
            }
            else if(position > gAngleZero || position < gAngleMax){
                positives++;
                
            }else if(position < gAngleZero || position > gAngleMax){
                negatives++;
            }
        }
        
        out.println(positives);//////////////////////////////////////////////////////////////////////////////
        out.println(negatives);//////////////////////////////////////////////////////////////////////////////
        
        if(positives>negatives){
            sgn =  1;
        }else{
            sgn = -1;
        }
        return sgn;
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
        angleZero = quad1[0];
        angleMax = quad2[quad2.length-1];
        
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
        angleZero = quad1[quad1.length-1];
        angleMax = quad4[0];
        
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
        angleZero = quad3[0];
        angleMax = quad4[quad4.length-1];
        
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
        angleZero = quad2[0];
        angleMax = quad3[quad3.length-1];
        
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
        double sgn = Math.signum(angle);
        if(sgn == 0){
            sgn+=1.0;
        }
        oppAngle = -1*sgn*(180-Math.abs(angle));
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
    
    
    
    /**
     * Finds the quadrant in which this position belongs.
     * @param position
     * @return 
     */
    private static int getQuadNumber(Double position){
       int qNo = -1;
       int[] constant = new int[]{360,0,0};
       int sgn = (int) Math.signum(position);
       double absPosition = position + constant[sgn+1];
       qNo = (int) (absPosition/90.0);       
       
       return qNo+1;
    }
    
    /**
     * Finds the angle obtained by moving a distance of delta_x from x in a clockwise direction
     * if delta_x is positive and anticlockwise direction is delta_x is negative.
     * @param x
     * @param delta_x
     * @return the transformed angle obtained after clockwise addition.
     */
    private static Double add(Double x, Double delta_x){
        Double y = null;
        if(delta_x > 0){
            y = addClockwise(x, delta_x);
            
        }else if(delta_x < 0){
            y = addAntiClockwise(x, Math.abs(delta_x));
            
        }else{
            y = x;
        }
        return y;        
    }
    
    /**
     * Finds the angle obtained by moving a distance of delta_x from x in a clockwise direction.
     * @param x
     * @param delta_x magnitude of the clockwise shift.
     * @return the transformed angle obtained after clockwise addition.
     */
    private static Double addClockwise(Double x, Double delta_x){
        Double y = null;
        delta_x = delta_x % 360;
        
        if(x+delta_x>180.0){
            y = (x+delta_x) - 360;
            
        }else{
            y = x + delta_x;
            
        }
        return y;
    }
    
    /**
     * Finds the angle obtained by moving a distance of delta_x from x in a anti-clockwise direction.
     * @param x
     * @param delta_x magnitude of the anti-clockwise shift.
     * @return the transformed angle obtained after anti-clockwise addition.
     */
    private static Double addAntiClockwise(Double x, Double delta_x){
        Double y = null;
        delta_x = delta_x % 360;
        
        if(x-delta_x<-180.0){
            y = (x - delta_x) + 360;
                    
        }else{
            y = x - delta_x;
            
        }
        return y;
    }
    
    /**
     * Computes the normal mean.
     * @param tPosVector
     * @return 
     */
    private static double computeMean(Vector<Double> tPosVector) {
        double meanPos = 0.0;
        double sum = 0.0;
        int n = 0;
        for(Double position: tPosVector){
            sum+=position;
            n++;
        }
        meanPos = sum/n;
        return meanPos;
    }
    

    /**
     * Computes the weighted mean.
     * @param tPosVector
     * @param wVector
     * @return 
     */
    private static double computeWeightedMean(Vector<Double> tPosVector, Vector<Double> wVector) {
        double meanPos = 0.0;
        double sum = 0.0;
        double sumW = 0.0;
        
        for(int i=0; i<=tPosVector.size()-1;i++){
            Double position = tPosVector.elementAt(i);
            Double weight = wVector.elementAt(i);
            sum+= position*weight;
            sumW+=weight;
        }
        meanPos = sum/sumW;
        return meanPos;
    }
    
    
    /**
     * Normalizes the tVector.
     * @param tVector
     * @return 
     */
    private static Vector<Double> getWeightVector(Vector<Double> tVector){
        Vector<Double> wVector = new Vector<Double>();
        for(Double position:tVector){
            wVector.add(1.0/position);
        }
        return wVector;
    }
    
    /**
     * Does the reverse transformation of an angle.
     * @param tMeanPos
     * @param gAngleZero
     * @param gAngleMax
     * @param maxDistance
     * @return 
     */
    private static double unTransform(Double tMeanPos, Double gAngleZero, Double gAngleMax, Double maxDistance){
        double meanPos = 0.0;
        if(maxDistance == 180.0){
            meanPos = add(gAngleZero, tMeanPos);
            
        }else{
            //Find the candidate mean postions.
            double cMean1 = addClockwise(gAngleZero, Math.abs(tMeanPos));
            double cMean2 = addAntiClockwise(gAngleZero, Math.abs(tMeanPos));
            
            out.println("CMean1 = "+cMean1);//////////////////////////////////////////
            out.println("CMean2 = "+cMean2);//////////////////////////////////////////
            
            //Find which one lies between gAngleZero and gAngleMax
            double d1 = findMinAngularDistance(gAngleZero, cMean1);
            double d2 = findMinAngularDistance(cMean1, gAngleMax);
            out.println(d1+" + "+ "d2 "+d2+" == "+(d1+d2)+", whereas"+maxDistance);///////////////////
            
            if(((d1+d2)-maxDistance) <= ERROR){
                meanPos = cMean1;
                out.println("1) d1 = "+d1+" + "+ "d2 "+d2+" == "+maxDistance);///////////////////
            }
            
            d1 = findMinAngularDistance(gAngleZero, cMean2);
            d2 = findMinAngularDistance(cMean2, gAngleMax);
            out.println(d1+" + "+ "d2 "+d2+" == "+(d1+d2)+", whereas"+maxDistance);///////////////////
            
            if( ((d1+d2)-maxDistance) <= ERROR){
                meanPos = cMean2;
                out.println("2) d1 = "+d1+" + "+ "d2 "+d2+" == "+maxDistance);///////////////////
            }
        }
        out.println("local mean pos "+meanPos);
        return meanPos;
    }
    
    public static void main(String args[]){
        Vector<Double> posVector = new Vector<Double>();
        posVector.add(132.0);
        posVector.add(145.0);
        
        double mean = findMeanPosition(posVector);
        out.println(mean);
    }
        
    
}
