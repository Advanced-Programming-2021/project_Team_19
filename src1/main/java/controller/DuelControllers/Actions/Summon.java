package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import model.Card.Card;

public abstract class Summon extends SummonAndSet {


    protected Card summoningMonster;


    protected Summon(GameData gameData, String actionName) {
        super(gameData, actionName);
        setSummoningMonster();
    }

    public Card getSummoningMonster() {
        return summoningMonster;
    }

    protected void setSummoningMonster() {
        summoningMonster = gameData.getSelectedCard();
    }

}
