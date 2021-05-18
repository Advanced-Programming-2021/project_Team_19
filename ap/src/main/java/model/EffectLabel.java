package model;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.SpellAndTraps;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import view.Printer.Printer;

public class EffectLabel {

    public int label = 0;
    public Card card;
    public Gamer gamer;
    public GameData gameData;

    public EffectLabel(GameData gameData, Gamer gamer, Card card){

        this.card = card;
        this.gameData = gameData;
        this.gamer = gamer;
    }

    public boolean checkLabel(){

        return ((SpellAndTraps)card).shouldEffectRun(this);
    }

    public ActivationData runEffect(){
        return ((SpellAndTraps)card).runEffect(this);
    }

}
