package event.game;

import enums.Direction;

public class MoveEvent extends GameEvent {
    private final Direction direction;

    public MoveEvent(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
