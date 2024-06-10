package examples.iotsensordemo;

import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

class VizSensorNode {

    final NodeViz app;
    char key;
    Arc arc;
    Circle node;
    Label label;
    Rectangle left, right;
    Compound main;
    int fcOffset;
    long lastParsed;
    Set<VizSensorNode> dynNeighbor = new LinkedHashSet<>();
    Point2D.Float moveVec;
    NodeDataParser.NodeData latest;

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
        if (moveVec.x > 0.1 || moveVec.y > 0.1) { // moving
            moveVec.x = ((NodeViz.LP_FACTOR - 1) * moveVec.x + newDx) / NodeViz.LP_FACTOR;
            moveVec.y = ((NodeViz.LP_FACTOR - 1) * moveVec.y + newDy) / NodeViz.LP_FACTOR;
        } else {
            moveVec = new Point2D.Float(newDx, newDy);
        }
    }

    public VizSensorNode(NodeViz app, char key) {
        this.app = app;
        this.key = key;
    }

    Compound init() {
        fcOffset = 16 * (key - 'A') + (int) (Math.random() * NodeViz.MAX_COMM_DST); // add "average" distance
        float[] pxpy = resetPos();
        main = new Compound(pxpy[0], pxpy[1]);
        arc = main.addRelative(new Arc(0, 0, 21 + 8, 90, 120, Colors.ORANGE, false));

        right = main.addRelative(new Rectangle(0, -16, 36, 32, Colors.getRandomColor()));
        left = main.addRelative(new Rectangle(-36, -16, 36, 32, Colors.getRandomColor()));

        right.setVisible(false);
        left.setVisible(false);

        node = main.addRelative(new Circle(0, 0, 21, Colors.LIGHT_GREY));
        label = main.addRelative(new Label(-8, 8, "" + key));
        return main;
    }

    float[] resetPos() {
        int id = key - 'A';
        int gridX = id % 5;
        int gridY = id / 5;
        float px = (0.5f + gridX) * NodeViz.width / 5f;
        px += (float) (Math.random() * 20f) * (Math.random() > 0.5 ? 1 : -1);
        float py = (0.5f + gridY) * NodeViz.height / 6f; // leave a empty row at bottom
        moveVec = new Point2D.Float(); // also reset any movement
        return new float[]{px, py};
    }

    void anim() {
        if (app.gravity) {
            moveVec.x = ((NodeViz.LP_FACTOR - 1) * moveVec.x) / NodeViz.LP_FACTOR;
            if (barIntersect(-1).length == 0) { // TODO silent stopping buggy - okay for now
                moveVec.y += 0.1; // apply gravity
                // TODO einfallswinkel = ausfallswinkel
            } else { // "abrutschen"
                moveVec.x = (float) (app.bar.getRotation() / 10);
            }
        } else if (app.getFrameCounter() % 16 == key - 'A') {
            jitter();
        }
        
        main.move(moveVec.x, moveVec.y);
        label.setVisible(app.labelMode != LabelMode.NONE);
        app.bar.setVisible(app.gravity);

        sideBounce();
        if (app.gravity) {
            checkTopAndBarBounce();
        } else {
            checkTopAndBottomBounce();
        }
    }

    private void checkTopAndBottomBounce() {
        if (main.getYPos() >= NodeViz.height || main.getYPos() <= 0) {
            moveVec.y *= -1;
            while (main.getYPos() >= NodeViz.height) {
                main.setYPos(main.getYPos() - 4);
            }
            while (main.getYPos() <= 0) {
                main.setYPos(main.getYPos() + 4);
            }
        }
    }

    private void checkTopAndBarBounce() {
        if (main.getYPos()-node.getRadius() <= 0) { // top bounce, rarely happens TODO test
            moveVec.y *= -1;
            while (main.getYPos()-node.getRadius() <= 0) {
                main.setYPos(main.getYPos()+1);
            }
        } else {
            checkForBarBounce();
        }
    }

    private void checkForBarBounce() {
        if (barIntersect().length > 0) {
            moveVec.y *= -0.75;
            double rel = (2.0 * main.getXPos() / NodeViz.width) - 1.0;
            if (isActive()) { // only for nodes that are in the network
                app.bar.rotate(Math.min(1, Math.abs(moveVec.y)) * rel / 10);
            }
            while (barIntersect().length > 1) {
                main.setYPos(main.getYPos() - 1);
            }
        }
    }

    private void sideBounce() {
        if (main.getXPos() + node.getRadius() > NodeViz.width || main.getXPos() - node.getRadius() <= 0) {
            moveVec.x *= -1;
            while (main.getXPos() + node.getRadius() > NodeViz.width) {
                main.move(-1, 0);
            }
            while (main.getXPos() - node.getRadius() <= 0) {
                main.move(1, 0);
            }
        }
    }

    boolean isActive() {
        return System.currentTimeMillis() - lastParsed < 500;
    }

    private double[] barIntersect() {
        return barIntersect(0);
    }

    private double[] barIntersect(int offset) {
        double m = Math.tan(Math.toRadians(app.bar.getRotation()));
        double b = GeometricHelper.calculateIntercept(m, NodeViz.width / 2.0, NodeViz.height - NodeViz.BAR_HEIGHT);
        b += offset;
        return GeometricHelper.findLineCircleIntersections(main.getXPos(), main.getYPos(), node.getRadius(), m, b);
    }

    // A -8 564 -892 29 0 0 0 -33
    // A -48 480 932 33 0 0 0
    public void parse(String sensorLine) {
        latest = NodeDataParser.parse(sensorLine);
        if (latest != null) {
            moveVec = latest.maybeApply(moveVec);

            if (app.labelMode == LabelMode.LINE) {
                label.setText(sensorLine + String.format(" %.2f %.2f", latest.pitch, latest.roll));
            } else {
                label.setText("" + key);
            }
            node.setRadius(latest.temp);
            arc.setRadius(latest.temp + 8);
            left.setVisible(latest.buttonA > 0);
            right.setVisible(latest.buttonB > 0);
            arc.setEnd(latest.rssiScaled());
            arc.setVisible(isActive());

            //xyVec.setEndPoint(dx, dy);
        }
        lastParsed = System.currentTimeMillis();
    }

    public void validateConnections() {

        for (VizSensorNode vsn : new ArrayList<>(dynNeighbor)) {
            double distance = Point2D.distance(main.getXPos(), main.getYPos(), // too far away
                    vsn.main.getXPos(), vsn.main.getYPos());
            if (distance > NodeViz.MAX_COMM_DST) {
                dynNeighbor.remove(vsn);
                break; // only one at a time
            }
            if (isBlocked(vsn)) { // some "block" inbetween?
                dynNeighbor.remove(vsn);
                break; // only one at a time
            }
        }
        if (dynNeighbor.size() == 3) {
            return;
        }
        // find ONE new
        VizSensorNode closest = null;
        double closestDistance = NodeViz.MAX_COMM_DST + 1;
        for (VizSensorNode vsn : app.nodes.values()) {
            if (dynNeighbor.contains(vsn) || vsn.dynNeighbor.contains(this) || isBlocked(vsn)) {
                continue;
            }
            double distance = Point2D.distance(main.getXPos(), main.getYPos(),
                    vsn.main.getXPos(), vsn.main.getYPos());
            if (distance < closestDistance) {
                closest = vsn;
                closestDistance = distance;
            }
        }
        if (closest != null) {
            dynNeighbor.add(closest);
        }
    }

    private boolean isBlocked(VizSensorNode vsn) {
        double distance = Point2D.distance(main.getXPos(), main.getYPos(),
                vsn.main.getXPos(), vsn.main.getYPos());
        for (VizSensorNode third : app.nodes.values()) {
            if (third == this || third == vsn) {
                continue;
            }
            double dst1 = Point2D.distanceSq(main.getXPos(), main.getYPos(),
                    third.main.getXPos(), third.main.getYPos());
            double dst2 = Point2D.distanceSq(third.main.getXPos(), third.main.getYPos(),
                    vsn.main.getXPos(), vsn.main.getYPos());
            if (dst1 + dst2 < 1.1 * distance * distance) {
                return true;
            }
        }
        return false;
    }
}
