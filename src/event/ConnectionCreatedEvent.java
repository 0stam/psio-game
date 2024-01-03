package event;

import tile.Tile;

public class ConnectionCreatedEvent extends ConnectionEvent {
    public ConnectionCreatedEvent(Tile from, Tile to) {
        super(from, to);
    }
}
