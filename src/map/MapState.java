package map;

import enums.Direction;
import gamemanager.GameManager;
import tile.ActionTile;
import tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class MapState {
    private int x, y;
    private boolean doUpdate = true;
    private Tile[][] bottomLayer;
    private Tile[][] upperLayer;
    private List<ActionTile> actionTiles = new ArrayList<>();
    private List<ActionTile> actionTilesToRemove = new ArrayList<>();
    private List<ActionTile> actionTilesToAdd = new ArrayList<>();

    public MapState(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public void setActionTiles(List<ActionTile> actionTiles) {
        this.actionTiles = actionTiles;
    }
    public void setBottomLayer(Tile[][] bottomLayer) {
        this.bottomLayer = bottomLayer;
    }
    public void setUpperLayer(Tile[][] upperLayer) {
        this.upperLayer = upperLayer;
    }

    public Tile getBottomLayer(int x, int y) {
        return bottomLayer[x][y];
    }
    public Tile getUpperLayer(int x, int y) {
        return upperLayer[x][y];
    }

    public Tile[][] getBottomLayer() {
        return bottomLayer;
    }

    public Tile[][] getUpperLayer() {
        return upperLayer;
    }

    public void setBottomLayer(int x, int y, Tile tile) {
        if (bottomLayer[x][y] instanceof ActionTile) {
            deleteActionTile((ActionTile) bottomLayer[x][y]);
        }
        if (tile instanceof ActionTile && !actionTiles.contains(tile)) {
            addActionTile((ActionTile) tile);
        }
        bottomLayer[x][y] = tile;
    }
    public void setUpperLayer(int x, int y, Tile tile) {
        if (upperLayer[x][y] instanceof ActionTile) {
            deleteActionTile((ActionTile) upperLayer[x][y]);
        }
        if (tile instanceof ActionTile && !actionTiles.contains(tile)) {
            addActionTile((ActionTile) tile);
        }
        upperLayer[x][y] = tile;
    }
    public void deleteBottomLayer(int x, int y) {
        if (bottomLayer[x][y] instanceof ActionTile) {
            deleteActionTile((ActionTile) bottomLayer[x][y]);
        }

        bottomLayer[x][y] = null;
    }
    public void deleteUpperLayer(int x, int y) {
        if (upperLayer[x][y] instanceof ActionTile) {
            deleteActionTile((ActionTile) upperLayer[x][y]);
        }

        upperLayer[x][y] = null;
    }
    public void deleteActionTile(ActionTile actionTile) {
        actionTilesToRemove.add(actionTile);
    }
    public void addActionTile(ActionTile actionTile) {
        System.out.println("added actionTile: " + actionTile);
        actionTilesToAdd.add(actionTile);
    }

    public List<ActionTile> getActionTiles() {
        return actionTiles;
    }

    public void move(int x, int y, Direction direction) {
        Tile movedTile = getUpperLayer(x, y);
        Tile emptiedTile = getBottomLayer(x, y);

        boolean destinationOutOfBounds = x + direction.x < 0 || x + direction.x >= this.x || y + direction.y < 0 || y + direction.y >= this.y;
        // here was code to not compute next turn if the player tried to go out of map bounds
        if (destinationOutOfBounds) { // if moving into a map border equals to moving into a wall this condition would be enough
            return;
        }

        // layers of a tile we want to move onto
        Tile destinationTileBottom = getBottomLayer(x + direction.x, y + direction.y);
        Tile destinationTileUpper = getUpperLayer(x + direction.x, y + direction.y);
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
    public boolean update(Direction direction) {
        doUpdate = true;
        for (ActionTile actionTile : actionTiles) {
            //check if actionTile is not in actionTilesToRemove
            if (!actionTilesToRemove.contains(actionTile)) {
                actionTile.onTurn(direction);
            }
        }

        // Remove all actionTiles that were deleted during the turn
        for (ActionTile actionTile : actionTilesToRemove) {
            actionTiles.remove(actionTile);
        }

        // Add all actionTiles that were added during the turn
        for (ActionTile actionTile : actionTilesToAdd) {
            actionTiles.add(actionTile);
        }
        return doUpdate;
    }
}
