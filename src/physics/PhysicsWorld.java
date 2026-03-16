package physics;

import maths.Vec2;

import java.util.ArrayList;
import java.util.HashMap;

public class PhysicsWorld  implements  Runnable{
    private final Vec2 GRAVITY = new Vec2(0, -9.81f);
    private final Vec2 WORLD_SIZE;

    private final int CELL_SIZE = 40;
    private ArrayList<RigidBody>[][] grid;
    private final int gridWidth;
    private final int gridHeight;

    private boolean edgeStatus = true; // Turns edge collision on by default
    private static final int cps = 1000; // Calculations per second limiter
    private static final float timeMultiplier = 4f; // Slows down or speeds up simulation

    private ArrayList<RigidBody> objects = new ArrayList<>();

    public PhysicsWorld() {
        this.WORLD_SIZE = new Vec2(1000, 1000);
        gridWidth = 25;
        gridHeight = 25;
        this.grid = new ArrayList[this.gridWidth][this.gridHeight];
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                grid[x][y] = new ArrayList<>();
            }
        }
    }

    public PhysicsWorld(Vec2 worldSize) {
        this.WORLD_SIZE = worldSize;
        this.WORLD_SIZE.i = (int) this.WORLD_SIZE.i;
        this.WORLD_SIZE.j = (int) this.WORLD_SIZE.j;

        // Sets the grid's dimensions to fit the world size + any extra after dividing by 40
        this.gridWidth = ((int) worldSize.i) / CELL_SIZE + 1;
        this.gridHeight = ((int)worldSize.j) / CELL_SIZE + 1;
        this.grid = new ArrayList[this.gridWidth][this.gridHeight];
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                grid[x][y] = new ArrayList<>();
            }
        }
    }

    public int getNumOfObjects() {
        return objects.size();
    }

    private void clearGrid() {
        for (int i = 0; i < this.gridWidth; i++) {
            for (int j = 0; j < this.gridHeight; j++) {
                this.grid[i][j].clear();
            }
        }
    }

    public void addObject(RigidBody newObject) {
        synchronized (objects) {
            objects.add(newObject);
        }
    }

    public void destroyObject(int index) {
        synchronized (objects) {
            objects.remove(index);
        }
    }

    public RigidBody getObject(int index) {
        synchronized (objects) {
            return objects.get(index);
        }
    }

    private void toggleEdgesOn() {
        this.edgeStatus = true;
    }

    private void toggleEdgesOff() {
        this.edgeStatus = false;
    }

    private void edgeDetection(RigidBody object) {
        if (object.getPosition().i > this.WORLD_SIZE.i - object.getRadius()) {
            object.setVelocityIComp(-object.getVelocity().i * object.getRestitution());
            object.setPosition(new Vec2(this.WORLD_SIZE.i - object.getRadius(), object.getPosition().j));
        } else if (object.getPosition().i < 0) {
            object.setVelocityIComp(-object.getVelocity().i * object.getRestitution());
            object.setPosition(new Vec2(object.getRadius(), object.getPosition().j));
        }

        if (object.getPosition().j > this.WORLD_SIZE.j - object.getRadius()) {
            object.setVelocityJComp(-object.getVelocity().j * object.getRestitution());
            object.setPosition(new Vec2(object.getPosition().i, this.WORLD_SIZE.j - object.getRadius()));
        } else if (object.getPosition().j < object.getRadius()) {
            object.setVelocityJComp(-object.getVelocity().j * object.getRestitution());
            object.setPosition(new Vec2(object.getPosition().i, object.getRadius()));
        }
    }

    private void updateWorld(float dt) {

        clearGrid();

        synchronized (objects) {
            for (RigidBody object : objects) {
                if (!object.getActiveStatus()) {
                    continue;
                } // Skips all inactive objects

                object.setForce(object.getForce().add(GRAVITY.multiplyByScalar(object.getMass()))); // Applies Gravity Force
                object.setVelocity(object.getVelocity().add(object.getForce().divideByScalar(object.getMass()).multiplyByScalar(dt * timeMultiplier))); // Updates the Velocity by calculating acceleration

                if (edgeStatus) {
                    this.edgeDetection(object);
                } // Adds collision against the edge of the world


                // Collision Logic
                this.grid[(int)object.getPosition().i / CELL_SIZE][(int)object.getPosition().j / CELL_SIZE].add(object); // Adds all objects to their grid cell

                for (int x = (int)object.getPosition().i / CELL_SIZE - 1; x <= (int)object.getPosition().i / CELL_SIZE + 1; x++) {
                    for (int y = (int)object.getPosition().j / CELL_SIZE - 1; y <= (int)object.getPosition().j / CELL_SIZE + 1; y++) {

                        if (x < 0 || y < 0 || x >= gridWidth || y >= gridHeight)
                            continue;

                        for (RigidBody other : grid[x][y]) {

                            if (object == other) continue;

                            object.collision(other);
                        }
                    }
                }

                object.setPosition(object.getPosition().add(object.getVelocity().multiplyByScalar(dt * timeMultiplier))); // Updates position
                object.clearForce();
            }
        }
    }

    Thread worldThread;

    public void startWorldThread() {
        worldThread = new Thread(this);
        worldThread.start();
    }

    @Override
    public void run() {
        final double delta = 1.0 / cps;
        double accumulator = 0.0;

        long lastTime = System.nanoTime();

        while(worldThread != null) {

            long currentTime = System.nanoTime();
            double frameTime = (currentTime - lastTime) / 1000000000.0;
            lastTime = currentTime;

            frameTime = Math.min(frameTime, 0.25);

            accumulator += frameTime;


            if (accumulator >= delta) {

                // Calls the update function
                updateWorld(1f / cps);

                accumulator -= delta;
            }
        }
    }
}
