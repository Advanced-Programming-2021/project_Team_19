package controller.DuelControllers;

import model.Board.MonsterCardZone;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardFamily;
import model.User;
import view.Printer.Printer;

public class CheatCodes {

    public static void cheatSheet(){
        Printer.print("""
                increase --money <amount>
                increase --LP <amount>
                duel set-winner <nickname>
                multiply --attack <number>""");
    }

    public static void increaseLifePoint(GameData gameData, String number) {
        gameData.getCurrentGamer().increaseLifePoint(Integer.parseInt(number));
    }

    public static boolean winGame(GameData gameData, String nickName){
        if (gameData.getCurrentGamer().getUser().getNickname().equals(nickName)){
            gameData.changeTurn();
            return true;
        }

        return gameData.getSecondGamer().getUser().getNickname().equals(nickName);
    }

    public static void increaseMoney(User user, String number){
        user.increaseCredit(Integer.parseInt(number));
    }

    public static void multiplyAttack(GameData gameData, String number) {
        Card card = gameData.getSelectedCard();
        if (card.getCardFamily().equals(CardFamily.MONSTER) &&
                gameData.getCurrentGamer().getGameBoard().getZone(card) instanceof MonsterCardZone){
            ((Monster) card).setAttackMultiplier(Integer.parseInt(number));
        }
    }
}
