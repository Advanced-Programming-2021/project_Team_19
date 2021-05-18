package model.Card.Traps;

import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import controller.TrapCheckers.CardControllerIsCurrentGamerChecker;
import controller.TrapCheckers.CardOwnerIsNotActionDoerChecker;
import controller.TrapCheckers.Checker;
import controller.TrapCheckers.TurnChecker;
import model.Card.Card;
import model.Card.Monster;
import model.Card.SpellAndTraps;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Phase;
import view.Printer.Printer;

import java.util.ArrayList;

public class TimeSeal extends Trap {


    private int effectTurn = 0;
    @Override
    public ActivationData activate(GameData gameData)  {

        handleCommonsForActivate(gameData);

        setEffectTurn(gameData);

        handleDestroy(gameData);

        gameData.getCurrentGamer().addEffectLabel
                (new EffectLabel(gameData, gameData.getCurrentGamer(), this));

        return new ActivationData(this, "trap activated successfully");
    }

    private void setEffectTurn(GameData gameData) {
        if(gameData.getCurrentGamer().equals(gameData.getTurnOwner()))
            effectTurn = turnActivated + 1;
        else
            effectTurn = turnActivated + 2;
    }

    public boolean canActivate(GameData gameData) {

        ArrayList<Checker> checkers = new ArrayList<>();

        checkers.add(new TurnChecker(gameData, this));

        if(!Checker.multipleCheck(checkers)){
            return false;
        }

        return true;
    }


    public boolean shouldEffectRun(EffectLabel label){

        if(effectTurn == label.gameData.getTurn()){
            if(label.gameData.getCurrentPhase().equals(Phase.DRAW)){

                return true;
            }
        }

        return false;
    }

    public TriggerActivationData runEffect(EffectLabel label){

        label.gamer.removeLabel(label);

        label.label = 1;

        return new TriggerActivationData
                (true, "you can't draw this turn", label.card);
    }

}
