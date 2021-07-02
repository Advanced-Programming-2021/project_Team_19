package controller.DuelControllers;

import controller.DuelControllers.Actions.Select;
import model.Phase;

public class GameHelp {

    public static void run(Phase phaseName) {
        switch (phaseName) {
            case BATTLE -> {
                System.out.println("""
                        attack direct
                        attack <id>""");
                Select.help();
            }
            case MAIN1, MAIN2 -> {
                System.out.println("""
                        summon
                        set
                        set --position attack|defence
                        flip-summon""");
                Select.help();
            }
        }
        System.out.println("""
                show hand
                surrender
                help
                next phase
                show board
                show graveyard
                show AD
                activate""");
    }

}
