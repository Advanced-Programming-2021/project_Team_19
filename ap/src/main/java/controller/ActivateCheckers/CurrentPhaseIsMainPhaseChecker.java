package controller.ActivateCheckers;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Phase;

public class CurrentPhaseIsMainPhaseChecker extends ActivationChecker {
    
    public CurrentPhaseIsMainPhaseChecker(GameData gameData, Card card){
        super(gameData, card);
    }

    public String check(){
        if(gameData.getCurrentPhase().equals(Phase.MAIN1) ||
                gameData.getCurrentPhase().equals(Phase.MAIN2)){
            return "you can’t activate an effect on this phase";
        }
        return null;
    }
}
