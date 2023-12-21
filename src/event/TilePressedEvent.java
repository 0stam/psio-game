package event;

import enums.Layer;

public class TilePressedEvent extends EditorEvent {
    private final int x;
    private final int y;
    private final Layer layer;

    public TilePressedEvent(int x, int y, Layer layer) {
        this.x = x;
        this.y = y;
        this.layer = layer;
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
