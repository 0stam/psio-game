package tile;

import display.PopupClass;
import enterablestrategy.Empty;
import enums.Direction;
import enums.Graphics;
import event.game.PopupEvent;
import event.game.PopupResetEvent;
import gamemanager.GameManager;

public class Sign extends Tile {
	public Sign (int x, int y) {
		super(x, y);
		this.setEnterableStrategy(new Empty());
		this.setGraphicsID(Graphics.SIGN);
	}

	@Override
	public void onEntered(Direction direction, Tile tile) {
		super.onEntered(direction, tile);
		if (tile instanceof PlayerCharacter) {
			GameManager.getInstance().onEvent(new PopupEvent(new PopupClass("test")));
		}
	}

	@Override
	public void onExited(Direction direction, Tile tile) {
		super.onExited(direction, tile);
		if (tile instanceof PlayerCharacter) {
			GameManager.getInstance().onEvent(new PopupResetEvent());
		}
	}
}