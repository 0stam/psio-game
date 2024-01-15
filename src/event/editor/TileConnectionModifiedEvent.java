package event.editor;

import connectableinterface.Connectable;
import enums.Layer;
import tile.Tile;

public class TileConnectionModifiedEvent extends EditorEvent {
    private final Connectable from;
    private final int x;
    private final int y;
    private final Layer layer;
    private final boolean remove;

    public TileConnectionModifiedEvent(Connectable from, int x, int y, Layer layer, boolean remove) {
        this.from = from;
        this.x = x;
        this.y = y;
        this.layer = layer;
        this.remove = remove;
    }

    public Connectable getFrom() {
        return from;
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
