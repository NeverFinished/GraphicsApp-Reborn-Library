package de.ur.mi.oop.app;

import de.ur.mi.oop.events.MouseClickedEvent;
import de.ur.mi.oop.events.MousePressedEvent;
import de.ur.mi.oop.graphics.GraphicsObject;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private Collection<GraphicsObject> scene = new CopyOnWriteArrayList<>();
    private Deque<MouseClickedEvent> events = new ConcurrentLinkedDeque<>();

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

    @Override
    public void onMousePressed(MousePressedEvent event) {
        super.onMousePressed(event);
    }

    public MouseClickedEvent getMouseEvent() {
        return events.size() > 0 ? events.removeFirst() : null;
    }

    void addToEventQueue(MouseClickedEvent event) {
        events.addLast(event);
    }
}
