import IO.ConsoleIO;
import IO.GraphicIO;
import IO.IOManager;
import gameManager.GameManager;
import map.Map;

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = GameManager.getInstance();
        Map map = new Map(10, 10);
        map.setupMap();

        gameManager.startGame();

        gameManager.setMap(map);

        IOManager io = IOManager.getInstance(new GraphicIO());

        io.draw();
    }
}
