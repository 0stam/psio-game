package map;

import enums.Direction;
import gamemanager.GameManager;
import tile.ActionTile;
import tile.PlayerCharacter;
import tile.Tile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.Serializable;

public class MapState implements Serializable, Cloneable {
    private int x, y;
    private boolean doUpdate = true;
    private Tile[][] bottomLayer;
    private Tile[][] upperLayer;
    private List<ActionTile> actionTiles = new ArrayList<>();
    private List<ActionTile> actionTilesToRemove = new ArrayList<>();
    private List<ActionTile> actionTilesToAdd = new ArrayList<>();
    private PlayerCharacter player;

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
        if (tile instanceof ActionTile actionTile) {
            if (actionTile instanceof PlayerCharacter playerCharacter) {
                if (player != null && player != playerCharacter) {
                    deleteUpperLayer(player.getX(), player.getY());
                }
                player = playerCharacter;
            }

            if (!actionTiles.contains(actionTile)) {
                addActionTile(actionTile);
            }
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
        actionTilesToAdd.add(actionTile);
    }

    public List<ActionTile> getActionTiles() {
        return actionTiles;
    }

    protected void updateActionTiles() {
        actionTiles.addAll(actionTilesToAdd);
        actionTiles.removeAll(actionTilesToRemove);
        Collections.sort(actionTiles, Collections.reverseOrder());

        actionTilesToAdd = new ArrayList<>();
        actionTilesToRemove = new ArrayList<>();
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
      
        // Is field enterable
        if (GameManager.getInstance().getMap().checkEnterable(x+ direction.x, y + direction.y, direction, movedTile))
        {
            emptiedTile.onExited(direction, movedTile);
            destinationTileBottom.onEntered(direction, movedTile);
            if (destinationTileUpper != null)
            {
                destinationTileUpper.onEntered(direction, movedTile);
            }

            // In case we are moving on an object, delete it
            deleteUpperLayer(x + direction.x, y + direction.y);

            // Trigger onExited method

            // Move tile
            setUpperLayer(x + direction.x, y + direction.y, movedTile);
            movedTile.setX(x + direction.x);
            movedTile.setY(y + direction.y);
            upperLayer[x][y] = null; // Do not use deleteUpperLayer, we don't want to lose reference to the object in actionTiles
        }
    }

    public boolean checkEnterable(int x, int y, Direction direction, Tile tile)
    {
        if ((0<=x)&&(x<this.x)&&(0<=y)&&(y<this.y)) { // if position is in bounds
            if (bottomLayer[x][y].isEnterable(direction, tile)) {
                if (upperLayer[x][y] == null) {
                    return true;
                } else {
                    return upperLayer[x][y].isEnterable(direction, tile);
                }
            }
        }
        return false;
    }

    public boolean update(Direction direction) {
        doUpdate = true;
        updateActionTiles();

        for (ActionTile actionTile : actionTiles) {
            //check if actionTile is not in actionTilesToRemove
            if (!actionTilesToRemove.contains(actionTile)) {
                actionTile.onTurn(direction);
            }
        }

        updateActionTiles();
        return doUpdate;
    }

    @Override
    public MapState clone() {
        try {
            return (MapState) super.clone();
        } catch (CloneNotSupportedException e) {
            // Should never happen
            return new MapState(x, y);
        }
    }

    public PlayerCharacter getPlayer() {
        return player;
    }
}
