package graphics;

import physics.RigidBody;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphicsPanel extends JPanel implements Runnable {

    private final int height;
    ArrayList<RigidBody> objects = new ArrayList<>();

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
        int fps = 240;
        double drawInterval = 1000000000 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(graphicsThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if (delta >=1) {
                // Paint
                repaint();

                delta--;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.black);

        for (RigidBody object : objects) {
            // Y Coordinate is the height of the window - the objects position as JFrame uses the top left as 0,0 and not the bottom right
            g2.fillOval((int)object.getPosition().i,  height - (int)object.getPosition().j, object.getRadius() * 2, object.getRadius() * 2);
            //System.out.printf("Drawing at, x: %d, y: %d\n", (int)object.getPosition().i, height - (int)object.getPosition().j);
        }
    }
}
