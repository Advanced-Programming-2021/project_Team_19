package model.Card.Monsters;

import com.google.gson.annotations.Expose;
import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.Actoins.SpecialSummon;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Board.GraveYard;
import model.Board.Hand;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Gamer;
import view.Printer.Printer;

import java.util.ArrayList;

public class HeraldOfCreation extends ShouldAskForActivateEffectMonster {

    @Expose
    private int lastTurnEffectUsed;

    public void useEffect(GameData gameData) {
        if (!canUseEffect(gameData))
            return;

        Hand hand = gameData.getCurrentGamer().getGameBoard().getHand();
        GraveYard graveYard = gameData.getCurrentGamer().getGameBoard().getGraveYard();

        Card selectedCardFromHand = Utils.askUserToSelectCard(hand.getCardsInHand(),
                "select a card id to discard:", null);

        if (selectedCardFromHand == null)
            return;

        Card selectedCardFromGraveyard = Utils.askUserToSelectCard(
                monstersInGraveyardWithLevelAtLeast7(graveYard.getCardsInGraveYard()),
                "select a card id to revive from graveyard:",
                null);

        if (selectedCardFromGraveyard == null)
            return;

        discardAndRevive(gameData,
                selectedCardFromHand,
                selectedCardFromGraveyard);
        lastTurnEffectUsed = gameData.getTurn();

    }

    private void discardAndRevive(GameData gameData, Card toDiscard, Card toRevive) {
        new Destroy(gameData).run(toDiscard, true);
        new SpecialSummon(gameData).run(toRevive);
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

    public HeraldOfCreation(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects) {
        super(name, description, price, attack, defence, level, attribute, monsterType, monsterTypesForEffects);
    }
}
