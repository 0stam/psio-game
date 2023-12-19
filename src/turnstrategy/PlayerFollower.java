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
        //przy y musi byc minus - bo y jest od 0 w gorze do wysokosci planszy na dole
        double angle = atan2(-(target.getY() - follower.getY()), target.getX() - follower.getX());
        //matematycznie: Dzielimy mape na 4 czesci. 1 - od -45 stopni do 45 st, druga 45, 135 itd
        //wtedy -45 45 - idz dogit fetch przodu, 45 135 - idz do gory itd. Ponizej zamienione na radiany
        //atan2 zwraca kat miedzy PI a -PI
        //przypadki specjalne
        if (angle == PI*0.25d)
        {
            if (GameManager.getInstance().getMap().checkEnterable(follower.getX() + UP.x, follower.getY() + UP.y, UP, follower))
            {
                return UP;
            }
            if (GameManager.getInstance().getMap().checkEnterable(follower.getX() + RIGHT.x, follower.getY() + RIGHT.y, RIGHT, follower))
            {
                return RIGHT;
            }
            return DEFAULT;
        }
        if (angle == -PI*0.25d)
        {
            if (GameManager.getInstance().getMap().checkEnterable(follower.getX() + RIGHT.x, follower.getY() + RIGHT.y, RIGHT, follower))
            {
                return RIGHT;
            }
            if (GameManager.getInstance().getMap().checkEnterable(follower.getX() + DOWN.x, follower.getY() + DOWN.y, DOWN, follower))
            {
                return DOWN;
            }
            return DEFAULT;
        }
        if (angle == PI*0.75d)
        {
            if (GameManager.getInstance().getMap().checkEnterable(follower.getX() + LEFT.x, follower.getY() + LEFT.y, LEFT, follower))
            {
                return LEFT;
            }
            if (GameManager.getInstance().getMap().checkEnterable(follower.getX() + UP.x, follower.getY() + UP.y, UP, follower))
            {
                return UP;
            }
            return DEFAULT;
        }
        if (angle == -PI*0.75d)
        {
            if (GameManager.getInstance().getMap().checkEnterable(follower.getX() + DOWN.x, follower.getY() + DOWN.y, DOWN, follower))
            {
                return DOWN;
            }
            if (GameManager.getInstance().getMap().checkEnterable(follower.getX() + LEFT.x, follower.getY() + LEFT.y, LEFT, follower))
            {
                return LEFT;
            }
            return DEFAULT;
        }
        if ((angle < PI*0.25d)&&(angle > -PI*0.25d))
            return RIGHT;
        if ((angle < PI*0.75d)&&(angle > PI*0.25d))
            return UP;
        if ((angle < -PI*0.25d)&&(angle > -PI*0.75))
            return DOWN;
        return LEFT;
    }


    @Override
    public void onTurn(Direction direction, ActionTile follower) {
        GameManager.getInstance().getMap().move(follower.getX(), follower.getY(), getDirection(follower, targetTile));
    }

}
