package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.Actoins.FlipSummon;
import controller.DuelControllers.Actoins.NormalSummon;
import controller.DuelControllers.Actoins.Summon;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import model.Card.Trap;

public class TorrentialTribute extends Trap {
    @Override
    public void activate(GameData gameData) {

        gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().removeAllCards();
        gameData.getSecondGamer().getGameBoard().getMonsterCardZone().removeAllCards();
    }

    public boolean canActivate(GameData gameData) {

        for (Action action : gameData.getCurrentActions()) {
            if (action instanceof Summon) {
                return true;
            }
        }
        return false;
    }
}
