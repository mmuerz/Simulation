/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulations.simplenbody;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import main.Main;

/**
 *
 * @author Fich
 */
public class Body extends Geometry {

    private Vector3f velocity = Vector3f.ZERO;
    private Vector3f position = Vector3f.ZERO;
    private Sphere sphere = null;
    private float mass = 1000000.0f;

    public Body() {
        super();
    }

    public Body(String name, Sphere aSphere) {
        super(name, aSphere);
        sphere = aSphere;

        setMass((float) (mass * Math.pow(getRadius(), 3)));
    }

    public void setMass(float mass) {
        this.mass = mass;
    }
    private boolean isExstend = true;

    public boolean isExistent() {
        return isExstend;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public Vector3f getVelocity() {
        return velocity;
    }

    @Override
    public Spatial move(Vector3f offset) {
        return moveInternally(offset);
    }

    @Override
    public Spatial move(float x, float y, float z) {
        Vector3f vector3f = new Vector3f(x, y, z);

        return moveInternally(vector3f);
    }

    private Spatial moveInternally(Vector3f offset) {

        position = position.add(offset);
        Node tmpParent = parent;
        if (tmpParent != null) {
            parent.detachChild(this);
        }
        Spatial movedBody = super.move(offset);

        if (tmpParent != null) {
            tmpParent.attachChild(movedBody);
        }
        if (isOutOfBox()) {
            System.out.println("moved out of box");
            isExstend = false;
        }
        return movedBody;
    }

    private boolean isOutOfBox() {
        final int size = Main.SIMULATIONSIZE;

        boolean isOutOfBox = position.getX() < 0;
        isOutOfBox |= position.getY() < 0;
        isOutOfBox |= position.getZ() < 0;
        isOutOfBox |= position.getX() > size;
        isOutOfBox |= position.getY() > size;
        isOutOfBox |= position.getZ() > size;

        return isOutOfBox;
    }

    public float getMass() {
        return mass;
    }

    public void merge(Body lighterBody) {

        float mass2 = lighterBody.getMass();
        Body heavierBody;
        if (mass2 > mass) {
            // switch bodies
            heavierBody = lighterBody;
            lighterBody = this;
        } else {
            heavierBody = this;
        }

        float newMass = heavierBody.getMass() + lighterBody.getMass();
        Vector3f p1 = heavierBody.getVelocity().mult(heavierBody.getMass());
        Vector3f p2 = lighterBody.getVelocity().mult(lighterBody.getMass());
        Vector3f pNacher = p1.add(p2);
        heavierBody.setVelocity(pNacher.divide(newMass));
        heavierBody.setMass(newMass);
        float newRadius = heavierBody.calculateRadiusAfterMerge(lighterBody);
        heavierBody.setRadius(newRadius);

        lighterBody.setExisting(false);
    }

    private float calculateRadiusAfterMerge(Body body2) {
        double thisRadiusCube = Math.pow(getRadius(), 3.0);
        double otherRadiusCube = Math.pow(body2.getRadius(), 3.0);
        float newRadius = (float) Math.pow(thisRadiusCube + otherRadiusCube, 1.0 / 3.0);
        return newRadius;
    }

    public void collideGamasutra(Body otherBody) {
        final Vector3f v2 = otherBody.getVelocity();
        final Vector3f v1 = getVelocity();
        // First, find the normalized vector n from the center of 
        // circle1 to the center of circle2
        Vector3f n = getPosition().subtract(otherBody.getPosition());
        n.normalize();
        // Find the length of the component of each of the movement
        // vectors along n. 
        // a1 = v1 . n
        // a2 = v2 . n
        float a1 = v1.dot(n);
        float a2 = v2.dot(n);

        // Using the optimized version, 
        // optimizedP =  2(a1 - a2)
        //              -----------
        //                m1 + m2
        float optimizedP = (float) ((2.0 * (a1 - a2)) / (getMass() + otherBody.getMass()));

        // Calculate v1', the new movement vector of circle1
        // v1' = v1 - optimizedP * m2 * n
        Vector3f v1Final = v1.subtract(n.mult(optimizedP * otherBody.getMass()));

        // Calculate v1', the new movement vector of circle1
        // v2' = v2 + optimizedP * m1 * n
        Vector3f v2Final = v2.add(n.mult(optimizedP * getMass()));

        setVelocity(v1Final);
        otherBody.setVelocity(v2Final);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Body) {
            Body body2 = (Body) o;
            if (body2.getMass() == mass && body2.getVelocity().equals(velocity) && body2.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    private void setExisting(boolean b) {
        isExstend = b;
    }

    private Vector2f calcV1elastic(Vector3f velocity1, Vector3f velocity2, float m1, float m2) {
        float vFinal2 = 0.0f;
        float v1 = velocity1.length();
        float v2 = velocity2.length();
        float vFinalPlus = 0f;
        float vFinalMinus = 0f;
        if (m1 != m2) {

            float mStar = 1 - m2 / m1;
            float q = 2 * v1 * v2 / mStar - v2 * v2 * mStar;
            float pHalf = (v1 - v2) / mStar;

            vFinalPlus = -pHalf + (float) (Math.sqrt(((double) pHalf * pHalf - q)));
            vFinalMinus = -pHalf - (float) (Math.sqrt(((double) pHalf * pHalf - q)));
        } else {
            // masses are equal ==> only one solution
            float v1MinusV2 = (v1 - v2);
            if (v1MinusV2 == 0.0f) {
                v1MinusV2 = 0.000000001f;
            }
            vFinalMinus = vFinalPlus = -v1 * v2 / v1MinusV2;
        }

        return new Vector2f(vFinalPlus, vFinalMinus);
    }

    public float getRadius() {
        return sphere.radius;
    }

    public void setRadius(float aRadius) {
        sphere.updateGeometry(sphere.getZSamples(), sphere.getRadialSamples(), aRadius);
    }

}
