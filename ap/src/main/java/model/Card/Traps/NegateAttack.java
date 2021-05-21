package model.Card.Traps;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.Actoins.Attack;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import controller.TrapCheckers.CardOwnerIsNotActionDoerChecker;
import controller.TrapCheckers.Checker;
import controller.TrapCheckers.TurnChecker;
import controller.Utils;
import model.Card.Trap;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;
import model.Phase;
import view.Printer.Printer;

import java.util.ArrayList;

public class NegateAttack extends TrapsActivateBecauseOfActionAttack {

    public ActivationData activate(GameData gameData) {

        handleDestroy(gameData);

        turnActivated = gameData.getTurn();

        gameData.getCurrentGamer().addEffectLabel
                (new EffectLabel(gameData, gameData.getCurrentGamer(), this));

        return new TriggerActivationData
                (true,
                        "trap activated successfully\nattack has stopped and battle phase has finished",
                        this);

    }

    public boolean shouldEffectRun(EffectLabel label){

        if(label.gameData.getCurrentPhase().equals(Phase.BATTLE)){
            if(label.gameData.getCurrentActions().size() == 0){
                return true;
            }
        }
        return false;
    }

    public TriggerActivationData runEffect(EffectLabel label){

        label.gamer.removeLabel(label);

        label.label = 1;

        return new TriggerActivationData(true,"",
                label.card);
    }

    public NegateAttack(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }
}
