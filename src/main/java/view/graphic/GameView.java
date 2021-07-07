package view.graphic;

import controller.DataForGameRun;
import controller.DataFromGameRun;
import controller.DuelControllers.Game;
import controller.Utils;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Card.Card;
import model.Card.Monster;
import model.Card.SpellAndTraps;
import model.Data.graphicDataForServerToNotifyOtherClient;
import model.Enums.CardMod;
import model.Enums.SpellCardMods;
import model.Gamer;
import view.graphic.CardViewAnimations.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;

import static view.graphic.ActionsAnimationHandler.*;

public class GameView {

    GameGraphicControllerForTest gameController;
    Gamer self;
    Gamer rival;
    Game game;
    GameView rivalGameView;

    Stage stage;

    Pane mainPane = new Pane();

    Pane gamePane = new Pane();
    BorderPane cardShowPane = new BorderPane();

    CardView selfDeck;
    CardView rivalDeck;

    VBox phaseBox;
    Popup nextPhasePopup;

    ArrayList<CardView> selfHand = new ArrayList<>();
    ArrayList<CardView> selfGraveyardCards = new ArrayList<>();
    StackPane selfGraveYard = new StackPane();

    ArrayList<CardView> rivalHand = new ArrayList<>();
    ArrayList<CardView> rivalGraveyardCards = new ArrayList<>();
    StackPane rivalGraveYard = new StackPane();

    CardView cardForShow = null;
    Text cardDescription = new Text();
    ScrollPane descriptionScrollPane = new ScrollPane();

    ScrollPane cardViewsScrollPane = new ScrollPane();

    Label selfLpLabel = new Label();
    Label rivalLpLabel = new Label();

    int selfLp;
    int rivalLp;

    Label selfUsernameLabel = new Label();
    Label rivalUsernameLabel = new Label();

    HBox selfAtkDefLabels = new HBox(12);
    HBox rivalAtkDefLabels = new HBox(12);


    ArrayList<CardView> monsterZoneCards = new ArrayList<>();
    ArrayList<CardView> spellZoneCards = new ArrayList<>();
    ArrayList<CardView> rivalMonsterZoneCards = new ArrayList<>();
    ArrayList<CardView> rivalSpellZoneCards = new ArrayList<>();

    CardView mainCardForMultiCardAction;
    ArrayList<Integer> idsForMultiCardAction;
    int numberOfNeededCards = 0;

    {
        for (int i = 0; i < 5; i++) {
            monsterZoneCards.add(null);
            spellZoneCards.add(null);
            rivalMonsterZoneCards.add(null);
            rivalSpellZoneCards.add(null);

            selfAtkDefLabels.getChildren().add(getLabelForAtkDef());
            rivalAtkDefLabels.getChildren().add(getLabelForAtkDef());
        }
    }

    public GameView(Stage stage, GameGraphicControllerForTest controller, Gamer self, Gamer rival, Game game) {
        this.stage = stage;
        this.gameController = controller;
        this.self = self;
        this.rival = rival;
        this.game = game;

        setCardShowPane();
        setGamePane();
        setDeck();
        setGraveYard();
        setData();
        setAtkDefLabels();

        HBox box = new HBox(cardShowPane, gamePane);
        mainPane.getChildren().add(box);
        box.setLayoutY((menuGraphic.sceneY - 600) / 2);

        mainPane.getChildren().add(descriptionScrollPane);

        setTestButton();
        setMouseLocationMonitor();
    }

    private void setAtkDefLabels() {
        selfAtkDefLabels.setLayoutX(112.5);
        selfAtkDefLabels.setLayoutY(380);
        rivalAtkDefLabels.setLayoutX(112.5);
        rivalAtkDefLabels.setLayoutY(260);
        gamePane.getChildren().addAll(selfAtkDefLabels, rivalAtkDefLabels);
    }

    private Label getLabelForAtkDef() {
        Label label = new Label();
        label.setMinWidth(70);
        label.setMaxWidth(70);
        label.setAlignment(Pos.CENTER);
        label.getStyleClass().add("atkDef");
        return label;
    }

    public void setRivalGameView(GameView gameView) {
        rivalGameView = gameView;
    }

