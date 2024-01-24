package event.editor;

import event.game.LevelEvent;

public class LevelLoadedEvent extends LevelEvent {
    public LevelLoadedEvent(String path) {
        super(path);
    }
}
