package controller.ActivateCheckers;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monsters.ShouldAskForActivateEffectMonster;
import model.Card.SpellAndTraps;

public class CardHasNotBeenActivatedYetChecker extends ActivationChecker {

    public CardHasNotBeenActivatedYetChecker(GameData gameData, Card card) {
        super(gameData, card);
    }

    public String check() {

        try {
            if (((SpellAndTraps) card).getTurnActivated() != 0) {
                return "you have already activated this card";
            }
            return null;

        } catch (ClassCastException e) {

            try {
                if (((ShouldAskForActivateEffectMonster) card).getTurnActivated() != 0) {
                    return "you have already activated this card";
                }
                return null;
            } catch (ClassCastException e1) {
                return "ERROR";
            }
        }
    }
}
