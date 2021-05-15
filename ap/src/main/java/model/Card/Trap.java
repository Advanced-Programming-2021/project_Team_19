package model.Card;

import com.google.gson.annotations.Expose;
import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.GameData;
import model.Enums.CardFamily;
import model.Enums.SpellCardMods;
import model.Enums.SpellsAndTraps.TrapTypes;

public abstract class Trap extends SpellAndTraps {

    @Expose
    TrapTypes trapType;

    {
        setCardFamily(CardFamily.TRAP);
    }

    public TrapTypes getTrapType() {
        return trapType;
    }

    public void setTrapType(TrapTypes trapType) {
        this.trapType = trapType;
    }

    public boolean canActivate(GameData gameData){return false;}


}
