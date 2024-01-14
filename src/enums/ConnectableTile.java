package enums;

public enum ConnectableTile {
    DEFAULT(new EditableTile[]{}, false, EditableTile.EMPTY),
    BUTTON(new EditableTile[]{EditableTile.DOOR}, false, EditableTile.BUTTON),
    ENEMY(new EditableTile[]{EditableTile.PLAYER, EditableTile.MIMIC}, true, EditableTile.ENEMY);

    public final EditableTile[] allowedTiles; // Must be EditableTile and not ConnectibleTile to avoid forward references
    public final boolean exactlyOne; // if set to true, tile must have exactly one connection
    public final EditableTile editableTile;

    ConnectableTile(EditableTile[] allowedTiles, boolean exactlyOne, EditableTile editableTile) {
        this.allowedTiles = allowedTiles;
        this.exactlyOne = exactlyOne;
        this.editableTile = editableTile;
    }
}
