package IO;

import gameManager.GameManager;
import map.Map;

import java.util.Scanner;

public class ConsoleIO implements IOStrategy {
	private GameManager gameManager;
	private Map map;

	public void draw () {
		this.gameManager = GameManager.getInstance();
		this.map = this.gameManager.getMap();

		char[][] screenBuffer = new char[this.map.getX()][this.map.getY()];

		for (int i = 0;i < this.map.getX();i++) {
			for (int j = 0;j < this.map.getY();j++) {
				switch (this.map.getBottomLayer()[i][j].getGraphicsId()) {
					case GraphicsEnum.FLOOR:
						screenBuffer[i][j] = '_';
						break;
					case GraphicsEnum.WALL:
						screenBuffer[i][j] = '#';
						break;
					case GraphicsEnum.BOX:
						screenBuffer[i][j] = 'O';
						break;
					case GraphicsEnum.PLAYER:
						screenBuffer[i][j] = 'X';
						break;
					case GraphicsEnum.ENEMY:
						screenBuffer[i][j] = 'Y';
						break;
					case GraphicsEnum.BUTTON_PRESSED:
						screenBuffer[i][j] = 'v';
						break;
					case GraphicsEnum.BUTTON_RELEASED:
						screenBuffer[i][j] = '^';
						break;
					case GraphicsEnum.DOOR_OPEN:
						screenBuffer[i][j] = 'u';
						break;
					case GraphicsEnum.DOOR_CLOSED:
						screenBuffer[i][j] = 'n';
						break;
					case GraphicsEnum.GOAL:
						screenBuffer[i][j] = 'P';
						break;
					default:
						screenBuffer[i][j] = '!';
						break;
				}
			}
		}

		for (int i = 0;i < this.map.getX();i++) {
			for (int j = 0;j < this.map.getY();j++) {
				if (this.map.getFrontLayer()[i][j] != null) {
					switch (this.map.getFrontLayer()[i][j].getGraphicsId()) {
						case GraphicsEnum.FLOOR:
							screenBuffer[i][j] = '_';
							break;
						case GraphicsEnum.WALL:
							screenBuffer[i][j] = '#';
							break;
						case GraphicsEnum.BOX:
							screenBuffer[i][j] = 'O';
							break;
						case GraphicsEnum.PLAYER:
							screenBuffer[i][j] = 'X';
							break;
						case GraphicsEnum.ENEMY:
							screenBuffer[i][j] = 'Y';
							break;
						case GraphicsEnum.BUTTON_PRESSED:
							screenBuffer[i][j] = 'v';
							break;
						case GraphicsEnum.BUTTON_RELEASED:
							screenBuffer[i][j] = '^';
							break;
						case GraphicsEnum.DOOR_OPEN:
							screenBuffer[i][j] = 'u';
							break;
						case GraphicsEnum.DOOR_CLOSED:
							screenBuffer[i][j] = 'n';
							break;
						case GraphicsEnum.GOAL:
							screenBuffer[i][j] = 'P';
							break;
						default:
							screenBuffer[i][j] = '!';
							break;
					}
				}
			}
		}

		for (int i = 0;i < this.map.getX();i++) {
			for (int j = 0;j < this.map.getY();j++) {
				System.out.print(screenBuffer[i][j]);
			}
			System.out.println();
		}

		Scanner inputListener = new Scanner(System.in);

		boolean inputError = true;
		char move = ' ';
		DirectionsEnum direction = DirectionsEnum.DEFAULT;

		while (inputError) {
			inputError = false;

			System.out.println("Podaj twoj ruch: ");
			move = inputListener.nextLine().charAt(0);

			switch (move) {
				case 'w':
					direction = DirectionsEnum.UP;
					break;
				case 'W':
					direction = DirectionsEnum.UP;
					break;
				case 'a':
					direction = DirectionsEnum.LEFT;
					break;
				case 'A':
					direction = DirectionsEnum.LEFT;
					break;
				case 's':
					direction = DirectionsEnum.DOWN;
					break;
				case 'S':
					direction = DirectionsEnum.DOWN;
					break;
				case 'd':
					direction = DirectionsEnum.RIGHT;
					break;
				case 'D':
					direction = DirectionsEnum.RIGHT;
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
