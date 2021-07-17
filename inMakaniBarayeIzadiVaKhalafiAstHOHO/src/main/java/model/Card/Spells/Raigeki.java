package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;

public class Raigeki extends Spell {
    @Override
    public ActivationData activate(GameData gameData) {
        String ids = gameData.getSecondGamer().destroyAllMonstersOnBoard(gameData);

        handleCommonsForActivate(gameData);
        handleDestroy(gameData);
        if (ids.equals("")) {
            return new ActivationData(this, "destroy this spell");
        }
        return new ActivationData(this, "destroy rival monsters " + ids);
    }

    public Raigeki(String name, String description, int price, Type type, SpellTypes spellType, Status status) {
        super(name, description, price, type, spellType, status);
    }

}
