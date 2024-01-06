package event;

import enums.EditableTile;

public class PalettePressedEvent extends EditorEvent {
    private final EditableTile type;

    public PalettePressedEvent(EditableTile type) {
        this.type = type;
    }

    public EditableTile getType() {
        return type;
    }
}
