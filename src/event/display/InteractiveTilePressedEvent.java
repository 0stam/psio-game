package event.display;

public class InteractiveTilePressedEvent extends DisplayEvent {
    private final int x;
    private final int y;
    private boolean rightMouseButton;

    public InteractiveTilePressedEvent(int x, int y, boolean rightMouseButton) {
        this.x = x;
        this.y = y;
        this.rightMouseButton = rightMouseButton;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isRightMouseButton() {
        return rightMouseButton;
    }
}
