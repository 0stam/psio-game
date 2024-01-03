package gamemanager;
import IO.ConsoleIO;
import IO.GraphicIO;
import IO.IOManager;
import editor.Editor;
import enums.Direction;
import map.Map;
import event.*;

public class GameManager implements EventObserver {
    private static GameManager gameManager;
    private Map map;
    private Editor editor;
    private boolean levelCompleted;

    private GameManager() {
        // TODO: remove when proper Editor initialization is implemented
        editor = new Editor();
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
        if (!(event instanceof InputEvent)) {
            return;
        }

        if (event instanceof EditorEvent) {
            editor.onEvent(event);
            return;
        }

        if (event instanceof MoveEvent moveEvent) {
            startTurn(moveEvent.getDirection());
            IOManager.getInstance().drawGame();
            return;
        }

        if (event instanceof ResetEvent) {
            startGame();
            return;
        }
    }

    public boolean getLevelCompleted() {
        return levelCompleted;
    }
}