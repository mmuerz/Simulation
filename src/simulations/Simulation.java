/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulations;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

/**
 *
 * @author Fich
 */
public abstract class Simulation {

    protected Node rootNode;
    protected AssetManager assetManager;

    public Simulation(Node rootNode, AssetManager assetManager) {
        super();
        this.rootNode = rootNode;
        this.assetManager = assetManager;
    }

    public abstract void init();

    public abstract void update(float tpf);
}
