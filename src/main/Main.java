package main;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import simulations.Simulation;
import simulations.simplenbody.SimpleKineticNBodySimulation;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    public static final int SIMULATIONSIZE = 4000;
    private Simulation currentSimulation;
    public static final float TIME_FACTOR = 1000.0f;

    @Override
    public void simpleInitApp() {
        float halfLength = ((float) SIMULATIONSIZE) / 2.0f;
        getCamera().setLocation(new Vector3f(-SIMULATIONSIZE, halfLength, halfLength));
        getCamera().lookAt(new Vector3f(halfLength,halfLength,halfLength), new Vector3f(0, 1, 0));
        getCamera().setFrustumFar(10000);
        getFlyByCamera().setMoveSpeed(500);
        //Node simulationBoundary = buildSimulationBoundary();
        //rootNode.attachChild(simulationBoundary);

        currentSimulation = new SimpleKineticNBodySimulation(rootNode, assetManager);
        currentSimulation.init();
    }

    @Override
    public void simpleUpdate(float tpf) {
        currentSimulation.update(tpf * TIME_FACTOR);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private Node buildSimulationBoundary() {
        Node boundary = new Node("boundary");
        Box theWorldBox = new Box(SIMULATIONSIZE, SIMULATIONSIZE, SIMULATIONSIZE);
        Geometry geom = new Geometry("box", theWorldBox);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Black);
        mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
        geom.setMaterial(mat);
        boundary.attachChild(geom);

        return boundary;
    }
    
    
}
