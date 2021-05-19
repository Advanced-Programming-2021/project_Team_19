package model.Card;

import com.google.gson.annotations.Expose;
import controller.DuelControllers.GameData;
import model.Enums.*;
import model.Enums.SpellsAndTraps.SpellTypes;

public abstract class Spell extends SpellAndTraps{

    private SpellTypes spellType;

    @Expose
    private int activationTurn;
    {
        setCardFamily(CardFamily.SPELL);
    }
    public Spell(String name, String description, int price, Type type, Icon icon, Status status){
        super(name,description,price,type, icon, status);
    }
    public SpellTypes getSpellType() {
        return spellType;
    }

    public void setSpellType(SpellTypes spellType) {
        this.spellType = spellType;
    }

    public int getActivationTurn() {
        return activationTurn;
    }

    public void setActivationTurn(int activationTurn) {
        this.activationTurn = activationTurn;
    }

    public boolean canActivate(GameData gameData){return false;}

}
