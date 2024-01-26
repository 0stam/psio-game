package event.editor;

import event.game.LevelEvent;

public class LevelSavedEvent extends LevelEvent {
    public LevelSavedEvent(String path) {
        super(path);
    }
}
