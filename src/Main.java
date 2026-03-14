import graphics.GraphicsPanel;
import physics.PhysicsWorld;
import physics.RigidBody;
import maths.Vec2;

import javax.swing.JFrame;

class Main {
    public static void main(String[] args) {

        // Initialises test world
        PhysicsWorld world = new PhysicsWorld();
        RigidBody particle = new RigidBody();
        world.addObject(particle);
        particle.setPosition(new Vec2(0, -450));
        particle.setMass(10);
        particle.setVelocity(new Vec2(75, 100));
        particle.setForce(new Vec2(2, 2));
        particle.setActive();

        // Creates window to display particle
        int height = 500;
        JFrame worldView = new JFrame();
        worldView.setSize(500, height);
        worldView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        worldView.setResizable(true);
        worldView.setLocationRelativeTo(null);
        worldView.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Creates the panel to draw the particle
        GraphicsPanel panel = new GraphicsPanel(particle, height);
        worldView.add(panel);
        panel.startGraphicsThread();

        // Sets the world to be visable and starts physics thread
        worldView.setVisible(true);
        world.startWorldThread();
    }
}
