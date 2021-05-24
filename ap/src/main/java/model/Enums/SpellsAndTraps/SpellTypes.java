package model.Enums.SpellsAndTraps;

import com.google.gson.annotations.SerializedName;

public enum SpellTypes {
    @SerializedName("Normal")
    NORMAL,
    @SerializedName("Continuous")
    CONTINUOUS,
    @SerializedName("Quick-play")
    QUICK_PLAY,
    @SerializedName("Field")
    FIELD,
    @SerializedName("Equip")
    EQUIP,
    @SerializedName("Ritual")
    RITUAL,

}
