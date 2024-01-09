package event;

import enums.Layer;

public class TilePressedEvent extends EditorEvent {
    private final int x;
    private final int y;
    private final Layer layer;
    private final boolean rightMouseButton;

    public boolean isRightMouseButton() {
        return rightMouseButton;
    }

    public TilePressedEvent(int x, int y, Layer layer, boolean rightMouseButton) {
        this.x = x;
        this.y = y;
        this.layer = layer;
        this.rightMouseButton = rightMouseButton;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Layer getLayer() {
        return layer;
    }
}
