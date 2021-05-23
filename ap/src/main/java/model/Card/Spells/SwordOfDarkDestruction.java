package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

public class SwordOfDarkDestruction extends EquipSpell {

    public SwordOfDarkDestruction(String name, String description,
                                  int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
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
