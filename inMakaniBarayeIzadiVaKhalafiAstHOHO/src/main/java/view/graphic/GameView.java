package view.graphic;

import controller.DataForGameRun;
import controller.DataFromGameRun;
import controller.DuelControllers.CardActionManager;
import controller.DuelControllers.Game;
import controller.DuelControllers.actionManagerMode;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
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
import model.Enums.CardMod;
import model.Enums.SpellCardMods;
import model.Gamer;
import view.graphic.CardViewAnimations.FadeAnimation;
import view.graphic.CardViewAnimations.RotateAnimation;
import view.graphic.CardViewAnimations.Translation;

import java.awt.*;
import java.util.ArrayList;
import java.util.regex.Matcher;

import static view.graphic.ActionsAnimationHandler.*;

public class GameView {

    GameGraphicController gameController;
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
    ArrayList<String> phases = new ArrayList<>();

    {
        for (int i = 0; i < 5; i++) {
            monsterZoneCards.add(null);
            spellZoneCards.add(null);
            rivalMonsterZoneCards.add(null);
            rivalSpellZoneCards.add(null);

            selfAtkDefLabels.getChildren().add(getLabelForAtkDef());
            rivalAtkDefLabels.getChildren().add(getLabelForAtkDef());
        }
        phases.add("draw phase");
        phases.add("standby phase");
        phases.add("main phase");
        phases.add("battle phase");
        phases.add("main phase");
        phases.add("end phase");
    }

