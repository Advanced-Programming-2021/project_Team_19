package model;

import controller.DuelControllers.GameData;
import model.Card.Card;

public class EffectLabel {

    public Card card;
    public Gamer gamer;
    public GameData gameData;

    public EffectLabel(GameData gameData, Gamer gamer, Card card){

        this.card = card;
        this.gameData = gameData;
        this.gamer = gamer;
    }
}
