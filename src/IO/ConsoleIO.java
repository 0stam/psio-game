package IO;

import enums.Graphics;
import enums.Direction;
import event.EventObserver;
import event.game.LevelSelectedEvent;
import event.game.MoveEvent;
import event.game.ResetEvent;
import gamemanager.GameManager;
import levelloader.LevelLoader;
import map.Map;

import java.util.Scanner;

public class ConsoleIO implements IOStrategy {
	private GameManager gameManager;
	private Map map;

	public void drawGame () {
		this.gameManager = GameManager.getInstance();
		this.map = this.gameManager.getMap();

		char[][] screenBuffer = new char[this.map.getWidth()][this.map.getHeight()];

		for (int i = 0;i < this.map.getWidth();i++) {
			for (int j = 0;j < this.map.getHeight();j++) {
				if (this.map.getUpperLayer(i, j) != null) {
					screenBuffer[i][j] = idToChar(map.getUpperLayer(i, j).getGraphicsID());
				} else {
					screenBuffer[i][j] = idToChar(map.getBottomLayer(i, j).getGraphicsID());
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
				case 'W':
					direction = Direction.UP;
					break;
                case 'a':
				case 'A':
					direction = Direction.LEFT;
					break;
                case 's':
				case 'S':
					direction = Direction.DOWN;
					break;
                case 'd':
				case 'D':
					direction = Direction.RIGHT;
					break;
				case 'r':
				case 'R':
					GameManager.getInstance().onEvent(new ResetEvent());
					return;
                default:
					inputError = true;
					break;
			}
		}

		GameManager.getInstance().onEvent(new MoveEvent(direction));
	}

	public void drawEditor () {
		System.out.println("Editor not supported in console!");
	}

	@Override
	public void drawMenu() {
		Scanner scn = new Scanner(System.in);

		boolean validInput = false;
		int levelCount = LevelLoader.getLevelCount();

		while (!validInput) {
			System.out.print("Select level [0-" + (levelCount - 1) + "]: ");
			try {
				int selection = Integer.parseInt(scn.nextLine());

				if (selection >= 0 && selection < levelCount) {
					validInput = true;
					GameManager.getInstance().onEvent(new LevelSelectedEvent(selection));
				}
			} catch (NumberFormatException e) {}

			if (!validInput) {
				System.out.println("Incorrect level number.");
			}
		}
	}

	@Override
	public EventObserver getInputHandler() {
		return null;
	}

	public char idToChar(Graphics graphics)	{
		char c;

        switch (graphics) {
			case FLOOR:
				c = '_';
				break;
			case DANGER:
				c = '~';
				break;
			case WALL:
				c = '#';
				break;
			case BOX:
				c = 'O';
				break;
			case PLAYER:
				c = 'X';
				break;
			case ENEMY:
				c = 'Y';
				break;
			case MIMIC:
				c = 'x';
				break;
			case SMART:
				c = 'S';
				break;
			case BUTTON_PRESSED:
				c = 'v';
				break;
			case BUTTON_RELEASED:
				c = '^';
				break;
			case BUTTON_PERMANENT_PRESSED:
				c = 'v';
				break;
			case BUTTON_PERMANENT_RELEASED:
				c = '^';
				break;
			case DOOR_OPEN:
				c = 'u';
				break;
			case DOOR_CLOSED:
				c = 'n';
				break;
			case GOAL:
				c = 'P';
				break;
			default:
				c = '!';
				break;
        }

		return c;
	}
}
