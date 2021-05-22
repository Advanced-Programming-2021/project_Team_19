package model.Card.Monsters;

import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.Actoins.Activate;
import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.EffectTypes.Exist;
import model.Card.Monster;
import model.Card.Trap;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.CardMod;
import model.Enums.MonsterEnums.Attribute;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.MonsterEnums.MonsterTypesForEffects;
import model.Gamer;

import java.util.ArrayList;

public class MirageDragon extends EffectMonster{


    public MirageDragon(String name, String description, int price, int attack, int defence
            , int level, Attribute attribute, MonsterType monsterType,
                        MonsterTypesForEffects monsterTypesForEffects){

        super(name,description,price,attack,defence,level,attribute,monsterType,monsterTypesForEffects);
    }

    @Override
    public boolean shouldEffectRun(EffectLabel label){

        if(getCardMod().equals(CardMod.DEFENSIVE_HIDDEN)){
            return false;
        }

        ArrayList<Action> actions = label.gameData.getCurrentActions();

        Action action = actions.get(actions.size() - 1);

        if(!(action instanceof Activate)){
            return false;
        }

        Activate activate = (Activate) action;

        if(!(activate.getActivatedCard() instanceof Trap)){
            return false;
        }

        Gamer gamer = label.gameData.getCardController(activate.getActivatedCard());
        if(gamer.equals(activate.getActionDoer())){
            return false;
        }

        return true;
    }

    @Override
    public TriggerActivationData runEffect(EffectLabel label){
        return new TriggerActivationData(true, "", this);
    }

    @Override
    public void handleSummon(GameData gameData, int numberOfSacrifices) {
        super.handleSummon(gameData, numberOfSacrifices);
        gameData.getCurrentGamer().addEffectLabel
                (new EffectLabel(gameData, gameData.getCurrentGamer(), this));
    }
}
