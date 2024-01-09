package enums;

public enum EditableTile {
    EMPTY(true, true, Layer.BOTH, false,"EMPTY", Graphics.EMPTY),
    FLOOR(true, true, Layer.BOTTOM, false,"FLOOR", Graphics.FLOOR),
    WALL(true, true, Layer.BOTTOM, true,"WALL", Graphics.WALL),
    BUTTON(true, true, Layer.BOTTOM, false, "BUTTON", Graphics.BUTTON_RELEASED),
    DOOR(true, true, Layer.BOTTOM, true, "DOOR", Graphics.DOOR_CLOSED),
    ENEMY(false, true, Layer.UPPER, false, "CHASING ENEMY", Graphics.ENEMY),
    BOX(false, true, Layer.UPPER, false,"BOX", Graphics.BOX),
    PLAYER(false, true, Layer.UPPER, false,"PLAYER", Graphics.PLAYER),
    GOAL(true, true, Layer.BOTTOM, false,"GOAL", Graphics.GOAL);

    public final boolean isPlaceableBottom;
    public final boolean isPlaceableUpper;
    public final Layer preferredLayer;
    public final boolean fullTileWhenBoth;
    public final String name;
    public final Graphics graphics;


    EditableTile(boolean isPlaceableBottom, boolean isPlaceableUpper, Layer preferredLayer, boolean fullTileWhenBoth, String name, Graphics graphics) {
        this.isPlaceableBottom = isPlaceableBottom;
        this.isPlaceableUpper = isPlaceableUpper;
        this.preferredLayer = preferredLayer;
        // If editor layer is set to BOTH and tile's preferredLayer is not set to BOTH, the tile will act as if it occupied two layers with null on one of them
        // When single-layer tile is placed on a tile with fullTileWhenBoth, the latter one will be deleted
        // This property can only be true if tile's preferredLayer IS NOT SET TO BOTH
        this.fullTileWhenBoth = fullTileWhenBoth;
        this.name = name;
        this.graphics = graphics;
    }
}
