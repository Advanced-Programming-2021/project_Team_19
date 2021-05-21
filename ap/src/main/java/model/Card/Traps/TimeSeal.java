package model.Card.Traps;

import controller.DuelControllers.GameData;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;
import model.Phase;

public class TimeSeal extends SpeedEffectTrap {

    public TimeSeal(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }

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
