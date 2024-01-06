package map;

import java.util.ArrayList;
import java.util.List;

import enums.Direction;
import tile.*;

import java.io.Serializable;

public class Map implements Serializable{
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

    public void setupMap() {
        // Create floor
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                bottomLayer[i][j] = new Floor(i, j);
            }
        }

        // Make a horizontal wall
        for (int i = 1; i < 10; i++) {
            bottomLayer[i][5] = new Wall(i, 5);
        }
        //Make vertical walls
        for(int i=6; i<10; i++){
            bottomLayer[1][i] = new Wall(1, i);
            bottomLayer[4][i] = new Wall(4, i);
            bottomLayer[7][i] = new Wall(7, i);
        }
        bottomLayer[5][7] = new Wall(5, 7);
        bottomLayer[6][7] = new Wall(6, 7);




        // Create door in the wall
        Door door1 = new Door(5, 5);
        bottomLayer[5][5] = door1;
        Door door2 = new Door(6, 5);
        bottomLayer[6][5] = door2;
        Door door3 = new Door(5, 7);
        bottomLayer[5][7]=door3;
        Door door4 = new Door(6, 7);
        bottomLayer[6][7]=door4;

        // Create button
        Button button = new Button(9, 2);
        button.addObserver(door1);
        button.addObserver(door2);
        bottomLayer[9][2] = button;

        //Create second button
        Button button2 = new Button(9, 0);
        button2.addObserver(door3);
        button2.addObserver(door4);
        bottomLayer[9][0]=button2;

        // Create boxes
        upperLayer[1][2] = new Box(1, 2);
        upperLayer[1][1] = new Box(1, 1);
        //bottomLayer[1][9] = new Wall(1, 1);
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

    public void addCurrentActionTile(ActionTile actionTile) {actionTiles.add(actionTile);}

    public void getActionTiles(){
        System.out.println("There are " + actionTiles.size() + " actiontiles");
        for(int i=0; i<actionTiles.size(); i++){
            System.out.println(actionTiles.get(i).getClass().getSimpleName());
        }
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
          // should never happen in current implementation
          System.out.println("Invalid move. Turn skipped.");
      }
    }

    public boolean checkEnterable(int x, int y, Direction direction, Tile tile)
    {
        return nextMapState.checkEnterable(x, y, direction, tile);
    }
}