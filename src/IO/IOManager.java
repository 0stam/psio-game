package IO;
public class IOManager {
	private static IOManager instance;
	private IOStrategy strategy;

	private IOManager () {
		this.strategy = new ConsoleIO();
	}

	private IOManager (IOStrategy strategy) {
		this.strategy = strategy;
	}

	public static IOManager getInstance () {
		if (instance == null) {
			instance = new IOManager();
		}

		return instance;
	}

	public static IOManager getInstance (IOStrategy strategy) {
		if (instance == null) {
			instance = new IOManager(strategy);
		}

		return instance;
	}
	public void drawGame()
	{
		this.strategy.drawGame();
	}
	public void drawEditor () {
		this.strategy.drawEditor();
	}
	public void drawMenu () {
		this.strategy.drawMenu();
	}
}
