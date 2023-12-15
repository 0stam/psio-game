package enterablestrategy;

import enums.Direction;
import gamemanager.GameManager;
import tile.Tile;

public class Dummy implements EnterableStrategy{

    @Override
    public boolean isEnterable(Direction direction, Tile tile) {
        return true;
    }

    @Override
    public void onEntered(Direction direction, Tile tile) {

    }

    @Override
    public void onExited(Direction direction, Tile tile) {

    }
}
