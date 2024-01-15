package event.editor;

import tile.SmartEnemy;
import enums.Layer;
import enums.EditorMode;

public class EditorStateEvent extends EditorEvent {
	private final SmartEnemy enemyChange;
	private final Layer layerChange;
	private final EditorMode modeChange;
	EditorStateEvent(SmartEnemy enemy, Layer layer, EditorMode mode)
	{
		enemyChange = enemy;
		layerChange = layer;
		modeChange = mode;
	}
	public SmartEnemy getEnemy() {return enemyChange;}
	public Layer getLayer() {return layerChange;}
	public EditorMode getMode() {return modeChange;}
}
