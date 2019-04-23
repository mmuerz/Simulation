/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulations.simplenbody;

import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Sphere;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Fich
 */
public class BodyTest {

    public BodyTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getPosition method, of class Body.
     */
    @Test
    public void testGetPosition() {
        System.out.println("getPosition");
        Body instance = new Body("sphere", new Sphere(100, 100, 1));

        Vector3f expResult = new Vector3f(15, 1, -3.819f);
        instance.move(expResult);
        Vector3f result = instance.getPosition();
        assertEquals(expResult, result);
    }

    /**
     * Test of getVelocity method, of class Body.
     */
    @Test
    public void testGetVelocity() {
        System.out.println("getVelocity");

        Body instance = new Body("sphere", new Sphere(100, 100, 1));
        Vector3f expResult = new Vector3f(15, 1, -3.819f);
        instance.setVelocity(expResult);
        Vector3f result = instance.getVelocity();
        assertEquals(expResult, result);
    }

    /**
     * Test of move method, of class Body.
     */
    @Test
    public void testMove_Vector3f() {
        System.out.println("move");
        Vector3f offset = null;
        Body instance = new Body("sphere", new Sphere(100, 100, 1));
        Vector3f expResult = Vector3f.ZERO;
        instance.move(expResult);
        Vector3f result = instance.getPosition();
        assertEquals(expResult, result);
        expResult = new Vector3f(15, 1, -3.819f);
        instance.move(expResult);
        result = instance.getPosition();
        assertEquals(expResult, result);
    }

    /**
     * Test of move method, of class Body.
     */
    @Test
    public void testMove_3args() {
        System.out.println("move");
        Body instance = new Body("sphere", new Sphere(100, 100, 1));
        Vector3f expResult = new Vector3f(15, 1, -3.819f);
        instance.move(expResult.x, expResult.y, expResult.z);
        Vector3f result = instance.getPosition();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMass method, of class Body.
     */
    @Test
    public void testGetMass() {
        System.out.println("getMass");
        Body instance = new Body("sphere", new Sphere(100, 100, 1));
        float expResult = 10.05616F;
        instance.setMass(expResult);
        float result = instance.getMass();
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of merge method, of class Body.
     */
    @Test
    public void testMerge() {
        System.out.println("merge");
        Body body2 = new Body("sphere", new Sphere(100, 100, 1));
        body2.setVelocity(new Vector3f(15, 15, 0.5f));
        Body instance = new Body("sphere", new Sphere(100, 100, 1));
        instance.setVelocity(new Vector3f(20, 0.5f, 30));
        double combinedMass = instance.getMass() + body2.getMass();
        instance.merge(body2);

        assertEquals(instance.getMass(), combinedMass, 0.0);
        assertEquals(instance.getVelocity(), new Vector3f(17.5f, 7.75f, 15.25f));
        assertEquals(instance.getRadius() > 1.0f, true);
        assertTrue(!body2.isExistent());
    }

    /**
     * Test of collide method, of class Body.
     */
    @Test
    public void testCollide() {
        System.out.println("collide");
        Body body2 = new Body("sphere", new Sphere(100, 100, 1));
        Body instance = new Body("sphere", new Sphere(100, 100, 1));

        body2.move(0, 0, 0);
        instance.move(0, 0, 0);
        body2.setVelocity(new Vector3f(-1, 0, 0));
        instance.setVelocity(new Vector3f(1, 0, 0));

        instance.collideGamasutra(body2);

        assertEquals(new Vector3f(-1, 0, 0), instance.getVelocity());
        assertEquals(new Vector3f(1, 0, 0), body2.getVelocity());
        body2.move(0, 0, 0);
        instance.move(0, 0, 0);
        body2.setVelocity(new Vector3f(-1, 2, 0));
        instance.setVelocity(new Vector3f(1, 3, 0));

        instance.collideGamasutra(body2);

        assertEquals(new Vector3f(1, 2, 0), body2.getVelocity());
        assertEquals(new Vector3f(-1, 3, 0), instance.getVelocity());
    }

    /**
     * Test of equals method, of class Body.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object o = null;
        Body instance = new Body("sphere", new Sphere(100, 100, 1));
        instance.setVelocity(Vector3f.ZERO);
        instance.move(Vector3f.ZERO);
        boolean expResult = false;
        boolean result = instance.equals(o);
        assertEquals(expResult, result);

        o = instance;
        instance = new Body("sphere", new Sphere(100, 100, 1));
        expResult = true;
        result = instance.equals(o);
        assertEquals(expResult, result);

        Body other = new Body("sphere", new Sphere(100, 100, 1));
        other.setVelocity(new Vector3f(2, 0, 0));
        other.move(Vector3f.ZERO);
        o = other;
        instance = new Body("sphere", new Sphere(100, 100, 1));
        expResult = false;
        result = instance.equals(o);
        assertEquals(expResult, result);

        other = new Body("sphere", new Sphere(100, 100, 1));
        other.setVelocity(Vector3f.ZERO);
        other.move(new Vector3f(2, 0, 0));
        o = other;
        instance = new Body("sphere", new Sphere(100, 100, 1));
        expResult = false;
        result = instance.equals(o);
        assertEquals(expResult, result);

        other = new Body("sphere", new Sphere(100, 100, 1));
        other.setVelocity(Vector3f.ZERO);
        other.move(Vector3f.ZERO);
        other.setMass(0.0f);
        o = other;
        instance = new Body("sphere", new Sphere(100, 100, 1));
        expResult = false;
        result = instance.equals(o);
        assertEquals(expResult, result);

        other = new Body("sphere", new Sphere(100, 100, 1));
        other.setVelocity(Vector3f.ZERO);
        other.move(Vector3f.ZERO);
        other.setMass(0.0f);
        instance = new Body("sphere", new Sphere(100, 100, 1));
        instance.setVelocity(Vector3f.ZERO);
        instance.move(Vector3f.ZERO);
        instance.setMass(0.0f);
        o = other;
        expResult = true;
        result = instance.equals(o);
        assertEquals(expResult, result);
    }

}
