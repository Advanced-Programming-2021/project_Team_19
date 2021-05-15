package model.Card.Monsters;

import com.google.gson.annotations.Expose;
import controller.DuelControllers.Actoins.SpecialSummon;
import controller.DuelControllers.GameData;
import model.Board.DeckZone;
import model.Board.GraveYard;
import model.Card.Card;
import model.Card.EffectTypes.Defend;
import model.Card.Monster;
import model.Enums.CardFamily;
import model.Enums.CardMod;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Gamer;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public class Textchanger extends Monster {

    @Expose
    int lastTurnEffectUsed;

    @Override
    public boolean attackIsNormal(GameData gameData) {
        if (lastTurnEffectUsed == gameData.getTurn())
            return true;
        if ((((Monster) gameData.getSelectedCard()).getAttack(gameData) < getDefence() && !getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED)) ||
                (((Monster) gameData.getSelectedCard()).getAttack(gameData) < getAttack(gameData) && getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED))) {
            return true;
        }
        Printer.print("the attack has been canceled because you attacked Tex Changer\n" +
                "your opponent can special summon a Cyberse card from their deck or graveyard");
//   todo     turn should be changed here
        summonWhenAttacked(gameData);
//   todo     turn should be changed here
        return false;
    }

    public boolean summonWhenAttacked(GameData gameData) {
        ArrayList<Monster> cyberseCards = getCyberseInGraveyardAndDeck(gameData.getCurrentGamer());
        if (cyberseCards.isEmpty()) {
            Printer.print("there are no Cyberse cards in your deck or graveyard to revive");
            return false;
        } else if (gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().isZoneFull()) {
            Printer.print("your monster card zone is full and you cannot summon a Cyberse monster");
            return false;
        }
        String command;
        Printer.print("do you want to summon a normal Cyberse monster from your graveyard or deck?");
        while (true) {
            command = GetInput.getString();
            if (command.matches("yes")) {
                return summonFromDeckOrGraveyard(gameData, cyberseCards);
            } else if (command.matches("no") || command.matches("cancel")) {
                return false;
            } else {
                Printer.printInvalidCommand();
            }
        }
    }

    private boolean summonFromDeckOrGraveyard(GameData gameData, ArrayList<Monster> cyberseCards) {
        String command;
        while (true) {
            showChooseCard(cyberseCards);
            command = GetInput.getString();
            if (command.matches("\\d+")) {
                if (Integer.parseInt(command) > cyberseCards.size()) {
                    Printer.print("invalid id");
                    showChooseCard(cyberseCards);
                } else {
                    new SpecialSummon(gameData).run(cyberseCards.get(Integer.parseInt(command) - 1));
                    return true;
                }
            } else if (command.matches("cancel")) {
                return false;
            } else {
                Printer.printInvalidCommand();
            }
        }
    }

    private void showChooseCard(ArrayList<Monster> monsters) {
        Printer.print("select a card id to summon:");
        for (int i = 1; i <= monsters.size(); i++) {
            Printer.print(i + "- " + monsters.get(i - 1).toString());
        }
    }

    private ArrayList<Monster> getCyberseInGraveyardAndDeck(Gamer gamer) {
        GraveYard graveYard = gamer.getGameBoard().getGraveYard();
        DeckZone deck = gamer.getGameBoard().getDeckZone();
        ArrayList<Monster> cyberseCards = new ArrayList<>();
        for (Card card : graveYard.getCardsInGraveYard()) {
            if (card.getCardFamily().equals(CardFamily.MONSTER) &&
                    ((Monster) card).getMonsterType().equals(MonsterType.CYBERSE) &&
                    ((Monster) card).getEffectType().equals(MonsterTypesForEffects.Normal))
                cyberseCards.add((Monster) card);
        }
        for (int i = 0; i < deck.getSize(); i++) {
            Card card = deck.getCard(i);
            if (card.getCardFamily().equals(CardFamily.MONSTER) &&
                    ((Monster) card).getMonsterType().equals(MonsterType.CYBERSE) &&
                    ((Monster) card).getEffectType().equals(MonsterTypesForEffects.Normal))
                cyberseCards.add((Monster) card);
        }
        return cyberseCards;
    }
}
