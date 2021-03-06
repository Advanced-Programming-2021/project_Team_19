package model.Card.Spells;

import controller.DuelControllers.Actions.SpecialSummon;
import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;
import model.Pair;
import view.GetInput;
import view.Printer.Printer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonsterReborn extends Spell {
    @Override
    public ActivationData activate(GameData gameData) {
        if (gameData.getSecondGamer().getGameBoard().getGraveYard().getSize() + gameData.getCurrentGamer().getGameBoard().getGraveYard().getSize() == 0) {
            return new ActivationData(this, "This card cannot be activated");
        }
        Printer.print("Your grave yard");
        gameData.getCurrentGamer().getGameBoard().getGraveYard().printGraveYard();
        Printer.print("Your rival's graveYard");
        gameData.getSecondGamer().getGameBoard().getGraveYard().printGraveYard();
        Printer.print("Please select a card of format: (index in the graveYard) --<opponent>(optional)");
        Pair<Integer, Boolean> cardPosition = chooseCard(gameData);
        Card cardToSummon;
        Card newCard;
        if (cardPosition == null) {
            return new ActivationData(this, "The procces was canceled by user");
        } else if (cardPosition.getSecond()) {
            cardToSummon = gameData.getSecondGamer().getGameBoard().getGraveYard().getCard(cardPosition.getFirst());
            newCard = gameData.moveCardFromOneZoneToAnother(cardToSummon,
                    gameData.getSecondGamer().getGameBoard().getGraveYard(), gameData.getCurrentGamer().getGameBoard().getMonsterCardZone());
        } else {
            cardToSummon = gameData.getCurrentGamer().getGameBoard().getGraveYard().getCard(cardPosition.getFirst());
            newCard = gameData.moveCardFromOneZoneToAnother(cardToSummon,
                    gameData.getCurrentGamer().getGameBoard().getGraveYard(), gameData.getCurrentGamer().getGameBoard().getMonsterCardZone());
        }
        new SpecialSummon(gameData).run(newCard);

        return new ActivationData(this, "The card " + cardToSummon.getName() + " was summoned specially");
    }

    private Pair<Integer, Boolean> chooseCard(GameData gameData) {
        String userCommand = GetInput.getString();
        if (userCommand.equals("cancel")) {
            return null;
        } else if (!userCommand.matches("(\\d+)(| --opponent)")) {
            Printer.print("Invalid input! please try again");
            Printer.print("User format: (index in the graveYard) --<opponent>(optional)");
            return chooseCard(gameData);
        } else {
            Pattern chooseCardPattern = Pattern.compile("(\\d+)(| --opponent)");
            Matcher chooseCardMatcher = chooseCardPattern.matcher(userCommand);
            chooseCardMatcher.matches();
            boolean isForRival = !(chooseCardMatcher.group(2).equals(""));
            int selectIndex = Integer.parseInt(chooseCardMatcher.group(1));
            if (isForRival && selectIndex > gameData.getSecondGamer().getGameBoard().getGraveYard().getSize()) {
                Printer.print("This is an invalid index rival's grave yard has " +
                        gameData.getSecondGamer().getGameBoard().getGraveYard().getSize() +
                        " elements");
                return chooseCard(gameData);
            } else if (!isForRival && selectIndex > gameData.getCurrentGamer().getGameBoard().getGraveYard().getSize()) {
                Printer.print("This is an invalid index your grave yard has " +
                        gameData.getCurrentGamer().getGameBoard().getGraveYard().getSize() +
                        " elements");
                return chooseCard(gameData);
            } else {
                return new Pair<>(selectIndex, isForRival);
            }
        }
    }

    public MonsterReborn(String name, String description, int price, Type type, SpellTypes spellType, Status status) {
        super(name, description, price, type, spellType, status);
    }
}
