package model.Data;

import model.Card.Card;

public class TriggerActivationData extends ActivationData {

    public boolean hasActionStopped;

    public TriggerActivationData(boolean hasActionStopped, String message,
                                 Card activatedCard){

        super(activatedCard, message);
        this.hasActionStopped = hasActionStopped;
    }

}