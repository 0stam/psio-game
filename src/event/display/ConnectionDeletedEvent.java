package event.display;

import tile.Tile;

public class ConnectionDeletedEvent extends DisplayConnectionEvent {
    public ConnectionDeletedEvent(Tile tile) {
        super(tile);
    }
}
