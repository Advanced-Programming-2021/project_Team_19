package model.Card.Traps;

import controller.DuelControllers.GameData;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.SpellsAndTraps.TrapTypes;
import model.Enums.Status;
import model.Enums.Type;
import model.Phase;

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

    public boolean shouldEffectRun(EffectLabel label) {

        if (label.gameData.getCurrentPhase().equals(Phase.BATTLE)) {
            return label.gameData.getCurrentActions().size() == 0;
        }
        return false;
    }

    public TriggerActivationData runEffect(EffectLabel label) {

        label.gamer.removeLabel(label);

        label.label = 1;

        return new TriggerActivationData(true, "",
                label.card);
    }

    public NegateAttack(String name, String description, int price, Type type, TrapTypes trapType, Status status) {
        super(name, description, price, type, trapType, status);
    }
}
