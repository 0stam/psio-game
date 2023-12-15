package enterablestrategy;
import tile.*;
import enums.Direction;

public class Pushable implements EnterableStrategy{
    public boolean isEnterable(Direction direction, Tile tile){
        Tile coveredTileBottom = (map.getBottomLayer(tile.getX()+direction.x, tile.getY()+direction.y));
        Tile coveredTileUpper = (map.getUpperLayer(tile.getX()+direction.x, tile.getY()+direction.y));

        if(coveredTileBottom.isEnterable(direction, coveredTileBottom) && coveredTileUpper!=null && !(coveredTileUpper instanceof Pushable) && coveredTileUpper.isEnterable(direction, coveredTileUpper) ){
            return true;
        }
        else{
            return false;
        }
    }

    public void onEntered(Direction direction, Tile tile, ActionTile actionTile){
        return;
    }

    public void onExited(Direction direction, Tile tile){
        return;
    }
}