package model.Card.Spells;

import controller.DuelControllers.Actions.Attack;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Monster;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.CardMod;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;
import model.Phase;

public class SwordsOfRevealingLight extends Spell {


    public SwordsOfRevealingLight(String name, String description, int price, Type type, SpellTypes spellType, Status status) {
        super(name, description, price, type, spellType, status);
    }

    @Override
    public ActivationData activate(GameData gameData) {

        handleCommonsForActivate(gameData);
        EffectLabel label = new EffectLabel(gameData, gameData.getCurrentGamer(), this);

        gameData.getCurrentGamer().addEffectLabel(label);

        return new ActivationData(this, "");
    }


    @Override
    public boolean shouldEffectRun(EffectLabel label) {

        if (shouldCardDestroy(label.gameData)) {
            return true;
        }

        if (shouldAttackEffectRun(label)) {
            return true;
        }

        return shouldFlipEffectRun(label);
    }

    private boolean shouldFlipEffectRun(EffectLabel label) {

        for (Monster monster : label.gameData.getOtherGamer(label.gamer).getGameBoard()
                .getMonsterCardZone().getCards()) {
            if (monster != null && monster.getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)) {
                return true;
            }
        }
        return false;
    }

    private boolean shouldAttackEffectRun(EffectLabel label) {

        Attack attack = (Attack) Utils.getLastActionOfSpecifiedAction
                (label.gameData.getCurrentActions(), Attack.class);

        if (attack != null) {
            return label.gameData.getCardController(attack.getAttackingMonster())
                    .equals(label.gameData.getOtherGamer(label.gamer));
        }
        return false;
    }

    @Override
    public TriggerActivationData runEffect(EffectLabel label) {

        if (shouldCardDestroy(label.gameData)) {
            handleDestroy(label.gameData);
            return new TriggerActivationData(false,
                    "the card swords of revealing light of your rival has destroyed",
                    this);
        }

        if (shouldAttackEffectRun(label)) {
            return new TriggerActivationData(true,
                    "you can not attack this turn" +
                            " because of the card swords of revealing light of your rival",
                    this);
        }

        if (shouldFlipEffectRun(label)) {

            for (Monster monster : label.gameData.getOtherGamer(label.gamer).getGameBoard()
                    .getMonsterCardZone().getCards()) {
                if (monster != null && monster.getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)) {
                    monster.handleFlip(label.gameData, CardMod.DEFENSIVE_OCCUPIED);
                }
            }
            return new TriggerActivationData(true,
                    label.gameData.getOtherGamer(label.gamer).getUsername() +
                            "'s hidden monsters has flipped" +
                            " because of the card swords of revealing light of " +
                            label.gamer.getUsername(),
                    this);

        }


        return new TriggerActivationData(false, "", null);
    }

    public boolean shouldCardDestroy(GameData gameData) {

        if (turnActivated == gameData.getTurn() + 5) {
            return gameData.getCurrentPhase().equals(Phase.END);
        }
        return false;
    }

}
