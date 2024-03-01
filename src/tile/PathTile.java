package tile;

import enums.Direction;
import enums.Graphics;

public class PathTile extends Tile{
    private Direction direction;
    public PathTile(int x, int y, Direction direction) {
        super(x, y);
        setEnterableStrategy(null);
        switch (direction)
        {
            case UP:
                setGraphicsID(Graphics.ARROW_UP);
                break;
            case DOWN:
                setGraphicsID(Graphics.ARROW_DOWN);
                break;
            case LEFT:
                setGraphicsID(Graphics.ARROW_LEFT);
                break;
            case RIGHT:
                setGraphicsID(Graphics.ARROW_RIGHT);
                break;
        }
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
