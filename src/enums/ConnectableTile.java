package enums;

public enum ConnectableTile {
    DEFAULT(new EditableTile[]{}, false),
    BUTTON(new EditableTile[]{EditableTile.DOOR}, false),
    ENEMY(new EditableTile[]{EditableTile.PLAYER, EditableTile.MIMIC}, true);

    public final EditableTile[] allowedTiles; // Must be EditableTile and not ConnectibleTile to avoid forward references
    public final boolean exactlyOne; // if set to true, tile must have exactly one connection

    ConnectableTile(EditableTile[] allowedTiles, boolean exactlyOne) {
        this.allowedTiles = allowedTiles;
        this.exactlyOne = exactlyOne;
    }
}
