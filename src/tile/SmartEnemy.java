package tile;

import enterablestrategy.Solid;
import enums.Graphics;
import turnstrategy.Idle;
import turnstrategy.StateMachine;

import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;

public class SmartEnemy extends ActionTile {
	private ArrayList<PathTile> pathTileList;
	public SmartEnemy(int x, int y) {
		super(x, y, 1);
		setGraphicsID(Graphics.SMART);
		setEnterableStrategy(new Solid());
		/*
			Rozwazalbym jakas strategie ktora ma liste typu (klasa): -koordynaty -kierunek
			Kazde pole musi miec kordy aby latwo bylo je wyswietlac podczas gry i wracac do sciezki
			Przeciwnik podczas gry uzywalby listy kierunkow ze strategii aby szybciej wykonywal obliczenia
		 */
		setTurnStrategy(new StateMachine());
	}

	public ArrayList<PathTile> getPathTileList() {
		return pathTileList;
	}

	public void setPathTileList(ArrayList<PathTile> pathTileList) {
		this.pathTileList = pathTileList;
		setTurnStrategy(new StateMachine(pathTileList));
	}
}
