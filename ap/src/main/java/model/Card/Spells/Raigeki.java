package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

public class Raigeki extends Spell {
    @Override
    public ActivationData activate(GameData gameData) {
       gameData.getSecondGamer().destroyAllMonstersOnBoard(gameData);

       handleCommonsForActivate(gameData);
        handleDestroy(gameData);
        return new ActivationData(this, "all enemy monsters were destroyed");
    }

    public Raigeki(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }

}
