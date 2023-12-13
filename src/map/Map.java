package map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import enums.Direction;
import tile.*;

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
        bottomLayer[x][y] = null;
    }
    public void deleteUpperLayer(int x, int y) {
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

        // is upper null? is upper pushable? is bottom enterable?
        if (destinationTileBottom.isEnterable() && destinationTileUpper.isEnterable()) {
            if (destinationTileUpper != null) {
                // implement the code for pushable tiles here
            }
            // move the tile and delete the reference to it on a previous position
            setUpperLayer(x + direction.x, y + direction.y, movedTile);
            deleteUpperLayer(x, y);
            emptiedTile.onExited(direction, movedTile);
            destinationTileBottom.onEntered(direction, movedTile);
        }
        else {
            System.out.println("Can't move there");
        }
    }

    public void startTurn(Direction direction) {
        actionTiles.add(new ActionTile(0, 0, 0));
        Collections.sort(actionTiles);

        for (int i = 0; i < actionTiles.size(); i++) {
            ActionTile actionTile = actionTiles.get(i);
            actionTile.onTurn(direction);
            i--; //po usunieciu elementu nastepny bedzie mial indeks tego poprzedniego
        }
        System.out.println(actionTiles.size());
    }
}


