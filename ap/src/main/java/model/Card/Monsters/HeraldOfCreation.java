package model.Card.Monsters;

import com.google.gson.annotations.Expose;
import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Board.GraveYard;
import model.Board.Hand;
import model.Card.Card;
import model.Card.Monster;
import model.Gamer;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public class HeraldOfCreation extends Monster {

    @Expose
    private int lastTurnEffectUsed;

    public void useEffect(GameData gameData) {
        if (canUseEffect(gameData)) {
            String command = "";
            boolean effectNotDone = true;
            Hand hand = gameData.getCurrentGamer().getGameBoard().getHand();
            showChooseFromHand(hand);
            GraveYard graveYard = gameData.getCurrentGamer().getGameBoard().getGraveYard();

            int cardIdFromHand = 0;
            boolean cardIsSelected = false;
            while (effectNotDone) {
                command = GetInput.getString();
                if (command.matches("\\d+")) {
                    if (!cardIsSelected) {
                        if (Integer.parseInt(command) <= hand.getCardsInHand().size()) {
                            cardIdFromHand = Integer.parseInt(command);
                            cardIsSelected = true;
                            showChooseFromGraveyard(graveYard);
                        } else {
                            Printer.print("invalid id");
                            showChooseFromHand(hand);
                        }
                    } else {
                        if (Integer.parseInt(command) <= monstersInGraveyardWithLevelAtLeast7
                                (graveYard.getCardsInGraveYard()).size()) {
                            discardAndRevive(gameData,
                                    hand.getCardsInHand().get(cardIdFromHand),
                                    monstersInGraveyardWithLevelAtLeast7
                                            (graveYard.getCardsInGraveYard()).get(Integer.parseInt(command)));
                            lastTurnEffectUsed = gameData.getTurn();
                            effectNotDone = false;
                        } else {
                            Printer.print("invalid id");
                            showChooseFromGraveyard(graveYard);
                        }

                    }
                } else if (command.matches("cancel")) {
                    effectNotDone = false;
                } else {
                    Printer.printInvalidCommand();
                }

            }
        }
    }

    private void discardAndRevive(GameData gameData, Card toDiscard, Card toRevive) {
        new Destroy(gameData).run(toDiscard);
        gameData.moveCardFromOneZoneToAnother(toRevive,
                gameData.getCurrentGamer().getGameBoard().getGraveYard(),
                gameData.getCurrentGamer().getGameBoard().getHand());
    }

    private void showChooseFromHand(Hand hand) {
        Printer.print("select a card id to discard:");
        hand.showHand();
    }

    private void showChooseFromGraveyard(GraveYard graveYard) {
        Printer.print("select a card id to revive from graveyard:");
        Utils.printArrayListOfCards(monstersInGraveyardWithLevelAtLeast7(graveYard.getCardsInGraveYard()));
    }


    private boolean canUseEffect(GameData gameData) {
        Gamer gamer = gameData.getCurrentGamer();
        if (gameData.getTurn() == lastTurnEffectUsed) {
            Printer.print("you already used this card's effect this turn");
            return false;
        } else if (gamer.getGameBoard().getHand().getCardsInHand().size() == 0) {
            Printer.print("you have no cards in your hand to discard");
            return false;
        } else if (monstersInGraveyardWithLevelAtLeast7(gamer.getGameBoard().getGraveYard().getCardsInGraveYard()).size() == 0) {
            Printer.print("you have no cards with level 7 or above in graveyard");
            return false;
        }
        return true;
    }

    private ArrayList<Card> monstersInGraveyardWithLevelAtLeast7(ArrayList<Card> cardsInGraveYard) {
        ArrayList<Card> monstersWithLevel7OrAbove = new ArrayList<>();
        for (Card card : cardsInGraveYard) {
            if (card instanceof Monster && ((Monster) card).getLevel() >= 7) {
                monstersWithLevel7OrAbove.add(card);
            }
        }
        return monstersWithLevel7OrAbove;
    }
}
