package controller.DuelControllers;

import controller.DataBaseControllers.DeckDataBaseController;
import controller.DataBaseControllers.UserDataBaseController;
import controller.Utils;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Enums.GameEvent;
import model.Gamer;
import model.User;
import view.Menu.MainMenu;
import view.Menu.Menu;
import view.Menu.RockPaper;
import view.Printer.Printer;
import view.graphic.GameGraphicController;

public class DuelMenuController extends Menu {

    public static User user;
    public static Gamer gameStarter;
    public static Gamer rivalGamer;
    private Pane pane = new Pane();
    public ChoiceBox numberOfRounds;
    private ChoiceBox rivalChoiceBox;
    private HBox rivalNameField;
    private Label responseLabel = new Label();
    public TextField rivalUserNameTextField = new TextField();
    private static boolean gameIsHappening = false;

    public DuelMenuController() {
        super("Duel Menu");
    }


    public void graphicRun(String username) {
        pane.getStylesheets().add("CSS/Css.css");
        pane.setId("shopBackGround");
        user = UserDataBaseController.getUserByUsername(username);
        setRivalChoosingMenu();

    }

    private void setRivalChoosingMenu() {

        rivalNameField = textFieldGridToEnterInfo("enter user's name:");

        rivalNameField.setLayoutX(200);
        rivalNameField.setLayoutY(250);
        rivalNameField.setVisible(false);

        rivalUserNameTextField = (TextField) ((VBox) rivalNameField.getChildren().get(1)).getChildren().get(0);

        initializeButtons();

        initChoiceBoxes();

        responseLabel.setLayoutX(200);
        responseLabel.setLayoutY(300);

        pane.getChildren().addAll(responseLabel, numberOfRounds, rivalChoiceBox, rivalNameField);

        stage.setTitle("duel menu");
        readyFxmlButtonsForCursor(pane);
        stage.getScene().setRoot(pane);
    }

    private void initializeButtons() {

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> {
            responseLabel.setText("");
            MainMenu.getInstance(null).run();
        });


        Button duelStartButton = new Button("start duel");

        duelStartButton.setOnMouseClicked(event -> {
            if (canStartDuel()) {
                startDuel();
            }
        });

        duelStartButton.setLayoutX(300);
        duelStartButton.setLayoutY(200);


