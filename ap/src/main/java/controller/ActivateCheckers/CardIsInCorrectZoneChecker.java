package controller.ActivateCheckers;


import controller.DuelControllers.GameData;
import model.Board.SpellAndTrapCardZone;
import model.Card.Card;
import model.Enums.CardFamily;

public class CardIsInCorrectZoneChecker extends ActivationChecker {

    public CardIsInCorrectZoneChecker(GameData gameData, Card card) {
        super(gameData, card);
    }

    @Override
    public String check() {

        if (!(gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof SpellAndTrapCardZone)) {

            return "you can't activate this card";
        }

        return null;
    }
}
