package event.editor;

import enums.EditableTile;
import enums.Layer;

public class TileModifiedEvent extends EditorEvent {
    private final EditableTile type;
    private final int x;
    private final int y;
    private final Layer layer;
    private final boolean remove;

    public TileModifiedEvent(EditableTile type, int x, int y, Layer layer, boolean remove) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.layer = layer;
        this.remove = remove;
    }

    public EditableTile getType() {
        return type;
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

    public boolean isRemove() {
        return remove;
    }
}
