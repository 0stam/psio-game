package tile;

import connectableinterface.Connectable;
import enterablestrategy.Solid;
import enums.Graphics;
import turnstrategy.Follower;

import javax.tools.ForwardingFileObject;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ChasingEnemy extends ActionTile implements Connectable {
    public ChasingEnemy(int x, int y, Tile player) {
        super(x, y, 1);
        setGraphicsID(Graphics.ENEMY);
        setEnterableStrategy(new Solid());
        setTurnStrategy(new Follower(player));
    }

    @Override
    public HashSet<Tile> getConnections() {
        HashSet<Tile> result = new HashSet<>();
        result.add(((Follower) getTurnStrategy()).getTargetTile());
        return result;
    }

    @Override
    public void addConnection(Tile tile) {
        ((Follower) getTurnStrategy()).setTargetTile(tile);
    }

    @Override
    public void removeConnection(Tile tile) {
        ((Follower) getTurnStrategy()).setTargetTile(null);
    }
}
