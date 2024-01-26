package event.game;

import event.Event;

public class ButtonEvent extends GameEvent {
    private final boolean pressed;

    public ButtonEvent(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isPressed() {
        return pressed;
    }
}
