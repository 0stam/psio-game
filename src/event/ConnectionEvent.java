package event;

import tile.Tile;

public class ConnectionEvent extends EditorEvent {
    private final Tile from;
    private final Tile to;

    public ConnectionEvent(Tile from, Tile to) {
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
