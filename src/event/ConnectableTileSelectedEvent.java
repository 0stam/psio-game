package event;

import enums.ConnectableTile;

public class ConnectableTileSelectedEvent extends EditorEvent {
    private final ConnectableTile connectableTile;

    public ConnectableTileSelectedEvent(ConnectableTile connectableTile) {
        this.connectableTile = connectableTile;
    }

    public ConnectableTile getConnectableTile() {
        return connectableTile;
    }
}
