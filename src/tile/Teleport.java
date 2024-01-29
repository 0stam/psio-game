package tile;

import connectableinterface.Connectable;
import enterablestrategy.Empty;
import enterablestrategy.Teleporting;
import enums.Direction;
import enums.Graphics;
import gamemanager.GameManager;
import map.Map;

import java.util.HashSet;

public class Teleport extends Tile implements Connectable {
    public Teleport(int x, int y) {
        super(x, y);

        setEnterableStrategy(new Teleporting());
        setGraphicsID(Graphics.TELEPORT);
    }

    private void setTarget(Tile tile) {
        ((Teleporting) getEnterableStrategy()).setTarget(tile);
    }

    private Tile getTarget() {
        return  ((Teleporting) getEnterableStrategy()).getTarget();
    }

    @Override
    public void addConnection(Tile tile) {
        setTarget(tile);
    }

    @Override
    public void setConnections(HashSet<Tile> list) {
        if (list.isEmpty()) {
            setTarget(null);
        } else {
            setTarget(list.iterator().next());
        }
    }

    @Override
    public void removeConnection(Tile tile) {
        if (getTarget() == tile) {
            setTarget(null);
        }
    }

    @Override
    public HashSet<Tile> getConnections() {
        if (getTarget() == null) {
            return new HashSet<>();
        }

        HashSet<Tile> result = new HashSet<>();
        result.add(getTarget());
        return result;
    }
}
