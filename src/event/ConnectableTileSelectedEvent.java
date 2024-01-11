package event;

import enums.ConnectableTile;
import tile.Tile;

public class ConnectableTileSelectedEvent extends ConnectionEvent {
    public ConnectableTileSelectedEvent(Tile tile) {
        super(tile);
    }
}
