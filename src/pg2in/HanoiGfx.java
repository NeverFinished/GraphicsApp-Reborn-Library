package pg2in;

import de.ur.mi.oop.app.GraphicsApp2;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.graphics.GraphicsObject;
import de.ur.mi.oop.graphics.Rectangle;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.util.Arrays;
import java.util.Stack;

public class HanoiGfx extends GraphicsApp2 {

    public static final int WIDTH = 900;
    public static final int HEIGHT = 500;
    public static final int L_CENTER = WIDTH / 6 * 1;
    public static final int M_CENTER = WIDTH / 6 * 3;
    public static final int R_CENTER = WIDTH / 6 * 5;
    public static final int ROD_DISTANCE = WIDTH / 3;
    public static final int BOTTOM_MARGIN = 50;
    public static final int ROD_WIDTH = 10;

    public static final int NUM_DISCS = 3;

    public static final int DISC_HEIGHT = 20;
    public static final int DISC_BASE_WIDTH = 80;
    public static final int DISC_BASE_INCR = 20;

    public static final int ANIM_PX_PER_STEP = 15;
    public static final int ANIM_DELAY = 10;

    Rectangle[] discs;

    void addRod(int center) {
        int height = (NUM_DISCS + 3) * DISC_HEIGHT;
        int rod_top_y = HEIGHT - BOTTOM_MARGIN - height;
        Rectangle rod = new Rectangle(center - ROD_WIDTH / 2, rod_top_y, ROD_WIDTH, height);
        rod.setColor(Colors.BLACK);
        // rod.setFilled(1);
        scene.add(rod);
    }

    void addDisc(int num) {
        int width = ((num + 1) * DISC_BASE_INCR) + DISC_BASE_WIDTH;
        Rectangle disc = new Rectangle(L_CENTER - width / 2, 0, width, DISC_HEIGHT);
        disc.setColor(Colors.BLUE);
        // setFilled(disc, 1);
        scene.add(disc);
        discs[num] = disc;
    }

    @Override
    public void initialize() {
        setCanvasSize(900, 500);
        addRod(L_CENTER);
        addRod(M_CENTER);
        addRod(R_CENTER);
        discs = new Rectangle[numDiscs];
        var left = stacks[0];
        for (int n = 0; n < numDiscs; n++) {
            addDisc(n);
            left.push(numDiscs - n);
        }
    }

    @Override
    public void draw() {
        for (GraphicsObject go : scene) {
            go.draw();
        }
    }

    private Stack<Integer>[] stacks = new Stack[3];
    private int numDiscs;

    public HanoiGfx() {
        this.numDiscs = NUM_DISCS;
        for (int i = 0; i < stacks.length; i++) {
            stacks[i] = new Stack<>();
        }
    }

    /**
     * disc number (from stack): 1 smallest disk etc., cannot be zero
     * gridy is pos from stack: 0: bottom etc.
     * gridx is 0=left, 1=middle, 2=right (see stack2index())
     */
    void animate(int disc, int grid_y, int grid_x) {
        Rectangle discRect = discs[disc - 1];
        float dest_x = L_CENTER + (grid_x * ROD_DISTANCE) - discRect.getWidth() / 2;
        float dest_y = HEIGHT - BOTTOM_MARGIN - (grid_y * 1.25F + 1) * DISC_HEIGHT;
        float start_x = discRect.getXPos();
        float start_y = discRect.getYPos();
        float delta_x = dest_x - start_x;
        float delta_y = dest_y - start_y;
    int steps = (int) Math.sqrt(delta_x * delta_x + delta_y * delta_y) / ANIM_PX_PER_STEP;
        for (int i = 0; i <= steps; i++) {
            discRect.setPosition(start_x + delta_x * i / steps,
                    start_y + delta_y * i / steps + (float) Math.sin(Math.PI + Math.PI * i / steps) * Math.abs(delta_x) / 2);
            pause(ANIM_DELAY);
        }
    }

    void printPeg(int row, Stack<Integer> stack) {
        if (stack.size() > row) {
            System.out.printf("%d", stack.get(row));
        } else {
            System.out.print("|");
        }
        System.out.print("\t\t");
    }

    void printHanoi() {
        System.out.println();
        for (int row = numDiscs - 1; row >= 0; row--) {
            System.out.print("\t");
            printPeg(row, stacks[0]);
            printPeg(row, stacks[1]);
            printPeg(row, stacks[2]);
            System.out.println();
        }
        System.out.println("#########################");
    }

    void hanoi(int n, Stack<Integer> from, Stack<Integer> to, Stack<Integer> tmp) {
        if (n == 1) { // Basisfall: Eine Scheibe
            to.push(from.pop());
            printHanoi();
            animate(to.peek(), to.size()-1, stack2index(to));
        } else {
            hanoi(n - 1, from, tmp, to); // Alles außer die aktuelle Scheibe auf den Hilfsstapel legen...
            to.push(from.pop()); // "Eigene" Scheibe umlegen
            printHanoi();
            animate(to.peek(), to.size()-1, stack2index(to));
            hanoi(n - 1, tmp, to, from); // ... und wieder zurück legen, auf die eigene Scheibe
        }
    }

    int stack2index(Stack<Integer> stack) {
        return Arrays.asList(stacks).indexOf(stack);
    }

    @Override
    public void run() {
        pause(1000);
        for (int n = NUM_DISCS; n > 0; n--) { // "buildup" animation
            animate(n, NUM_DISCS - n, 0);
        }
        pause(1000);
        solve();
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }

    private void solve() {
        printHanoi();
        hanoi(numDiscs, stacks[0], stacks[2], stacks[1]);
    }
}
