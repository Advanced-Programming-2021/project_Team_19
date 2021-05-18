package controller.ActivateCheckers;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.SpellAndTraps;

public class CardHasNotBeenActivatedYetChecker extends ActivationChecker {

    public CardHasNotBeenActivatedYetChecker(GameData gameData, Card card){
        super(gameData, card);
    }

    public String check(){

        try{
            if (((SpellAndTraps)card).getTurnActivated() != 0) {
                return "you have already activated this card";
            }return null;

        } catch (ClassCastException e){
            e.printStackTrace();
            return "ERROR";
        }

    }
}
