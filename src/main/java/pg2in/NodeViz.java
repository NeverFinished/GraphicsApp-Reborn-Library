package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.graphics.Label;
import de.ur.mi.oop.graphics.Line;
import de.ur.mi.oop.graphics.Rectangle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class NodeViz extends SimpleGraphicsApp {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public static final int RADIUS = 26;
    public static final int ANIM_DELAY = 20;
    public static final int LABEL_X_OFFSET = 15;
    public static final int LABEL_Y_OFFSET = -10;

    Map<Character, VizSensorNode> nodes = new LinkedHashMap<>(); // TODO color mapping

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);
        setBackgroundColor(Colors.BLACK);
        for (char c = 'A'; c <= 'Y'; c++) {
            nodes.put(c, new VizSensorNode(c));
        }
        new Thread(() -> {
            try {
                var bis = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = bis.readLine()) != null) {
                    System.out.println(line);
                    parseLine(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
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

    class VizSensorNode {

        char key;
        float dx, dy;
        Circle node;
        Label label;
        Line xyVec;
        Rectangle left, right;

        void jitter() {
            dx = (float) (Math.random() * .15) * (Math.random() > 0.5 ? 1 : -1);
            dy = (float) (Math.random() * .15) * (Math.random() > 0.5 ? 1 : -1);
        }

        public VizSensorNode(char key) {
            this.key = key;
            int id = key - 'A';
            int gridX = id % 5;
            int gridY = id / 5;
            float px = (gridX+1) * WIDTH / 6f;
            float py = (gridY+1) * HEIGHT / 6f;
            left = add(new Rectangle(px, py-16, 32, 32, Colors.getRandomColor()));
            right = add(new Rectangle(px-32, py-16, 32, 32, Colors.getRandomColor()));
            node = add(new Circle(px, py, RADIUS, Colors.LIGHT_GREY));
            label = add(new Label(px, py, "" + key));
            xyVec = add(new Line(px, py, px, py, Colors.GREY));
        }

        void anim() {
            if (getFrameCounter() % 32 == key - 'A') jitter();
            node.move(dx, dy);
            if (node.getXPos() > WIDTH || node.getXPos() <= 0) {
                dx *= -1;
            }
            if (node.getYPos() >= HEIGHT || node.getYPos() <= 0) {
                dy *= -1;
            }
            label.setPosition(node.getXPos()-8, node.getYPos()+8);
            xyVec.setPosition(node.getPosition());
            left.setPosition(node.getXPos(), node.getYPos()-16);
            right.setPosition(node.getXPos()-32, node.getYPos()-16);
        }

        // A -8 564 -892 29 0 0 0 -33
        public void parse(String sensorLine) {

            String[] tokens = sensorLine.split(" ");
            if (tokens.length < 9) {
                System.err.println("cannot parse: " + sensorLine);
                return;
            }
            int accX = Integer.parseInt(tokens[1]);
            int accY = Integer.parseInt(tokens[2]);
            int accZ = Integer.parseInt(tokens[3]);
            int temp = Integer.parseInt(tokens[4]);
            int buttonA = Integer.parseInt(tokens[5]);
            int buttonB = Integer.parseInt(tokens[6]);
            int touchLogo = Integer.parseInt(tokens[7]);
            int rssi = Integer.parseInt(tokens[8]);

            // TODO

            label.setText(sensorLine);
            node.setRadius(temp);
            // TODO xyVec
            left.setVisible(buttonA > 0);
            right.setVisible(buttonB > 0);
        }
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
