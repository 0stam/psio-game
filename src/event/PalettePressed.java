package event;

import enums.Graphics;

public class PalettePressed extends EditorEvent {
    private final Graphics type;

    public PalettePressed(Graphics type) {
        this.type = type;
    }

    public Graphics getType() {
        return type;
    }
}
