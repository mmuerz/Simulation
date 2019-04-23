/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulations.simplenbody;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.List;
import main.Main;
import physics.PhysicsHelper;
import simulations.Simulation;

/**
 *
 * @author Fich
 */
public class SimpleKineticNBodySimulation extends Simulation {

    private static final float VELOCITY_MODIFIER = 0.0002f;
    private static final int N = 150;
    public static final List< Body> BODIES = new ArrayList<>();
    private boolean isInitFinished = false;

    public SimpleKineticNBodySimulation(Node rootNode, AssetManager assetManager) {
        super(rootNode, assetManager);
    }

    @Override
    public void init() {
        for (int i = 0; i < N; i++) {
            createBody(randomVector3fInWorld().divide(2.0f), rootNode);
        }
        createCenter();
        createEdges();
        
        isInitFinished = true;
        System.out.println("isInitFinished? " + isInitFinished);
    }

    private void createCenter() {
        float halfLength = ((float) Main.SIMULATIONSIZE) / 2.0f;
        Sphere b = new Sphere(100, 100, 5);
        Body body = new Body("sphere", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Green);
        body.setMaterial(mat);
        body.move(new Vector3f(halfLength, halfLength, halfLength));
        rootNode.attachChild(body);
        
        Sphere b1 = new Sphere(100, 100, 6);
        Body body1 = new Body("sphere", b1);

        body1.setMaterial(mat);
        body1.move(new Vector3f(0, 0, 0));
        rootNode.attachChild(body1);
    }
    
    
    private void createEdges() {
        
        Geometry body = createEdge();
        body.lookAt(Vector3f.UNIT_X, Vector3f.UNIT_Y);
        final int size = Main.SIMULATIONSIZE;
        final float halfSize = size/2.0f;
        body.move(0+halfSize,0,0);        
        Geometry body2 = createEdge();
        body2.lookAt(Vector3f.UNIT_Y, Vector3f.UNIT_Y);
        body2.move(0,0+halfSize,0);
        Geometry body3 = createEdge();
        body3.lookAt(Vector3f.UNIT_Z, Vector3f.UNIT_Y);
        body3.move(0,0,0+halfSize);
        
        Vector3f endOfBox = new Vector3f(size, size, size);
        Geometry body4 = createEdge();
        body4.move(size-halfSize,size,size);
        body4.lookAt(endOfBox, Vector3f.UNIT_Y);
        Geometry body5 = createEdge();
        body5.move(size,size-halfSize,size);
        body5.lookAt(endOfBox, Vector3f.UNIT_Y);
        Geometry body6 = createEdge();
        body6.move(size,size,size-halfSize);
        body6.lookAt(endOfBox, Vector3f.UNIT_Y);
        
        
        Vector3f endOfBox1 = new Vector3f(0, size, size);
        Geometry body7 = createEdge();
        body7.move(halfSize,size,size);
        body7.lookAt(endOfBox1, Vector3f.UNIT_Y);
        Geometry body8 = createEdge();
        body8.move(0,size-halfSize,size);
        body8.lookAt(endOfBox1, Vector3f.UNIT_Y);
        Geometry body9 = createEdge();
        body9.move(0,size,size-halfSize);
        body9.lookAt(endOfBox1, Vector3f.UNIT_Y);
        
        
        Vector3f endOfBox2 = new Vector3f(size, 0, size);
        Geometry body10 = createEdge();
        body10.move(size-halfSize,0,size);
        body10.lookAt(endOfBox2, Vector3f.UNIT_Y);
        Geometry body11 = createEdge();
        body11.move(size,0,size-halfSize);
        body11.lookAt(endOfBox2, Vector3f.UNIT_Y);
        
        
        Vector3f endOfBox3 = new Vector3f(size, size, 0);
        Geometry body12 = createEdge();
        body12.move(size-halfSize,size,0);
        body12.lookAt(endOfBox3, Vector3f.UNIT_Y);
        Geometry body13 = createEdge();
        body13.move(size,size-halfSize,0);
        body13.lookAt(endOfBox3, Vector3f.UNIT_Y);
    }

    private Geometry createEdge() {
        Cylinder edge = new Cylinder(1000, 20, 5, Main.SIMULATIONSIZE, true);
        Geometry body = new Geometry("edge", edge);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Red);
        body.setMaterial(mat);
        rootNode.attachChild(body);
        return body;
    }

    @Override
    public void update(float elapsedTime) {
        if (isInitFinished) {
            for (Body body : BODIES) {
                if (body.isExistent()) {
                    updateBody(elapsedTime, body);
                } else {
                    Node parent = body.getParent();
                    if (parent != null) {
                        parent.detachChild(body);
                        System.out.println("Detaching child because it no longer exists!");
                    }
                }
            }
        }
    }

    private void createBody(Vector3f position, Node parent) {

        Sphere b = new Sphere(100, 100, (float) (Math.random() * 4.0f));
        Body body = new Body("sphere", b);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White);
        body.setMaterial(mat);
        body.move(position);
        body.setVelocity(randomVector3fInWorld().subtract(Main.SIMULATIONSIZE, Main.SIMULATIONSIZE, Main.SIMULATIONSIZE).mult((float)VELOCITY_MODIFIER / (float)Main.SIMULATIONSIZE));

        addBody(body);
        parent.attachChild(body);
    }

    private Vector3f randomVector3fInWorld() {
        float randomFloatX = (float) (Math.random() * (Main.SIMULATIONSIZE) * 2);
        float randomFloatY = (float) (Math.random() * (Main.SIMULATIONSIZE) * 2);
        float randomFloatZ = (float) (Math.random() * (Main.SIMULATIONSIZE) * 2);
        return new Vector3f(randomFloatX, randomFloatY, randomFloatZ);
    }

    private synchronized void updateBody(float elapsedTime, Body body) {
        for (Body body2 : BODIES) {
            if (!body2.isExistent()) {
                continue;
            }
            PhysicsHelper.handlePhysicsOnBody(body, body2, elapsedTime);
        }

    }

    public synchronized void addBody(Body body) {

        BODIES.add(body);

    }
}
