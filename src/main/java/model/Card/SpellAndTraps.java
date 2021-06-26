package model.Card;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import controller.DuelControllers.Actoins.Action;
import controller.DuelControllers.GameData;
import controller.Utils;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import model.Data.ActivationData;
import model.Data.TriggerActivationData;
import model.EffectLabel;
import model.Enums.SpellCardMods;
import model.Enums.Status;
import model.Enums.Type;

public abstract class SpellAndTraps extends Card {

    @SerializedName("Status")
    public Status status;
    @SerializedName("Type")
    public Type type;

    @Expose
    protected int setTurn = 0;
    @Expose
    protected SpellCardMods spellCardMod;
    @Expose
    protected int turnActivated = 0;

    public SpellAndTraps(String name, String description, int price, Type type, Status status) {
        super(name, description, price);
        this.type = type;
        this.status = status;
    }

    public int getTurnActivated() {
        return turnActivated;
    }

    public void setTurnActivated(int turnActivated) {
        this.turnActivated = turnActivated;
    }

    public void setSetTurn(int turn) {
        setTurn = turn;
    }

    public abstract ActivationData activate(GameData gameData);

    public SpellCardMods getSpellCardMod() {
        return spellCardMod;
    }

    public void setSpellCardMod(SpellCardMods spellCardMod) {
        this.spellCardMod = spellCardMod;
    }

    public abstract boolean canActivate(GameData gameData);

    public boolean handleSet(GameData gameData) {
        setSpellCardMod(SpellCardMods.HIDDEN);
        setTurn = gameData.getTurn();
        return true;
    }

    public boolean canActivateThisTurn(GameData gameData) {

        return setTurn != gameData.getTurn();
    }

    public boolean canActivateBecauseOfAnAction(Action action) {
        return false;
    }

    public boolean shouldEffectRun(EffectLabel label) {
        return false;
    }

    public TriggerActivationData runEffect(EffectLabel label) {
        return null;
    }

    public void handleCommonsForActivate(GameData gameData) {
        setTurnActivated(gameData.getTurn());
        setSpellCardMod(SpellCardMods.OFFENSIVE);
    }


    //test

    public static void changeMode(SpellAndTraps card, SpellCardMods spellCardMod) {
        card.spellCardMod = spellCardMod;
    }

}