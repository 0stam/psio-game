package event;

import enums.Direction;

public class MoveEvent extends Event {
    private final Direction direction;

    public MoveEvent(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
