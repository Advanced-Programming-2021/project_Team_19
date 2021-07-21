package controller.DuelControllers;

import controller.DuelControllers.Actions.*;
import model.Card.Card;

import java.util.ArrayList;

public class CardActionManager {

    public Card card;
    public GameData gameData;
    public static actionManagerMode mode = actionManagerMode.NORMAL_MODE;


    public CardActionManager(Card card, GameData gameData) {
        this.card = card;
        this.gameData = gameData;
    }


    public static void setMode(actionManagerMode mode) {
        CardActionManager.mode = mode;
    }

    public ArrayList<String> getValidActions() {
        String result;
        ArrayList<String> validActions = new ArrayList<>();

        System.out.println(mode);

        switch (mode) {
            case NORMAL_MODE -> {
                result = new AttackMonster(gameData, 0).actionIsValid();
                if (result.equals("attack monster"))
                    validActions.add(result);

                result = new DirectAttack(gameData).actionIsValid();
                if (result.equals("attack direct"))
                    validActions.add(result);

                result = new NormalSummon(gameData).actionIsValid();
                if (result.equals("normal summon"))
                    validActions.add(result);

                if (result.matches("get \\d monsters"))
                    validActions.add("summon with sacrifice");


                result = new Set(gameData).actionIsValid();
                if (result.equals("set"))
                    validActions.add("set");

                if (result.matches("get \\d monsters"))
                    validActions.add("set with sacrifice");


                result = new SetPosition(gameData).actionIsValid(true);
                if (result.startsWith("set position")) {
                    validActions.add(result);
                }

                result = new FlipSummon(gameData).actionIsValid();
                if (result.equals("flip summon")) {
                    validActions.add(result);
                }

                result = new SetPosition(gameData).actionIsValid(false);
                if (result.startsWith("set position")) {
                    validActions.add(result);
                }

                result = new ActivateSpellOrTrapNormally(gameData).actionIsValid();
                if (result.equals("activate spell normally")) {
                    validActions.add(result);
                }
                result = new ActivateEffectMonster(gameData).actionIsValid();
                if (result.equals("activate effect monster")) {
                    validActions.add(result);
                }
            }
            case SUMMON_MODE, SET_MODE -> {
                if (NormalSummon.canSacrifice(gameData)) {
                    validActions.add("sacrifice");
                }
            }
            case ATTACK_MONSTER_MODE -> {
                if (AttackMonster.canAttack(gameData)) {
                    validActions.add("attack");
                }
            }
            case ACTION_NOT_ALLOWED_MODE -> {

            }
            case TRIGGER_TRAP_MODE -> {
                if (new ActivateTriggerTrapEffect
                        (gameData.triggerLabel.action).canDoThisActionBecauseOfAnAction()) {
                    validActions.add("activate trap");
                }
            }
        }

        return validActions;
    }

}


