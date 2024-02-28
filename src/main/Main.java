package main;

import IO.ConsoleIO;
import IO.IOManager;
import gamemanager.GameManager;
import IO.GraphicIO;
import levelloader.LevelLoader;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = GameManager.getInstance();

        IOManager io = IOManager.getInstance(new GraphicIO());

        // Wyświetlenie menu głównego
        io.drawMenu();
        //gameManager.startGame();
    }
}