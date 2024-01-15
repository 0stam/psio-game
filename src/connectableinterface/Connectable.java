package connectableinterface;

import tile.Tile;

import java.util.HashSet;
import java.util.List;


public interface Connectable {
    public HashSet<Tile> getConnections();
    public void addConnection(Tile tile);
    public void removeConnection(Tile tile);
    public void setConnections(HashSet<Tile> list);
}
