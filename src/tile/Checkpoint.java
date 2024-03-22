package tile;

import enterablestrategy.Empty;
import enums.Direction;
import enums.Graphics;
import event.game.CheckpointActivatedEvent;
import gamemanager.GameManager;

public class Checkpoint extends Tile {
    private boolean activated;
    private boolean destroyed;

    public Checkpoint(int x, int y) {
        super(x, y);
        setEnterableStrategy(new Empty());
        setGraphicsID(Graphics.CHECKPOINT_UNUSED);
        activated = false;
        destroyed = false;
    }

    @Override
    public void onEntered(Direction direction, Tile tile) {
        super.onEntered(direction, tile);

        if (tile instanceof PlayerCharacter && !activated) {
            activated = true;
            setGraphicsID(Graphics.CHECKPOINT_USED);
            GameManager.getInstance().onEvent(new CheckpointActivatedEvent());
        }
    }

    public void setDestroyed (boolean d) {
        destroyed = d;
        setGraphicsID(d ? Graphics.CHECKPOINT_DESTROYED : (activated ? Graphics.CHECKPOINT_USED : Graphics.CHECKPOINT_UNUSED));
    }
}
