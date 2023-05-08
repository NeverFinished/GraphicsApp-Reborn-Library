package pg2in;

import de.ur.mi.oop.app.SimpleGraphicsApp;
import de.ur.mi.oop.colors.Colors;
import de.ur.mi.oop.events.MouseClickedEvent;
import de.ur.mi.oop.graphics.Circle;
import de.ur.mi.oop.graphics.Line;

import de.ur.mi.oop.launcher.GraphicsAppLauncher;

public class TicTacToe extends SimpleGraphicsApp {

    public static final int WINDOW_SIZE = 900;
    public static final int CELL_SIZE = WINDOW_SIZE / 3;
    public static final int HALF_CELL_SIZE = CELL_SIZE / 2;

    enum PlayerOrToken {
        CROSS,
        CIRCLE;
        PlayerOrToken next() {
            return PlayerOrToken.values()[(ordinal()+1)%PlayerOrToken.values().length];
        }
    }

    PlayerOrToken[][] game = new PlayerOrToken[3][3];
    PlayerOrToken currentPlayer = PlayerOrToken.CIRCLE;

    public void initialize() {
        setCanvasSize(WINDOW_SIZE, WINDOW_SIZE);
        setBackgroundColor(Colors.WHITE);
        for (int i = 1; i < 3; i++) {
            add(new Line(0, i*CELL_SIZE, WINDOW_SIZE, i*CELL_SIZE, Colors.BLACK, 10));
            add(new Line(i*CELL_SIZE, 0, i*CELL_SIZE, WINDOW_SIZE, Colors.BLACK, 10));
        }
    }

    @Override
    public void runMain() {
        while (isRunning()) {
            MouseClickedEvent mce = waitForMouseEvent();
            int x = mce.getXPos() / CELL_SIZE;
            int y = mce.getYPos() / CELL_SIZE;
            tryToSetTokenAndSwitchPlayer(x, y);
            if (checkForWinner() != null) {
                break;
            }
        }
        System.exit(0);
    }

    private PlayerOrToken checkForWinner() {
        return null;
    }

    private void tryToSetTokenAndSwitchPlayer(int x, int y) {
        if (game[x][y] != null) {
            return;
        }
        float r = 0.8f * HALF_CELL_SIZE;
        if (currentPlayer == PlayerOrToken.CIRCLE) {
            add(new Circle((x * CELL_SIZE) + HALF_CELL_SIZE, (y * CELL_SIZE) + HALF_CELL_SIZE, r, Colors.RED));
        } else {
            int cx = x * CELL_SIZE + HALF_CELL_SIZE;
            int cy = y * CELL_SIZE + HALF_CELL_SIZE;
            add(new Line(cx- r, cy- r, cx+ r, cy+ r, Colors.BLUE, 20));
            add(new Line(cx- r, cy+ r, cx+ r, cy- r, Colors.BLUE, 20));
        }
        game[x][y] = currentPlayer;
        currentPlayer = currentPlayer.next();
    }

    public static void main(String[] args) {
        GraphicsAppLauncher.launch();
    }
}
