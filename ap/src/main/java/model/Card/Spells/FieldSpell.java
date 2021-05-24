package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Enums.MonsterEnums.MonsterType;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;

import java.util.HashMap;

public class FieldSpell extends Spell {

    static private HashMap<String, HashMap<MonsterType, Integer[]>> spellToProperties = new HashMap<>();

    static {
        HashMap<MonsterType, Integer[]> forest = new HashMap<>();
        HashMap<MonsterType, Integer[]> yami = new HashMap<>();
        HashMap<MonsterType, Integer[]> umiiRuka = new HashMap<>();

        forest.put(MonsterType.INSECT, new Integer[]{200, 200});
        forest.put(MonsterType.BEAST, new Integer[]{200, 200});
        forest.put(MonsterType.BEAST_WARRIOR, new Integer[]{200, 200});

        yami.put(MonsterType.SPELLCASTER, new Integer[]{200, 200});
        yami.put(MonsterType.FIEND, new Integer[]{200, 200});
        yami.put(MonsterType.FAIRY, new Integer[]{-200, -200});

        umiiRuka.put(MonsterType.AQUA, new Integer[]{500, -400});


        spellToProperties.put("Forest", forest);
        spellToProperties.put("Yami", yami);
        spellToProperties.put("Umii Ruka", umiiRuka);
    }




    @Override
    public ActivationData activate(GameData gameData) {
        return null;
    }

    protected HashMap<MonsterType, Integer[]> typesAndAmountToChangeAttackAndDefence = spellToProperties.get(getName());

    public int attackDifference(MonsterType monsterType, GameData gameData){
        if (!typesAndAmountToChangeAttackAndDefence.containsKey(monsterType)){
            return 0;
        }
        return typesAndAmountToChangeAttackAndDefence.get(monsterType)[0];

    }

    public int defenceDifference(MonsterType monsterType){
        if (!typesAndAmountToChangeAttackAndDefence.containsKey(monsterType)){
            return 0;
        }
        return typesAndAmountToChangeAttackAndDefence.get(monsterType)[1];

    }

    public FieldSpell(String name, String description, int price, Type type, SpellTypes spellType, Status status){
        super(name,description,price,type, spellType, status);
    }
}
