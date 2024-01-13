package enums;

public enum Arrow implements EditorGraphics{
    ARROW_UP("ARROW_UP", Graphics.ARROW_UP),
    ARROW_DOWN("ARROW_DOWN", Graphics.ARROW_DOWN),
    ARROW_LEFT("ARROW_LEFT", Graphics.ARROW_LEFT),
    ARROW_RIGHT("ARROW_RIGHT", Graphics.ARROW_RIGHT),
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
