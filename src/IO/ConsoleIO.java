package IO;

import enums.Graphics;
import enums.Direction;
import gamemanager.GameManager;
import map.Map;

import java.util.Scanner;

public class ConsoleIO implements IOStrategy {
	private GameManager gameManager;
	private Map map;

	public void draw () {
		this.gameManager = GameManager.getInstance();
		this.map = this.gameManager.getMap();

		char[][] screenBuffer = new char[this.map.getWidth()][this.map.getHeight()];

		for (int i = 0;i < this.map.getWidth();i++) {
			for (int j = 0;j < this.map.getHeight();j++) {
				switch (this.map.getBottomLayer(i, j).getGraphicsID()) {
					case FLOOR:
						screenBuffer[i][j] = '_';
						break;
					case WALL:
						screenBuffer[i][j] = '#';
						break;
					case BOX:
						screenBuffer[i][j] = 'O';
						break;
					case PLAYER:
						screenBuffer[i][j] = 'X';
						break;
					case ENEMY:
						screenBuffer[i][j] = 'Y';
						break;
					case BUTTON_PRESSED:
						screenBuffer[i][j] = 'v';
						break;
					case BUTTON_RELEASED:
						screenBuffer[i][j] = '^';
						break;
					case DOOR_OPEN:
						screenBuffer[i][j] = 'u';
						break;
					case DOOR_CLOSED:
						screenBuffer[i][j] = 'n';
						break;
					case GOAL:
						screenBuffer[i][j] = 'P';
						break;
					default:
						screenBuffer[i][j] = '!';
						break;
				}
			}
		}

		for (int i = 0;i < this.map.getWidth();i++) {
			for (int j = 0;j < this.map.getHeight();j++) {
				if (this.map.getUpperLayer(i, j) != null) {
					switch (this.map.getUpperLayer(i, j).getGraphicsID()) {
						case FLOOR:
							screenBuffer[i][j] = '_';
							break;
						case WALL:
							screenBuffer[i][j] = '#';
							break;
						case BOX:
							screenBuffer[i][j] = 'O';
							break;
						case PLAYER:
							screenBuffer[i][j] = 'X';
							break;
						case ENEMY:
							screenBuffer[i][j] = 'Y';
							break;
						case BUTTON_PRESSED:
							screenBuffer[i][j] = 'v';
							break;
						case BUTTON_RELEASED:
							screenBuffer[i][j] = '^';
							break;
						case DOOR_OPEN:
							screenBuffer[i][j] = 'u';
							break;
						case DOOR_CLOSED:
							screenBuffer[i][j] = 'n';
							break;
						case GOAL:
							screenBuffer[i][j] = 'P';
							break;
						default:
							screenBuffer[i][j] = '!';
							break;
					}
				}
			}
		}

		for (int j = 0;j < this.map.getHeight();j++) {
			for (int i = 0;i < this.map.getWidth();i++) {
				System.out.print(screenBuffer[i][j]);
			}
			System.out.println();
		}

		//chyba to powinna robic inna klasa
		Scanner inputListener = new Scanner(System.in);

		boolean inputError = true;
		char move = ' ';
		Direction direction = Direction.DEFAULT;

		while (inputError) {
			inputError = false;

			System.out.println("Podaj twoj ruch: ");
			move = inputListener.nextLine().charAt(0);

			switch (move) {
				case 'w':
					direction = Direction.UP;
					break;
				case 'W':
					direction = Direction.UP;
					break;
				case 'a':
					direction = Direction.LEFT;
					break;
				case 'A':
					direction = Direction.LEFT;
					break;
				case 's':
					direction = Direction.DOWN;
					break;
				case 'S':
					direction = Direction.DOWN;
					break;
				case 'd':
					direction = Direction.RIGHT;
					break;
				case 'D':
					direction = Direction.RIGHT;
					break;
				default:
					inputError = true;
					break;
			}
		}

		System.out.println(direction + " koniec rysowania");
		GameManager.getInstance().onInput(new InputEvent(direction));
		return;
	}
}
