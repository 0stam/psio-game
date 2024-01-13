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
            case UP -> setGraphicsID(Graphics.ARROW_UP);
            case DOWN -> setGraphicsID(Graphics.ARROW_DOWN);
            case LEFT -> setGraphicsID(Graphics.ARROW_LEFT);
            case RIGHT -> setGraphicsID(Graphics.ARROW_RIGHT);
        }
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PathTile pathTile = (PathTile) obj;
        return this.getX() == pathTile.getX() && this.getY() == pathTile.getY() && this.direction == pathTile.direction;
    }
}
