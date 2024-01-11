package Connectable_interface;

import tile.Tile;

import java.util.List;


public interface Connectable {
    public List<Tile> getConnections();
    public void addConnection(Tile tile);
    public void removeConnection(Tile tile);
}
