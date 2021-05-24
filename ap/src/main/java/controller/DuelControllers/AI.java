package controller.DuelControllers;

import model.Enums.GameEvent;
import view.GetInput;

import java.util.Scanner;

public class AI {

    public static void run(GameData gameData){
        if(gameData.getEvent().equals(GameEvent.ASK_FOR_ACTIVATE_TRAP)){
            GetInput.initializeAIScanner(new Scanner("yes"), 1);
            return;
        }
        else if(gameData.getEvent().equals(GameEvent.NORMAL)){
            GetInput.initializeAIScanner(new Scanner("next phase"), 1);
        }
        else if(gameData.getEvent().equals(GameEvent.ACTIVATE_TRAP)){
            GetInput.initializeAIScanner(new Scanner("cancel"), 1);
        } else if (gameData.getEvent().equals(GameEvent.ASK_FOR_CONFIRMATION_FOR_ACTIVATE_ANOTHER_SPELL)){
            GetInput.initializeAIScanner(new Scanner("2"), 1);
        } else if (gameData.getEvent().equals(GameEvent.ASK_FOR_CONFIRMATION_FOR_CHAIN)){
            GetInput.initializeAIScanner(new Scanner("2"), 1);
        } else if (gameData.getEvent().equals(GameEvent.ASK_FOR_SIDE_DECK)){
            GetInput.initializeAIScanner(new Scanner("2"), 1);
        }else if (gameData.getEvent().equals(GameEvent.SACRIFICE_FOR_SUMMON_SET)){
            GetInput.initializeAIScanner(new Scanner("cancel"), 1);
        }
    }
}
