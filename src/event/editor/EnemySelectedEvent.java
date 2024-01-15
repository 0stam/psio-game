package event.editor;

import tile.SmartEnemy;

public class EnemySelectedEvent extends EditorEvent{
    private final SmartEnemy smartEnemy;
    public EnemySelectedEvent(SmartEnemy smartEnemy) {
        this.smartEnemy = smartEnemy;
    }

    public SmartEnemy getSmartEnemy() {
        return smartEnemy;
    }
}
