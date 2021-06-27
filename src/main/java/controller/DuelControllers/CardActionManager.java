package controller.DuelControllers;

import controller.DuelControllers.Actoins.*;
import model.Card.Card;

import javax.naming.ldap.HasControls;
import java.util.ArrayList;
import java.util.HashMap;

public class CardActionManager {
    public Card card;

    public CardActionManager(Card card) {
        this.card = card;
    }

    public String getValidActions(){

        StringBuilder validActions = new StringBuilder();

        validActions.append("attack monster ").append(new AttackMonster(GameData.getGameData()).actionIsValid());
        validActions.append(" attack direct ").append(new DirectAttack(GameData.getGameData()).actionIsValid());
        validActions.append(" normal summon ").append(new NormalSummon(GameData.getGameData()).actionIsValid());
        validActions.append(" set ").append(new Set(GameData.getGameData()).actionIsValid());
        validActions.append(" set position ").append(new SetPosition(GameData.getGameData()).actionIsValid());

        return new String(validActions);

    }
}
