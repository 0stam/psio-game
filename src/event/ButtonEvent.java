package event;

public class ButtonEvent extends Event {
    private final boolean pressed;

    public ButtonEvent(boolean pressed) {
        this.pressed = pressed;
    }
}
