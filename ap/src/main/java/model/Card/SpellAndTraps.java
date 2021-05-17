package model.Card;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.Game;
import controller.DuelControllers.GameData;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.Icon;
import model.Enums.SpellCardMods;
import model.Phase;

public abstract class SpellAndTraps extends Card {

    @SerializedName("Icon")
    public Icon icon;

    @Expose
    int setTurn = 0;
    @Expose
    private SpellCardMods spellCardMod;
    @Expose
    protected int turnActivated = 0;
    @Expose
    public Card cardThatStoppedActivating;

    public int getTurnActivated() {
        return turnActivated;
    }

    public void setTurnActivated(int turnActivated) {
        this.turnActivated = turnActivated;
    }

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

    public static boolean shouldEffectRun(EffectLabel label){
        return false;
    }

    public static TriggerActivationData runEffect(EffectLabel label){
        return null;
    }

}
