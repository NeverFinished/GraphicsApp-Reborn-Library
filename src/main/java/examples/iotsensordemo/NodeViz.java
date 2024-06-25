package examples.iotsensordemo;

import de.ur.mi.oop.app.Config;
import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Color;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.KeyPressedEvent;
import de.ur.mi.oop.events.MouseDraggedEvent;
import de.ur.mi.oop.graphics.*;
import de.ur.mi.oop.graphics.Label;
import de.ur.mi.oop.graphics.Point;
import de.ur.mi.oop.graphics.Rectangle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.*;

enum LabelMode {NONE, KEY, LINE}

public class NodeViz extends SimpleGraphicsApp implements DrawAdapter {

    public static final int ANIM_DELAY = 20;
    public static final int ARROW_BASE_SIZE = 40;
    public static final int BAR_HEIGHT = 180;
    public static final char MAX_CHAR = 'Y';
    public static final int LP_FACTOR = 4;
    public static final int MAX_COMM_DST = 300;
    public static final int FC_DIV_DYN_CONN = 8;
    public static final double MAX_BAR_ROTATION = 13.5;

    public static int width = 1200;
    public static int height = 800;

    public static final de.ur.mi.oop.colors.Color RED_MB = new de.ur.mi.oop.colors.Color(247, 115, 74);
    public static final de.ur.mi.oop.colors.Color YELLOW_MB = new de.ur.mi.oop.colors.Color(250, 245, 97);
    public static final de.ur.mi.oop.colors.Color ORANGE_MB = new de.ur.mi.oop.colors.Color(250, 150, 35);
    public static final de.ur.mi.oop.colors.Color BLUE_LIGHT_MB = new de.ur.mi.oop.colors.Color(121, 247, 237);
    public static final de.ur.mi.oop.colors.Color BLUE_DARK_MB = new de.ur.mi.oop.colors.Color(80, 190, 250);

    boolean gravity = false, limitForceToActive = true, dynamicConnections, showLines = true, showRSSI = true;
    LabelMode labelMode = LabelMode.KEY;
    char activeKey = 0;
    enum Direction { LEFT, STOP, RIGHT }
    Direction animDir = Direction.STOP;

    Map<Character, VizSensorNode> nodes = new LinkedHashMap<>();
    Map<Character, Color> colors = new LinkedHashMap<>();
    Collection<NodePair> lines = new ArrayList<>();
    Rectangle bar;
    Label debugLabel; // TODO add render here

    { // TODO more!
        colors.put('A', BLUE_LIGHT_MB);
        colors.put('B', ORANGE_MB);
        colors.put('C', ORANGE_MB);
        colors.put('D', ORANGE_MB);
        colors.put('E', YELLOW_MB);
        colors.put('F', YELLOW_MB);
        colors.put('G', BLUE_LIGHT_MB);
        colors.put('H', BLUE_DARK_MB);
        colors.put('I', BLUE_LIGHT_MB);
        colors.put('J', RED_MB);
        colors.put('K', RED_MB);
        colors.put('L', BLUE_DARK_MB);
        colors.put('M', YELLOW_MB);
        colors.put('N', YELLOW_MB);
        colors.put('O', BLUE_LIGHT_MB);
        colors.put('P', BLUE_LIGHT_MB);
        colors.put('Q', ORANGE_MB);
        colors.put('R', BLUE_DARK_MB);
        colors.put('S', RED_MB);
        colors.put('T', RED_MB);
        colors.put('U', RED_MB);
        colors.put('V', RED_MB);
        colors.put('W', RED_MB);
        colors.put('X', ORANGE_MB);
        colors.put('Y', YELLOW_MB);
    }

    record NodePair(VizSensorNode l, VizSensorNode r) {
    }

    @Override
    public void initialize() {
        getConfig().setFullScreen(true);
        // TODO move out of here...
        java.awt.Rectangle bounds = getAppManager().getGraphicsContext().getDeviceConfiguration().getBounds();
        setCanvasSize(bounds.width, bounds.height);
        getAppManager().getAppFrame().setLocation(0, 0);
        width = getConfig().getWidth();
        height = getConfig().getHeight();
        setBackgroundColor(Colors.BLACK);
        createNodes();
        debugLabel = add(new Label(15, 25, "fmin=1, fmax=8, fdiv=10, g=0.1", Colors.BLACK.brighter()));
        bar = add(new Rectangle(-50, height - BAR_HEIGHT, width + 100, BAR_HEIGHT, Colors.BLACK.brighter().brighter()) {
            @Override
            public void rotate(double degrees) {
                if ((getRotation() < MAX_BAR_ROTATION && degrees > 0)
                        || (getRotation() > -MAX_BAR_ROTATION && degrees < 0)) {
                    super.rotate(degrees);
                }
            }
        });
        add(new BarBoundCircle(40, Colors.BLACK.brighter()));
        add(new BarBoundCircle(25, Colors.BLACK));
        setDrawAdapter(this);

        buildStandardNodeConnections();

        //StdIn.pass(s -> parseLine(s));
        //new SerialReader(false).connect(this::parseLine);
    }

