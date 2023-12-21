package event;

import enums.Graphics;

public class PalettePressedEvent extends EditorEvent {
    private final Graphics type;

    public PalettePressedEvent(Graphics type) {
        this.type = type;
    }

    public Graphics getType() {
        return type;
    }
}
