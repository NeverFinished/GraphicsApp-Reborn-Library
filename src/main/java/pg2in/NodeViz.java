package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.KeyPressedEvent;
import de.ur.mi.oop.events.MouseDraggedEvent;
import de.ur.mi.oop.events.MousePressedEvent;
import de.ur.mi.oop.graphics.*;
import de.ur.mi.oop.graphics.Label;
import de.ur.mi.oop.graphics.Point;
import de.ur.mi.oop.graphics.Rectangle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

enum LabelMode {NONE, KEY, LINE}

public class NodeViz extends SimpleGraphicsApp implements DrawAdapter {

    public static final int ANIM_DELAY = 20;
    public static final int BAR_HEIGHT = 180;
    public static final char MAX_CHAR = 'Y';
    static final int LP_FACTOR = 4;

    public static int width = 1200;
    public static int height = 800;

    boolean gravity, showLines = true, showRSSI = true;
    LabelMode labelMode = LabelMode.LINE;
    char activeKey = 0;

    Map<Character, VizSensorNode> nodes = new LinkedHashMap<>(); // TODO color mapping
    Collection<NodePair> lines = new ArrayList<>();
    private Rectangle bar;

    record NodePair(VizSensorNode l, VizSensorNode r) {
    }

    @Override
    public void initialize() {
        java.awt.Rectangle bounds = getAppManager().getGraphicsContext().getDeviceConfiguration().getBounds();
        //setCanvasSize(WIDTH, HEIGHT);
        setCanvasSize(bounds.width, bounds.height - 64);
        getAppManager().getAppFrame().setLocation(0, 0);
        width = bounds.width;
        height = bounds.height - 64;
        setBackgroundColor(Colors.BLACK);
        for (char c = 'A'; c <= MAX_CHAR; c++) {
            nodes.put(c, new VizSensorNode(c));
        }
        bar = add(new Rectangle(-50, height - BAR_HEIGHT, width + 100, BAR_HEIGHT, Colors.BLACK.brighter().brighter()) {
            @Override
            public void rotate(double degrees) {
                if ((getRotation() < 13.5 && degrees > 0)
                        || (getRotation() > -13.5 && degrees < 0)) {
                    super.rotate(degrees);
                }
            }
        });
        setDrawAdapter(this);

        var lines = new LinkedHashSet<NodePair>();
        for (int x1 = 0; x1 < 5; x1++) { // TODO instead of static - keep connection to the closest three nodes
            for (int y1 = 0; y1 < 5; y1++) {
                char c1 = (char) ('A' + (y1 * 5) + x1);
                char c2 = (char) ('A' + (y1 * 5) + Math.min(4, x1 + 1)); // right
                if (nodes.containsKey(c2) && c1 != c2) {
                    lines.add(new NodePair(nodes.get(c1), nodes.get(c2)));
                }
                c2 = (char) ('A' + ((y1 + 1) * 5) + Math.min(4, x1 + 1)); // right-down
                if (nodes.containsKey(c2) && c1 != c2) {
                    lines.add(new NodePair(nodes.get(c1), nodes.get(c2)));
                }
                c2 = (char) ('A' + (Math.min(4, (y1 + 1)) * 5) + x1); // down
                if (nodes.containsKey(c2) && c1 != c2) {
                    lines.add(new NodePair(nodes.get(c1), nodes.get(c2)));
                }
                c2 = (char) ('A' + Math.max(0, Math.min(4, ((y1 - 1)))) * 5 + Math.min(4, x1 + 1)); // right-up
                if (nodes.containsKey(c2) && c1 != c2) {
                    lines.add(new NodePair(nodes.get(c1), nodes.get(c2)));
                }
            }
        }
        for (NodePair np : lines) {
            if (Math.random() > 0.5) { // swap the half
                this.lines.add(np);
            } else {
                this.lines.add(new NodePair(np.r, np.l));
            }
        }

        //StdIn.pass(s -> parseLine(s));
        new Serial(false).connect(this::parseLine);
    }

    private void parseLine(String line) {
        char id = line.charAt(0);
        var vsn = nodes.get(id);
        if (vsn != null) {
            vsn.parse(line);
        }
    }

    @Override
    public void runMain() {
        while (isRunning()) {

            nodes.values().forEach(VizSensorNode::anim);

            pause(ANIM_DELAY);
        }
        System.exit(0);
    }

