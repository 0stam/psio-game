package event.display;

import enums.Layer;
import event.editor.EditorEvent;

public class ChangeLayerEvent extends DisplayEvent {
    private final Layer layer;
    public ChangeLayerEvent(Layer layer)
    {
        this.layer = layer;
    }

    public Layer getLayer() {
        return layer;
    }
}
