package event;

public class ButtonEvent extends InputEvent {
    private final boolean pressed;

    public ButtonEvent(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean isPressed() {
        return pressed;
    }
}
