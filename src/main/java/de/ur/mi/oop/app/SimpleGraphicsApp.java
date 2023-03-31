package de.ur.mi.oop.app;

import de.ur.mi.oop.graphics.GraphicsObject;
import de.ur.mi.oop.launcher.GraphicsAppLauncher;

import java.util.ArrayList;
import java.util.Collection;

/**
 * different approach to control the graphics scene:
 *
 * Do NOT override draw(), but add graphics objects to the instance itself.
 * Will be drawn (in order of being added)
 *
 * Instead, you might want to override run(), which is invoked by the main thread
 * after graphics initialization.
 *
 * initialize() can be used like in the regular GraphicsApp.
 */
public abstract class SimpleGraphicsApp extends GraphicsApp {

    private Collection<GraphicsObject> scene = new ArrayList<>();

    public void run() {
    }

    @Override
    public final void draw() {
        for (GraphicsObject go : scene) {
            if (!go.isVisible()) {
                continue;
            }
            go.draw();
        }
    }

    protected <T extends GraphicsObject> T add(T graphicsObject) {
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
