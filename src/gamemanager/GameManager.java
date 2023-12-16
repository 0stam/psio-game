package gamemanager;
import IO.ConsoleIO;
import IO.GraphicIO;
import IO.IOManager;
import enums.Direction;
import map.Map;
import event.*;

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
        Map map = new Map(10, 10);
        map.setupMap();

        this.map = map;

        IOManager io = IOManager.getInstance(new ConsoleIO());
        //IOManager io = IOManager.getInstance(new GraphicIO());

        // TODO: change after testing
        while (true) {
            io.draw();
        }
    }

    public void endLevel{
        //tutaj endLevel
    }
    public void setMap(Map map) {
        this.map = map;
    }

    public Map getMap() {
        return map;
    }

    // should have input parameters
    public void startTurn(Direction input) {
        this.map.startTurn(input);
    }

    public void onInput(Event event) {
        if (event instanceof InputEvent inputEvent) {
            startTurn(inputEvent.getDirection());
        }
    }
}