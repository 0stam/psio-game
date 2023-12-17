package turnstrategy;

import enums.Direction;
import gamemanager.GameManager;
import tile.ActionTile;
import tile.Tile;

import static enums.Direction.*;
import static java.lang.Math.*;

public class PlayerFollower implements TurnStrategy{

    //cel dla followera
    private Tile targetTile;
    public PlayerFollower(Tile targetTile)
    {
        this.targetTile = targetTile;
    }
    public static Direction getDirection(ActionTile follower, Tile target)
    {
        //Metoda bedzie wyszukiwac sciezke naiwna - w zaleznosci od kata gracza od followera
        //bedzie szla w ta strone
        //mozliwosci tworzenia wlasnych klas wyjatkow i gdzie moga wystapic
        //przy y musi byc minus - bo y jest od 0 w gorze do wysokosci planszy na dole
        //jakis zly czlowiek stwierdzil ze pierwszym argumentem jest y a nie x
        double angle = atan2(-(target.getY() - follower.getY()), target.getX() - follower.getX());
        //matematyka... Dzielimy mape na 4 czesci. 1 - od -45 stopni do 45 st, druga 45, 135 itd
        //wtedy -45 45 - idz do przodu, 45 135 - idz do gory itd. Ponizej zamienione na radiany
        //atan2 zwraca kat miedzy PI a -PI
        if ((angle <= PI*0.25d)&&(angle >= -PI*0.25d))
            return RIGHT;
        if ((angle <= PI*0.75d)&&(angle >= PI*0.25d))
            return UP;
        if ((angle <= -PI*0.25d)&&(angle >= -PI*0.75))
            return DOWN;
        //jaja z przechodzeniem przez kat PI (tam granicza PI i -PI) dlatego lewo jest w else
        //bez ifa (najwygodniej zeby left byl wlasnie tu)
        return LEFT;
    }


    @Override
    public void onTurn(Direction direction, ActionTile follower) {
        GameManager.getInstance().getMap().move(follower.getX(), follower.getY(), getDirection(follower, targetTile));
    }

}