    public GameView(Stage stage, GameGraphicController controller, Gamer self, Gamer rival, Game game) {
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
            if (event.getCode().equals(KeyCode.C) && event.isControlDown() && event.isShiftDown()) {
                Stage cheatSheet = new Stage();
                cheatSheet.setScene(new Scene(getCheatPane(cheatSheet), 100, 100));
                cheatSheet.show();
            } else if (event.getCode().equals(KeyCode.A) && event.isAltDown()){
                if (game.gameData.getCurrentGamer().equals(self)) {
                    ArrayList<DataFromGameRun> events = new ArrayList<>(game.run(new DataForGameRun("next phase", self)));
                    gameController.graphicsForEvents(events, null, 0);
                }
            }
        });
    }

    private void addPhaseChangeButton() {

        Button phaseButton = new Button("N\nP");
        phaseButton.setLayoutX(0);
        phaseButton.setLayoutY(5);
        phaseButton.setOnMouseEntered(mouseEvent -> {
            if (game.gameData.getCurrentGamer().equals(self)) {
                nextPhasePopup = menuGraphic.showPopupMessage(stage, "next phase",
                        stage.getX() + 235,
                        stage.getY() + 20);
            }
        });

        phaseButton.setOnMouseExited(mouseEvent -> {
            if (nextPhasePopup != null && nextPhasePopup.isShowing()) {
                nextPhasePopup.hide();
            }
        });
        phaseButton.setOnMouseClicked(event -> {
            if (game.gameData.getCurrentGamer().equals(self)) {
                ArrayList<DataFromGameRun> events = new ArrayList<>(game.run(new DataForGameRun("next phase", self)));
                gameController.graphicsForEvents(events, null, 0);
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
        TextField cheatTextField = new TextField();

        Button submit = new Button("submit");
        submit.setOnMouseClicked(event -> {
            ArrayList<DataFromGameRun> data = game.run(new DataForGameRun("game button " + cheatTextField.getText(), self));
            gameController.graphicsForEvents(data, null, 0);
            stage.close();
        });

        cheatButtonBox.getChildren().addAll(cheatTextField, submit);

        cheatPane.getChildren().add(cheatButtonBox);

        return cheatPane;
    }


    private void setData() {
        selfLp = 4000;
        rivalLp = 4000;

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

        cardView.setOnMouseClicked(mouseEvent -> {
            if (!mainPane.getChildren().contains(cardViewsScrollPane)) {
                showGraveYard(isSelf);
            }

        });

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

    }

    public void initHand() {
//        DataFromGameRun data = game.run(new DataForGameRun("get init hand data", self)).get(0);

        ArrayList<Card> cards = new ArrayList<>();
        ArrayList<Card> rivalCards = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            cards.add(self.getGameBoard().getHand().getCardsInHand().get(i));
            rivalCards.add(rival.getGameBoard().getHand().getCardsInHand().get(i));
        }
        handleAddCardsFromDeckToSelfHandGraphic(cards);
        handleAddCardsFromDeckToRivalHandGraphic(rivalCards);
    }

    public void handleAddCardsFromDeckToRivalHandGraphic(ArrayList<Card> cards) {
        Timeline timeline = new Timeline();

        for (int i = 0; i < cards.size(); i++) {
            int finalI = i;
            KeyFrame frame = new KeyFrame(Duration.millis(i * 500 + 1),
                    Event -> handleAddRivalCardFromDeckToHandGraphic(cards.get(finalI)));
            timeline.getKeyFrames().add(frame);
        }

        timeline.play();
    }

    void setBooleanForShowActions(ArrayList<CardView> cardViews, boolean bool) {
        for (CardView cardView : cardViews) {
            cardView.setCanShowValidActions(bool);
        }
        if (bool) {
            if (mouseEnteredCardView != null) {
                try {
                    showValidActionForCard
                            (mouseEnteredCardView.getFirstValidAction(game, self), mouseEnteredCardView);
                } catch (Exception ignored) {

                }
            }
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

    CardView mouseEnteredCardView = null;

    CardView getCardForHand(Card card) {
        CardView cardView = new CardView(card, 5, false, true);

        cardView.setOnMouseEntered(mouseEvent -> {
            mouseEnteredCardView = cardView;
            cardView.hasBeenClicked = false;
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
            mouseEnteredCardView = null;
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
                try {
                    showValidActionForCard(cardView.getNextValidAction(), cardView);
                } catch (Exception ignored) {
                }
            } else {
                try {
                    cardView.throwExceptionIfHasNoValidAction();
                    cardOnLeftClick(cardView);
                } catch (Exception ignored) {
                }
            }
        });

        return cardView;
    }

    void cardOnLeftClick(CardView cardView) {
        if (cardView.hasBeenClicked) {
            return;
        }
        cardView.hasBeenClicked = true;
        ArrayList<DataFromGameRun> dataFromGameRun;
        if (numberOfNeededCards != 0 && cardView.getCurrentAction() != null) {
            if (cardView.getCurrentAction().equals("sacrifice")) {
                idsForMultiCardAction.add
                        (getIdByIndex(monsterZoneCards.indexOf(cardView)));
            } else if (cardView.getCurrentAction().equals("attack")) {
                idsForMultiCardAction.add(getRivalIdByIndex(rivalMonsterZoneCards.indexOf(cardView)));
            }

            if (idsForMultiCardAction.size() == numberOfNeededCards) {
                numberOfNeededCards = 0;
                StringBuilder command = new StringBuilder(cardView.getCurrentAction());
                for (Integer integer : idsForMultiCardAction) {
                    command.append(" ").append(integer);
                }
                dataFromGameRun = game.run(new DataForGameRun(String.valueOf(command), self));
                cardView = mainCardForMultiCardAction;
            } else {
                return;
            }
        } else {
            dataFromGameRun = game.run(new DataForGameRun(cardView.getCurrentAction(), self));
        }

        gameController.graphicsForEvents(dataFromGameRun, cardView, 0);

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

    public static int getIdByIndex(int index) {
        String data = "53124";
        return data.charAt(index) - '0';
    }

    public static int getRivalIdByIndex(int index){
        String data = "42135";
        return data.charAt(index) - '0';
    }

    public static int getIndexById(int id) {
        String data = "53124";
        return data.indexOf(id + "");
    }

    public static int getIndexByRivalId(int id) {
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
        cardForShow.setFill(new ImagePattern(cardView.getImage()));
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
        return getCardXAndYFromMode(cardView, mode, monsterZoneCards, spellZoneCards);
    }

    double getCardInRivalFieldX(CardView cardView, int mode) {

        return getCardXAndYFromMode(cardView, mode, rivalMonsterZoneCards, rivalSpellZoneCards);
    }

    private double getCardXAndYFromMode(CardView cardView, int mode, ArrayList<CardView> rivalMonsterZoneCards, ArrayList<CardView> rivalSpellZoneCards) {
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

    CardView searchCardInSelfHand(Card card) {
        for (CardView cardView : selfHand) {
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

    double handleSummonGraphic(Card card, int index) {
        return runMoveCardFromHandToFieldGraphic
                (this, card, 0, 0, index);
    }

    double handleSetMonsterGraphic(Card card, int index) {
        return runMoveCardFromHandToFieldGraphic
                (this, card, 1, 0, index);
    }

    double handleSetSpellGraphic(Card card, int index) {
        return runMoveCardFromHandToFieldGraphic
                (this, card, 3, 1, index);
    }

    double handleFlipSummonGraphic(Card card) {
        return runFlipSummonGraphic(this, card);
    }

    double handleFlipCardGraphic(CardView cardView) {
        return runFlipCardGraphic(cardView, this);
    }

    double handleIncreaseLpGraphic(int lp, boolean isSelf) {
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

    double handleAddCardsFromDeckToSelfHandGraphic(ArrayList<Card> cards) {
        ArrayList<CardView> cardViews = new ArrayList<>();
        for (Card card : cards) {
            cardViews.add(new CardView(
                    card, 8, true, true));
        }
        return runAddCardsToSelfHandFromDeckAnimation(this, cardViews);
    }

    double handleSummonSetWithSacrificeGraphics
            (Card card, int index, boolean isSet, ArrayList<Integer> sacrificesIndex) {

        double time = 0;

        for (int i : sacrificesIndex) {
            time = handleDestroyCardFromField(i, 0, true);
        }
        EventHandler eventHandler = isSet ? EventHandler -> handleSetMonsterGraphic(card, index)
                : EventHandler -> handleSummonGraphic(card, index);

        new Timeline(new KeyFrame(Duration.millis(time), eventHandler)).play();

        return time + 800;
    }

    double handleRivalSummonSetWithSacrificeGraphic
            (Card card, int index, boolean isSet, ArrayList<Integer> sacrificesIndex) {

        double time = 0;

        for (int i : sacrificesIndex) {
            time = handleDestroyCardFromField(i, 0, false);
        }
        EventHandler eventHandler = isSet ? EventHandler -> handleSetRivalMonsterGraphicBOOCN(card, index)
                : EventHandler -> handleSummonRivalMonsterGraphic(card, index);

        new Timeline(new KeyFrame(Duration.millis(time), eventHandler)).play();

        return time + 800;
    }


    double handleRivalAttackResultGraphic(Matcher matcher) {

        matcher.find();
        int attackerID = getIndexByRivalId(Integer.parseInt(matcher.group(1)));
        boolean destroyAttacker = matcher.group(2).equals("destroy");
        int attackedID = getIndexById(Integer.parseInt(matcher.group(3)));
        boolean destroyAttacked = matcher.group(4).equals("destroy");
        boolean flipAttacked = matcher.group(5).equals("flip ");
        boolean isLpLoserSelf = matcher.group(6).equals("rival");
        int lp = -Integer.parseInt(matcher.group(7));

        System.out.println(attackerID + " " + destroyAttacker + " " +
                attackedID + " " + destroyAttacked + " " + flipAttacked + " " + isLpLoserSelf + " " +
                lp);

        double time = 10;

        if (flipAttacked) {
            time = handleFlipCardGraphic(monsterZoneCards.get(attackedID));
        }

        new Timeline(new KeyFrame(Duration.millis(time), actionEvent -> {

            double time1 = handleIncreaseLpGraphic(lp, isLpLoserSelf) + 10;

            new Timeline(new KeyFrame(Duration.millis(time1), actionEvent1 -> {
                if (destroyAttacker) {
                    handleDestroyCardFromField(attackerID, 0, false);
                }

                if (destroyAttacked) {
                    handleDestroyCardFromField(attackedID, 0, true);
                }
            })).play();
        })).play();

        return time + lp + 500;
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
            time = handleFlipCardGraphic(rivalMonsterZoneCards.get(attackedID));
        }

        new Timeline(new KeyFrame(Duration.millis(time), actionEvent -> {

            double time1 = handleIncreaseLpGraphic(lp, isLpLoserSelf) + 10;

            new Timeline(new KeyFrame(Duration.millis(time1), actionEvent1 -> {
                if (destroyAttacker) {
                    handleDestroyCardFromField(attackerID, 0, true);
                }

                if (destroyAttacked) {

                    handleDestroyCardFromField(attackedID, 0, false);
                }
            })).play();
        })).play();

        return time + lp + 500;
    }

    //zone 0 for monster zone 1 for spell zone


    //get Index of ArrayList
    double handleDestroyCardFromField(int index, int zone, boolean isSelf) {
        double ans = runDestroyCardFromFieldOrHandGraphic(index, zone, isSelf);
        return ans;
    }

    double justDestroyActivatedSpellOrTrap(int index, Card card, boolean isSelf) {
        CardView cardView;

        if (index != -1) {
            cardView = isSelf ? searchCardInSelfHand(card) : searchCardInRivalHand(card);
        } else {
            cardView = isSelf ? searchCardInSelfField(card) : searchCardInRivalField(card);
        }

        double time = 500;

        if (index != -1) {
            if (isSelf) {
                time += runMoveCardFromHandToFieldGraphic(this, cardView.card, 2, 1, index);
            } else {
                time += runMoveRivalCardFromHandToFiledGraphic(this, card, 2, 1, 4 - index);
            }
        } else {
            time += handleFlipCardGraphic(cardView);
        }

        new Timeline(new KeyFrame(Duration.millis(time), Event -> {
            if (isSelf) {
                handleDestroyCardFromField(
                        spellZoneCards.indexOf(searchCardInSelfField(cardView.getCard())), 1, true);
            } else {
                handleDestroyCardFromField
                        (rivalSpellZoneCards.indexOf(searchCardInRivalField(cardView.getCard())), 1, false);
            }
        })).play();

        return time + 500;
    }

    double activateSpell1(int index, Card card, boolean isSelf, String ids) {

        System.out.println(card + " " + index);
        double time = justDestroyActivatedSpellOrTrap(index, card, isSelf) - 500;

        new Timeline(new KeyFrame(Duration.millis(time), Event -> {
            for (String indexStr : ids.split(" ")) {
                if (indexStr.equals(" ") || indexStr.equals("")) {
                    continue;
                }
                if (isSelf) {
                    handleDestroyCardFromField(getIndexByRivalId(Integer.parseInt(indexStr)), 0, false);
                } else {
                    handleDestroyCardFromField(getIndexById(Integer.parseInt(indexStr)), 0, true);
                }
            }
        })).play();

        return time + 500;
    }

    double activateSpell2(int index, Card card, boolean isSelf, String rivalIds, String activatorIDs) {

        double time = activateSpell1(index, card, isSelf, rivalIds) - 500;

        new Timeline(new KeyFrame(Duration.millis(time), Event -> {
            for (String indexStr : activatorIDs.split(" ")) {
                if (indexStr.equals(" ") || indexStr.equals("")) {
                    continue;
                }
                if (isSelf) {
                    handleDestroyCardFromField(getIndexById(Integer.parseInt(indexStr)), 0, true);
                } else {
                    handleDestroyCardFromField(getIndexByRivalId(Integer.parseInt(indexStr)), 0, false);
                }
            }
        })).play();

        return time + 500;
    }

    double activateSpell3(int index, Card card, boolean isSelf, String ids) {

        double time = justDestroyActivatedSpellOrTrap(index, card, isSelf) - 500;

        new Timeline(new KeyFrame(Duration.millis(time), Event -> {
            for (String indexStr : ids.split(" ")) {
                if (indexStr.equals(" ") || indexStr.equals("")) {
                    continue;
                }
                if (isSelf) {
                    handleDestroyCardFromField(getIndexByRivalId(Integer.parseInt(indexStr)), 1, false);
                } else {
                    handleDestroyCardFromField(getIndexById(Integer.parseInt(indexStr)), 1, true);
                }
            }
        })).play();


        return time + 500;
    }


    double handleChangePositionGraphicForSelfMonsters(Card card, String position) {
        CardView cardView = searchCardInSelfField(card);
        int mode = position.equals("attack") ? 0 : 4;
        CardView newCardView = getCardViewForField(cardView.card, mode);
        monsterZoneCards.set(monsterZoneCards.indexOf(cardView), newCardView);
        newCardView.setX(getCardInFieldX(newCardView, mode));
        newCardView.setY(getCardInFieldY(mode));
        newCardView.setVisible(false);
        gamePane.getChildren().add(newCardView);
        return runSetPosition(cardView, newCardView, position);
    }

    double handleChangePositionOfRivalMonsterGraphicBOOCN(Card card, String position) {
        CardView cardView = searchCardInRivalField(card);
        int mode = position.equals("attack") ? 0 : 4;
        CardView newCardView = getCardViewForField(cardView.card, mode);
        rivalMonsterZoneCards.set(rivalMonsterZoneCards.indexOf(cardView), newCardView);
        newCardView.setX(getCardInRivalFieldX(newCardView, mode));
        newCardView.setY(getCardInRivalFieldY(mode));
        newCardView.setVisible(false);
        gamePane.getChildren().add(newCardView);
        return runSetPosition(cardView, newCardView, position);
    }

    double runSetPosition(CardView cardView, CardView newCardView, String position) {

        Animation animation;

        if (position.equals("attack")) {
            animation = new RotateAnimation(cardView, 500, -90).getAnimation();
        } else {
            animation = new RotateAnimation(cardView, 500, 90).getAnimation();
        }
        animation.setOnFinished(EventHandler -> {
            newCardView.setVisible(true);
            gamePane.getChildren().remove(cardView);
        });
        animation.play();
        return 500;
    }

    CardView selfFieldSpell;
    CardView rivalFieldSpell;

    public double activateFieldSpell(Card card, boolean isSelf) {
        CardView cardView;
        double time;

        if (isSelf) {
            cardView = searchCardInSelfHand(card);
            time = runRemoveCardFromHand(this, cardView);
        } else {
            cardView = searchCardInRivalHand(card);
            time = runRemoveCardFromRivalHand(this, cardView);
        }

        if (selfFieldSpell != null) {
            gamePane.getChildren().remove(selfFieldSpell);
        }
        if (rivalFieldSpell != null) {
            gamePane.getChildren().remove(rivalFieldSpell);
        }

        if (isSelf) {
            selfFieldSpell = new CardView(card, 9, false, true);
        } else {
            rivalFieldSpell = new CardView(card, 9, false, true);
        }

        if (isSelf) {
            selfFieldSpell.setX(535);
            selfFieldSpell.setY(200);
            selfFieldSpell.setOpacity(0);
            gamePane.getChildren().add(selfFieldSpell);
            new FadeAnimation(selfFieldSpell, 500, 0, 1).getAnimation().play();
        } else {
            rivalFieldSpell.setX(40);
            rivalFieldSpell.setY(320);
            rivalFieldSpell.setOpacity(0);
            gamePane.getChildren().add(rivalFieldSpell);
            new FadeAnimation(rivalFieldSpell, 500, 0, 1).getAnimation().play();
        }

        return time;
    }

    //move to graveyard
    private double runDestroyCardFromFieldOrHandGraphic(int index, int zone, boolean isSelf) {

        CardView cardView = findCardViewForDestroy(index, zone, isSelf);
        if (isSelf) {
            if (zone == 0) {
                monsterZoneCards.set(monsterZoneCards.indexOf(cardView), null);
                Animation animation =
                        new FadeAnimation(cardView, 500, 1, 0).getAnimation();
                animation.setOnFinished(EventHandler -> {
                    gamePane.getChildren().remove(cardView);
                    handleAddCardToGraveYardGraphicBOOTN(cardView.card, true);
                });
                animation.play();
            } else if (zone == 1) {
                spellZoneCards.set(spellZoneCards.indexOf(cardView), null);
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
                rivalMonsterZoneCards.set(rivalMonsterZoneCards.indexOf(cardView), null);
                Animation animation =
                        new FadeAnimation(cardView, 500, 1, 0).getAnimation();
                animation.setOnFinished(EventHandler -> {
                    gamePane.getChildren().remove(cardView);
                    handleAddCardToGraveYardGraphicBOOTN(cardView.card, false);
                });
                animation.play();
            } else if (zone == 1) {
                rivalSpellZoneCards.set(rivalSpellZoneCards.indexOf(cardView), null);
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

    void handleSummonRivalMonsterGraphic(Card card, int index) {
        runMoveRivalCardFromHandToFiledGraphic(this, card, 0, 0, index);
    }

    void handleSetRivalMonsterGraphicBOOCN(Card card, int index) {
        runMoveRivalCardFromHandToFiledGraphic(this, card, 1, 0, index);
    }

    void handleActivateRivalSpellGraphicBOOCN(Card card, int index) {
        runMoveRivalCardFromHandToFiledGraphic(this, card, 2, 1, index);
    }

    void handleSetRivalSpellGraphic(Card card, int index) {
        runMoveRivalCardFromHandToFiledGraphic(this, card, 3, 1, index);
    }

    void handleAddRivalCardFromDeckToHandGraphic(Card card) {
        addCardToRivalHandFromDeck(this, card);
    }

    void handleRivalFlipSummonGraphicBOOCN(Card card) {
        runRivalFlipSummonGraphic(this, card);
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

        stage.getScene().setOnMouseMoved(event -> {
            String msg =
                    "(x: " + event.getX() + ", y: " + event.getY() + ") -- " +
                            "(sceneX: " + event.getSceneX() + ", sceneY: " + event.getSceneY() + ") -- " +
                            "(screenX: " + event.getScreenX() + ", screenY: " + event.getScreenY() + ")";

            reporter.setText(msg);
        });


        return monitored;
    }

    public void askForTrap(String event) {

        Button yesButton = new Button("yes");
        Button noButton = new Button("no");

        HBox buttons = new HBox(20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(yesButton, noButton);

        Label data = new Label(event + " has occurred just now\ndo you want do activate your trap ?");
        VBox mainBox = new VBox(10);

        mainBox.getChildren().addAll(data, buttons);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setLayoutX(400);
        mainBox.setLayoutY(300);
        mainBox.setBackground(new Background(
                new BackgroundFill(Color.rgb(243, 89, 17), new CornerRadii(0),
                        new Insets(-12, 0, 0, 0))));

        mainPane.getChildren().add(mainBox);

        noButton.setOnMouseClicked(mouseEvent -> {
            gameController.graphicsForEvents(
                    game.run(new DataForGameRun("cancel activate trap", self)),
                    null, 0);

            mainPane.getChildren().remove(mainBox);
        });

        yesButton.setOnMouseClicked(mouseEvent -> {
            mainPane.getChildren().remove(mainBox);
            CardActionManager.setMode(actionManagerMode.TRIGGER_TRAP_MODE);
        });
    }
}
