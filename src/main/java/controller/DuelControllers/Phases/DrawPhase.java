package controller.DuelControllers.Phases;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Gamer;
import view.Printer.Printer;

public class DrawPhase {

    public Card run(GameData gameData) {

        return addCardToPlayerHand(gameData);
    }

    private Card addCardToPlayerHand(GameData gameData) {
        Gamer currentPlayer = gameData.getCurrentGamer();
        Card cardToAddToHand = currentPlayer.getGameBoard().getDeckZone().removeCard(0);
        Printer.print("new card added to the hand : " + cardToAddToHand.getName());
        currentPlayer.getGameBoard().getHand().addCard(cardToAddToHand);

        return cardToAddToHand;
    }

}