    @Override
    public void draw(Graphics2D g2d) { // comm lines (fade out?)
        if (showLines) {
            Stroke stroke = new BasicStroke(1);
            g2d.setStroke(stroke);
            for (NodePair np : lines) {
                g2d.setColor(Colors.GREEN.asAWTColor().darker());
                float x1 = np.l.main.getXPos();
                float y1 = np.l.main.getYPos();
                float x2 = np.r.main.getXPos();
                float y2 = np.r.main.getYPos();
                g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
                g2d.setColor(Colors.GREEN.asAWTColor());
                try {
                    double dst = Point2D.distance(x1, y1, x2, y2);
                    long tmp = getFrameCounter() + np.l.fcOffset + np.r.fcOffset;
                    double scale = tmp % (2L * dst);
                    if (scale > 0 && scale < dst) {
                        double[] pc = GeometricHelper.cartesianToPolar(x2 - x1, y2 - y1);
                        double[] scaled = GeometricHelper.scalePolarAndConvertToCartesian(pc[0], pc[1], scale / dst);
                        g2d.fillOval((int) (x1 + scaled[0]) - 2, (int) (y1 + scaled[1]) - 2, 5, 5);
                    }
                } catch (ArithmeticException e) {
                    System.err.println(e.getMessage());
                }
            }
        } else {
            Stroke stroke = new BasicStroke(2);
            g2d.setStroke(stroke);
            g2d.setColor(Colors.CYAN.asAWTColor().darker());
            for (VizSensorNode vsn : nodes.values()) {
                float x1 = vsn.main.getXPos();
                float y1 = vsn.main.getYPos();
                float x2 = x1 + vsn.dx;
                float y2 = y1 + vsn.dy;
                double[] pc = GeometricHelper.cartesianToPolar(x2 - x1, y2 - y1);
                double[] scaled = GeometricHelper.scalePolarAndConvertToCartesian(pc[0], pc[1], vsn.node.getRadius(), 10);
                x2 = (float) (x1 + scaled[0]);
                y2 = (float) (y1 + scaled[1]);
                g2d.drawLine((int) x1, (int) y1,(int) x2, (int) y2);
                Shape arrowHead = Arrow.createArrowShape(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
                g2d.fill(arrowHead);
            }

        }
    }

    class VizSensorNode {

        char key;
        float dx, dy;
        Arc arc;
        Circle node;
        Label label;
        Line xyVec;
        Rectangle left, right;
        Compound main;
        int fcOffset = (int) (Math.random() * 200); // "average" distance
        long lastParsed;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            VizSensorNode that = (VizSensorNode) o;
            return key == that.key;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }

        @Override
        public String toString() {
            return "" + key;
        }

        void jitter() {
            float newDx = (float) (Math.random() * .1) * (Math.random() > 0.5 ? 1 : -1);
            float newDy = (float) (Math.random() * .1) * (Math.random() > 0.5 ? 1 : -1);
            if (dx > 0.1 || dy > 0.1) { // moving
                dx = ((LP_FACTOR - 1) * dx + newDx) / LP_FACTOR;
                dy = ((LP_FACTOR - 1) * dy + newDy) / LP_FACTOR;
            } else {
                dx = newDx;
                dy = newDy;
            }
        }

        public VizSensorNode(char key) {
            this.key = key;
            float[] pxpy = resetPos();
            main = add(new Compound(pxpy[0], pxpy[1]));
            arc = main.addRelative(new Arc(0, 0, 21 + 8, 90, 120, Colors.ORANGE, false));

            right = main.addRelative(new Rectangle(0, -16, 36, 32, Colors.getRandomColor()));
            left = main.addRelative(new Rectangle(-36, -16, 36, 32, Colors.getRandomColor()));

            right.setVisible(false);
            left.setVisible(false);

            node = main.addRelative(new Circle(0, 0, 21, Colors.LIGHT_GREY));
            label = main.addRelative(new Label(-8, 8, "" + key));
            xyVec = main.addRelative(new Line(0, 0, dx, dy, Colors.GREY));
            xyVec.setVisible(false); // TODO buggy
        }

        private float[] resetPos() {
            int id = key - 'A';
            int gridX = id % 5;
            int gridY = id / 5;
            float px = (gridX + 1) * width / 6f;
            px += (float) (Math.random() * 20f) * (Math.random() > 0.5 ? 1 : -1);
            float py = (gridY + 1) * height / 7f; // leave a empty row at bottom
            dx = 0; // also reset any movement
            dy = 0;
            return new float[]{px, py};
        }


        void anim() {
            if (gravity) {
                dx = ((LP_FACTOR - 1) * dx) / LP_FACTOR;
                if (barIntersect(-1).length == 0) { // TODO silent stopping buggy - okay for now
                    dy += 0.1;
                    // TODO einfallswinkel = ausfallswinkel
                } else { // "abrutschen"
                    dx = (float) (bar.getRotation() / 10);
                }
            } else {
                if (getFrameCounter() % 16 == key - 'A') {
                    jitter();
                }
            }
            main.move(dx, dy);

            label.setVisible(labelMode != LabelMode.NONE);
            bar.setVisible(gravity);
            if (main.getXPos() + node.getRadius() > width || main.getXPos() - node.getRadius() <= 0) {
                dx *= -1;
                while (main.getXPos() + node.getRadius() > width) {
                    main.move(-1, 0);
                }
                while (main.getXPos() - node.getRadius() <= 0) {
                    main.move(1, 0);
                }
            }
            if (gravity) {
                if (main.getYPos() <= 0) {
                    dy *= -1;
                    if (main.getYPos() >= height) {
                        main.setYPos(height - 8);
                    }
                } else {
                    double[] i = barIntersect();

                    if (i.length > 0) {
                        dy *= -0.75;
                        double rel = (2.0 * main.getXPos() / width) - 1.0;
                        if (isActive()) { // only for nodes that are in the network
                            bar.rotate(Math.min(1, Math.abs(dy)) * rel / 10);
                        }
                        while (barIntersect().length > 1) {
                            main.setYPos(main.getYPos() - 1);
                        }
                    }
                }
            } else {
                if (main.getYPos() >= height || main.getYPos() <= 0) {
                    dy *= -1;
                    while (main.getYPos() >= height) {
                        main.setYPos(main.getYPos() - 4);
                    }
                }
            }
            xyVec.setEndPoint(main.getXPos() + dx, main.getYPos() + dy);
        }

        private boolean isActive() {
            return System.currentTimeMillis() - lastParsed < 500;
        }

        private double[] barIntersect() {
            return barIntersect(0);
        }

        private double[] barIntersect(int offset) {
            double m = Math.tan(Math.toRadians(bar.getRotation()));
            double b = GeometricHelper.calculateIntercept(m, width / 2.0, height - BAR_HEIGHT);
            b += offset;
            return GeometricHelper.findLineCircleIntersections(main.getXPos(), main.getYPos(), node.getRadius(), m, b);
        }

        // A -8 564 -892 29 0 0 0 -33
        // A -48 480 932 33 0 0 0
        public void parse(String sensorLine) {

            var nd = NodeDataParser.parse(sensorLine);
            if (nd != null) {
                if (nd.anyButtonPressed()) {
                    dx = (float) (-1 * nd.roll / 10);
                    dy = (float) (nd.pitch / 10);
                }

                if (labelMode == LabelMode.LINE) {
                    label.setText(sensorLine + String.format(" %.2f %.2f", nd.pitch, nd.roll));
                } else {
                    label.setText("" + key);
                }
                node.setRadius(nd.temp);
                arc.setRadius(nd.temp + 8);
                left.setVisible(nd.buttonA > 0);
                right.setVisible(nd.buttonB > 0);
                arc.setEnd(nd.rssiScaled());
                arc.setVisible(isActive());

                //xyVec.setEndPoint(dx, dy);
            }
            lastParsed = System.currentTimeMillis();
        }
    }

