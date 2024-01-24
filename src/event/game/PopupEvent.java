package event.game;

import display.PopupClass;
import event.Event;


public class PopupEvent extends GameEvent {
	private PopupClass popupClass;
	public PopupEvent(PopupClass popupClass)
	{
		this.popupClass = popupClass;
	}
	public void setPopupClass(PopupClass popupClass)
	{
		this.popupClass = popupClass;
	}
	public PopupClass getPopupClass()
	{
		return popupClass;
	}

}
