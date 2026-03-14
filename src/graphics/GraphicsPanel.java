package graphics;

import physics.RigidBody;

import javax.swing.*;
import java.awt.*;

public class GraphicsPanel extends JPanel implements Runnable {

    private int height;
    RigidBody particle;

    public GraphicsPanel(RigidBody currentObject, int height) {
        this.particle = currentObject;
        this.height = height;
        setDoubleBuffered(true);
        setLayout(null);
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
        g2.fillOval((int)particle.getPosition().i, height - (int)particle.getPosition().j, 20, 20);
        g2.dispose();
    }
}
