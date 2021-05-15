package model.Data;

import model.Card.Card;
import model.Enums.EffectLabels;

import java.util.ArrayList;

public class ActivationData {

    public Card activatedCard;

    public ArrayList<EffectLabels> labels = new ArrayList<>();

    public ActivationData(Card activatedCard){
        this.activatedCard = activatedCard;
    }

}
