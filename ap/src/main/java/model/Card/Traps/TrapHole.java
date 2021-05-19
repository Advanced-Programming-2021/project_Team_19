package model.Card.Traps;

import controller.DuelControllers.Actoins.*;
import controller.DuelControllers.GameData;
import controller.TrapCheckers.CardOwnerIsNotActionDoerChecker;
import controller.TrapCheckers.Checker;
import controller.TrapCheckers.TurnChecker;
import controller.Utils;
import model.Card.Monster;
import model.Card.TrapAndSpellTypes.Normal;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;
import view.Printer.Printer;

import java.util.ArrayList;

public class TrapHole extends Normal {

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


        return Checker.multipleCheck(checkers) &&
                (action instanceof FlipSummon || action instanceof NormalSummon) &&
                !(((Monster)((Summon) action).getSummoningMonster()).getAttack(action.getGameData()) >= 1000);

    }

    public TrapHole(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }

}