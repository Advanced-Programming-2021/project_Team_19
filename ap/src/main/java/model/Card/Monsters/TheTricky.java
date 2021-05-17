package model.Card.Monsters;

import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.GameData;
import model.Board.Hand;
import model.Card.Card;
import model.Card.Monster;
import view.GetInput;
import view.Printer.Printer;

public class TheTricky extends Monster {

    public void useEffect(GameData gameData) {
        if (canUseEffect(gameData)) {
            String command;
            Hand handWithoutThisCard = cardsInHandExceptThisCard(gameData);
            showChooseFromHand(handWithoutThisCard);

            while (true) {
                command = GetInput.getString();
                if (command.matches("\\d+")) {
                    if (Integer.parseInt(command) <= handWithoutThisCard.getCardsInHand().size()) {
                        discardAndSummon(gameData, handWithoutThisCard.getCardsInHand().get(Integer.parseInt(command)));
                        break;
                    } else {
                        Printer.print("invalid id");
                        showChooseFromHand(handWithoutThisCard);
                    }
                } else if (command.matches("cancel")) {
                    break;
                } else {
                    Printer.printInvalidCommand();
                }

            }
        }
    }

    private Hand cardsInHandExceptThisCard(GameData gameData) {
        Hand handWithoutThisCard = (Hand) gameData.getCurrentGamer().getGameBoard().getHand().getCardsInHand().clone();
        handWithoutThisCard.removeCard(handWithoutThisCard.getId(this));
        return handWithoutThisCard;
    }

    private void discardAndSummon(GameData gameData, Card toDiscard) {
        new Destroy(gameData).run(toDiscard, true);
        gameData.moveCardFromOneZoneToAnother(this,
                gameData.getCurrentGamer().getGameBoard().getHand(),
                gameData.getCurrentGamer().getGameBoard().getMonsterCardZone());
    }

    private void showChooseFromHand(Hand hand) {
        Printer.print("select a card id to discard:");
        hand.showHand();
    }

    private boolean canUseEffect(GameData gameData) {
        if (gameData.getCurrentGamer().getGameBoard().getHand().getCardsInHand().size() < 2) {
            Printer.print("you do not have enough cards in your hand to special summon this card");
            return false;
        }
        return true;
    }

}
