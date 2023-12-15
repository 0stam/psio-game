package gamemanager;
import IO.ConsoleIO;
import IO.GraphicIO;
import IO.IOManager;
import enums.Direction;
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
        Map map = new Map(10, 10);
        map.setupMap();

        this.map = map;

        IOManager io = IOManager.getInstance(new ConsoleIO());
        //IOManager io = IOManager.getInstance(new GraphicIO());
        while (true) {
            io.draw();
            //moim zdaniem to powinno byc tu a nie w draw -> startTurn(direction);
        }
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

    public void onInput() {
        //Do argumentu dac input z IOManager,
        /* tutaj bedzie jakis switch zalezny od inputu */
    }


}