package graphics;

import physics.RigidBody;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements Runnable {

    private final int height;
    ArrayList<RigidBody> objects = new ArrayList<>();
    private final static int fps = 1000;

    public GraphicsPanel(int height) {
        this.height = height;
        setDoubleBuffered(true);
        setLayout(null);
    }

    public void addObject(RigidBody object) {
        objects.add(object);
    }

    public void removeObject(RigidBody object) {
        objects.remove(object);
    }

    Thread graphicsThread;

    public void startGraphicsThread() {
        graphicsThread = new Thread(this);
        graphicsThread.start();
    }

    @Override
    public void run() {
        final double delta = 1.0 / fps;
        double accumulator = 0.0;

        long lastTime = System.nanoTime();

        while(graphicsThread != null) {

            long currentTime = System.nanoTime();
            double frameTime = (currentTime - lastTime) / 1000000000.0;
            lastTime = currentTime;

            frameTime = Math.min(frameTime, 0.25);

            accumulator += frameTime;


            if (accumulator >= delta) {
                // Paint
                repaint();

                accumulator -= delta;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.black);

        ArrayList<RigidBody> snapshot;

        synchronized (objects) {
            snapshot = new ArrayList<>(objects);
        }

        for (RigidBody object : snapshot) {
            // Y Coordinate is the height of the window - the objects position as JFrame uses the top left as 0,0 and not the bottom right
            g2.fillOval((int)object.getPosition().i,  height - (int)object.getPosition().j, object.getRadius() * 2, object.getRadius() * 2);
            //System.out.printf("Drawing at, x: %d, y: %d\n", (int)object.getPosition().i, height - (int)object.getPosition().j);
        }
    }
}
