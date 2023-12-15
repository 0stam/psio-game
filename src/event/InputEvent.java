package event;

import enums.Direction;

public class InputEvent extends Event {
    private final Direction direction;

    public InputEvent(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
