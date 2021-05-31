package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.Actoins.FlipSummon;
import controller.DuelControllers.Actoins.NormalSummon;
import controller.DuelControllers.Actoins.Summon;
import controller.DuelControllers.GameData;
import controller.TrapCheckers.CardOwnerIsNotActionDoerChecker;
import controller.TrapCheckers.Checker;
import controller.TrapCheckers.TurnChecker;
import controller.TrapCheckers.mirageDragonChecker;
import model.Card.Monster;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;

import java.util.ArrayList;

public class TrapHole extends TrapsActivateBecauseOfActionSummon {


    public TrapHole(String name, String description, int price, Type type, TrapTypes trapType, Status status) {
        super(name, description, price, type, trapType, status);
    }

    public ActivationData activate(GameData gameData) {

        Action action = gameData.getCurrentActions().get(
                (gameData.getCurrentActions().size() - 1));

        ((Summon) action).getSummoningMonster().handleDestroy(gameData);

        handleDestroy(gameData);

        handleCommonsForActivate(gameData);

        return new TriggerActivationData
                (false, "trap activate successfully", this);
    }

    public boolean canActivateBecauseOfAnAction(Action action) {

        ArrayList<Checker> checkers = new ArrayList<>();

        checkers.add(new TurnChecker(action.getGameData(), this));
        checkers.add(new CardOwnerIsNotActionDoerChecker(action, this));
        checkers.add(new mirageDragonChecker(action.getGameData(), this));

        if (!Checker.multipleCheck(checkers)) {
            return false;
        }


        if (!(action instanceof FlipSummon || action instanceof NormalSummon)) {
            return false;
        }


        return ((Monster) ((Summon) action).getSummoningMonster()).getAttack(action.getGameData()) >= 1000;
    }


}