package event;

import tile.Tile;

public class ConnectionDeletedEvent extends ConnectionEvent {
    public ConnectionDeletedEvent(Tile tile) {
        super(tile);
    }
}
