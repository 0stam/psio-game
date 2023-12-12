package main;
import enums.Direction;
import gamemanager.GameManager;
import map.Map;
public class Main {
    public static void main(String[] args) {
        GameManager gameManager = GameManager.getInstance();
        // placeholder poki nie mamy Tilesów
        gameManager.setMap(new Map(10, 10));
        // placeholder poki nie mamy IOManagera
        gameManager.startTurn(Direction.RIGHT);
    }
}
