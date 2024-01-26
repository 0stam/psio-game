package display;

import IO.GraphicIO;
import IO.IOManager;
import event.Event;
import event.EventObserver;
import event.display.InteractiveTilePressedEvent;
import event.game.PopupEvent;

import javax.swing.*;

public class GameEventHandler implements EventObserver {
	private boolean popupDisplayed = false;
	private PopupFactory popupFactory = new PopupFactory();
	private Popup displayedPopup;
	@Override
	public void onEvent(Event event) {
		switch (event.getClass().getSimpleName()) {
			case "PopupEvent":
				onPopupEvent((PopupEvent) event);
				break;
		}
	}
	//ma sens tylko jezeli io strategy to grafika
	private void onPopupEvent(PopupEvent event)
	{
		//PopupClass popupContent = event.getPopupClass();
		//test
		PopupClass popupContent = new PopupClass("WOJToWICZ ZAJMUJE DWA (2) MIEJSCA");
		JFrame frame = ((GraphicIO)IOManager.getInstance().getStrategy()).getWindow();
		displayedPopup = popupFactory.getPopup(frame, popupContent, popupContent.getWidth(), popupContent.getHeight());
	}

}
