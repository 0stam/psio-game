package enums;

public enum ConnectableTile {
    DEFAULT(new EditableTile[]{}, false),
    BUTTON(new EditableTile[]{EditableTile.DOOR, EditableTile.REVERSE}, false),
    BUTTON_PERMANENT(new EditableTile[]{EditableTile.DOOR, EditableTile.REVERSE}, false),
    ENEMY(new EditableTile[]{EditableTile.PLAYER, EditableTile.MIMIC, EditableTile.SMART}, true);

    public final EditableTile[] allowedTiles; // Must be EditableTile and not ConnectibleTile to avoid forward references
    public final boolean exactlyOne; // if set to true, tile must have exactly one connection

    ConnectableTile(EditableTile[] allowedTiles, boolean exactlyOne) {
        this.allowedTiles = allowedTiles;
        this.exactlyOne = exactlyOne;
    }

    public boolean canConnect(EditableTile tile) {
        for (EditableTile allowedTile : allowedTiles) {
            if (allowedTile == tile) return true;
        }

        return false;
    }
}