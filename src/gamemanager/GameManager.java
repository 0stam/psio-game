package gamemanager;
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

    public void startGame() {
        // TODO: connect to IOManager and get level
        //this.map = IOManager.getInstance().getLevel().getMap(); nwm
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    // should have input parameters
    public void startTurn() {
        //direction = input;
        this.map.startTurn();
    }



}
