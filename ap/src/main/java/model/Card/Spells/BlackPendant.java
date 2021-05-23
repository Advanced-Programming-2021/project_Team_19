package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

public class BlackPendant extends EquipSpell {

    public BlackPendant(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }

    @Override
    public int changeInAttack(GameData gameData) {
        return 500;
    }
}
