package event.editor;

import enums.EditorMode;
import enums.Layer;

public class TilePressedEvent extends EditorEvent {
    private final int x;
    private final int y;
    private final Layer layer;
    private EditorMode mode;
    private final boolean remove;

    public TilePressedEvent(int x, int y, Layer layer, EditorMode mode, boolean remove) {
        this.x = x;
        this.y = y;
        this.layer = layer;
        this.mode = mode;
        this.remove = remove;
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

    public EditorMode getMode() {
        return mode;
    }

    public boolean isRemove() {
        return remove;
    }
}
