package model.Card;

import com.google.gson.annotations.Expose;
import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.GameData;
import model.Data.ActivationData;
import model.Enums.SpellCardMods;

public abstract class SpellAndTraps extends Card {

    @Expose
    int setTurn = 0;
    @Expose
    private SpellCardMods spellCardMod;
    @Expose
    public boolean wasActivated = false;
    @Expose
    public Card cardThatStoppedActivating;

    public abstract ActivationData activate(GameData gameData);

    public SpellCardMods getSpellCardMod() {
        return spellCardMod;
    }

    public void stopActivate(Card card){
        cardThatStoppedActivating = card;
    }

    protected void setSpellCardMod(SpellCardMods spellCardMod) {
        this.spellCardMod = spellCardMod;
    }

    public abstract boolean canActivate(GameData gameData);

    public boolean handleSet(GameData gameData){
        setSpellCardMod(SpellCardMods.HIDDEN);
        setTurn = gameData.getTurn();
        return true;
    }

    public boolean canActivateThisTurn(GameData gameData){

        if(setTurn == gameData.getTurn()){
            return false;
        }
        return true;
    }

    public boolean canActivateBecauseOfAnAction(Action action){
        return false;
    }

}
