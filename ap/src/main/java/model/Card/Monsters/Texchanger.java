package model.Card.Monsters;

import com.google.gson.annotations.Expose;
import controller.DuelControllers.Actoins.SpecialSummon;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.AllBoards;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardFamily;
import model.Enums.CardMod;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Gamer;
import view.Printer.Printer;

import java.util.ArrayList;
import java.util.Collections;

public class Texchanger extends Monster {

    @Expose
    int lastTurnEffectUsed;

    @Override
    public boolean attackIsNormal(GameData gameData) {
        ((Monster) gameData.getSelectedCard()).setLastTurnAttacked(gameData);

        if (lastTurnEffectUsed == gameData.getTurn())
            return true;


        lastTurnEffectUsed = gameData.getTurn();

        if ((((Monster) gameData.getSelectedCard()).getAttack(gameData) < getDefence(gameData) && !getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED)) ||
                (((Monster) gameData.getSelectedCard()).getAttack(gameData) < getAttack(gameData) && getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED))) {
            return true;
        }

        Printer.print("the attack has been canceled because you attacked Texchanger\n" +
                "your opponent can special summon a Cyberse card from their deck, hand or graveyard");
        Utils.changeTurn(gameData);
        summonWhenAttacked(gameData);
        Utils.changeTurn(gameData);
        return false;
    }

    public void summonWhenAttacked(GameData gameData) {
        ArrayList<Card> cyberseCards = getCyberseInGraveyardAndDeck(gameData.getCurrentGamer());
        if (cyberseCards.isEmpty()) {
            Printer.print("there are no Cyberse cards in your deck or graveyard to revive");
            return;
        } else if (gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().isZoneFull()) {
            Printer.print("your monster card zone is full and you cannot summon a Cyberse monster");
            return;
        }

        if (Utils.askForConfirmation("do you want to summon a normal Cyberse monster from your graveyard or deck?")){
            Card selectedCard = Utils.askUserToSelectCard
                    (cyberseCards, "select a card id to summon", null);
            if (selectedCard == null)
                return;
            new SpecialSummon(gameData).run(selectedCard);
        }
    }

    private ArrayList<Card> getCyberseInGraveyardAndDeck(Gamer gamer) {
        AllBoards gameBoard = gamer.getGameBoard();

        ArrayList<Card> cyberseCards = new ArrayList<>(gameBoard.getHand().getCardsInHand());
        cyberseCards.addAll(gameBoard.getGraveYard().getCardsInGraveYard());
        cyberseCards.addAll(gameBoard.getDeckZone().getMainDeckCards());

        cyberseCards.removeAll(Collections.singleton(null));

        cyberseCards.removeIf(card -> !card.getCardFamily().equals(CardFamily.MONSTER) ||
                !((Monster) card).getMonsterType().equals(MonsterType.CYBERSE) ||
                !((Monster) card).getEffectType().equals(MonsterTypesForEffects.NORMAL));

        return cyberseCards;
    }

    public Texchanger(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects){
        super(name,description,price,attack,defence,level,attribute,monsterType,monsterTypesForEffects);
    }
}
