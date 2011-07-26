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
import org.junit.Ignore;
import java.util.Vector;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apurv
 */
public class ComputeUtilityTest {
    
    public ComputeUtilityTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of findMeanCoordinate method, of class ComputeUtility.
     */
    @Test
    public void testFindMeanCoordinate1() {
        System.out.println("\nfindMeanCoordinate Test1........");
        Vector<Double> posVector = null;
        
        posVector = new Vector<Double>();
        posVector.add(10.0);
        posVector.add(10.0);
        posVector.add(10.0);
        posVector.add(-40.0);
        posVector.add(100.0);

        double expResult = 0.0;
        double result = ComputeUtility.findMeanCoordinate(posVector);
        assertEquals(expResult, result, 0.0);
        
        //fail("The test case is a prototype.");
    }
    
    /**
     * Test of findMeanCoordinate method, of class ComputeUtility.
     */
    @Test
    public void testFindMeanCoordinate2() {
        System.out.println("\nfindMeanCoordinate Test2........");
        Vector<Double> posVector = null;
        
        posVector = new Vector<Double>();
        posVector.add(10.0);
        posVector.add(10.0);
        posVector.add(10.0);
        posVector.add(-40.0);
        posVector.add(100.0);
        posVector.add(-170.0);
        posVector.add(-80.0);
        posVector.add(20.0);
        posVector.add(-110.0);
        posVector.add(-105.0);
        posVector.add(140.0);

        double expResult = 0.0;
        double result = ComputeUtility.findMeanCoordinate(posVector);
        assertEquals(expResult, result, 0.0);
        
        //fail("The test case is a prototype.");
    }
    
    
    /**
     * Test of findMeanCoordinate method, of class ComputeUtility.
     */
    @Test
    public void testFindMeanCoordinate3() {
        System.out.println("\nfindMeanCoordinate Test3........");
        Vector<Double> posVector = null;
        
        posVector = new Vector<Double>();
        posVector.add(50.0);
        posVector.add(-40.0);
        posVector.add(100.0);
        posVector.add(-170.0);
        posVector.add(-80.0);       
        posVector.add(-110.0);
        posVector.add(140.0);
        posVector.add(-120.0);
        posVector.add(160.0);

        double expResult = 0.0;
        double result = ComputeUtility.findMeanCoordinate(posVector);
        assertEquals(expResult, result, 0.0);
        
        //fail("The test case is a prototype.");
    }
    
        
}
