package event.display;

public class ModeActiveChangedEvent extends DisplayEvent {
    private final boolean state;

    public ModeActiveChangedEvent(boolean state) {
        this.state = state;
    }

    public boolean isState() {
        return state;
    }
}
