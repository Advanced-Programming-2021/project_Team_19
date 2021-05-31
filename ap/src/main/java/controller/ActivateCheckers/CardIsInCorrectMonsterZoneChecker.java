package controller.ActivateCheckers;

import controller.DuelControllers.GameData;
import model.Board.MonsterCardZone;
import model.Card.Card;

public class CardIsInCorrectMonsterZoneChecker extends ActivationChecker {

    public CardIsInCorrectMonsterZoneChecker(GameData gameData, Card card) {
        super(gameData, card);
    }

    public String check() {
        if (gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof MonsterCardZone) {
            return null;
        }
        return "you can't activate this card";
    }
}
