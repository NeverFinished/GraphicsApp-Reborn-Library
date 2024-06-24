package de.ur.mi.oop.graphics;

import java.awt.*;

public interface DrawAdapter {

    default void drawPreScene(Graphics2D g2d) {}
    void drawPostScene(Graphics2D g2d);

}
