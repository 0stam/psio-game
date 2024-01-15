package event.display;

import tile.Tile;

public class ConnectionCreatedEvent extends DisplayConnectionEvent {
    public ConnectionCreatedEvent(Tile tile) {
        super(tile);
    }
}
