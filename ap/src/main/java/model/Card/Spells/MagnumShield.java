package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Enums.CardMod;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;

public class MagnumShield extends EquipSpell {


    public MagnumShield(String name, String description, int price, Type type, SpellTypes spellType, Status status) {
        super(name, description, price, type, spellType, status);
    }

    @Override
    public int changeInAttack(GameData gameData) {
        if (monsterEquippedWithThisSpell.getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED)) {
            return monsterEquippedWithThisSpell.defence;
        }
        return 0;
    }

    @Override
    public int changeInDefence(GameData gameData) {
        if (!monsterEquippedWithThisSpell.getCardMod().equals(CardMod.OFFENSIVE_OCCUPIED)) {
            return monsterEquippedWithThisSpell.attack;
        }
        return 0;
    }

}
