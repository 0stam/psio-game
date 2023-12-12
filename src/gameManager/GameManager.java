package gameManager;
import map.Map;

public class GameManager {
    private static GameManager gameManager;

    private Map map;

    private GameManager() {
    }

    public static GameManager getInstance() {
        if (gameManager == null) {
            gameManager = new GameManager();
        }
        return gameManager;
    }

    public void startGame() {}

    // should have input parameters
    public void startTurn() {
        System.out.println("Turn is starting...");
    }

    public void startLevel(int level) {
        System.out.println("Level " + level + " is starting...");
    }

    public void finishLevel() {
        System.out.println("Level is finished...");
    }

    public Map getMap() {
        return this.map;
    }
    public GameManager setMap(Map map) {
        this.map = map;
        return this;
    }
}
