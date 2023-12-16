package enterablestrategy;
import tile.*;
import enums.Direction;

public class Pushable implements EnterableStrategy{
    public boolean isEnterable(Direction direction, Tile tile){
        Tile pushedTile = (map.getUpperLayer(tile.getX()+direction.x, tile.getY()+direction.y));
        Tile coveredTileUpper = (map.getUpperLayer(pushedTile.getX()+direction.x, pushedTile.getY()+direction.y));
        Tile coveredTileBottom = (map.getBottomLayer(pushedTile.getX()+direction.x, pushedTile.getY()+direction.y));

        if(coveredTileUpper==null && coveredTileBottom.isEnterable(direction, pushedTile)){
            return true;
        }
        else{
            return false;
        }
    }

    public void onEntered(Direction direction, Tile tile){
        Tile pushedTile = (map.getUpperLayer(tile.getX()+direction.x, tile.getY()+direction.y));
        Tile coveredTileUpper = (map.getUpperLayer(pushedTile.getX()+direction.x, pushedTile.getY()+direction.y));

        setUpperLayer(coveredTileUpper.getX(), coveredTileUpper.getY(), pushedTile);
        psuhedTile.setX(coveredTileUpper.getX());
        movedTile.setY(coveredTileUpper.getY());
    }

    public void onExited(Direction direction, Tile tile){
        return;
    }
}