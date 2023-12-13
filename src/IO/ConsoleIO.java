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
					case Graphics.FLOOR:
						screenBuffer[i][j] = '_';
						break;
					case Graphics.WALL:
						screenBuffer[i][j] = '#';
						break;
					case Graphics.BOX:
						screenBuffer[i][j] = 'O';
						break;
					case Graphics.PLAYER:
						screenBuffer[i][j] = 'X';
						break;
					case Graphics.ENEMY:
						screenBuffer[i][j] = 'Y';
						break;
					case Graphics.BUTTON_PRESSED:
						screenBuffer[i][j] = 'v';
						break;
					case Graphics.BUTTON_RELEASED:
						screenBuffer[i][j] = '^';
						break;
					case Graphics.DOOR_OPEN:
						screenBuffer[i][j] = 'u';
						break;
					case Graphics.DOOR_CLOSED:
						screenBuffer[i][j] = 'n';
						break;
					case Graphics.GOAL:
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
						case Graphics.FLOOR:
							screenBuffer[i][j] = '_';
							break;
						case Graphics.WALL:
							screenBuffer[i][j] = '#';
							break;
						case Graphics.BOX:
							screenBuffer[i][j] = 'O';
							break;
						case Graphics.PLAYER:
							screenBuffer[i][j] = 'X';
							break;
						case Graphics.ENEMY:
							screenBuffer[i][j] = 'Y';
							break;
						case Graphics.BUTTON_PRESSED:
							screenBuffer[i][j] = 'v';
							break;
						case Graphics.BUTTON_RELEASED:
							screenBuffer[i][j] = '^';
							break;
						case Graphics.DOOR_OPEN:
							screenBuffer[i][j] = 'u';
							break;
						case Graphics.DOOR_CLOSED:
							screenBuffer[i][j] = 'n';
							break;
						case Graphics.GOAL:
							screenBuffer[i][j] = 'P';
							break;
						default:
							screenBuffer[i][j] = '!';
							break;
					}
				}
			}
		}

		for (int i = 0;i < this.map.getWidth();i++) {
			for (int j = 0;j < this.map.getHeight();j++) {
				System.out.print(screenBuffer[i][j]);
			}
			System.out.println();
		}

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

		System.out.println(direction);

		return;
	}
}
