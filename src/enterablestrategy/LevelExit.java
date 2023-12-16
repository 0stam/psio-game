package enterablestrategy;
import tile.*;
import enums.Direction;

public class LevelExit implements EnterableStrategy{
    public boolean isEnterable(Direction direction, Tile tile){
        return true;
    }

    public void onEntered(Direction direction, Tile tile, ActionTile actionTile){
        if(actionTile instanceof PlayerCharacter){ // Trzeba napisac PlayerCharacter
            gameManager.endLevel();
        }
        else{
            return;
        }
    }

    public void onExited(Direction direction, Tile tile){
        return;
    }
}