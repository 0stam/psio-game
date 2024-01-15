package event.editor;

import connectableinterface.Connectable;
import tile.Tile;

public class TilesDisconnectedEvent extends EditorEvent{
    private final Connectable from;
    private final Tile to;

    public TilesDisconnectedEvent(Connectable from, Tile to) {
        this.from = from;
        this.to = to;
    }

    public Connectable getFrom() {
        return from;
    }

    public Tile getTo() {
        return to;
    }
}
