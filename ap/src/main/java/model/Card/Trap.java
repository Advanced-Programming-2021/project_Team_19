package model.Card;

import com.google.gson.annotations.Expose;
import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.GameData;
import model.Enums.*;
import model.Enums.SpellsAndTraps.TrapTypes;

public abstract class Trap extends SpellAndTraps {

    @Expose
    private TrapTypes trapType;

    public Trap(String name, String description, int price, Type type, TrapTypes trapType, Status status){
        super(name,description,price,type, status);

        setTrapType(trapType);
        setCardFamily(CardFamily.TRAP);
        setEffectSpeed(2);
    }

    public TrapTypes getTrapType() {
        return trapType;
    }

    public void setTrapType(TrapTypes trapType) {
        this.trapType = trapType;
    }

    public boolean canActivate(GameData gameData){
        return false;
    }

}
