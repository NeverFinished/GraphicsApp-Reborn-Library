package examples.iotsensordemo;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Color;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.KeyPressedEvent;
import de.ur.mi.oop.events.MouseDraggedEvent;
import de.ur.mi.oop.events.MousePressedEvent;
import de.ur.mi.oop.graphics.*;
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
    public static final int MAX_COMM_DST = 300;

    public static int width = 1200;
    public static int height = 800;

    public static final de.ur.mi.oop.colors.Color RED_MB = new de.ur.mi.oop.colors.Color(247, 115, 74);
    public static final de.ur.mi.oop.colors.Color YELLOW_MB = new de.ur.mi.oop.colors.Color(250, 245, 97);
    public static final de.ur.mi.oop.colors.Color ORANGE_MB = new de.ur.mi.oop.colors.Color(250, 150, 35);
    public static final de.ur.mi.oop.colors.Color BLUE_LIGHT_MB = new de.ur.mi.oop.colors.Color(121, 247, 237);
    public static final de.ur.mi.oop.colors.Color BLUE_DARK_MB = new de.ur.mi.oop.colors.Color(80, 190, 250);

    boolean gravity, dynamicConnections, showLines = true, showRSSI = true;
    LabelMode labelMode = LabelMode.LINE;
    char activeKey = 0;

    Map<Character, VizSensorNode> nodes = new LinkedHashMap<>();
    Map<Character, Color> colors = new LinkedHashMap<>();
    Collection<NodePair> lines = new ArrayList<>();
    Rectangle bar;

    {
        colors.put('B', RED_MB);
        colors.put('C', YELLOW_MB);
        colors.put('D', ORANGE_MB);
        colors.put('E', BLUE_DARK_MB);
        colors.put('F', BLUE_LIGHT_MB);
    }

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
            VizSensorNode vsn = new VizSensorNode(this, c, colors.get(c));
            add(vsn.init());
            nodes.put(c, vsn);
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

        buildStandardNodeConnections();

        //StdIn.pass(s -> parseLine(s));
        new SerialReader(false).connect(this::parseLine);
    }

    private void buildStandardNodeConnections() {
        var lines = new LinkedHashSet<NodePair>();
        for (int x1 = 0; x1 < 5; x1++) {
            for (int y1 = 0; y1 < 5; y1++) {
                char c1 = (char) ('A' + (y1 * 5) + x1);
                char c2 = (char) ('A' + (y1 * 5) + Math.min(4, x1 + 1)); // right
                if (nodes.containsKey(c2) && c1 != c2) {
                    lines.add(new NodePair(nodes.get(c1 < c2 ? c1 : c2), nodes.get(c1 < c2 ? c2 : c1)));
                }
                c2 = (char) ('A' + ((y1 + 1) * 5) + Math.min(4, x1 + 1)); // right-down
                if (nodes.containsKey(c2) && c1 != c2) {
                    lines.add(new NodePair(nodes.get(c1 < c2 ? c1 : c2), nodes.get(c1 < c2 ? c2 : c1)));
                }
                c2 = (char) ('A' + (Math.min(4, (y1 + 1)) * 5) + x1); // down
                if (nodes.containsKey(c2) && c1 != c2) {
                    lines.add(new NodePair(nodes.get(c1 < c2 ? c1 : c2), nodes.get(c1 < c2 ? c2 : c1)));
                }
                c2 = (char) ('A' + Math.max(0, Math.min(4, ((y1 - 1)))) * 5 + Math.min(4, x1 + 1)); // right-up
                if (nodes.containsKey(c2) && c1 != c2) {
                    lines.add(new NodePair(nodes.get(c1 < c2 ? c1 : c2), nodes.get(c1 < c2 ? c2 : c1)));
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

            if (dynamicConnections && getFrameCounter() % 8 == 0) {
                var all = nodes.values().toArray(new VizSensorNode[0]);
                try {
                    (all[(int) (Math.random() * all.length)]).validateConnections();
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println(e.getMessage());
                }
            }

            pause(ANIM_DELAY);
        }
        System.exit(0);
    }

    @Override
    public void draw(Graphics2D g2d) { // comm lines (fade out?)
        if (showLines) {
            Stroke stroke = new BasicStroke(1);
            g2d.setStroke(stroke);
            if (dynamicConnections) {
                nodes.values().forEach(n -> drawDynamicConnections(g2d, n));
            } else {
                lines.forEach(n -> drawFixedConnection(g2d, n));
            }
        } else { // show moving vectors (from accel)
            drawMovingOrAccelVector(g2d);
        }
    }

    private void drawMovingOrAccelVector(Graphics2D g2d) {
        Stroke stroke = new BasicStroke(2);
        g2d.setStroke(stroke);
        g2d.setColor(Colors.CYAN.asAWTColor().darker());
        for (VizSensorNode vsn : nodes.values()) {
            float x1 = vsn.main.getXPos();
            float y1 = vsn.main.getYPos();
            float x2 = x1 + vsn.moveVec.x;
            float y2 = y1 + vsn.moveVec.y;
            if (vsn.latest != null && vsn.isActive()) {
                Point2D.Float vec = vsn.latest.maybeApply(null);
                x2 = x1 + vec.x;
                y2 = y1 + vec.y;
            }
            Point2D.Double vec = GeometricHelper.scaleVector(x2 - x1, y2 - y1, vsn.node.getRadius(), 10);
            x2 = (float) (x1 + vec.x);
            y2 = (float) (y1 + vec.y);
            g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            Shape arrowHead = Arrow.createArrowShape(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2));
            g2d.fill(arrowHead);
        }
    }

    private void drawDynamicConnections(Graphics2D g2d, VizSensorNode n) {
        for (VizSensorNode o : n.dynNeighbor) {
            drawConnection(g2d, n.main.getXPos(), n.main.getYPos(),
                    o.main.getXPos(), o.main.getYPos(), n.fcOffset + o.fcOffset);
        }
    }

    private void drawFixedConnection(Graphics2D g2d, NodePair np) {
        g2d.setColor(Colors.GREEN.asAWTColor().darker());
        drawConnection(g2d, np.l.main.getXPos(), np.l.main.getYPos(),
                np.r.main.getXPos(), np.r.main.getYPos(), np.l.fcOffset + np.r.fcOffset);
    }

    private void drawConnection(Graphics2D g2d, float x1, float y1, float x2, float y2, long fcOffset) {
        g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
        g2d.setColor(Colors.GREEN.asAWTColor());
        try {
            double dst = Point2D.distance(x1, y1, x2, y2);
            long tmp = getFrameCounter() + (fcOffset / 10);
            double scale = tmp % fcOffset;
            if (scale > 0 && scale < dst) {
                Point2D.Double vec = GeometricHelper.scaleVector(x2 - x1, y2 - y1, scale / dst);
                g2d.fillOval((int) (x1 + vec.x) - 2, (int) (y1 + vec.y) - 2, 5, 5);
            }
        } catch (ArithmeticException e) {
            System.err.println(e.getMessage());
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
        } else if (keyChar == 'r') {
            showRSSI = !showRSSI;
        } else if (keyChar == 'd') {
            dynamicConnections = !dynamicConnections;
            if (!dynamicConnections) {
                nodes.values().forEach(n -> n.dynNeighbor.clear());
            }
        } else if (keyChar == 'p') {
            nodes.values().forEach(n -> {
                float[] pxpy = n.resetPos();
                Point current = n.main.getPosition();
                n.main.move(pxpy[0] - current.getXPos(), pxpy[1] - current.getYPos());
            });
            bar.setRotation(0);
        } else if (keyChar == ',') {
            bar.rotate(-2);
        } else if (keyChar == '.') {
            bar.rotate(2);
        } else if (keyChar == '-') {
            bar.setRotation(0);
        } else if (keyChar == ' ' || keyChar == 'g') {
            gravity = !gravity;
            if (gravity) {
                for (VizSensorNode vsn : nodes.values()) {
                    while (vsn.main.getYPos() + vsn.node.getRadius() > bar.getYPos()) {
                        vsn.main.move(0, -32);
                    }
                }
            }
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

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
