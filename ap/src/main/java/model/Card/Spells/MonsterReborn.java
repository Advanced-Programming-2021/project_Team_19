package model.Card.Spells;

import controller.DuelControllers.Actoins.SpecialSummon;
import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Pair;
import view.GetInput;
import view.Printer.Printer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MonsterReborn extends Spell {
    @Override
    public ActivationData activate(GameData gameData) {
        Printer.print("Your grave yard");
        gameData.getCurrentGamer().getGameBoard().getGraveYard().printGraveYard();
        Printer.print("Your rival's graveYard");
        gameData.getSecondGamer().getGameBoard().getGraveYard().printGraveYard();
        Printer.print("Please select a card of format: (index in the graveYard) --<opponent>(optional)");
        Pair<Integer, Boolean> cardPosition = chooseCard(gameData);
        Card cardToSummon;
        if(cardPosition.getSecond()){
            cardToSummon = gameData.getSecondGamer().getGameBoard().getGraveYard().getCard(cardPosition.getFirst());
            gameData.moveCardFromOneZoneToAnother(gameData.getSecondGamer().getGameBoard().getGraveYard().getCard(cardPosition.getFirst()),
                    gameData.getSecondGamer().getGameBoard().getGraveYard(),gameData.getCurrentGamer().getGameBoard().getMonsterCardZone());
        }
        else{
            cardToSummon = gameData.getCurrentGamer().getGameBoard().getGraveYard().getCard(cardPosition.getFirst());
            gameData.moveCardFromOneZoneToAnother(gameData.getCurrentGamer().getGameBoard().getGraveYard().getCard(cardPosition.getFirst()),
                    gameData.getSecondGamer().getGameBoard().getGraveYard(),gameData.getCurrentGamer().getGameBoard().getMonsterCardZone());
        }
        new SpecialSummon(gameData).run(cardToSummon);

        return new ActivationData(this, "The card "+cardToSummon.getName()+" was summoned specially");
    }

    private Pair<Integer, Boolean> chooseCard(GameData gameData){
        String userCommand = GetInput.getString();
        if(!userCommand.matches("(\\d+)(| --opponent)")){
            Printer.print("Invalid input! please try again");
            Printer.print("User format: (index in the graveYard) --<opponent>(optional)");
            return chooseCard(gameData);
        }
        else {
            Pattern chooseCardPattern = Pattern.compile("(\\d+)(| --opponent)");
            Matcher chooseCardMatcher = chooseCardPattern.matcher(userCommand);
            chooseCardMatcher.matches();
            boolean isForRival = (chooseCardMatcher.group(2).equals(""));
            int selectIndex = Integer.parseInt(chooseCardMatcher.group(2));
            if(isForRival&&selectIndex > gameData.getSecondGamer().getGameBoard().getGraveYard().getSize()){
                Printer.print("This is an invalid index rival's grave yard has "+
                        gameData.getSecondGamer().getGameBoard().getGraveYard().getSize()+
                        "elements");
                return chooseCard(gameData);
            }
            else if(!isForRival&&selectIndex > gameData.getCurrentGamer().getGameBoard().getGraveYard().getSize()){
                Printer.print("This is an invalid index your grave yard has "+
                        gameData.getCurrentGamer().getGameBoard().getGraveYard().getSize()+
                        "elements");
                return chooseCard(gameData);
            }
            else{
                return new Pair<>(selectIndex, isForRival);
            }
        }
    }
}