    private void setGamePane() {
        Image image = new Image("Assets/Field/fie_normal.bmp");
        Rectangle field = new Rectangle(600, 600);
        field.setFill(new ImagePattern(image));
        gamePane.getChildren().add(field);

        addPhaseBox();
        addPhaseChangeButton();
        gamePane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.C) && event.isAltDown() && event.isShiftDown()) {
                Stage cheatSheet = new Stage();
                cheatSheet.setScene(new Scene(getCheatPane(cheatSheet), 100, 100));
                cheatSheet.show();
            }
        });
    }

    private void addPhaseChangeButton() {

        Button phaseButton = new Button("N\nP");
        phaseButton.setLayoutX(0);
        phaseButton.setLayoutY(5);
        phaseButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (game.gameData.getCurrentGamer().equals(self)) {
                    nextPhasePopup = menuGraphic.showPopupMessage(stage, "next phase",
                            stage.getX() + 235,
                            stage.getY() + 20);
                }
            }
        });

        phaseButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (nextPhasePopup != null && nextPhasePopup.isShowing()) {
                    nextPhasePopup.hide();
                }
            }
        });
        phaseButton.setOnMouseClicked(event -> {
            if (game.gameData.getCurrentGamer().equals(self)) {
                ArrayList<DataFromGameRun> events = new ArrayList<>(game.run(new DataForGameRun("next phase", self)));
                gameController.graphicsForEvents(this, events, null, 0);
            }
        });

        gamePane.getChildren().add(phaseButton);
    }


    private void addPhaseBox() {

        phaseBox = new VBox();

        phaseBox.setSpacing(25);

        phaseBox.setLayoutY(87);
        phaseBox.setLayoutX(1);

        phaseBox.getChildren().add(getPhaseLabel(" D\n P"));
        phaseBox.getChildren().add(getPhaseLabel(" S\n P"));
        phaseBox.getChildren().add(getPhaseLabel(" M\n 1"));
        phaseBox.getChildren().add(getPhaseLabel(" B\n P"));
        phaseBox.getChildren().add(getPhaseLabel(" M\n 2"));
        phaseBox.getChildren().add(getPhaseLabel(" E\n P"));

        gamePane.getChildren().add(phaseBox);
    }

    private Label getPhaseLabel(String text) {
        Label phaseLabel = new Label(text);
        if (text.equals(" D\n P")) {
            phaseLabel.getStyleClass().addAll("phaseLabel", "activePhase");
            return phaseLabel;
        }
        phaseLabel.getStyleClass().addAll("phaseLabel", "inactivePhase");
        return phaseLabel;
    }


    void handleChangePhase() {

        runChangePhase();
        gameController.notifyOtherGameViewToDoSomething(this,
                new graphicDataForServerToNotifyOtherClient("change phase", null, -1));

    }

    void handleChangePhaseBecauseOfOtherClientNotification() {
        runChangePhase();
    }

    private void runChangePhase() {
        for (int i = 0; i < 6; i++) {
            if (phaseBox.getChildren().get(i).getStyleClass().contains("activePhase")) {

                phaseBox.getChildren().get(i).getStyleClass().remove("activePhase");
                phaseBox.getChildren().get(i).getStyleClass().add("inactivePhase");
                phaseBox.getChildren().get((i + 1) % 6).getStyleClass().add("activePhase");
                break;
            }
        }
    }

    private Pane getCheatPane(Stage stage) {
        VBox cheatButtonBox = new VBox();

        Pane cheatPane = new Pane();
        Button lpIncreaseButton = new Button("increase LP");
        lpIncreaseButton.setOnMouseClicked(event -> {
            selfLpLabel.setText(String.valueOf(selfLp += 1000));
            stage.close();
        });

        Button winGameButton = new Button("win game");
        winGameButton.setOnMouseClicked(event -> {
            //TODO
            stage.close();
        });

        cheatButtonBox.getChildren().addAll(lpIncreaseButton, winGameButton);

        cheatPane.getChildren().add(cheatButtonBox);

        return cheatPane;
    }


    private void setData() {
        selfLp = 8000;
        rivalLp = 8000;

        cardShowPane.setBottom(getVboxForData(selfLpLabel, selfUsernameLabel, false));
        cardShowPane.setTop(getVboxForData(rivalLpLabel, rivalUsernameLabel, true));
    }

    private VBox getVboxForData(Label lpLabel, Label usernameLabel, boolean isRival) {

        VBox box = new VBox(5);
        box.setCenterShape(true);
        box.setAlignment(Pos.CENTER);

        setLpLabel(lpLabel);
        Gamer gamer = isRival ? rival : self;
        setUsernameLabel(usernameLabel, gamer);

        int usernameIndex = 0;

        if (!isRival) {
            usernameIndex = 1;
        }

        box.getChildren().add(lpLabel);
        box.getChildren().add(usernameIndex, usernameLabel);

        return box;
    }

    private void setUsernameLabel(Label label, Gamer gamer) {
        label.setAlignment(Pos.CENTER);
        label.setMinWidth(50);
        label.setTextAlignment(TextAlignment.CENTER);
        label.getStyleClass().add("username");
        label.setText(gamer.getUsername());
    }


    private void setLpLabel(Label label) {
        label.setAlignment(Pos.CENTER);
        label.setMinWidth(40);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setText(selfLp + "");
        label.getStyleClass().add("LP");
    }

    public void setCardShowPane() {

        cardShowPane.setMaxWidth(200);
        cardShowPane.setMinWidth(200);
        cardShowPane.setMaxHeight(600);
        cardShowPane.setMinHeight(600);

        cardShowPane.setBackground(new Background(new BackgroundFill(Color.rgb(230, 180, 40)
                , new CornerRadii(0),
                new Insets(0, 0, 0, 0))));

        cardForShow = new CardView(2.3);

        cardShowPane.getChildren().add(cardForShow);
        cardForShow.setX(9);
        cardForShow.setY(110);

        descriptionScrollPane.setContent(cardDescription);
        cardDescription.setWrappingWidth(CardView.width / 2.4 - 10);

        descriptionScrollPane.setPannable(true);
        descriptionScrollPane.setMinWidth(100);
        descriptionScrollPane.setMinWidth(100);
        descriptionScrollPane.setMaxHeight(70);
        descriptionScrollPane.setLayoutX(10);
        descriptionScrollPane.setLayoutY(390);

        descriptionScrollPane.setId("scroll");
        descriptionScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        descriptionScrollPane.setStyle("-fx-background-color:transparent;");
        descriptionScrollPane.setPadding(new Insets(4, 0, 4, 4));

    }

    private void setDeck() {
        setSelfDeck();
        setRivalDeck();
    }

    private void setRivalDeck() {
        rivalDeck = new CardView(8);
        rivalDeck.setX(40);
        rivalDeck.setY(92);
        gamePane.getChildren().add(rivalDeck);
    }

    private void setSelfDeck() {
        selfDeck = new CardView(8);
        selfDeck.setX(530);
        selfDeck.setY(425);
        gamePane.getChildren().add(selfDeck);
    }

    private void setGraveYard() {
        gamePane.getChildren().add(selfGraveYard);
        selfGraveYard.setLayoutX(530);
        selfGraveYard.setLayoutY(340);
        gamePane.getChildren().add(rivalGraveYard);
        rivalGraveYard.setLayoutX(43);
        rivalGraveYard.setLayoutY(177);
    }

    void handleAddCardToGraveYardGraphic(Card card, boolean addToSelfGraveYard) {
        runAddCardToGraveYard(card, addToSelfGraveYard);
        gameController.notifyOtherGameViewToDoSomething(this,
                new graphicDataForServerToNotifyOtherClient
                        ("add card to " + (addToSelfGraveYard ? "rival" : "self") + " graveyard",
                                card, -1));
    }

    void handleAddCardToGraveYardGraphicBOOTN(Card card, boolean addToSelfGraveYard) {
        runAddCardToGraveYard(card, addToSelfGraveYard);
    }

    private void runAddCardToGraveYard(Card card, boolean addToSelfGraveYard) {
        if (addToSelfGraveYard) {
            runAddCardToSelfGraveYard(card);
        } else {
            runAddCardToRivalGraveYard(card);
        }
    }

    private void runAddCardToSelfGraveYard(Card card) {
        CardView cardView = getCardForGraveyard(card, true);
        cardView.opacityProperty().set(0);
        selfGraveyardCards.add(cardView);
        selfGraveYard.getChildren().add(cardView);
        new FadeAnimation(cardView, 500, 0, 1).getAnimation().play();
    }

    public void runAddCardToRivalGraveYard(Card card) {
        CardView cardView = getCardForGraveyard(card, false);
        cardView.opacityProperty().set(0);
        rivalGraveyardCards.add(cardView);
        rivalGraveYard.getChildren().add(cardView);
        new FadeAnimation(cardView, 500, 0, 1).getAnimation().play();
    }

    private CardView getCardForGraveyard(Card card, boolean isSelf) {
        CardView cardView = new CardView(card, 9, false, true);

        cardView.setOnMouseClicked(mouseEvent -> showGraveYard(isSelf));

        return cardView;
    }

    private ArrayList<CardView> showCardsInScrollPane(ArrayList<Card> cards, boolean canCloseByRightClick) {

        ArrayList<CardView> cardViews = new ArrayList<>();

        if (canCloseByRightClick) {
            cardViewsScrollPane.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    mainPane.getChildren().remove(cardViewsScrollPane);
                }
            });
        }

        cardViewsScrollPane.setId("cardViewsScrollPane");
        mainPane.getChildren().add(cardViewsScrollPane);

        cardViewsScrollPane.setLayoutX(250);
        cardViewsScrollPane.setLayoutY(200);

        HBox box = new HBox(5);

        for (Card card : cards) {
            CardView temp = new CardView(card, 4, false, true);
            setShowCardOnMouseEntered(temp);
            box.getChildren().add(temp);
            cardViews.add(temp);
        }

        cardViewsScrollPane.setContent(box);
        cardViewsScrollPane.setMaxWidth(450);
        cardViewsScrollPane.setMinWidth(450);
        cardViewsScrollPane.setMinHeight(170);

        return cardViews;
    }

    private void showGraveYard(boolean isSelf) {

        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<CardView> cardViews = isSelf ? selfGraveyardCards : rivalGraveyardCards;
        for (CardView cardView : cardViews) {
            cards.add(cardView.getCard());
        }
        showCardsInScrollPane(cards, true);
    }

    public void run() {
        stage.getScene().setRoot(mainPane);
        stage.show();

        new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                initHand();
            }
        })).play();

