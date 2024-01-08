package enterablestrategy;
import tile.*;
import enums.Direction;
import gamemanager.GameManager;
import java.io.Serializable;

public class LevelExit implements EnterableStrategy, Serializable{
    public boolean isEnterable(Direction direction, Tile tile){
        return true;
    }

    public void onEntered(Direction direction, Tile tile) {
        if (tile instanceof PlayerCharacter) {
            GameManager.getInstance().setEndLevel(true);
        }
    }

    public void onExited(Direction direction, Tile tile){
        return;
    }
}