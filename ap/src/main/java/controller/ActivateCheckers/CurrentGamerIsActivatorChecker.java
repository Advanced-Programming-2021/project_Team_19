package controller.ActivateCheckers;

import controller.DuelControllers.GameData;
import model.Board.SpellAndTrapCardZone;
import model.Card.Card;

public class CurrentGamerIsActivatorChecker extends ActivationChecker {

    public CurrentGamerIsActivatorChecker(GameData gameData, Card card){
        super(gameData, card);
    }

    public String check(){

        if (!(gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof SpellAndTrapCardZone)) {

            return "you can't activate this card";
        }
        return null;
    }
}