//        addCardToSelfGraveYard(controller.Utils.getCardByName("trap hole"));
    }

    public void initHand() {
        ArrayList<Card> cards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            cards.add(self.getGameBoard().getHand().getCardsInHand().get(i));
        }
        handleAddCardsFromDeckToHandGraphic(cards);
        gameController.graphicsForEvents(this, game.run(new DataForGameRun
                ("start game", self)), null, 0);

    }

    void setBooleanForShowActions(ArrayList<CardView> cardViews, boolean bool) {
        for (CardView cardView : cardViews) {
            cardView.setCanShowValidActions(bool);
        }
    }

    double getCardinHandY() {
        return menuGraphic.sceneY - 80;
    }

    double getCardinRivalHandY() {
        return -44;
    }

    double getCardInHandX(CardView cardView) {

        double ans = menuGraphic.sceneX / 2 - 100;
        double index = selfHand.indexOf(cardView);
        double size = ((double) selfHand.size()) / 2;
        double x = (size - index) * CardView.width / 4.8;
        return ans - x;
    }

    double getCardInRivalHandX(CardView cardView) {
        double ans = menuGraphic.sceneX / 2 - 190;
        double index = rivalHand.indexOf(cardView);
        double size = ((double) rivalHand.size()) / 2;
        double x = (size - index) * CardView.width / 4.8;
        return ans + x;
    }

    CardView getCardForHand(Card card) {
        CardView cardView = new CardView(card, 5, false, true);

        cardView.setOnMouseEntered(mouseEvent -> {
            game.gameData.setSelectedCard(card);
            showCard(cardView);
            new Translation(cardView, cardView.getLayoutY() - 15, 150).getAnimation().play();
            if (cardView.canShowValidActions) {
                try {
                    showValidActionForCard(cardView.getFirstValidAction(game, self), cardView);
                } catch (Exception ignored) {
                }
            }

        });

        cardView.setOnMouseExited(mouseEvent -> {
            new Translation(cardView, cardView.getLayoutY(), 150).getAnimation().play();
            Popup popup = cardView.tempPopup;
            if (popup != null) {
                popup.hide();
            }
            if (cardView.filter != null) {
                stage.getScene().removeEventFilter(MouseEvent.MOUSE_MOVED, cardView.filter);
            }
        });

        cardView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                showValidActionForCard(cardView.getNextValidAction(), cardView);
            } else {
                cardOnLeftClick(cardView);
            }
        });

        return cardView;
    }

    void cardOnLeftClick(CardView cardView) {
        ArrayList<DataFromGameRun> dataFromGameRun = new ArrayList<>();
        if (numberOfNeededCards != 0 && cardView.getCurrentAction() != null) {
            if (cardView.getCurrentAction().equals("sacrifice")) {
                idsForMultiCardAction.add(game.gameData.getCurrentGamer().getGameBoard().getMonsterCardZone().getId(cardView.card));
            } else if (cardView.getCurrentAction().equals("attack")) {
                idsForMultiCardAction.add(game.gameData.getSecondGamer().getGameBoard().getMonsterCardZone().getId(cardView.card));
            }

            if (idsForMultiCardAction.size() == numberOfNeededCards) {
                numberOfNeededCards = 0;
                StringBuilder command = new StringBuilder(cardView.getCurrentAction());
                for (Integer integer : idsForMultiCardAction) {
                    command.append(" ").append(integer);
                }
                dataFromGameRun = game.run(new DataForGameRun(String.valueOf(command), self));
                cardView = mainCardForMultiCardAction;
            }
        } else {
            dataFromGameRun = game.run(new DataForGameRun(cardView.getCurrentAction(), self));
        }

        gameController.graphicsForEvents(this, dataFromGameRun, cardView, 0);

        cardView.tempPopup.hide();
    }

    void initForSummonOrSetBySacrifice(int numberOfSacrifices, CardView mainCard) {
        numberOfNeededCards = numberOfSacrifices;
        idsForMultiCardAction = new ArrayList<>();
        mainCardForMultiCardAction = mainCard;
    }

    void initForAttackMonster(CardView mainCard) {
        numberOfNeededCards = 1;
        idsForMultiCardAction = new ArrayList<>();
        mainCardForMultiCardAction = mainCard;
    }

    public static int getIndexById(int id){
        String data = "53124";
        return data.indexOf(id + "");
    }

    public static int getIndexByRivalId(int id){
        String data = "42135";
        return data.indexOf(id + "");
    }


    CardView getCardForRivalHand(Card card) {
        return new CardView(card, 5, true, true);
    }

    public void showCard(CardView cardView) {

        Card card = cardView.getCard();

        if (card == null) {
            return;
        }
        if (rivalMonsterZoneCards.contains(cardView) || rivalSpellZoneCards.contains(cardView)) {
            if (card instanceof Monster) {
                if (CardMod.DEFENSIVE_HIDDEN.equals(((Monster) card).getCardMod())) {
                    return;
                }
            } else if (card instanceof SpellAndTraps) {
                SpellAndTraps spell = (SpellAndTraps) card;
                if (SpellCardMods.HIDDEN.equals(spell.getSpellCardMod())) {
                    return;
                }
            }
        }
        cardForShow.setFill(new ImagePattern(CardView.getImageByCard(card)));
        cardDescription.setText(card.getDescription());
    }

    void setShowCardOnMouseEntered(CardView cardView) {
        cardView.setOnMouseEntered(mouseEvent -> {
            showCard(cardView);
        });
    }

    public void showValidActionForCard(String message, CardView cardView) {

        if (cardView.tempPopup != null) {
            cardView.tempPopup.hide();
        }
        if (cardView.filter != null) {
            stage.getScene().removeEventFilter(MouseEvent.MOUSE_MOVED, cardView.filter);
        }

        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();

        cardView.tempPopup = menuGraphic.showPopupMessage(stage, message, mouseLocation.getX() + 12,
                mouseLocation.getY() - 50);

        EventHandler filter = new EventHandler() {
            @Override
            public void handle(Event event) {
                MouseEvent mouseEvent = (MouseEvent) event;
                cardView.tempPopup.setX(stage.getX() + mouseEvent.getX() + 20);
                cardView.tempPopup.setY(stage.getY() + mouseEvent.getY() - 20);
            }
        };
        cardView.filter = filter;
        stage.getScene().addEventFilter(MouseEvent.MOUSE_MOVED, filter);
    }


    CardView getCardViewForField(Card card, int mode) {
        CardView cardView = new CardView(9);
        if (mode == 0 || mode == 2) {
            cardView = new CardView(card, 9, false, true);
        } else if (mode == 1) {
            cardView = new CardView(card, 9, true, false);
        } else if (mode == 3) {
            cardView = new CardView(card, 9, true, true);
        } else if (mode == 4) {
            cardView = new CardView(card, 9, false, false);
        }
        cardView.setShowCardAndShowValidActions(this);
        return cardView;
    }

    double getCardInFieldX(CardView cardView, int mode) {
        if (mode == 0) {
            int i = monsterZoneCards.indexOf(cardView);
            return 122 + i * 82;
        } else if (mode == 1 || mode == 4) {
            int i = monsterZoneCards.indexOf(cardView);
            return 122 + i * 82 - 10;
        } else if (mode == 2 || mode == 3) {
            int i = spellZoneCards.indexOf(cardView);
            return 122 + i * 82;
        }
        return 0;
    }

    double getCardInRivalFieldX(CardView cardView, int mode) {

        if (mode == 0) {
            int i = rivalMonsterZoneCards.indexOf(cardView);
            return 122 + i * 82;
        } else if (mode == 1 || mode == 4) {
            int i = rivalMonsterZoneCards.indexOf(cardView);
            return 122 + i * 82 - 10;
        } else if (mode == 2 || mode == 3) {
            int i = rivalSpellZoneCards.indexOf(cardView);
            return 122 + i * 82;
        }
        return 0;
    }

    double getCardInFieldY(int mode) {
        if (mode == 0) {
            return 101 + 220;
        } else if (mode == 1 || mode == 4) {
            return 111 + 220;
        } else if (mode == 2 || mode == 3) {
            return 101 + 220 + 110;
        }
        return 0;
    }


    double getCardInRivalFieldY(int mode) {
        if (mode == 0) {
            return 101 + 100;
        } else if (mode == 1 || mode == 4) {
            return 111 + 100;
        } else if (mode == 2 || mode == 3) {
            return 101 + 100 - 100;
        }
        return 0;
    }

    CardView searchCardInRivalHand(Card card) {
        for (CardView cardView : rivalHand) {
            if (cardView.getCard().equals(card)) {
                return cardView;
            }
        }
        return null;
    }

    CardView searchCardInRivalField(Card card) {
        for (CardView cardView : rivalMonsterZoneCards) {
            if (cardView != null && cardView.getCard().equals(card)) {
                return cardView;
            }
        }
        for (CardView cardView : rivalSpellZoneCards) {
            if (cardView != null && cardView.getCard().equals(card)) {
                return cardView;
            }
        }
        return null;
    }

    CardView searchCardInSelfField(Card card) {
        for (CardView cardView : monsterZoneCards) {
            if (cardView != null && cardView.getCard().equals(card)) {
                return cardView;
            }
        }
        for (CardView cardView : spellZoneCards) {
            if (cardView != null && cardView.getCard().equals(card)) {
                return cardView;
            }
        }
        return null;
    }

    double handleSummonGraphic(CardView cardView, int index) {

        gameController.notifyOtherGameViewToDoSomething(this,
                new graphicDataForServerToNotifyOtherClient("summon", cardView.getCard(), 4 - index));
        return runMoveCardFromHandToFieldGraphic
                (this, cardView, 0, 0, index);
    }

    double handleSetMonsterGraphic(CardView cardView, int index) {
        gameController.notifyOtherGameViewToDoSomething(this,
                new graphicDataForServerToNotifyOtherClient("set monster", cardView.getCard(), 4 - index));
        return runMoveCardFromHandToFieldGraphic
                (this, cardView, 1, 0, index);
    }

    double handleSetSpellGraphic(CardView cardView, int index) {
        gameController.notifyOtherGameViewToDoSomething(this,
                new graphicDataForServerToNotifyOtherClient("set spell", cardView.getCard(), 4 - index));
        return runMoveCardFromHandToFieldGraphic
                (this, cardView, 3, 1, index);
    }

    double handleActivateSpellGraphic(CardView cardView, int index) {
        gameController.notifyOtherGameViewToDoSomething(this,
                new graphicDataForServerToNotifyOtherClient("activate spell", cardView.getCard(), 4 - index));
        return runMoveCardFromHandToFieldGraphic
                (this, cardView, 2, 1, index);
    }

    double handleFlipSummonGraphic(CardView cardView) {
        gameController.notifyOtherGameViewToDoSomething(this,
                new graphicDataForServerToNotifyOtherClient("flip summon", cardView.getCard(), -1));
        return runFlipSummonGraphic(this, cardView);
    }

    double handleFlipCardGraphic(CardView cardView) {
        gameController.notifyOtherGameViewToDoSomething(this,
                new graphicDataForServerToNotifyOtherClient("flip", cardView.getCard(), -1));
        return runFlipCardGraphic(cardView);
    }

    double handleIncreaseLpGraphic(int lp, boolean isSelf) {
        gameController.notifyOtherGameViewToDoSomething(this,
                new graphicDataForServerToNotifyOtherClient("increase "
                        + (isSelf ? "rival" : "self") + " lp", null, lp));
        return runIncreaseLpGraphic(this, lp, isSelf);
    }

    double handleAddCardFromDeckToHandGraphic(Card card) {
        CardView cardView = new CardView(card, 8, true, true);
        setBooleanForShowActions(selfHand, false);
        ParallelTransition transition = getTransitionForAddCardFromDeckToHand(this, cardView);
        transition.setOnFinished(EventHandler -> setBooleanForShowActions(selfHand, true));
        transition.play();
        return 500;
    }

    double handleAddCardsFromDeckToHandGraphic(ArrayList<Card> cards) {
        ArrayList<CardView> cardViews = new ArrayList<>();
        for (Card card : cards) {
            cardViews.add(new CardView(
                    card, 8, true, true));
        }
        return runAddCardsToHandFromDeckAnimation(this, cardViews);
    }

    double handleSummonSetWithSacrificeGraphics(CardView cardView, int index, String sacrificeData, boolean isSet) {

        double time = 0;
        for (String indexStr : sacrificeData.split(" ")) {
            time = handleDestroyCardFromFieldOrHand(getIndexById(Integer.parseInt(indexStr)), 0, true);
        }

        EventHandler eventHandler = isSet ? EventHandler -> handleSetMonsterGraphic(cardView, index)
                : EventHandler -> handleSummonGraphic(cardView, index);

        new Timeline(new KeyFrame(Duration.millis(time), eventHandler)).play();

        return time + 800;
    }

    double handleAttackResultGraphic(Matcher matcher) {

        matcher.find();
        int attackerID = getIndexById(Integer.parseInt(matcher.group(1)));
        boolean destroyAttacker = matcher.group(2).equals("destroy");
        int attackedID = getIndexByRivalId(Integer.parseInt(matcher.group(3)));
        boolean destroyAttacked = matcher.group(4).equals("destroy");
        boolean flipAttacked = matcher.group(5).equals("flip ");
        boolean isLpLoserSelf = matcher.group(6).equals("self");
        int lp = -Integer.parseInt(matcher.group(7));

        double time = 10;

        if (flipAttacked) {
            time = handleFlipCardGraphic(rivalMonsterZoneCards.get(4 - attackedID));
        }

        new Timeline(new KeyFrame(Duration.millis(time), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                double time = handleIncreaseLpGraphic(lp, isLpLoserSelf) + 10;

                new Timeline(new KeyFrame(Duration.millis(time), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (destroyAttacker) {
                            handleDestroyCardFromFieldOrHand(attackerID, 0, true);
                        }

                        if (destroyAttacked) {

                            handleDestroyCardFromFieldOrHand(attackedID, 0, false);
                        }
                    }
                })).play();

            }
        })).play();

        return time + lp + 500;
    }

    //zone 0 for monster zone 1 for spell zone 2 for hand

    double handleDestroyCardFromFieldOrHand(int index, int zone, boolean isSelf) {
        double ans = runDestroyCardFromFieldOrHandGraphic(index, zone, isSelf);
        gameController.notifyOtherGameViewToDoSomething(this,
                new graphicDataForServerToNotifyOtherClient("destroy card from" +
                        (isSelf ? ":rival:" : ":self:") +
                        (zone == 0 ? "monster zone" : (zone == 1 ? "spell zone" : "hand"))
                        , null, (zone == 2 ? index : 4 - index)));
        return ans;
    }

    double handleChangePositionGraphic(CardView cardView, String position) {
        gameController.notifyOtherGameViewToDoSomething(this,
                new graphicDataForServerToNotifyOtherClient("set position " + position
                        , cardView.card, -1));
        return runSetPosition(cardView, position);
    }

    double handleChangePositionGraphicBOOCN(Card card, String position) {
        CardView cardView = searchCardInRivalField(card);
        if (cardView == null) {
            cardView = searchCardInSelfField(card);
        }
        return runSetPosition(cardView, position);
    }

    double runSetPosition(CardView cardView, String position) {
        if (position.equals("attack")) {
            new RotateAnimation(cardView, 500, 90).getAnimation().play();
        } else {
            new RotateAnimation(cardView, 500, -90).getAnimation().play();
        }
        return 500;
    }


    double handleDestroyCardFromFieldOrHandBOOCN(int index, int zone, boolean isSelf) {
        return runDestroyCardFromFieldOrHandGraphic(index, zone, isSelf);
    }

    //move to graveyard
    private double runDestroyCardFromFieldOrHandGraphic(int index, int zone, boolean isSelf) {
        CardView cardView = findCardViewForDestroy(index, zone, isSelf);
        if (isSelf) {
            if (zone == 0) {
                monsterZoneCards.remove(cardView);
                Animation animation =
                        new FadeAnimation(cardView, 500, 1, 0).getAnimation();
                animation.setOnFinished(EventHandler -> {
                    gamePane.getChildren().remove(cardView);
                    handleAddCardToGraveYardGraphicBOOTN(cardView.card, true);
                });
                animation.play();
            } else if (zone == 1) {
                spellZoneCards.remove(cardView);
                Animation animation =
                        new FadeAnimation(cardView, 500, 1, 0).getAnimation();
                animation.setOnFinished(EventHandler -> {
                    gamePane.getChildren().remove(cardView);
                    handleAddCardToGraveYardGraphicBOOTN(cardView.card, true);
                });
                animation.play();
            } else {
                runMoveCardFromHandToGraveYardGraphic(this, cardView);
            }
        } else {
            if (zone == 0) {
                rivalMonsterZoneCards.remove(cardView);
                Animation animation =
                        new FadeAnimation(cardView, 500, 1, 0).getAnimation();
                animation.setOnFinished(EventHandler -> {
                    gamePane.getChildren().remove(cardView);
                    handleAddCardToGraveYardGraphicBOOTN(cardView.card, false);
                });
                animation.play();
            } else if (zone == 1) {
                rivalSpellZoneCards.remove(cardView);
                Animation animation =
                        new FadeAnimation(cardView, 500, 1, 0).getAnimation();
                animation.setOnFinished(EventHandler -> {
                    gamePane.getChildren().remove(cardView);
                    handleAddCardToGraveYardGraphicBOOTN(cardView.card, false);
                });
                animation.play();
            } else {
                runRemoveCardFromRivalHandToGraveYardGraphic(this, cardView);
            }
        }
        return 500;
    }

    private CardView findCardViewForDestroy(int index, int zone, boolean isSelf) {

        if (isSelf) {
            if (zone == 0) {
                return monsterZoneCards.get(index);
            } else if (zone == 1) {
                return spellZoneCards.get(index);
            } else {
                return selfHand.get(index);
            }
        } else {
            if (zone == 0) {
                return rivalMonsterZoneCards.get(index);
            } else if (zone == 1) {
                return rivalSpellZoneCards.get(index);
            } else {
                return rivalHand.get(index);
            }
        }
    }

    void handleSummonRivalMonsterGraphicBOOCN(Card card, int index) {
        runMoveRivalCardFromHandToFiledGraphic(this, card, 0, 0, index);
    }

    void handleSetRivalMonsterGraphicBOOCN(Card card, int index) {
        runMoveRivalCardFromHandToFiledGraphic(this, card, 1, 0, index);
    }

    void handleActivateRivalSpellGraphicBOOCN(Card card, int index) {
        runMoveRivalCardFromHandToFiledGraphic(this, card, 2, 1, index);
    }

    void handleSetRivalSpellGraphicBOOCN(Card card, int index) {
        runMoveRivalCardFromHandToFiledGraphic(this, card, 3, 1, index);
    }

    void handleAddRivalCardFromDeckToHandGraphicBOOCN(Card card) {
        addCardToRivalHandFromDeck(this, card);
    }

    void handleRivalFlipSummonGraphicBOOCN(Card card) {
        runRivalFlipSummonGraphic(this, card);
    }

    void handleFlipCardGraphicBOOCN(Card card) {
        CardView cardView = searchCardInRivalField(card);
        if (cardView == null) {
            cardView = searchCardInSelfField(card);
        }
        runFlipCardGraphic(cardView);
    }

    void handleIncreaseLpGraphicBOOCN(int lp, boolean isSelf) {
        runIncreaseLpGraphicBecauseOfRivalNotification(this, lp, isSelf);
    }

    // MindCrush data collector

    private void getCardNameForMindCrush() {
        VBox vBox = new VBox(10);
        vBox.setMinWidth(200);
        vBox.setMaxWidth(200);
        vBox.setLayoutX(200);
        vBox.setLayoutY(270);
        vBox.setCenterShape(true);
        vBox.setAlignment(Pos.CENTER);
        vBox.setBackground(new Background(
                new BackgroundFill(Color.rgb(243, 89, 17), new CornerRadii(0), new Insets(-12, 0, 0, 0))));

        Label messageLabel = new Label("sadfds saldfjl lsadjf ljsdf aslkdjf lasdkjfl jasdf ljsdf lksjdflja l jlsdf");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(150);
        messageLabel.setAlignment(Pos.CENTER);

        TextField textField = new TextField();
        textField.setMaxWidth(150);
        vBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().getName().equals("Enter")) {
                    String data = textField.getText();
                    String result = sendMindCrushDataToServer(data);
                    if (result.equals("invalid card name")) {
                        showPopupForMindCrush("invalid card name");
                    } else {
                        gamePane.getChildren().remove(vBox);
                        showPopupForMindCrush("hehe");
                        //TODO : do what should to do
                    }
                    System.out.println(textField.getText());
                }
            }
        });

        vBox.getChildren().addAll(messageLabel, textField);
        gamePane.getChildren().add(vBox);
    }

    private void showPopupForMindCrush(String message) {
        Popup popup = menuGraphic.showPopupMessage
                (stage, message, stage.getX() + 200 + 220,
                        stage.getY() + 400);
        popup.setAutoHide(true);
    }

    private String sendMindCrushDataToServer(String data) {
        String ans = " name";
        //TODO
        return ans;
    }

    //end Mind Crush
    static int counter = 0;
    public Button testButton = new Button("test");

    private void f() {

//
//        handleAddCardToGraveYardGraphic
//                (controller.Utils.getCardByName("Battle ox"), false);
//        ArrayList<Card>cards = new ArrayList<>();
//        cards.add(Utils.getCardByName("trap hole"));
//        cards.add(Utils.getCardByName("trap hole"));
//        cards.add(Utils.getCardByName("trap hole"));
//        handleAddCardsFromDeckToHandGraphic(cards);
//        getCardNameForMindCrush();
//        runMoveCardFromHandToFieldGraphic(
//                this, selfHand.get(0), 3,1,2);

//        addCardToSelfGraveYard(controller.Utils.getCardByName("Battle ox"));
//        runRemoveCardFromHandGraphic(this, selfHand.get(0));
//        fadeCard(selfHand.get(0));

//        handleIncreaseLpGraphic(123, true);
//
        counter++;
//        handleAddCardToGraveYardGraphic(controller.Utils.getCardByName("Battle ox"), true);
//        if(counter == 1)
//        handleActivateSpellGraphic(selfHand.get(0), 0);
//        else
//            handleFlipCardGraphic(rivalSpellZoneCards.get(4));
    }

    private void setTestButton() {
        testButton.setLayoutX(100);
        testButton.setLayoutY(100);
        mainPane.getChildren().add(testButton);
        testButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                f();
            }
        });
    }

    private void setMouseLocationMonitor() {
        final Label reporter = new Label(OUTSIDE_TEXT);
        Label monitored = createMonitoredLabel(reporter);

        VBox layout = new VBox(10);
        layout.setLayoutX(200);
        layout.setLayoutY(605);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10px;");
        layout.getChildren().setAll(
                monitored,
                reporter
        );
        layout.setPrefWidth(500);
        mainPane.getChildren().add(layout);
    }

    private static final String OUTSIDE_TEXT = "Outside Label";

    private Label createMonitoredLabel(final Label reporter) {
        final Label monitored = new Label("Mouse Location Monitor");

        monitored.setStyle("-fx-background-color: forestgreen; -fx-text-fill: white; -fx-font-size: 20px;");

        stage.getScene().setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String msg =
                        "(x: " + event.getX() + ", y: " + event.getY() + ") -- " +
                                "(sceneX: " + event.getSceneX() + ", sceneY: " + event.getSceneY() + ") -- " +
                                "(screenX: " + event.getScreenX() + ", screenY: " + event.getScreenY() + ")";

                reporter.setText(msg);
            }
        });


        return monitored;
    }

}
