package event;

import tile.Tile;

public class ConnectionDeletedEvent extends ConnectionEvent {
    public ConnectionDeletedEvent(Tile from, Tile to) {
        super(from, to);
    }
}
