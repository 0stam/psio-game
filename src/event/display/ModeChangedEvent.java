package event.display;

import enums.EditorMode;

public class ModeChangedEvent extends DisplayEvent {
    private final EditorMode mode;

    public ModeChangedEvent(EditorMode mode) {
        this.mode = mode;
    }

    public EditorMode getMode() {
        return mode;
    }
}
