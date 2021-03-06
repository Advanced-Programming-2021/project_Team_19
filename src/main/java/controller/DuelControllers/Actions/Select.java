package controller.DuelControllers.Actions;

import controller.DuelControllers.GameData;
import controller.Utils;
import model.Card.Card;
import model.Card.Monster;
import model.Enums.CardMod;
import view.Printer.Printer;

import java.util.regex.Matcher;

public class Select extends Action {

    private boolean commandIsDone;

    public static void help() {
        System.out.println("""
                card show --selected
                select --monster <id>
                select --spell <id>
                select --hand <id>
                select --field
                select --monster --opponent <id>
                select --spell --opponent <id>
                select --field --opponent
                select -d""");
    }

    public Select(GameData gameData) {
        super(gameData, "select");
    }

    public void select(String command) {

        commandIsDone = false;
        showSelected(Utils.getMatcher(command, "card show --selected"));
        selectMonster(Utils.getMatcher(command, "select --monster (\\d)"));
        selectOpponentMonster(Utils.getMatcher(command, "select (?=.*?--monster)(?=.*?--opponent)--\\S+ --\\S+ (\\d)"));
        selectSpell(Utils.getMatcher(command, "select --spell (\\d)"));
        selectOpponentSpell(Utils.getMatcher(command, "select (?=.*?--spell)(?=.*?--opponent)--\\S+ --\\S+ (\\d)"));
        selectField(Utils.getMatcher(command, "select --field"));
        selectOpponentField(Utils.getMatcher(command, "select (?=.*?--field)(?=.*?--opponent)--\\S+ --\\S+"));
        selectHand(Utils.getMatcher(command, "select --hand (\\d+)"));
        removeSelect(Utils.getMatcher(command, "select -d"));
        if (!commandIsDone) {
            Printer.print("invalid selection");
        }


    }

    private void showSelected(Matcher matcher) {
        if (matcher.matches()) {
            commandIsDone = true;
            Card selectedCard = gameData.getSelectedCard();
            if (selectedCard == null)
                Printer.print("no card is selected yet");
            else if (gameData.getSecondGamer().getGameBoard().getZone(selectedCard) != null &&
                    gameData.getSecondGamer().getGameBoard().getZone(selectedCard).equals(gameData.getSecondGamer().getGameBoard().getMonsterCardZone()) &&
                    ((Monster) selectedCard).getCardMod().equals(CardMod.DEFENSIVE_HIDDEN))
                Printer.print("card is not visible");
            else
                Printer.print(selectedCard.toString());
        }
    }

    private void selectMonster(Matcher matcher) {
        if (matcher.matches()) {
            int selectIndex = Integer.parseInt(matcher.group(1));
            if (selectIndex <= 5) {
                commandIsDone = true;
                Card selectedCard = gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getCardById(selectIndex);
                if (selectedCard == null) {
                    Printer.print("no card found in the given position");
                } else {
                    gameData.setSelectedCard(selectedCard);
                    printSelectedCard();
                }
            }
        }
    }

    private void selectOpponentMonster(Matcher matcher) {
        if (matcher.matches()) {
            int selectIndex = Integer.parseInt(matcher.group(1));
            if (selectIndex <= 5) {
                commandIsDone = true;
                Card selectedCard = gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getCardById(selectIndex);
                if (selectedCard == null) {
                    Printer.print("no card found in the given position");
                } else {
                    gameData.setSelectedCard(selectedCard);
                    printSelectedCard();
                }
            }
        }
    }

    private void selectSpell(Matcher matcher) {
        if (matcher.matches()) {
            int selectIndex = Integer.parseInt(matcher.group(1));
            if (selectIndex <= 5) {
                commandIsDone = true;
                Card selectedCard = gameData.getCurrentGamer().getGameBoard().
                        getSpellAndTrapCardZone().getCard(selectIndex);
                if (selectedCard == null) {
                    Printer.print("no card found in the given position");
                } else {
                    gameData.setSelectedCard(selectedCard);
                    printSelectedCard();
                }
            }
        }
    }

    private void selectOpponentSpell(Matcher matcher) {
        if (matcher.matches()) {
            int selectIndex = Integer.parseInt(matcher.group(1));
            if (selectIndex <= 5) {
                commandIsDone = true;
                Card selectedCard = gameData.getSecondGamer().getGameBoard().
                        getSpellAndTrapCardZone().getCard(selectIndex);
                if (selectedCard == null) {
                    Printer.print("no card found in the given position");
                } else {
                    gameData.setSelectedCard(selectedCard);
                    printSelectedCard();
                }
            }
        }
    }

    private void selectField(Matcher matcher) {
        if (matcher.matches()) {
            commandIsDone = true;
            Card selectedCard = gameData.getCurrentGamer().getGameBoard().getFieldZone().getCard();
            if (selectedCard == null) {
                Printer.print("no card found in the given position");
            } else {
                gameData.setSelectedCard(selectedCard);
                printSelectedCard();
            }
        }
    }

    private void selectOpponentField(Matcher matcher) {
        if (matcher.matches()) {
            commandIsDone = true;
            gameData.setSelectedCard(gameData.getSecondGamer().getGameBoard().getFieldZone().getCard());
        }
    }

    private void selectHand(Matcher matcher) {
        if (matcher.matches()) {
            int selectIndex = Integer.parseInt(matcher.group(1));
            if (gameData.getCurrentGamer().getGameBoard().getHand().getSize() >= selectIndex) {
                commandIsDone = true;
                gameData.setSelectedCard(gameData.getCurrentGamer().getGameBoard().getHand().getCard(selectIndex));
                printSelectedCard();
            }
        }
    }

    private void removeSelect(Matcher matcher) {
        if (matcher.matches()) {
            commandIsDone = true;
            if (gameData.getSelectedCard() == null) {
                Printer.print("no card is selected yet");
            } else {
                gameData.setSelectedCard(null);
                Printer.print("card deselected");
            }
        }
    }

    private void printSelectedCard() {
        Printer.print("card " + gameData.getSelectedCard().getName() + " selected");
    }
}
