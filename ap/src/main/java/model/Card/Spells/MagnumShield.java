package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.Undo;
import model.Data.ActivationData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

public class MagnumShield extends EquipSpell implements Undo {

    @Override
    public void undo() {

    }

    public MagnumShield(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }

    @Override
    public int changeInAttack(GameData gameData) {
        return super.changeInAttack(gameData);
    }

    @Override
    public int changeInDefence(GameData gameData) {
        return super.changeInDefence(gameData);
    }

}
