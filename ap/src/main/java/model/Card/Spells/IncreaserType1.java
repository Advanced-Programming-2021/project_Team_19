package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.Undo;
import model.Data.ActivationData;
import model.Enums.MonsterEnums.MonsterType;

import java.util.ArrayList;

public class IncreaserType1 extends Spell implements Undo {
    public ArrayList<MonsterType> monsterTypes = new ArrayList<>();
    public int amountToIncreaseAttack;
    public int amountToIncreaseDefence;

    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    @Override
    public void undo() {

    }
}