    @Override
    public void onKeyPressed(KeyPressedEvent event) {
        char keyChar = event.getKeyChar();
        if (keyChar >= 'A' && keyChar <= 'Z') {
            activeKey = keyChar;
        } else {
            activeKey = 0;
        }
        if (keyChar == 'l') {
            labelMode = LabelMode.values()[(labelMode.ordinal() + 1) % LabelMode.values().length];
        } else if (keyChar == 'c') {
            showLines = !showLines;
        } else if (keyChar == 'g') {
            gravity = !gravity;
        } else if (keyChar == 'r') {
            showRSSI = !showRSSI;
        } else if (keyChar == 'p') {
            nodes.values().forEach(n -> {
                float[] pxpy = n.resetPos();
                Point current = n.main.getPosition();
                n.main.move(pxpy[0] - current.getXPos(), pxpy[1] - current.getYPos());
            });
            bar.setRotation(0);
        }

    }

    @Override
    public void onMouseDragged(MouseDraggedEvent event) {
        if (activeKey != 0) {
            var vsn = nodes.get(activeKey);
            if (vsn != null) {
                vsn.main.move(event.getXPos() - vsn.main.getXPos(), event.getYPos() - vsn.main.getYPos());
            }
        }
    }

    @Override
    public void onMousePressed(MousePressedEvent event) {
        if (event.getYPos() > height - BAR_HEIGHT) {
            gravity = !gravity;
        }
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
