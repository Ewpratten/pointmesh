package ca.retrylife.ics4u.pointmesh;

import java.util.ArrayList;
import java.awt.Point;

import ca.quarkphysics.hsa2.GraphicsConsole;

public class App implements Runnable {

    // Consts
    private final int POINT_SIZE = 10;
    private final int GAP_COUNT = 30;

    // Graphics window
    GraphicsConsole g2 = new GraphicsConsole(800, 600, "pointmesh");

    // List of points to draw between
    ArrayList<Point> points = new ArrayList<>();

    public static void main(String[] args) {
        new App().run();
    }

    private App() {

        // Configure console
        g2.setAntiAlias(true);
        g2.setLocationRelativeTo(null);
        g2.setResizable(false);
        g2.enableMouse();

    }

    @Override
    public void run() {

        // Program loop
        while (true) {

            // Watch for a mouse click
            if (g2.getMouseClick() == 1) {

                // Clear the canvas
                // g2.clear();

                // Pop off the first point
                if (points.size() == 3) {
                    points.remove(0);
                    g2.clear();
                }

                // Write point to list of points
                points.add(g2.getMousePosition());

                // Render each point
                for (Point p : points) {
                    g2.fillOval(p.x - POINT_SIZE / 2, p.y - POINT_SIZE / 2, POINT_SIZE / 2, POINT_SIZE / 2);
                }

                // If there is only 2 points, just draw a line between them
                if (points.size() == 2) {
                    // Get this and next point
                    Point t = points.get(0);
                    Point n = points.get(1);

                    // Draw a joiner line
                    g2.drawLine(t.x, t.y, n.x, n.y);

                    // Skip the rest of the loop
                    continue;
                } else if (points.size() == 1) {
                    continue;
                }

                // Render fill-lines between each point
                for (int i = 0; i < points.size() - 2; i++) {

                    // Get this, next and final point
                    Point t = points.get(i);
                    Point n = points.get(i + 1);
                    Point nn = points.get(i + 2);

                    // Determine the line lengths of T-N and N-NN
                    double dtn = Math.abs(Math.hypot(t.x - n.x, t.y - n.y));
                    double dnnn = Math.abs(Math.hypot(n.x - nn.x, n.y - nn.y));

                    // Join the inter-points between t and nn through n
                    for (int j = 0; j < GAP_COUNT; j++) {

                        // Find X coords
                        double x1 = t.getX() + (dtn / GAP_COUNT) * j;
                        double x2 = n.getX() + GAP_COUNT - ((dnnn / GAP_COUNT) * j);

                        // Find the approperate tn and nnn points for each line
                        double y1 = t.getY() + ((t.getY() - t.getY()) / (t.getX() - n.getX())) * (t.getX() - x1);
                        double y2 = n.getY() + ((n.getY() - nn.getY()) / (n.getX() - nn.getX())) * (n.getX() - x2);

                        // Draw joiner
                        g2.drawLine((int) (x1), (int) (y1), (int) (x2), (int) (y2));

                    }

                }
            }

            g2.sleep(50);
        }

    }

}
