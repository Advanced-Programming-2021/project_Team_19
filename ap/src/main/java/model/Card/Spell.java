package model.Card;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import controller.DuelControllers.GameData;
import model.Enums.*;
import model.Enums.SpellsAndTraps.SpellTypes;

public abstract class Spell extends SpellAndTraps{

    @SerializedName("Icon")
    public SpellTypes spellType;

    @Expose
    private int activationTurn;

    public Spell(String name, String description, int price, Type type, SpellTypes spellType, Status status){
        super(name,description,price,type,status);

        setSpellType(spellType);
        setCardFamily(CardFamily.SPELL);
        setEffectSpeed(1);
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

    public boolean canActivate(GameData gameData){
        return true;
    }

}
