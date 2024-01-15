package event.editor;

import event.LevelEvent;

public class LevelLoadedEvent extends LevelEvent {
    public LevelLoadedEvent(String path) {
        super(path);
    }
}
