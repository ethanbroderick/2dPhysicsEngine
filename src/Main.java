import graphics.GraphicsPanel;
import physics.PhysicsWorld;
import physics.RigidBody;
import maths.Vec2;

import javax.swing.JFrame;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class Main {
    public static void main(String[] args) {

        // Creates window to display particle
        JFrame worldView = new JFrame();
        worldView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        worldView.setResizable(true);
        worldView.setLocationRelativeTo(null);
        worldView.setExtendedState(JFrame.MAXIMIZED_BOTH);
        worldView.setVisible(true);
        int worldHeight = worldView.getHeight() - worldView.getInsets().top - worldView.getInsets().bottom - 10; // Subtracted 10 as the particles were off the screen slightly for some reason
        int worldWidth = worldView.getWidth() - worldView.getInsets().left - worldView.getInsets().right;

        // Creates the panel to draw the particle
        GraphicsPanel panel = new GraphicsPanel(worldHeight);

        // Initialises test world
        PhysicsWorld world = new PhysicsWorld(new Vec2(worldWidth, worldHeight));

        // Spawns Particles
        int numOfParticles = 5000;
        int addedLaterNum = 5000;
        RigidBody[] particles = new RigidBody[numOfParticles + addedLaterNum];
        Random random = new Random();
        int cols = 100;
        int spacing = 10;

        for (int i = 0; i < numOfParticles; i++) {

            int row = i / cols;
            int col = i % cols;

            float x = col * spacing + 500;
            float y = row * spacing + 500;

            particles[i] = new RigidBody(x, y, 25, 5);

            world.addObject(particles[i]);
            particles[i].setRestitution(0.1f);
            particles[i].setVelocity(new Vec2(random.nextFloat(10f), random.nextFloat(10f)));
            particles[i].setActive();
            panel.addObject(particles[i]);
        }

        worldView.add(panel);
        panel.startGraphicsThread();

        // Starts physics thread
        world.startWorldThread();

        // Waits then adds extra balls
        try {
            Thread.sleep(7500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Adding Particles");

        for (int i = numOfParticles; i < addedLaterNum + numOfParticles; i++) {
            particles[i] = new RigidBody(random.nextFloat(10, 1900), 980, 25, 5);
            world.addObject(particles[i]);
            particles[i].setRestitution(0.1f);
            particles[i].setVelocity(new Vec2(0, -500));

            particles[i].setActive();
            panel.addObject(particles[i]);
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.printf("There are %d objects in the world.\n", world.getNumOfObjects());
    }
}
