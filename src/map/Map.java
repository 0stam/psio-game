package map;

import java.util.ArrayList;
import java.util.List;

import enums.Direction;
import tile.*;

public class Map {
    private final int x, y;
    private Tile[][] bottomLayer;
    private Tile[][] upperLayer;

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
//    public void setupMap() {
//        Random rand = new Random();
//        for (int i = 0; i < y; i++) {
//            for (int j = 0; j < x; j++) {
//                bottomLayer[i][j] = new Floor(i, j);
//                if (rand.nextInt(100) > 90) {
//                    upperLayer[i][j] = new Box(i, j);
//                }
//                if (rand.nextInt(100) > 90) {
//                    upperLayer[i][j] = new Wall(i, j);
//                }
//                if (rand.nextInt(100) > 90) {
//                    bottomLayer[i][j] = new Button(i, j);
//                }
//            }
//        }
//
//        int goalX = rand.nextInt(x);
//        int goalY = rand.nextInt(y);
//        bottomLayer[goalX][goalY] = new Goal(goalX, goalY);
//        upperLayer[goalX][goalY] = null;
//
//        int playerX = rand.nextInt(x);
//        int playerY = rand.nextInt(y);
//        PlayerCharacter player = new PlayerCharacter(playerX, playerY);
//        upperLayer[playerX][playerY] = player;
//        actionTiles.add(player);
//    }

    public void setupMap() {
        // Create floor
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                bottomLayer[i][j] = new Floor(i, j);
            }
        }

        // Make a horizontal wall
        for (int i = 0; i < 10; i++) {
            bottomLayer[i][5] = new Wall(i, 5);
        }

        // Create door in the wall
        Door door1 = new Door(5, 5);
        bottomLayer[5][5] = door1;
        Door door2 = new Door(6, 5);
        bottomLayer[6][5] = door2;

        // Create button
        Button button = new Button(9, 2);
        button.addObserver(door1);
        button.addObserver(door2);
        bottomLayer[9][2] = button;

        // Create boxes
        upperLayer[1][2] = new Box(1, 2);
        upperLayer[1][1] = new Box(1, 1);
        bottomLayer[1][9] = new Wall(1, 1);
        //bottomLayer[0][8] = new Wall(1, 1);
        bottomLayer[5][3] = new Wall(5, 3);

        // Create an exit
        bottomLayer[6][8] = new Goal(6, 8);

        // Create player
        PlayerCharacter playerCharacter = new PlayerCharacter(2, 2);
        upperLayer[2][2] = playerCharacter;
        actionTiles.add(playerCharacter);

        // Create an enemy
        // TODO: fix player follower
        ChasingEnemy chasingEnemy = new ChasingEnemy(0, 9, playerCharacter);
        upperLayer[0][9] = chasingEnemy;
        actionTiles.add(chasingEnemy);
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
    public boolean checkEnterable(int x, int y, Direction direction, Tile tile)
    {
        if ((0<=x)&&(x<this.x)&&(0<=y)&&(y<this.y))
        {
            if (bottomLayer[x][y].isEnterable(direction, tile))
            {
                if (upperLayer[x][y] == null)
                {
                    return true;
                }
                else return upperLayer[x][y].isEnterable(direction, tile);
            }
        }
        return false;
    }
}