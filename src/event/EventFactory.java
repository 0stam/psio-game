package event;

import enums.Arrow;
import enums.EditableTile;
import enums.EditorGraphics;

public class EventFactory {
    public static EditorEvent createPaletteEvent(EditorGraphics type)
    {
        if (type instanceof EditableTile)
        {
            return new PalettePressedEvent((EditableTile) type);
        }
        if (type instanceof Arrow)
        {
            return new ArrowPalettePressedEvent((Arrow) type);
        }
        //return invalid event?
        return null;
    }
}
