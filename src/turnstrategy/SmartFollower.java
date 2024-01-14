package turnstrategy;

import enums.Direction;
import gamemanager.GameManager;
import main.Main;
import map.Map;
import tile.*;

import java.io.Serializable;

public class SmartFollower implements TurnStrategy, Serializable {
    private Tile targetTile;

    public SmartFollower(Tile targetTile) {
        this.targetTile = targetTile;
    }

    public Tile getTargetTile() {
        return targetTile;
    }

    public void setTargetTile(Tile targetTile) {
        this.targetTile = targetTile;
    }

    @Override
    public void onTurn(Direction direction, ActionTile follower) {
        Map map = GameManager.getInstance().getMap();

        double[] distances = new double[5];

        for (int i = 0; i < 5; i++) {
            Direction dir = Direction.values()[i];

            if (map.checkEnterable(follower.getX() + dir.x, follower.getY() + dir.y, dir, follower) || dir == Direction.DEFAULT) {
                distances[i] = Math.sqrt(Math.pow(follower.getX() + direction.x, 2) + Math.pow(follower.getY() + direction.y, 2));
            } else {
                distances[i] = Integer.MAX_VALUE;
            }
        }

        int minIndex = 0;
        double minValue = distances[0];
        for (int i = 1; i < 5; i++) {
            if (distances[i] < minValue) {
                minValue = distances[i];
                minIndex = i;
            }
        }

        GameManager.getInstance().getMap().move(follower.getX(), follower.getY(), Direction.values()[minIndex]);
    }
}
