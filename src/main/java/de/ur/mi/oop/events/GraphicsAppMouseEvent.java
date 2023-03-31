package de.ur.mi.oop.events;

import java.awt.event.MouseEvent;

public abstract class GraphicsAppMouseEvent extends Event {

    private final int xPos;
    private final int yPos;
    private final MouseEventType type;

    public GraphicsAppMouseEvent(long timestamp, int xPos, int yPos, MouseEventType type) {
        super(timestamp);
        this.xPos = xPos;
        this.yPos = yPos;
        this.type = type;
    }

    public MouseEventType getType() {
        return this.type;
    }

    public int getXPos() {
        return this.xPos;
    }

    public int getYPos() {
        return this.yPos;
    }

    public static GraphicsAppMouseEvent createMouseEventFromAWT(MouseEvent event, MouseEventType type) {
        long timestamp = System.currentTimeMillis();
        int xPos = event.getX();
        int yPos = event.getY();
        MouseButton button = MouseButton.values()[event.getButton()];
        return switch (type) {
            case PRESSED -> new MousePressedEvent(timestamp, xPos, yPos, button);
            case RELEASED -> new MouseReleasedEvent(timestamp, xPos, yPos, button);
            case MOVED -> new MouseMovedEvent(timestamp, xPos, yPos);
            case DRAGGED -> new MouseDraggedEvent(timestamp, xPos, yPos);
            case CLICKED -> new MouseClickedEvent(timestamp, xPos, yPos, button);
        };
    }

}
