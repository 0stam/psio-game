package enterablestrategy;
import tile.*;
import enums.Direction;

public class Solid implements EnterableStrategy{
    public boolean isEnterable(Direction direction, Tile tile){
        return false;
    }

    public onEntered(Direction direction, Tile tile){
        return;
    }

    public onExited(Direction direction, Tile tile){
        return;
    }
}