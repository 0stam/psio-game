package map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import enums.Direction;
import tile.*;
import turnstrategy.PlayerFollower;

public class Map {
    private final int x, y;
    private Tile[][] bottomLayer;
    private Tile[][] upperLayer;

    private List<ActionTile> actionTiles = new ArrayList<>();


    public Map(int x, int y) {
        this.x = x;
        this.y = y;
        bottomLayer = new Tile[x][y];
        upperLayer = new Tile[x][y];
    }

    // TODO: remove after testing
    public void setupMap() {
        Random rand = new Random();
        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                bottomLayer[i][j] = new Floor(i, j);
                if (rand.nextInt(100) > 90) {
                    upperLayer[i][j] = new Box(i, j);
                }
                if (rand.nextInt(100) > 90) {
                    upperLayer[i][j] = new Wall(i, j);
                }
                if (rand.nextInt(100) > 90) {
                    bottomLayer[i][j] = new Button(i, j);
                }
                if (rand.nextInt(100) > 98)
                {
                    PlayerCharacter toAdd = new PlayerCharacter(i, j, 100);
                    upperLayer[i][j] = toAdd;
                    actionTiles.add(toAdd);
                }
            }
        }

        int goalX = rand.nextInt(x);
        int goalY = rand.nextInt(y);
        upperLayer[goalX][goalY] = new Goal(goalX, goalY);
    }

    public int getWidth() {
        return x;
    }
    public int getHeight() {
        return y;
    }
    public Tile getBottomLayer(int x, int y) {
        return bottomLayer[x][y];
    }
    public Tile getUpperLayer(int x, int y) {
        return upperLayer[x][y];
    }
    public void setBottomLayer(int x, int y, Tile tile) {
        bottomLayer[x][y] = tile;
    }
    public void setUpperLayer(int x, int y, Tile tile) {
        upperLayer[x][y] = tile;
    }
    public void deleteBottomLayer(int x, int y) {
        if (bottomLayer[x][y] != null && bottomLayer[x][y] instanceof ActionTile) {
            actionTiles.remove((ActionTile) bottomLayer[x][y]);
        }

        bottomLayer[x][y] = null;
    }
    public void deleteUpperLayer(int x, int y) {
        if (upperLayer[x][y] != null && upperLayer[x][y] instanceof ActionTile) {
            actionTiles.remove((ActionTile) upperLayer[x][y]);
        }

        upperLayer[x][y] = null;
    }
    public void deleteActionTile(ActionTile actionTile) {
        actionTiles.remove(actionTile);
    }

    public void move(int x, int y, Direction direction) {
        Tile movedTile = getUpperLayer(x, y);
        Tile emptiedTile = getBottomLayer(x, y);

        // layers of a tile we want to move onto
        Tile destinationTileBottom = getBottomLayer(x + direction.x, y + direction.y);
        Tile destinationTileUpper = getUpperLayer(x + direction.x, y + direction.y);

        // Is field enterable
        if (destinationTileBottom.isEnterable(direction, movedTile))
        {
            if (destinationTileUpper==null || destinationTileUpper.isEnterable(direction, movedTile)) {
                // Trigger onEntered methods
                destinationTileBottom.onEntered(direction, movedTile);

                if (destinationTileUpper != null) {
                    destinationTileUpper.onEntered(direction, movedTile);
                }

                // In case we are moving on an object, delete it
                deleteUpperLayer(x + direction.x, y + direction.y);

                // Trigger onExited method
                emptiedTile.onExited(direction, movedTile);

                // Move tile
                setUpperLayer(x + direction.x, y + direction.y, movedTile);
                movedTile.setX(x + direction.x);
                movedTile.setY(y + direction.y);
                upperLayer[x][y] = null; // Do not use deleteUpperLayer, we don't want to lose reference to the object in actionTiles
            }
        }
        else {
            // TODO: remove after testing
            System.out.println("Can't move there");
        }
    }

    public void startTurn(Direction direction) {
        Collections.sort(actionTiles);

        for (ActionTile actionTile : actionTiles) {
            actionTile.onTurn(direction);
        }
        System.out.println(actionTiles.size());
    }
}