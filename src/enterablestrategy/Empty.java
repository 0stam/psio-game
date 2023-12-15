package enterablestrategy;
import tile.*;
import enums.Direction;

public class Empty implements EnterableStrategy{
    public boolean isEnterable(Direction direction, Tile tile){
        return true;
    }

    public void onEntered(Direction direction, Tile tile, ActionTile actionTile){
        return;
    }

    public void onExited(Direction direction, Tile tile){
        return;
    }
}