package controller.DuelControllers;

import controller.DuelControllers.Actions.*;
import model.Card.Card;

import java.util.ArrayList;

public class CardActionManager {

    private static CardActionManager instance;
    public Card card;
    private boolean shouldSelectCardForMultiCardAction = false;
    private String multiCardActionName;
    private ArrayList<Card> selectedCardsForMultiCardAction = new ArrayList<>();


    public static CardActionManager getInstance(Card card){
        if (instance == null){
            instance = new CardActionManager(card);
        }
        return instance;
    }

    private CardActionManager(Card card) {
        this.card = card;
    }

    public boolean isShouldSelectCardForMultiCardAction() {
        return shouldSelectCardForMultiCardAction;
    }

    public static void destroyCurrentActionManager(){
        instance = null;
    }

    public void setMultiCardAction(String actionName){
        shouldSelectCardForMultiCardAction = true;
        multiCardActionName = actionName;
    }

    public ArrayList<Card> getSelectedCardsForMultiCardAction(){
        return selectedCardsForMultiCardAction;
    }

    public void finishMultiCardAction(){
        shouldSelectCardForMultiCardAction = false;
        multiCardActionName = "";
        selectedCardsForMultiCardAction = new ArrayList<>();
    }

    public String addCardForMultiCardAction(){
        selectedCardsForMultiCardAction.add(GameData.getGameData().getSelectedCard());
        switch (multiCardActionName){
            case "attack monster":{
                new AttackMonster(GameData.getGameData()).run();
                finishMultiCardAction();
                destroyCurrentActionManager();
                return "attack successful";
            }
            default: return "";
        }
    }

    public ArrayList<String> getValidActions() {

        String result;
        ArrayList<String> validActions = new ArrayList<>();

        if (shouldSelectCardForMultiCardAction){
            validActions.add("select");
            return validActions;
        }

        result = new AttackMonster(GameData.getGameData()).actionIsValid();
        if (result.equals("attack monster"))
            validActions.add(result);

        result = new DirectAttack(GameData.getGameData()).actionIsValid();
        if (result.equals("attack direct"))
            validActions.add(result);

        result = new NormalSummon(GameData.getGameData()).actionIsValid();
        if (result.equals("normal summon"))
            validActions.add(result);

        result = new Set(GameData.getGameData()).actionIsValid();
        if (result.equals("set"))
            validActions.add("set");


        result = new SetPosition(GameData.getGameData()).actionIsValid(true);
        if (result.startsWith("set position")) {
            validActions.add(result);
        }

        result = new SetPosition(GameData.getGameData()).actionIsValid(false);
        if (result.startsWith("set position")) {
            validActions.add(result);
        }

        result = new ActivateSpellOrTrapNormally(GameData.getGameData()).actionIsValid();
        if(result.equals("activate spell normally")){
            validActions.add(result);
        }

        result = new ActivateEffectMonster(GameData.getGameData()).actionIsValid();
        if(result.equals("activate effect monster")){
            validActions.add(result);
        }

        return validActions;
    }

}


