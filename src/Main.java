import graphics.GraphicsPanel;
import physics.PhysicsWorld;
import physics.RigidBody;
import maths.Vec2;

import javax.swing.JFrame;
import java.util.Random;

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
        int numOfParticles = 500;
        RigidBody[] particles = new RigidBody[numOfParticles];
        Random random = new Random();
        int cols = 50;
        int spacing = 25;

        for (int i = 0; i < numOfParticles; i++) {

            int row = i / cols;
            int col = i % cols;

            float x = col * spacing + 50;
            float y = row * spacing + 500;

            particles[i] = new RigidBody(x, y, 10, 10);

            world.addObject(particles[i]);
            particles[i].setRestitution(0.75f);
            particles[i].setVelocity(new Vec2(random.nextFloat(1000f), random.nextFloat(1000f)));
            particles[i].setActive();
            panel.addObject(particles[i]);
        }

        worldView.add(panel);
        panel.startGraphicsThread();

        // Starts physics thread
        world.startWorldThread();
    }
}
