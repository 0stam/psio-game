package event;

import enums.EditableTile;
import enums.EditorGraphics;

public class PalettePressedEvent extends EditorEvent {
    private final EditorGraphics type;

    public PalettePressedEvent(EditorGraphics type) {
        this.type = type;
    }

    public EditorGraphics getType() {
        return type;
    }
}
