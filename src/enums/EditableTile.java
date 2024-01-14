package enums;

public enum EditableTile implements EditorGraphics{
    EMPTY(true, true, Layer.BOTH, false,"Empty", Graphics.EMPTY),
    FLOOR(true, true, Layer.BOTTOM, false,"Floor", Graphics.FLOOR),
    WALL(true, true, Layer.BOTTOM, true,"Wall", Graphics.WALL),
    BUTTON(true, true, Layer.BOTTOM, false, "Button", Graphics.BUTTON_RELEASED),
    DOOR(true, true, Layer.BOTTOM, true, "Door", Graphics.DOOR_CLOSED),
    ENEMY(false, true, Layer.UPPER, false, "Chasing enemy", Graphics.ENEMY),
    MIMIC(false, true, Layer.UPPER, false,"Mimic", Graphics.MIMIC),
    SMART(false, true, Layer.UPPER, false,"Smart enemy", Graphics.SMART),
    BOX(false, true, Layer.UPPER, false,"Box", Graphics.BOX),
    PLAYER(false, true, Layer.UPPER, false,"Player", Graphics.PLAYER),
    GOAL(true, true, Layer.BOTTOM, false,"Goal", Graphics.GOAL);

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

    @Override
    public Graphics getGraphics() {
        return this.graphics;
    }

    @Override
    public String getName() {
        return name;
    }
}
