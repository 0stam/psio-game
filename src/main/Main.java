package main;

import IO.ConsoleIO;
import IO.IOManager;
import gamemanager.GameManager;
import IO.GraphicIO;
public class Main {
    public static void main(String[] args) {
        GameManager gameManager = GameManager.getInstance();
        gameManager.saveExampleLevel();

        IOManager io = IOManager.getInstance(new GraphicIO());

        // Wyświetlenie menu głównego
        io.drawMenu();
        //gameManager.startGame();
    }
}