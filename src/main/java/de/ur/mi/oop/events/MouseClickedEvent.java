package de.ur.mi.oop.events;

public class MouseClickedEvent extends GraphicsAppMouseEvent {

    private final MouseButton button;

    public MouseClickedEvent(long timestamp, int xPos, int yPos, MouseButton button) {
        super(timestamp, xPos, yPos, MouseEventType.PRESSED);
        this.button = button;
    }

    public MouseButton getButton() {
        return button;
    }

    public boolean isRightClick() {
        return button == MouseButton.RIGHT;
    }

    public boolean isLeftClick() {
        return button == MouseButton.RIGHT;
    }
}

