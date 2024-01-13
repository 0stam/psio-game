package enums;

public enum Arrow implements EditorGraphics{
    ARROW_UP("UP", Graphics.ARROW_UP),
    ARROW_DOWN("DOWN", Graphics.ARROW_DOWN),
    ARROW_LEFT("LEFT", Graphics.ARROW_LEFT),
    ARROW_RIGHT("RIGHT", Graphics.ARROW_RIGHT),
    EMPTY("EMPTY", Graphics.EMPTY);
    public final String name;
    public final Graphics graphics;


    Arrow(String name, Graphics graphics) {
        this.name = name;
        this.graphics = graphics;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }
}
