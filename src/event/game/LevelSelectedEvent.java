package event.game;

import event.InputEvent;

public class LevelSelectedEvent extends InputEvent {
    private final int index;

    public LevelSelectedEvent(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
