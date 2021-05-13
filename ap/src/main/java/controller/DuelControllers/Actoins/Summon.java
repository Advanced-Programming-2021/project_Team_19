package controller.DuelControllers.Actoins;

import controller.DuelControllers.GameData;
import model.Card.Monster;
import model.Gamer;

public abstract class Summon extends Action{


    protected Monster summoningMonster;


    protected Summon(GameData gameData, String actionName){
        super(gameData, actionName);
        setSummoningMonster();
    }

    public Monster getSummoningMonster(){
        return summoningMonster;
    }

    private void setSummoningMonster(){
        summoningMonster = (Monster) gameData.getSelectedCard();
    }

}
