package turnstrategy;

import enums.Direction;
import gamemanager.GameManager;
import tile.ActionTile;
import tile.PathTile;

import java.awt.*;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;

public class StateMachine implements TurnStrategy, Serializable {
    private Hashtable<Point, Direction> path;
    public StateMachine () {}
    public StateMachine(ArrayList<PathTile> path) {setPath(path);}
    public StateMachine (Hashtable<Point, Direction> path) {this.path = path;}
    @Override
    public void onTurn(Direction direction, ActionTile owner) {
        Point point = new Point(owner.getX(), owner.getY());
        if (path.keySet().contains(point))
            GameManager.getInstance().getMap().move(owner.getX(), owner.getY(), path.get(point));
        else
            GameManager.getInstance().getMap().move(owner.getX(), owner.getY(), Direction.DEFAULT);
    }
    public void setPath(ArrayList<PathTile> path)
    {
        this.path = new Hashtable<>();
        if (path != null) {
            for (PathTile i : path) {
                this.path.put(new Point(i.getX(), i.getY()), i.getDirection());
            }
        }
    }
    public Hashtable<Point, Direction> getPath() {
        return path;
    }

    public void setPath(Hashtable<Point, Direction> path) {
        this.path = path;
    }
}
