package event.editor;

import tile.Tile;

public class LegacyConnectionEvent extends EditorEvent {
    private final Tile from;
    private final Tile to;

    public LegacyConnectionEvent(Tile from, Tile to) {
        this.from = from;
        this.to = to;
    }

    public Tile getFrom() {
        return from;
    }

    public Tile getTo() {
        return to;
    }
}
