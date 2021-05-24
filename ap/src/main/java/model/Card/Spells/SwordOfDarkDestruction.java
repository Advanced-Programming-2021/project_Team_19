package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;

public class SwordOfDarkDestruction extends EquipSpell {

    public SwordOfDarkDestruction(String name, String description, int price, Type type, SpellTypes spellType, Status status){
        super(name,description,price,type, spellType, status);
    }

    @Override
    public int changeInAttack(GameData gameData) {
        return 400;
    }

    @Override
    public int changeInDefence(GameData gameData) {
        return -200;
    }
}
