package de.ur.mi.oop.app;

import de.ur.mi.oop.graphics.GraphicsObject;

import java.util.ArrayList;
import java.util.Collection;

public abstract class GraphicsApp2 extends GraphicsApp {
    protected Collection<GraphicsObject> scene = new ArrayList<>();

    public abstract void run();

    @Override
    public void draw() {
        for (GraphicsObject go : scene) {
            go.draw();
        }
    }

    protected static void pause(int delayMs) {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
