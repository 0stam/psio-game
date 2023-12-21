package gamemanager;
import IO.ConsoleIO;
import IO.GraphicIO;
import IO.IOManager;
import enums.Direction;
import map.Map;
import event.*;

public class GameManager implements EventObserver {
    private static GameManager gameManager;
    private Map map;
    private boolean levelCompleted;

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
        levelCompleted = false;
        map.setupMap();

        this.map = map;

        //IOManager io = IOManager.getInstance(new ConsoleIO());
        IOManager io = IOManager.getInstance(new GraphicIO());

        io.drawGame();
        //io.drawEditor();
    }

    public void endLevel(){
        levelCompleted = true;
        System.out.println("Essa zwyciężyłeś");
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

    public void onEvent(Event event) {
        if (event instanceof MoveEvent moveEvent) {
            startTurn(moveEvent.getDirection());

            IOManager.getInstance().drawGame();
        }
    }

    public boolean getLevelCompleted() {
        return levelCompleted;
    }
}