package model.Card.Traps;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Data.ActivationData;
import model.Enums.*;
import model.Enums.SpellsAndTraps.TrapTypes;
import view.GetInput;
import view.Printer.Printer;

import java.util.ArrayList;
import java.util.Random;

public class MindCrush extends SpeedEffectTrap {

    public MindCrush(String name, String description, int price, Type type, TrapTypes trapType, Status status){
        super(name,description,price,type, trapType, status);
    }

    @Override
    public ActivationData activate(GameData gameData) {

        Printer.print("declare one card name or enter cancel");
        String declaredCardName = GetInput.getString();

        if(declaredCardName.equals("cancel")){
            spellCardMod = SpellCardMods.HIDDEN;
            return new ActivationData(null, "");
        }

        if(Utils.getCardByName(declaredCardName) == null){
            Printer.print("invalid card name");
            return activate(gameData);
        }

        handleCommonsForActivate(gameData);

        boolean isCardInRivalHand = isCardInRivalHand(gameData, declaredCardName);

        ActivationData activationData;

        if(isCardInRivalHand){
            activationData =  handleDisCardCardFromRivalHand(gameData, declaredCardName);
        } else{
            activationData = handleDisCardCardFromCurrentGamerHand(gameData);
        }

        handleDestroy(gameData);

        return activationData;

    }

    private ActivationData handleDisCardCardFromCurrentGamerHand(GameData gameData){

        ArrayList<Card> currentGamerHand =
                gameData.getCurrentGamer().getGameBoard().getHand().getCardsInHand();

        if(currentGamerHand.size() == 0){
            return new ActivationData(this,
                    "trap activated successfully" + "\n" +
                            "this card does not exist in rival rivalHand " +
                            "but your hand is empty " + "\n" + " so nothing happened");
        }

        int rand = Math.abs(new Random().nextInt() % currentGamerHand.size());

        gameData.getCurrentGamer().getGameBoard().getHand().getCard(rand).handleDestroy(gameData);
        return new ActivationData(this, "trap activated successfully" + "\n" +
                "this card does not exist in rival rivalHand "
                + "\nso " + "one random card has discarded from your rivalHand");
    }


    private ActivationData handleDisCardCardFromRivalHand(GameData gameData, String declaredCardName){

        ArrayList<Card> rivalHand = gameData.getSecondGamer().getGameBoard().getHand().getCardsInHand();

        for(Card card : (ArrayList<Card> ) rivalHand.clone()){
            if(card.getName().equalsIgnoreCase(declaredCardName)){
                card.handleDestroy(gameData);
            }
        }

        return new ActivationData(this,
                "trap activated successfully" + " and " +
                        "rival discard cards with name " + declaredCardName);
    }

    private boolean isCardInRivalHand(GameData gameData, String declaredCardName){

        ArrayList<Card> rivalHand = gameData.getSecondGamer().getGameBoard().getHand().getCardsInHand();

        for(Card card : rivalHand){
            if(card.getName().equalsIgnoreCase(declaredCardName)){
                return true;
            }
        }
        return false;
    }

}
