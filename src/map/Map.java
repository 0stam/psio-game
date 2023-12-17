package map;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import enums.Direction;
import gamemanager.GameManager;
import tile.*;

public class Map {
    private final int x, y;
    private Tile[][] bottomLayer;
    private Tile[][] upperLayer;

    private PlayerCharacter player;
    private Goal goal;

    private List<ActionTile> actionTiles = new ArrayList<>();
    private MapState currentMapState, nextMapState;

    public Map(int x, int y) {
        this.x = x;
        this.y = y;
        bottomLayer = new Tile[x][y];
        upperLayer = new Tile[x][y];
        currentMapState = new MapState(x, y);
        pushMapState(currentMapState);
        nextMapState = currentMapState;
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
            }
        }

        int goalX = rand.nextInt(x);
        int goalY = rand.nextInt(y);
        bottomLayer[goalX][goalY] = new Goal(goalX, goalY);
        upperLayer[goalX][goalY] = null;

        int playerX = rand.nextInt(x);
        int playerY = rand.nextInt(y);
        player = new PlayerCharacter(playerX, playerY, 100);
        upperLayer[playerX][playerY] = player;
        actionTiles.add(player);
    }

    public void pushMapState(MapState mapState) {
        mapState.setActionTiles(actionTiles);
        mapState.setBottomLayer(bottomLayer);
        mapState.setUpperLayer(upperLayer);
    }
    public void pullMapState(MapState mapState) {
        bottomLayer = mapState.getBottomLayer();
        upperLayer = mapState.getUpperLayer();
        actionTiles = mapState.getActionTiles();
    }
    public int getWidth() {
        return x;
    }
    public int getHeight() {
        return y;
    }
    public Tile getBottomLayer(int x, int y) {
        return nextMapState.getBottomLayer(x, y);
    }
    public Tile getUpperLayer(int x, int y) {
        return nextMapState.getUpperLayer(x, y);
    }
    public void setUpperLayer(int x, int y, Tile tile) {
        nextMapState.setUpperLayer(x, y, tile);
    }
    public void setBottomLayer(int x, int y, Tile tile) {
        nextMapState.setBottomLayer(x, y, tile);
    }
    public void deleteBottomLayer(int x, int y) {
        nextMapState.deleteBottomLayer(x, y);
    }
    public void deleteUpperLayer(int x, int y) {
        nextMapState.deleteUpperLayer(x, y);
    }
    public void deleteActionTile(ActionTile actionTile) {
        nextMapState.deleteActionTile(actionTile);
    }
    public void addActionTile(ActionTile actionTile) {
        nextMapState.addActionTile(actionTile);
    }

    public void move(int x, int y, Direction direction) {
        nextMapState.move(x, y, direction);
    }

    public void startTurn(Direction direction) {
      currentMapState = new MapState(x, y);
      pushMapState(currentMapState);
      nextMapState = currentMapState;
      if (nextMapState.update(direction)) {
          currentMapState = nextMapState;
          pullMapState(currentMapState);
      }
      else {
          // this should call some IO function to print to the screen
          System.out.println("Invalid move. Turn skipped.");
      }
    }
}