package event.display;

import enums.EditorGraphics;
import event.editor.EditorEvent;

public class PalettePressedEvent extends DisplayEvent {
    private final EditorGraphics type;

    public PalettePressedEvent(EditorGraphics type) {
        this.type = type;
    }

    public EditorGraphics getType() {
        return type;
    }
}
