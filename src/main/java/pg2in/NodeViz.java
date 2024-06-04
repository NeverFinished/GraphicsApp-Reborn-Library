package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.graphics.Label;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

public class NodeViz extends SimpleGraphicsApp {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    public static final int RADIUS = 8;
    public static final int ANIM_DELAY = 10;
    public static final int LABEL_X_OFFSET = 15;
    public static final int LABEL_Y_OFFSET = -10;

    Map<Character, VizSensorNode> nodes = new LinkedHashMap<>();

    @Override
    public void initialize() {
        setCanvasSize(WIDTH, HEIGHT);
        setBackgroundColor(Colors.BLACK);
        nodes.put('A', new VizSensorNode('A'));
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

    // A 172 32 -1040 2667 21 0 0 0 0 0:-57
    private void parseLine(String line) {
        char id = line.charAt(0);
        VizSensorNode vsn = nodes.get(id);
        if (vsn != null) {
            vsn.parse(line.substring(2));
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
        int dx = 1, dy = 2;
        Circle ball;
        Label label;

        public VizSensorNode(char key) {
            this.key = key;
            ball = add(new Circle(0, 0, RADIUS, Colors.BLUE));
            label = add(new Label(LABEL_X_OFFSET, LABEL_Y_OFFSET, ""+key));
        }

        void anim() {
            ball.move(dx, dy);
            if (ball.getXPos() > WIDTH || ball.getXPos() <= 0) {
                dx *= -1;
            }
            if (ball.getYPos() >= HEIGHT || ball.getYPos() <= 0) {
                dy *= -1;
            }
            label.setPosition(ball.getXPos() + LABEL_X_OFFSET, ball.getYPos() + LABEL_Y_OFFSET);
        }

        // 172 32 -1040 2667 21 0 0 0 0 0:-57
        public void parse(String sensorLine) {

            String[] tokens = sensorLine.split(" ");
            if (tokens.length < 11) {
                System.err.println("cannot parse: " + sensorLine);
                return;
            }
            int accX = Integer.parseInt(tokens[0]);
            int accY = Integer.parseInt(tokens[1]);
            int accZ = Integer.parseInt(tokens[2]);
            int temp = Integer.parseInt(tokens[3]);
            int lightLevel = Integer.parseInt(tokens[4]);
            int soundLevel = Integer.parseInt(tokens[5]);
            int buttonA = Integer.parseInt(tokens[6]);
            int buttonB = Integer.parseInt(tokens[7]);
            int touch1 = Integer.parseInt(tokens[8]);
            int touch2 = Integer.parseInt(tokens[9]);
            //int touch3 = Integer.parseInt(tokens[10]); // TODO replace : with space

            // TODO

            label.setText(sensorLine);
        }
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
