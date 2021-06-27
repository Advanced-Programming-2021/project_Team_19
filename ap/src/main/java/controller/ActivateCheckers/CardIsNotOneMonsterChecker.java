package controller.ActivateCheckers;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Enums.CardFamily;

public class CardIsNotOneMonsterChecker extends ActivationChecker {

    public CardIsNotOneMonsterChecker(GameData gameData, Card card) {
        super(gameData, card);
    }

    public String check() {

        if (card.getCardFamily().equals(CardFamily.MONSTER)) {
            return "activate effect is only for spell and trap cards";
        }
        return null;
    }
}
