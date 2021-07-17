package model.Card.Monsters;

import com.google.gson.annotations.Expose;
import controller.DuelControllers.Actions.Destroy;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Board.GraveYard;
import model.Board.Hand;
import model.Card.Card;
import model.Card.Monster;
import model.Data.ActivationData;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Gamer;

import java.util.ArrayList;

public class HeraldOfCreation extends ShouldAskForActivateEffectMonster {

    @Expose
    private int lastTurnEffectUsed;

    public ActivationData activate(GameData gameData) {
        if (!canActivate(gameData))
            return null;

        Hand hand = gameData.getCurrentGamer().getGameBoard().getHand();
        GraveYard graveYard = gameData.getCurrentGamer().getGameBoard().getGraveYard();

        Card selectedCardFromHand = Utils.askUserToSelectCard(hand.getCardsInHand(),
                "select a card id to discard:", null);

        if (selectedCardFromHand == null)
            return null;

        Card selectedCardFromGraveyard = Utils.askUserToSelectCard(
                monstersInGraveyardWithLevelAtLeast7(graveYard.getCardsInGraveYard()),
                "select a card id to move to your hand from graveyard:",
                null);

        if (selectedCardFromGraveyard == null)
            return null;

        discardAndRevive(gameData,
                selectedCardFromHand,
                selectedCardFromGraveyard);
        lastTurnEffectUsed = gameData.getTurn();

        return new ActivationData(this, "you successfully added the card to your hand");
    }

    private void discardAndRevive(GameData gameData, Card toDiscard, Card toRevive) {
        new Destroy(gameData).run(toDiscard, true);
        Gamer currentPlayer = gameData.getCurrentGamer();
        currentPlayer.getGameBoard().getGraveYard().removeCard(toRevive);
        currentPlayer.getGameBoard().getHand().addCard(toRevive);
    }

    public boolean canActivate(GameData gameData) {
        Gamer gamer = gameData.getCurrentGamer();
        if (gameData.getTurn() == lastTurnEffectUsed) {
//            you already used this card's effect this turn
            return false;
        } else if (gamer.getGameBoard().getHand().getCardsInHand().size() == 0) {
//            you have no cards in your hand to discard
            return false;
        } else if (monstersInGraveyardWithLevelAtLeast7(gamer.getGameBoard().getGraveYard().getCardsInGraveYard()).size() == 0) {
//            you have no cards with level 7 or above in graveyard
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
