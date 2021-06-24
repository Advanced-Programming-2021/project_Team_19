package model;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monsters.EffectMonster;
import model.Card.SpellAndTraps;
import model.Data.TriggerActivationData;

public class EffectLabel {

    public int label = 0;
    public Card card;
    public Gamer gamer;
    public GameData gameData;

    public EffectLabel(GameData gameData, Gamer gamer, Card card) {

        this.card = card;
        this.gameData = gameData;
        this.gamer = gamer;
    }

    public boolean checkLabel() {

        if (card instanceof EffectMonster) {
            return ((EffectMonster) card).shouldEffectRun(this);
        }
        return ((SpellAndTraps) card).shouldEffectRun(this);
    }

    public TriggerActivationData runEffect() {

        if (card instanceof EffectMonster) {
            return ((EffectMonster) card).runEffect(this);
        }
        return ((SpellAndTraps) card).runEffect(this);
    }

}
