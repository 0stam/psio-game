package tile;

import connectableinterface.Connectable;
import enterablestrategy.Solid;
import enums.Graphics;
import turnstrategy.PlayerFollower;
import java.util.Collections;
import java.util.List;

public class ChasingEnemy extends ActionTile implements Connectable {
    public ChasingEnemy(int x, int y, Tile player) {
        super(x, y, 1);
        setGraphicsID(Graphics.ENEMY);
        setEnterableStrategy(new Solid());
        setTurnStrategy(new PlayerFollower(player));
    }

    @Override
    public List<Tile> getConnections() {
        return Collections.singletonList(((PlayerFollower) getTurnStrategy()).getTargetTile());
    }

    @Override
    public void addConnection(Tile tile) {
        ((PlayerFollower) getTurnStrategy()).setTargetTile(tile);
    }

    @Override
    public void removeConnection(Tile tile) {
        ((PlayerFollower) getTurnStrategy()).setTargetTile(null);
    }
}
