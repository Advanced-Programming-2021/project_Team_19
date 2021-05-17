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
    protected int setTurn = 0;
    @Expose
    protected SpellCardMods spellCardMod;
    @Expose
    protected int turnActivated = 0;

    public int getTurnActivated() {
        return turnActivated;
    }

    public void setTurnActivated(int turnActivated) {
        this.turnActivated = turnActivated;
    }

    public void setSetTurn(int turn){
        setTurn = turn;
    }

    public abstract ActivationData activate(GameData gameData);

    public SpellCardMods getSpellCardMod() {
        return spellCardMod;
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

    public void handleCommonsForActivate(GameData gameData){
        setTurnActivated(gameData.getTurn());
        setSpellCardMod(SpellCardMods.OFFENSIVE);
    }



    //test

    public static void changeMode(SpellAndTraps card ,SpellCardMods spellCardMod) {
        card.spellCardMod = spellCardMod;
    }

}
