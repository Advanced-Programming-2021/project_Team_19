package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.SpellAndTraps;

public class Activate extends Action {

    protected Card activatedCard;

    public Activate(GameData gameData) {
        super(gameData, "Activate");
    }

    public Card getActivatedCard() {
        return activatedCard;
    }

    protected void setActivatedCard(Card card) {
        activatedCard = card;
    }

    public void handleActivatee() {


    }
}
