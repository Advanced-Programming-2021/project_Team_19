package controller.ActivateCheckers;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;

public class SelectedCardIsNotNullChecker extends ActivationChecker {

    public SelectedCardIsNotNullChecker(GameData gameData, Card card){
        super(gameData, card);
    }

    public String check(){

        if (Utils.IsSelectedCardNull(gameData)) {
            return "no card is selected yet";
        }
        return null;
    }

}
