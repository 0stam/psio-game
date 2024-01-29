package turnstrategy;

import enterablestrategy.EnterableStrategy;
import enums.Direction;
import gamemanager.GameManager;
import map.Map;
import tile.ActionTile;
import tile.Tile;

import java.io.Serializable;

public class Follower implements TurnStrategy, Serializable {
    Tile targetTile;

    public Follower(Tile targetTile) {
        this.targetTile = targetTile;
    }

    public Tile getTargetTile() {
        return targetTile;
    }

    public void setTargetTile(Tile targetTile) {
        this.targetTile = targetTile;
    }

    @Override
    public void onTurn(Direction d, ActionTile self) {
        // Directions in clockwise order
        Direction[] directions = {Direction.DOWN, Direction.LEFT, Direction.UP, Direction.RIGHT, Direction.DEFAULT};
        Map map = GameManager.getInstance().getMap();

        // Take tiles in all directions and calculate a distance between those tiles and targetTile
        double[] distances = new double[directions.length];

        for (int i = 0; i < directions.length; i++) {
            // Coordinates of the tile on which we are trying to move on
            int x = self.getX() + directions[i].x;
            int y = self.getY() + directions[i].y;

            if (map.checkEnterable(x, y, directions[i], self) || directions[i] == Direction.DEFAULT) {
                distances[i] = Math.sqrt(Math.pow(x - targetTile.getX(), 2) + Math.pow(y - targetTile.getY(), 2));
            } else {
                distances[i] = Integer.MAX_VALUE;
            }
        }

        double minDistance = distances[0];
        int minIndex = 0;

        for (int i = 1; i < directions.length; i++) {
            if (distances[i] < minDistance) {
                minIndex = i;
                minDistance = distances[i];
            }
        }

        if (directions[minIndex] == Direction.DEFAULT) return;

        // If we can move on two tiles with the same distance to targetTile, always move clockwise
        for (int i = 0; i < 4; i++) {
            int j = i + 1;
            if (j >= 4) j = 0;

            if (distances[i] == minDistance && distances[i] == distances[j]) {
                map.move(self.getX(), self.getY(), directions[i]);
                return;
            }
        }

        map.move(self.getX(), self.getY(), directions[minIndex]);
    }
}
