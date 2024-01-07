package main;

import IO.IOManager;
import gamemanager.GameManager;
import IO.GraphicIO;
public class Main {
    public static void main(String[] args) {
        GameManager gameManager = GameManager.getInstance();

        IOManager io = IOManager.getInstance(new GraphicIO());

        // Wyświetlenie menu głównego
        io.drawMenu();
        //gameManager.startGame();
    }
}