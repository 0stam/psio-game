package event;

import enums.Arrow;


public class ArrowPalettePressedEvent extends EditorEvent{
    private final Arrow type;

    public ArrowPalettePressedEvent(Arrow type) {
        this.type = type;
    }

    public Arrow getType() {
        return type;
    }
}
