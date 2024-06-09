package de.ur.mi.oop.app;

import de.ur.mi.oop.colors.Color;
import de.ur.mi.oop.events.MouseClickedEvent;
import de.ur.mi.oop.events.MousePressedEvent;
import de.ur.mi.oop.graphics.DrawAdapter;
import de.ur.mi.oop.graphics.GraphicsObject;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * different approach to control the graphics scene:
 *
 * Do NOT override draw(), but add graphics objects to the instance itself.
 * Will be drawn (in order of being added)
 *
 * Instead, you might want to override runMain(), which is invoked by the main thread
 * after graphics initialization. If so, (mouse click) events can be consumed with
 * {@link #getNextMouseEvent()}
 *
 * initialize() can be used like in the regular GraphicsApp.
 */
public abstract class SimpleGraphicsApp extends GraphicsApp {

    private final Collection<GraphicsObject> scene = new CopyOnWriteArrayList<>();
    private final BlockingQueue<MouseClickedEvent> events = new LinkedBlockingQueue<>();

    private Color backgroundColor;
    private boolean running;

    public void runMain() {
    }

    @Override
    public final void draw() {
        if (backgroundColor != null) {
            drawBackground(backgroundColor);
        }
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

    protected <T extends GraphicsObject> T remove(T graphicsObject) {
        scene.remove(graphicsObject);
        return graphicsObject;
    }

    protected Collection<GraphicsObject> getScene() {
        return scene;
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

    public MouseClickedEvent getNextMouseEvent() {
        try {
            return events.poll(0, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public MouseClickedEvent waitForMouseEvent() {
        try {
            return events.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void addToEventQueue(MouseClickedEvent event) {
        events.offer(event);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isRunning() {
        return running;
    }

    void setRunning(boolean running) {
        this.running = running;
    }

    public boolean overridesRun() {
        try {
            Class<? extends SimpleGraphicsApp> concreteClass = this.getClass();
            concreteClass.getDeclaredMethod("runMain", new Class<?>[0]);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

}
