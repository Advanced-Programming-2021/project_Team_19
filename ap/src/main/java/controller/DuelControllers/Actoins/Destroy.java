package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Card;

public class Destroy extends Action {

    public Destroy(GameData gameData) {
        super(gameData, "destroy");
    }

    public void run(Card card, boolean discardWithoutEffect) {

        if (gameData.getCurrentGamer().getGameBoard().getZone(card) != null)
            gameData.moveCardFromOneZoneToAnother(card,
                    gameData.getCurrentGamer().getGameBoard().getZone(card),
                    gameData.getCurrentGamer().getGameBoard().getGraveYard());
        else
            gameData.moveCardFromOneZoneToAnother(card,
                    gameData.getSecondGamer().getGameBoard().getZone(card),
                    gameData.getSecondGamer().getGameBoard().getGraveYard());
    }

}
