package model.Card.Traps;

import controller.DuelControllers.Actions.Action;
import controller.DuelControllers.Actions.Summon;
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

public class TorrentialTribute extends TrapsActivateBecauseOfActionSummon {

    public TorrentialTribute(String name, String description, int price, Type type, TrapTypes trapType, Status status) {
        super(name, description, price, type, trapType, status);
    }


    @Override
    public ActivationData activate(GameData gameData) {

        int trapIndex = gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getId(this);

        String rivalIds = " rival monsters " + gameData.getSecondGamer().destroyAllMonstersOnBoard(gameData);
        String selfIds = " self monsters " + gameData.getCurrentGamer().destroyAllMonstersOnBoard(gameData);

        handleCommonsForActivate(gameData);
        handleDestroy(gameData);

        Summon action = (Summon) Utils.getLastActionOfSpecifiedAction
                (gameData.getCurrentActions(), Summon.class);

        String changeTurn = "hehe";

        if(!action.getActionDoer().equals(gameData.getCurrentGamer())){
            changeTurn = "change turn";
        }

        TriggerActivationData data = new TriggerActivationData
                (false, "activate trap " +
                        trapIndex
                        + ":" + changeTurn + ":torrential tribute:" +
                        "activate spell -1 " +
                        ("destroy" + rivalIds + selfIds).trim(), this);

        return data;


    }

    public boolean canActivateBecauseOfAnAction(Action action) {

        ArrayList<Checker> checkers = new ArrayList<>();

        checkers.add(new TurnChecker(action.getGameData(), this));
        checkers.add(new mirageDragonChecker(action.getGameData(), this));

        if (!Checker.multipleCheck(checkers)) {
            return false;
        }

        return action instanceof Summon;
    }

}
