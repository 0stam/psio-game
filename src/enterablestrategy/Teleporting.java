package enterablestrategy;

import enums.Direction;
import gamemanager.GameManager;
import map.Map;
import tile.Box;
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

        Tile targetUpperTile = GameManager.getInstance().getMap().getUpperLayer(target.getX(), target.getY());
		if ((targetUpperTile != null) && (targetUpperTile.getEnterableStrategy() instanceof Pushable)) {
			if (GameManager.getInstance().getMap().getUpperLayer(target.getX() + direction.x, target.getY() + direction.y) != null) {
				return GameManager.getInstance().getMap().getBottomLayer(target.getX() + direction.x, target.getY() + direction.y).isEnterable(direction, tile) && GameManager.getInstance().getMap().getUpperLayer(target.getX() + direction.x, target.getY() + direction.y).isEnterable(direction, tile);
			} else {
				return GameManager.getInstance().getMap().getBottomLayer(target.getX() + direction.x, target.getY() + direction.y).isEnterable(direction, tile);
			}
		}
        return targetUpperTile == null || targetUpperTile.isEnterable(Direction.DEFAULT, tile);
    }

    @Override
    public void onEntered(Direction direction, Tile tile) {
        if (target == null) {
            return;
        }

		if ((GameManager.getInstance().getMap().getUpperLayer(target.getX(), target.getY()) != null) && (GameManager.getInstance().getMap().getUpperLayer(target.getX(), target.getY()).getEnterableStrategy() instanceof Pushable)) {
            Tile temp = GameManager.getInstance().getMap().getUpperLayer(target.getX() - direction.x, target.getY() - direction.y);
            GameManager.getInstance().getMap().setUpperLayer(target.getX() - direction.x, target.getY() - direction.y, new Box(target.getX() - direction.x, target.getY() - direction.y));
            GameManager.getInstance().getMap().getUpperLayer(target.getX(), target.getY()).onEntered(direction, GameManager.getInstance().getMap().getUpperLayer(target.getX() - direction.x, target.getY() - direction.y));
            GameManager.getInstance().getMap().setUpperLayer(target.getX() - direction.x, target.getY() - direction.y, temp);
        }
        GameManager.getInstance().getMap().teleport(tile.getX(), tile.getY(), target.getX(), target.getY(), direction);
    }

    @Override
    public void onExited(Direction direction, Tile tile) {

    }
}
