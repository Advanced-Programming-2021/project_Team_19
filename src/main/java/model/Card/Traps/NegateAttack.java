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

        int trapIndex = gameData.getCurrentGamer().getGameBoard().getSpellAndTrapCardZone().getId(this);

        handleDestroy(gameData);

        turnActivated = gameData.getTurn();

        EffectLabel label = new EffectLabel(gameData, gameData.getCurrentGamer(), this);
        label.label = 1;
        gameData.getCurrentGamer().addEffectLabel(label);

        return new TriggerActivationData
                (true, "activate trap " +
                        trapIndex
                        + ":change turn:negate attack:" +
                        "activate spell -1 " + "destroy this spell", this);

    }

    public boolean shouldEffectRun(EffectLabel label) {

        if (label.gameData.getCurrentPhase().equals(Phase.BATTLE)) {
            return label.gameData.getCurrentActions().size() == 1;
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
