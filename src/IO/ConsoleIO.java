package IO;

import enums.Graphics;
import enums.Direction;
import event.InputEvent;
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
				screenBuffer[i][j] = idToChar(map.getBottomLayer(i, j).getGraphicsID());
			}
		}

		for (int i = 0;i < this.map.getWidth();i++) {
			for (int j = 0;j < this.map.getHeight();j++) {
				if (this.map.getUpperLayer(i, j) != null) {
					screenBuffer[i][j] = idToChar(map.getUpperLayer(i, j).getGraphicsID());
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
				case 'w', 'W':
					direction = Direction.UP;
					break;
                case 'a', 'A':
					direction = Direction.LEFT;
					break;
                case 's', 'S':
					direction = Direction.DOWN;
					break;
                case 'd', 'D':
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

	public char idToChar(Graphics graphics)	{
        return switch (graphics) {
            case FLOOR -> '_';
            case WALL -> '#';
            case BOX -> 'O';
            case PLAYER -> 'X';
            case ENEMY -> 'Y';
            case BUTTON_PRESSED -> 'v';
            case BUTTON_RELEASED -> '^';
            case DOOR_OPEN -> 'u';
            case DOOR_CLOSED -> 'n';
            case GOAL -> 'P';
            default -> '!';
        };
	}
}
