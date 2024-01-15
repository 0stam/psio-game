package event.display;

import tile.Tile;

public class ConnectableTileSelectedEvent extends DisplayConnectionEvent {
    public ConnectableTileSelectedEvent(Tile tile) {
        super(tile);
    }
}