        pane.getChildren().addAll(backButton, duelStartButton);

    }

    private void initChoiceBoxes() {
        numberOfRounds = new ChoiceBox(FXCollections.observableArrayList("1 round", "3 rounds"));

        numberOfRounds.setLayoutX(400);
        numberOfRounds.setLayoutY(100);

        rivalChoiceBox = new ChoiceBox(FXCollections.observableArrayList("duel with AI", "duel with another player"));

        rivalChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            rivalNameField.setVisible(newValue.intValue() == 1);
            rivalUserNameTextField.setText("");
        });

        rivalChoiceBox.setLayoutX(200);
        rivalChoiceBox.setLayoutY(100);

    }

    private void startDuel() {
        if (rivalChoiceBox.getSelectionModel().getSelectedItem().equals("duel with AI")) {
            showPopupMessage("sorry! you cannot duel with AI :)");
        } else {
            startDuelWithAnotherPlayer();
        }
    }

    private boolean userIsOkForDuel(User user, boolean isSelf) {
        if (user.getActiveDeckName() == null) {
            String userName = isSelf ? "you have" : user.getUsername() + " has";
            Printer.setFailureResponseToLabel(responseLabel, userName + " no active deck");
            return false;
        } else if (!DeckDataBaseController.getDeckByName(user.getUsername() + "_" + user.getActiveDeckName()).isDeckValid()) {
            String userName = isSelf ? "your" : user.getUsername() + "’s";
            Printer.setFailureResponseToLabel(responseLabel, userName + " deck is invalid");
            return false;
        }
        return true;
    }

    public boolean canStartDuel() {
        if (!userIsOkForDuel(user, true)) {
            return false;
        }

        if (rivalChoiceBox.getSelectionModel().getSelectedItem() == null) {
            Printer.setFailureResponseToLabel(responseLabel, "select a rival");
            return false;
        }

        if (numberOfRounds.getSelectionModel().getSelectedItem() == null) {
            Printer.setFailureResponseToLabel(responseLabel, "choose a number of rounds");
            return false;
        }

        if (rivalChoiceBox.getSelectionModel().getSelectedItem().equals("duel with AI")) {
            return true;
        } else {
            return canStartDuelWithTwoPlayers();
        }
    }

    private void startDuelWithAi() {

        gameStarter = new Gamer(user);
        rivalGamer = AI.getGamer(0);
//        handleDuel(Integer.parseInt(((String) numberOfRounds.getSelectionModel().getSelectedItem()).substring(0, 1)));

    }

    private boolean canStartDuelWithTwoPlayers() {

        String rivalUserName = rivalUserNameTextField.getText();

        if (rivalUserName.equals("")) {
            Printer.setFailureResponseToLabel(responseLabel, "enter a user's name");
            return false;
        }

        if (rivalUserName.equals(user.getUsername())){
            Printer.setFailureResponseToLabel(responseLabel, "enter another user's name");
            return false;
        }

        User rival = UserDataBaseController.getUserByUsername(rivalUserNameTextField.getText());

        if (rival == null) {
            Printer.setFailureResponseToLabel(responseLabel, "no such user exists");
            return false;
        }

        return userIsOkForDuel(rival, false);
    }

    private void startDuelWithAnotherPlayer() {


    }


    public void handleDuel(int rounds, Stage firstStage, Stage secondStage, boolean isInverted) {
        gameIsHappening = true;
        new GameGraphicController(rounds, firstStage, secondStage, gameStarter, rivalGamer,isInverted).run();
    }

    private void changeDecks() {

        GameData gameData = GameData.getGameData();

        gameData.setEvent(GameEvent.ASK_FOR_SIDE_DECK);

        if (!gameData.getCurrentGamer().equals(gameStarter)) {
            gameData.changeTurn();
        }

        if (Utils.askForConfirmation(gameStarter.getUsername() + ", do you want to swap cards between your main deck and side deck?"))
            new DeckModifierBetweenGames(gameStarter.getUser()).run();

        gameData.changeTurn();

        if (Utils.askForConfirmation(rivalGamer.getUsername() + ", do you want to swap cards between your main deck and side deck?"))
            new DeckModifierBetweenGames(rivalGamer.getUser()).run();


        GameData.getGameData().setEvent(null);
    }

    public static void finishDuel(Gamer winner, GameData gameData, int rounds) {

        Pane gameFinishPane = getGameFinishedPane(winner, gameData);
        stage.getScene().setRoot(gameFinishPane);
        gameIsHappening = false;
        Gamer loser = gameData.getCurrentGamer();
        if (loser.equals(winner))
            loser = gameData.getSecondGamer();

        increaseCreditAndScoreAfterGame(winner, loser, rounds);
    }

    public static Pane getGameFinishedPane(Gamer winner, GameData gameData) {
        StackPane pane = new StackPane();

        Label winnerLabel = new Label(winner.getUsername() + " won the whole match with score: " +
                gameData.getGameStarter().getCurrentScoreInDuel() + " - " +
                gameData.getInvitedGamer().getCurrentScoreInDuel());

        winnerLabel.setTextAlignment(TextAlignment.CENTER);
        winnerLabel.setLayoutY(90);
        winnerLabel.getStylesheets().add("gameFinishLabel");

        Button backButton = new Button();
        setBackButton(backButton);
        backButton.setOnMouseClicked(event -> MainMenu.getInstance(null).run());

        StackPane.setAlignment(backButton, Pos.TOP_LEFT);
        pane.getChildren().addAll(backButton, winnerLabel);

        return pane;

    }

    public static boolean isGameIsHappening() {
        return gameIsHappening;
    }

    public static void increaseCreditAndScoreAfterGame(Gamer winner, Gamer loser, int rounds) {
        winner.increaseCredit(1000 * rounds + winner.getMaxLifePointsInDuel());
        loser.increaseCredit(1000 * rounds);
        winner.increaseUserScore(1000 * rounds);
        UserDataBaseController.saveChanges(winner.getUser());
        UserDataBaseController.saveChanges(loser.getUser());
    }

}
