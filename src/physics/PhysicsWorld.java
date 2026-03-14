package physics;

import maths.Vec2;

import javax.print.DocFlavor;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class PhysicsWorld  implements  Runnable{
    private final Vec2 GRAVITY = new Vec2(0, -9.81f);
    private ArrayList<RigidBody> objects = new ArrayList<>();
    private static final int cps = 240; // Calculations per second limiter
    private static final float timeMultiplier = 0.03f; // Slows down or speeds up simulation

    /** Default Constructor
    public PhysicsWorld() {

    }
    **/

    public void addObject(RigidBody newObject) {
        objects.add(newObject);
    }

    public void destroyObject(int index) {
        objects.remove(index);
    }

    public RigidBody getObject(int index) {
        return objects.get(index);
    }

    private void updateWorld(float dt) {
        for (RigidBody object : objects) {
            if (!object.getActiveStatus()) {continue;} // Skips all inactive objects

            object.setForce(object.getForce().add(GRAVITY.multiplyByScalar(object.getMass()))); // Applies Gravity Force
            object.setVelocity(object.getVelocity().add(object.getForce().divideByScalar(object.getMass()).multiplyByScalar(dt*timeMultiplier))); // Updates the Velocity by calculating acceleration
            object.setPosition(object.getPosition().add(object.getVelocity().multiplyByScalar(dt*timeMultiplier))); // Updates position
            object.clearForce();

            System.out.printf("x: %.5f, y:%.5f\n", object.getPosition().i, object.getPosition().j);
        }
    }

    Thread worldThread;

    public void startWorldThread() {
        worldThread = new Thread(this);
        worldThread.start();
    }

    @Override
    public void run() {
        double calculationInterval = 1000000000 / cps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(worldThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / calculationInterval;

            lastTime = currentTime;

            if (delta >= 1) {

                // Calls the update function
                updateWorld((float)delta);

                delta--;
            }
        }

    }
}
