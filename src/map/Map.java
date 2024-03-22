package map;

import java.io.*;

import enums.Direction;
import tile.*;

import java.util.Stack;

public class Map implements Serializable {
    private final int x, y;
    private MapState currentMapState;

    private ByteArrayOutputStream previousMapState;
    private Stack<ByteArrayOutputStream> checkpoints;
    private boolean checkpointJustCreated = false; // True if checkpoint was created on this turn
    private boolean moveCancelAvailable = false;

    public Map(int x, int y) {
        this.x = x;
        this.y = y;
        checkpoints = new Stack<>();
        currentMapState = new MapState(x, y);
    }

    public int getWidth() {
        return x;
    }
    public int getHeight() {
        return y;
    }
    public Tile getBottomLayer(int x, int y) {
        return currentMapState.getBottomLayer(x, y);
    }
    public Tile getUpperLayer(int x, int y) {
        return currentMapState.getUpperLayer(x, y);
    }
    public void setUpperLayer(int x, int y, Tile tile) {
        currentMapState.setUpperLayer(x, y, tile);
    }
    public void setBottomLayer(int x, int y, Tile tile) {
        currentMapState.setBottomLayer(x, y, tile);
    }
    public void deleteBottomLayer(int x, int y) {
        currentMapState.deleteBottomLayer(x, y);
    }
    public void deleteUpperLayer(int x, int y) {
        currentMapState.deleteUpperLayer(x, y);
    }
    public void deleteActionTile(ActionTile actionTile) {
        currentMapState.deleteActionTile(actionTile);
    }
    public void addActionTile(ActionTile actionTile) {
        currentMapState.addActionTile(actionTile);
    }
    public PlayerCharacter getPlayer() {
        return currentMapState.getPlayer();
    }

    public static ByteArrayOutputStream serializeMapState(MapState mapState) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(mapState);
            return bos;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static MapState deserializeMapState(ByteArrayOutputStream bos) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            return (MapState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void move(int x, int y, Direction direction) {
        currentMapState.move(x, y, direction);
    }

    public void teleport(int startX, int startY, int targetX, int targetY, Direction direction) {
        currentMapState.teleport(startX, startY, targetX, targetY, direction);
    }

    public void startTurn(Direction direction) {
        // If the starting map state isn't saved yet, do it
        if (checkpoints.empty()) {
            checkpoints.add(serializeMapState(currentMapState));
        }

        moveCancelAvailable = true;
        checkpointJustCreated = false;

        previousMapState = serializeMapState(currentMapState);
        currentMapState.update(direction);
    }

    public void registerCheckpoint() {
        checkpoints.add(serializeMapState(currentMapState));
        checkpointJustCreated = true;
    }

    public void resetMove() {
        if (moveCancelAvailable) {
            currentMapState = deserializeMapState(previousMapState);
            moveCancelAvailable = false;

            if (checkpointJustCreated) {
                checkpoints.pop();
            }
        }
    }

    public void reset() {
        if (!checkpoints.empty()) {
            currentMapState = deserializeMapState(checkpoints.pop());
            moveCancelAvailable = false;

            if (currentMapState.getBottomLayer(getPlayer().getX(), getPlayer().getY()) instanceof Checkpoint) {
                ((Checkpoint)currentMapState.getBottomLayer(getPlayer().getX(), getPlayer().getY())).setDestroyed(true);
            }
        }
    }

    public boolean checkEnterable(int x, int y, Direction direction, Tile tile)
    {
        return currentMapState.checkEnterable(x, y, direction, tile);
    }

    public boolean availableCheckpoints () {
        return (!checkpoints.empty());
    }
}