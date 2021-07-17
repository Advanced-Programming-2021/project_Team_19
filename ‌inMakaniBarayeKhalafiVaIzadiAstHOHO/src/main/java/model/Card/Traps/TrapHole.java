package model.Card.Traps;

import controller.DuelControllers.Actions.*;
import controller.DuelControllers.GameData;
import controller.TrapCheckers.CardOwnerIsNotActionDoerChecker;
import controller.TrapCheckers.Checker;
import controller.TrapCheckers.TurnChecker;
import controller.TrapCheckers.mirageDragonChecker;
import controller.Utils;
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

        Summon action = (Summon) Utils.getLastActionOfSpecifiedAction
                (gameData.getCurrentActions(), Summon.class);

        int trapIndex = gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getId(this);
        int monsterIndex = gameData.getSecondGamer().getGameBoard().getMonsterCardZone().
                getId(action.getSummoningMonster());

        action.getSummoningMonster().handleDestroy(gameData);
        handleDestroy(gameData);

        handleCommonsForActivate(gameData);

        return new TriggerActivationData
                (false, "activate trap " +
                        trapIndex
                        + ":change turn:trap hole:" +
                        "activate spell " +
                        "-1 destroy rival monsters " + monsterIndex, this);
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