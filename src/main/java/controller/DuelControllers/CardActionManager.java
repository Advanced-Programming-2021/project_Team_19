package controller.DuelControllers;

import controller.DuelControllers.Actions.*;
import model.Card.Card;

import java.util.ArrayList;

public class CardActionManager {
    public Card card;

    public CardActionManager(Card card) {
        this.card = card;
    }

    public ArrayList<String> getValidActions() {

        String result;
        ArrayList<String> validActions = new ArrayList<>();

        result = new AttackMonster(GameData.getGameData()).actionIsValid();
        if (result == "attack monster")
            validActions.add(result);

        result = new DirectAttack(GameData.getGameData()).actionIsValid();
        if (result == "attack direct")
            validActions.add(result);

        result = new NormalSummon(GameData.getGameData()).actionIsValid();
        if (result == "normal summon")
            validActions.add(result);

        result = new Set(GameData.getGameData()).actionIsValid();
        if (result == "set")
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
