/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package physics;

import com.jme3.math.Vector3f;
import simulations.simplenbody.Body;

/**
 *
 * @author Fich
 */
public class PhysicsHelper {
    
    private static final float G = 0.000000000667408f;
    
    public static void handlePhysicsOnBody(Body body, float elapsedTime) {
        Vector3f velocity = body.getVelocity();
        
        Vector3f accelleration = new Vector3f(0, -G, 0);
        // gravity of nearest balls

        velocity = velocity.add(accelleration.mult(elapsedTime));
        System.out.println(velocity);
        body.move(velocity);
    }
    
    public static void handlePhysicsOnBody(Body body1, Body body2, float elapsedTime) {
        if (body1.equals(body2)) {
            return;
        }
        Vector3f velocity1 = body1.getVelocity();
        float distance = body1.getPosition().distance(body2.getPosition());
        if (distance <= body1.getRadius() + body2.getRadius()) {
            // they touch
            if (Math.random() < 0.00) {                
                body1.collideGamasutra(body2);
            } else {
                body1.merge(body2);
            }
            return;
        }
        Vector3f direction = body2.getPosition().subtract(body1.getPosition());
        float accelerationScalar = G * body2.getMass() / distance / distance;
        
        Vector3f accelleration = direction.normalize().mult(accelerationScalar);
        
        velocity1 = velocity1.add(accelleration.mult(elapsedTime));
        body1.setVelocity(velocity1);
        body1.move(velocity1.mult(elapsedTime));
    }
}
