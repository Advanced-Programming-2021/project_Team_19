package model.Card.Spells;

import controller.DuelControllers.Actoins.Attack;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Monster;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.ContinuousEffect;
import model.Card.TrapAndSpellTypes.Undo;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.*;
import model.Gamer;
import model.Phase;
import view.Printer.Printer;

import java.awt.*;

public class SwordsOfRevealingLight extends Spell{


    public SwordsOfRevealingLight(String name, String description, int price, Type type,
                                  Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }

    @Override
    public ActivationData activate(GameData gameData) {
        
        handleCommonsForActivate(gameData);
        EffectLabel label = new EffectLabel(gameData, gameData.getCurrentGamer(), this);

        label.effectsOfLabel.add(EffectOfLabel.CAN_NOT_ATTACK);
        label.effectsOfLabel.add(EffectOfLabel.MONSTER_CAN_NOT_BE_HIDDEN);

        gameData.getCurrentGamer().addEffectLabel (label);

        return new ActivationData(this, "");
    }


    @Override
    public boolean shouldEffectRun(EffectLabel label){

        if(shouldCardDestroy(label.gameData)){
            return true;
        }

        if(shouldAttackEffectRun(label)){
            return true;
        }

        if(shouldFlipEffectRun(label)){
            return true;
        }

        return false;
    }

    private boolean shouldFlipEffectRun(EffectLabel label){

        for(Monster monster : label.gameData.getOtherGamer(label.gamer).getGameBoard()
                .getMonsterCardZone().getCards()){
            if(monster != null && monster.getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)){
                return true;
            }
        }
        return false;
    }

    private boolean shouldAttackEffectRun(EffectLabel label){

        Attack attack = (Attack)Utils.getLastActionOfSpecifiedAction
                (label.gameData.getCurrentActions(), Attack.class);

        if(attack != null){
            if(label.gameData.getCardController(attack.getAttackingMonster())
                    .equals(label.gameData.getOtherGamer(label.gamer))){
                return true;
            }
        }
        return false;
    }

    @Override
    public TriggerActivationData runEffect(EffectLabel label){

        if(shouldCardDestroy(label.gameData)){
            handleDestroy(label.gameData);
            return  new TriggerActivationData(false,
                    "the card swords of revealing light of your rival has destroyed",
                    this);
        }

        if(shouldAttackEffectRun(label)){
            return new TriggerActivationData(true,
                    "you can not attack this turn" +
                            " because of the card swords of revealing light of your rival",
                    this);
        }

        if(shouldFlipEffectRun(label)){

            for(Monster monster : label.gameData.getOtherGamer(label.gamer).getGameBoard()
                    .getMonsterCardZone().getCards()){
                if(monster != null && monster.getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)){
                    monster.handleFlip(label.gameData);//izadiiiiiiiiiiiiiiiiiiiiiiii
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

    public boolean shouldCardDestroy(GameData gameData){

        if(turnActivated == gameData.getTurn() + 5 ){
            if(gameData.getCurrentPhase().equals(Phase.END)){
                return true;
            }
        }
        return false;
    }

}
