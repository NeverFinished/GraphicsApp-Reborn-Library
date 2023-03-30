package de.ur.mi.oop.app;

import de.ur.mi.oop.graphics.GraphicsObject;

import java.util.ArrayList;
import java.util.Collection;

public abstract class GraphicsApp2 extends GraphicsApp {

    private Collection<GraphicsObject> scene = new ArrayList<>();

    public abstract void run();

    @Override
    public void draw() {
        for (GraphicsObject go : scene) {
            go.draw();
        }
    }

    protected GraphicsObject add(GraphicsObject graphicsObject) {
        scene.add(graphicsObject);
        return graphicsObject;
    }

    protected static void pause(int delayMs) {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
