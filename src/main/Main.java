package main;

import enums.Direction;
import gamemanager.GameManager;
public class Main {
    public static void main(String[] args) {
        GameManager gameManager = GameManager.getInstance();
        gameManager.startGame();
    }
}