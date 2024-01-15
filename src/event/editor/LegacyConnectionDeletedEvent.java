package event.editor;

import tile.Tile;

public class LegacyConnectionDeletedEvent extends LegacyConnectionEvent {
    public LegacyConnectionDeletedEvent(Tile from, Tile to) {
        super(from, to);
    }
}
