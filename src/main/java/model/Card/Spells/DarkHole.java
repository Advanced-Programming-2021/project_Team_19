package model.Card.Spells;

import controller.DuelControllers.GameData;
import model.Card.Spell;
import model.Data.ActivationData;
import model.Enums.SpellsAndTraps.SpellTypes;
import model.Enums.Status;
import model.Enums.Type;

public class DarkHole extends Spell {

    public DarkHole(String name, String description, int price, Type type, SpellTypes spellType, Status status) {
        super(name, description, price, type, spellType, status);
    }

    @Override
    public ActivationData activate(GameData gameData) {
        String rivalIds = " rival monsters " + gameData.getSecondGamer().destroyAllMonstersOnBoard(gameData);
        String selfIds = " self monsters " + gameData.getCurrentGamer().destroyAllMonstersOnBoard(gameData);
        handleCommonsForActivate(gameData);
        handleDestroy(gameData);

        return new ActivationData(this, ("destroy" + rivalIds + selfIds).trim());
    }

}
