package enterablestrategy;

import enums.Direction;
import gamemanager.GameManager;
import map.Map;
import tile.Tile;

public class Teleporting implements EnterableStrategy {
    Tile target;

    public Tile getTarget() {
        return target;
    }

    public void setTarget(Tile target) {
        this.target = target;
    }

    @Override
    public boolean isEnterable(Direction direction, Tile tile) {
        if (target == null) {
            return true;
        }

        return GameManager.getInstance().getMap().checkEnterable(target.getX(), target.getY(), Direction.DEFAULT, tile);
    }

    @Override
    public void onEntered(Direction direction, Tile tile) {
        if (target == null) {
            return;
        }

        GameManager.getInstance().getMap().teleport(tile.getX(), tile.getY(), target.getX(), target.getY(), direction);
    }

    @Override
    public void onExited(Direction direction, Tile tile) {

    }
}
