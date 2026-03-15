package physics;

import maths.Vec2;

import java.util.ArrayList;

public class PhysicsWorld  implements  Runnable{
    private final Vec2 GRAVITY = new Vec2(0, -9.81f);
    private final Vec2 WORLD_SIZE;
    private boolean edgeStatus = true; // Turns edge collision on by default

    private static final int cps = 1000; // Calculations per second limiter
    private static final float timeMultiplier = 0.01f; // Slows down or speeds up simulation

    private ArrayList<RigidBody> objects = new ArrayList<>();

    public PhysicsWorld() {
        this.WORLD_SIZE = new Vec2(1000, 1000);
    }

    public PhysicsWorld(Vec2 worldSize) {
        this.WORLD_SIZE = worldSize;
    }

    public void addObject(RigidBody newObject) {
        objects.add(newObject);
    }

    public void destroyObject(int index) {
        objects.remove(index);
    }

    public RigidBody getObject(int index) {
        return objects.get(index);
    }

    private void toggleEdgesOn() {
        this.edgeStatus = true;
    }

    private void toggleEdgesOff() {
        this.edgeStatus = false;
    }

    private void edgeDetection(RigidBody object) {
        if (object.getPosition().i > this.WORLD_SIZE.i - object.getRadius()) {
            object.setVelocityIComp(-object.getVelocity().i);
            object.setPosition(new Vec2(this.WORLD_SIZE.i - object.getRadius(), object.getPosition().j));
        } else if (object.getPosition().i < 0) {
            object.setVelocityIComp(-object.getVelocity().i);
            object.setPosition(new Vec2(object.getRadius(), object.getPosition().j));
        }

        if (object.getPosition().j > this.WORLD_SIZE.j - object.getRadius()) {
            object.setVelocityJComp(-object.getVelocity().j);
            object.setPosition(new Vec2(object.getPosition().i, this.WORLD_SIZE.j - object.getRadius()));
        } else if (object.getPosition().j < object.getRadius()) {
            object.setVelocityJComp(-object.getVelocity().j);
            object.setPosition(new Vec2(object.getPosition().i, object.getRadius()));
        }
    }

    private void updateWorld(float dt) {
        for (RigidBody object : objects) {
            if (!object.getActiveStatus()) {continue;} // Skips all inactive objects

            object.setForce(object.getForce().add(GRAVITY.multiplyByScalar(object.getMass()))); // Applies Gravity Force
            object.setVelocity(object.getVelocity().add(object.getForce().divideByScalar(object.getMass()).multiplyByScalar(dt*timeMultiplier))); // Updates the Velocity by calculating acceleration

            if (edgeStatus) {this.edgeDetection(object);} // Adds collision against the edge of the world
        }

        for (int i = 0; i < objects.size(); i++) {
            RigidBody objectA = objects.get(i);

            if (!objectA.getActiveStatus()) {continue;}

            for (int j = i + 1; j < objects.size(); j++) {
                RigidBody objectB = objects.get(j);

                if (!objectB.getActiveStatus()) {continue;}

                objectA.collision(objectB);
            }

        }

        for (RigidBody object : objects) {
            if (!object.getActiveStatus()) {continue;} // Skips all inactive objects

            object.setPosition(object.getPosition().add(object.getVelocity().multiplyByScalar(dt*timeMultiplier))); // Updates position
            object.clearForce();
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
