package event.display;

import event.display.DisplayEvent;
import tile.Tile;

public abstract class DisplayConnectionEvent extends DisplayEvent {
    private final Tile tile;

    public DisplayConnectionEvent(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }
}
