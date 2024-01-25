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
    public PlayerCharacter getPlayer() {
        return nextMapState.getPlayer();
    }

    public void move(int x, int y, Direction direction) {
        nextMapState.move(x, y, direction);
    }

    public void teleport(int startX, int startY, int targetX, int targetY, Direction direction) {
        nextMapState.teleport(startX, startY, targetX, targetY, direction);
    }

    public void startTurn(Direction direction) {
        currentMapState = nextMapState.clone();
        if (nextMapState.update(direction)) {
            currentMapState = nextMapState.clone();
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