package enterablestrategy;
import gamemanager.GameManager;
import map.Map;
import tile.*;
import enums.Direction;
import java.io.Serializable;

public class Pushable implements EnterableStrategy, Serializable{
    public boolean isEnterable(Direction direction, Tile tile){
        Map map = GameManager.getInstance().getMap();

        int coveredX = tile.getX() + direction.x * 2;
        int coveredY = tile.getY() + direction.y * 2;

        if (coveredX < 0 || coveredY < 0 || coveredX >= map.getWidth() || coveredY >= map.getHeight()) {
            return false;
        }

        Tile pushedTile = map.getUpperLayer(tile.getX()+direction.x, tile.getY()+direction.y);
        Tile coveredTileUpper = map.getUpperLayer(coveredX, coveredY);
        Tile coveredTileBottom = map.getBottomLayer(pushedTile.getX()+direction.x, pushedTile.getY()+direction.y);

        if(coveredTileUpper==null && coveredTileBottom.isEnterable(direction, pushedTile)){
            return true;
        }
        else{
            return false;
        }
    }

    public void onEntered(Direction direction, Tile tile){
        Map map = GameManager.getInstance().getMap();

        int x = tile.getX() + direction.x;
        int y = tile.getY() + direction.y;

        int coveredX = tile.getX() + direction.x * 2;
        int coveredY = tile.getY() + direction.y * 2;

        Tile pushedTile = map.getUpperLayer(x, y);
        Tile coveredTile = map.getBottomLayer(coveredX, coveredY);

        map.getBottomLayer(x, y).onExited(direction, pushedTile);
        coveredTile.onEntered(direction, pushedTile);

        map.setUpperLayer(coveredX, coveredY, pushedTile);
        pushedTile.setX(coveredX);
        pushedTile.setY(coveredY);
    }

    public void onExited(Direction direction, Tile tile){
        return;
    }
}