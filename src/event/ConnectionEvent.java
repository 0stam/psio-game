package event;

import tile.Tile;

public abstract class ConnectionEvent extends EditorEvent {
    private final Tile tile;

    public ConnectionEvent(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }
}
