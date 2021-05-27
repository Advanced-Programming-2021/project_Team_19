package controller.ActivateCheckers;

import controller.DuelControllers.GameData;
import model.Card.Card;
import model.Card.Monster;
import model.Card.Monsters.ScannerMonster;
import model.Card.Monsters.ShouldAskForActivateEffectMonster;

import java.util.Scanner;

public class SelectedCardIsOneEffectMonsterForActivateEffectChecker extends ActivationChecker{

    public SelectedCardIsOneEffectMonsterForActivateEffectChecker(GameData gameData, Card card){
        super(gameData, card);
    }

    public String check(){

        if(card instanceof ScannerMonster){
            card = ((ScannerMonster) card).getMonster();
        }

        if (!(card instanceof Monster)) {
            return "card should be a monster";
        }

        if(!(card instanceof ShouldAskForActivateEffectMonster)){
            return "you can't activate this card";
        }
        return null;
    }
}
