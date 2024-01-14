package event;

import tile.Tile;

public class LegacyConnectionCreatedEvent extends LegacyConnectionEvent {
    public LegacyConnectionCreatedEvent(Tile from, Tile to) {
        super(from, to);
    }
}
