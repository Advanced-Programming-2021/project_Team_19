package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Spell;
import model.Card.TrapAndSpellTypes.Destroyer;
import model.Data.ActivationData;
import model.Enums.Icon;
import model.Enums.Status;
import model.Enums.Type;

public class DarkHole extends Spell {

    public DarkHole(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }

    @Override
    public ActivationData activate(GameData gameData) {
        gameData.getSecondGamer().destroyAllMonstersOnBoard(gameData);
        gameData.getCurrentGamer().destroyAllMonstersOnBoard(gameData);

        handleDestroy(gameData);
        return new ActivationData(this, "all monsters were destroyed");
    }

}