    private void createNodes() {
        for (char c = 'A'; c <= MAX_CHAR; c++) {
            VizSensorNode vsn = new VizSensorNode(this, c, colors.get(c));
            add(vsn.init());
            nodes.put(c, vsn);
        }
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
        while (isRunning() && pause(ANIM_DELAY)) {

            nodes.values().forEach(VizSensorNode::anim);

            if (dynamicConnections && getFrameCounter() % FC_DIV_DYN_CONN == 0) {
                var all = nodes.values().toArray(new VizSensorNode[0]);
                try {
                    (all[(int) (Math.random() * all.length)]).validateConnections();
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        System.exit(0);
    }

    @Override
    public void drawPreScene(Graphics2D g2d) {
        if (showLines) {
            var stroke = new BasicStroke(1);
            g2d.setStroke(stroke);
            if (dynamicConnections) {
                nodes.values().forEach(n -> drawDynamicConnections(g2d, n));
            } else {
                lines.forEach(n -> drawFixedConnection(g2d, n));
            }
        }
    }

    @Override
    public void drawPostScene(Graphics2D g2d) { // comm lines (fade out?)
        if (!showLines) { // show moving vectors (from accel)
            drawMovingOrAccelVector(g2d);
        }
        drawAnimBar(g2d);
    }

    private void drawAnimBar(Graphics2D g2d) {
        if (animDir == Direction.STOP || !bar.isVisible()) {
            return;
        }
        var stroke = new BasicStroke(1);
        AffineTransform at = g2d.getTransform();
        g2d.rotate(Math.toRadians(bar.getRotation()), bar.getXPos() + bar.getWidth() / 2.0, bar.getYPos() + bar.getHeight() / 2.0);
        g2d.setStroke(stroke);
        g2d.setColor(Colors.YELLOW.asAWTColor().darker());
        int mod = width + 4*ARROW_BASE_SIZE; // 2x2 extra draw space left+right
        double gap = 2.0 * mod / ARROW_BASE_SIZE;
        for (int i = 0; i < (int) (mod / gap); i++) {
            long x = (mod + (int) (gap*i) + getFrameCounter()) % mod;
            if (animDir == Direction.LEFT) { // invert for decreasing x pos
                x = mod - x - 1;
            }
            x -= 2*ARROW_BASE_SIZE; // now apply the left side offset
            g2d.fill(DoubleParallelogram.createShape(new Point2D.Double(x, height-BAR_HEIGHT+15), ARROW_BASE_SIZE,
                    ARROW_BASE_SIZE-10, (animDir.ordinal()-1)*10));
        }
        g2d.setTransform(at);
    }

    private void drawMovingOrAccelVector(Graphics2D g2d) {
        var stroke = new BasicStroke(2);
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
            var vec = GeometricHelper.scaleVector(x2 - x1, y2 - y1, vsn.node.getRadius(), 10);
            x2 = (float) (x1 + vec.x);
            y2 = (float) (y1 + vec.y);
            g2d.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
            g2d.fill(Arrow.createArrowShape(new Point2D.Double(x1, y1), new Point2D.Double(x2, y2)));
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
        double dst = Point2D.distance(x1, y1, x2, y2);
        long tmp = getFrameCounter() + (fcOffset / 10);
        double scale = tmp % fcOffset;
        if (scale > 0 && scale < dst) {
            var vec = GeometricHelper.scaleVector(x2 - x1, y2 - y1, scale / dst);
            g2d.fillOval((int) (x1 + vec.x) - 2, (int) (y1 + vec.y) - 2, 5, 5);
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
        if (keyChar == 'a') {
            limitForceToActive = !limitForceToActive;
        } else if (keyChar == 'l') {
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
        } else if (keyChar == 'e') {
            debugLabel.setVisible(!debugLabel.isVisible());
        } else if (keyChar == 'p') {
            nodes.values().forEach(n -> {
                float[] pxpy = n.resetPos();
                Point current = n.main.getPosition();
                n.main.move(pxpy[0] - current.getXPos(), pxpy[1] - current.getYPos());
            });
            bar.setRotation(0);
        } else if (keyChar == 'z') {
            bar.setRotation(0);
            nodes.values().forEach(n -> n.checkForBarBounce(false));
        } else if (keyChar == ',') {
            bar.rotate(-2);
            nodes.values().forEach(n -> n.checkForBarBounce(false));
        } else if (keyChar == '.') {
            bar.rotate(2);
            nodes.values().forEach(n -> n.checkForBarBounce(false));
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
        } else if (keyChar == '1' || keyChar == '2' || keyChar == '3') {
            animDir = Direction.values()[Integer.parseInt(""+keyChar)-1];
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
        GraphicsAppLauncher.launch(NodeViz.class.getSimpleName(), new Config().withFullScreen(true));
    }

    private class BarBoundCircle extends Circle {
        public BarBoundCircle(float radius, Color c) {
            super(NodeViz.width / 2f, NodeViz.height - (NodeViz.BAR_HEIGHT / 2f), radius, c);
        }

        @Override
        public boolean isVisible() {
            return bar.isVisible();
        }
    }
}
