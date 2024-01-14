package event;

import enums.Layer;

public class ChangeLayerEvent extends EditorEvent{
    private final Layer layer;
    public ChangeLayerEvent(Layer layer)
    {
        this.layer = layer;
    }

    public Layer getLayer() {
        return layer;
    }
}
