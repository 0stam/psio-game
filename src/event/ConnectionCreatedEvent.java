package event;

import tile.Tile;

public class ConnectionCreatedEvent extends ConnectionEvent {
    public ConnectionCreatedEvent(Tile tile) {
        super(tile);
    }
}
