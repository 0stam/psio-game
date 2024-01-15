package event.editor;

import event.LevelEvent;

public class LevelSavedEvent extends LevelEvent {
    public LevelSavedEvent(String path) {
        super(path);
    }
}
