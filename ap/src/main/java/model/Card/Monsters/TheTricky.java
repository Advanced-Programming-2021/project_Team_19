package model.Card.Monsters;

import controller.DuelControllers.Actoins.Destroy;
import controller.DuelControllers.Actoins.SpecialSummon;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Board.Hand;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;

public class TheTricky extends Monster {

    @Override
    public int numberOfSacrifices(boolean isForSetting, int cardsThatCanBeSacrificed, GameData gameData) {

        if (isForSetting || !canUseEffect(gameData)){
            return 1;
        }

        Printer.print("""
                how do you want to summon this card?
                1- normal summon with one sacrifice
                2- special summon by discarding a card from your hand""");

        String command;
        while (true) {
            command = GetInput.getString();
            if (command.matches("1")) {
                return 1;
            } else if (command.matches("cancel")) {
                Printer.print("you successfully cancelled the summon");
                return -1;
            } else if (command.matches("2")) {
                break;
            } else {
                Printer.printInvalidCommand();
            }
        }

        specialSummonByDiscarding(gameData);
        return -1;
    }

    public void specialSummonByDiscarding(GameData gameData) {
        String command;
        ArrayList<Card> handWithoutThisCard = cardsInHandExceptThisCard(gameData);

        Card toDiscard = Utils.askUserToSelectCard(handWithoutThisCard, "select a card from your hand to discard", null);


        if (toDiscard != null){
            discardAndSummon(gameData, toDiscard);
            Printer.print("you successfully special summoned the Tricky");
            return;
        }
        Printer.print("you successfully cancelled the summon");

    }

    private ArrayList<Card> cardsInHandExceptThisCard(GameData gameData) {
        ArrayList<Card> handWithoutThisCard = (ArrayList<Card>) gameData.getCurrentGamer().getGameBoard().getHand().getCardsInHand().clone();
        handWithoutThisCard.remove(this);
        return handWithoutThisCard;
    }

    private void discardAndSummon(GameData gameData, Card toDiscard) {
        new Destroy(gameData).run(toDiscard, true);
        new SpecialSummon(gameData).run(this);
    }

    private boolean canUseEffect(GameData gameData) {
        if (gameData.getCurrentGamer().getGameBoard().getHand().getCardsInHand().size() < 2) {
            Printer.print("you do not have enough cards in your hand to special summon this card");
            return false;
        }
        return true;
    }

    public TheTricky(String name, String description, int price, int attack, int defence, int level, Attribute attribute, MonsterType monsterType, MonsterTypesForEffects monsterTypesForEffects) {
        super(name, description, price, attack, defence, level, attribute, monsterType, monsterTypesForEffects);
    }

}
