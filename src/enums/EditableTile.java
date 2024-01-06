package enums;

public enum EditableTile {
    EMPTY(true, true, Layer.BOTH, "EMPTY", Graphics.EMPTY),
    FLOOR(true, true, Layer.BOTTOM, "FLOOR", Graphics.FLOOR),
    WALL(true, true, Layer.BOTH, "WALL", Graphics.WALL),
    BUTTON(true, true, Layer.BOTTOM, "BUTTON", Graphics.BUTTON_RELEASED),
    DOOR(true, true, Layer.BOTTOM, "DOOR", Graphics.DOOR_CLOSED),
    ENEMY(false, true, Layer.UPPER, "CHASING ENEMY", Graphics.ENEMY),
    BOX(false, true, Layer.UPPER, "BOX", Graphics.BOX),
    PLAYER(false, true, Layer.UPPER, "PLAYER", Graphics.PLAYER),
    GOAL(true, true, Layer.BOTTOM, "GOAL", Graphics.GOAL);

    public final boolean isPlaceableBottom;
    public final boolean isPlaceableUpper;
    public final Layer preferredLayer;
    public final String name;
    public final Graphics graphics;

    EditableTile(boolean isPlaceableBottom, boolean isPlaceableUpper, Layer preferredLayer, String name, Graphics graphics) {
        this.isPlaceableBottom = isPlaceableBottom;
        this.isPlaceableUpper = isPlaceableUpper;
        this.preferredLayer = preferredLayer;
        this.name = name;
        this.graphics = graphics;
    }
}
