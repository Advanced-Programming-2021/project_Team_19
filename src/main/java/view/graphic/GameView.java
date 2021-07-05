package view.graphic;

import controller.DuelControllers.Game;
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
import model.Enums.CardMod;
import model.Enums.SpellCardMods;
import model.Gamer;
import view.graphic.Animations.*;


import java.awt.*;
import java.util.ArrayList;

import static view.graphic.Utils.getImageByCard;

public class GameView {

    public Gamer self;
    public Gamer rival;
    public Game game;
    public GameView rivalGameView;

    Stage stage;

    Pane mainPane = new Pane();

    Pane gamePane = new Pane();
    BorderPane cardShowPane = new BorderPane();

    public CardView selfDeck;
    public CardView rivalDeck;

    public VBox phaseBox;
    private Popup nextPhasePopup;
    public static ArrayList<VBox> phaseBoxes = new ArrayList<>();

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

    private ArrayList<CardView> monsterZoneCards = new ArrayList<>();
    private ArrayList<CardView> spellZoneCards = new ArrayList<>();
    private ArrayList<CardView> rivalMonsterZoneCards = new ArrayList<>();
    private ArrayList<CardView> rivalSpellZoneCards = new ArrayList<>();

    {
        for (int i = 0; i < 5; i++) {
            monsterZoneCards.add(null);
            spellZoneCards.add(null);
            rivalMonsterZoneCards.add(null);
            rivalSpellZoneCards.add(null);
        }
    }


    public GameView(Stage stage, Gamer self, Gamer rival, Game game) {
        this.stage = stage;
        this.self = self;
        this.rival = rival;
        this.game = game;

        setCardShowPane();
        setGamePane();
        setDeck();
        setGraveYard();
        setData();

        HBox box = new HBox(cardShowPane, gamePane);
        mainPane.getChildren().add(box);
        box.setLayoutY((menuGraphic.sceneY - 600) / 2);

        mainPane.getChildren().add(descriptionScrollPane);
        setTestButton();
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
               nextPhasePopup =  menuGraphic.showPopupMessage(stage, "next phase",
                       stage.getX() + 235,
                        stage.getY() + 20);
            }
        });

        phaseButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                nextPhasePopup.hide();
            }
        });

        phaseButton.setOnMouseClicked(event -> {
            game.run("next phase");
            changePhase();
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

        phaseBoxes.add(phaseBox);
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


    private void changePhase() {
        for (VBox phaseBox : phaseBoxes) {
            for (int i = 0; i < 6; i++) {
                if (phaseBox.getChildren().get(i).getStyleClass().contains("activePhase")) {

                    phaseBox.getChildren().get(i).getStyleClass().remove("activePhase");
                    phaseBox.getChildren().get(i).getStyleClass().add("inactivePhase");
                    phaseBox.getChildren().get((i + 1) % 6).getStyleClass().add("activePhase");
                    break;
                }
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

        cardShowPane.setTop(getVboxForData(selfLpLabel, selfUsernameLabel, false));
        cardShowPane.setBottom(getVboxForData(rivalLpLabel, rivalUsernameLabel, true));
    }

    private VBox getVboxForData(Label lpLabel, Label usernameLabel, boolean isRival) {

        VBox box = new VBox(5);
        box.setCenterShape(true);
        box.setAlignment(Pos.CENTER);

        setLpLabel(lpLabel);
        Gamer gamer = isRival ? rival : self;
        setUsernameLabel(usernameLabel, gamer);

        int usernameIndex = 0;

        if (isRival) {
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

    private void addCardToSelfGraveYard(Card card) {
        CardView cardView = getCardForGraveyard(card, true);
        selfGraveyardCards.add(cardView);
        selfGraveYard.getChildren().add(cardView);
        rivalGameView.addCardToRivalGraveYard(card);
    }

    public void addCardToRivalGraveYard(Card card) {
        CardView cardView = getCardForGraveyard(card, false);
        rivalGraveyardCards.add(cardView);
        rivalGraveYard.getChildren().add(cardView);
    }

    private CardView getCardForGraveyard(Card card, boolean isSelf) {
        CardView cardView = new CardView(card, 9, false, true);

        cardView.setOnMouseClicked(mouseEvent -> showGraveYard(isSelf));

        return cardView;
    }

    private ArrayList<CardView> showCardsInScrollPane(ArrayList<Card> cards, boolean canCloseByRightClick){

        ArrayList<CardView> cardViews = new ArrayList<>();

        if(canCloseByRightClick){
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
        for(CardView cardView : cardViews){
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

        addCardToSelfGraveYard(controller.Utils.getCardByName("trap hole"));
    }

    public void initHand() {

        ArrayList<CardView> tempHand = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            tempHand.add(new CardView(
                    self.getGameBoard().getHand().getCardsInHand().get(i), 8, true, true));
        }

        runHandAnimation(tempHand);
    }

    private void setBooleanForShowActions(ArrayList<CardView> cardViews, boolean bool) {
        for (CardView cardView : cardViews) {
            cardView.setCanShowValidActions(bool);
        }
    }

    private void runHandAnimation(ArrayList<CardView> cardViews) {

        setBooleanForShowActions(selfHand, false);
        getHandAnimation(cardViews, 0).play();
    }

    private Animation getHandAnimation(ArrayList<CardView> cardViews, int index) {

        CardView cardView = cardViews.get(index);

        Animation tr = getTransitionForAddCardFromDeckToHand(cardView);

        tr.setOnFinished(actionEvent -> {
            if (index < cardViews.size() - 1) {
                setBooleanForShowActions(selfHand, false);
                getHandAnimation(cardViews, index + 1).play();
            } else {
                setBooleanForShowActions(selfHand, true);
            }
        });

        return tr;
    }


    public void addCardToRivalHandFromDeck(Card card) {
        getTransitionForAddCardFromRivalDeckToRivalHand(
                new CardView(card, 8, true, true)).play();
    }

    public ParallelTransition getTransitionForAddCardFromRivalDeckToRivalHand(CardView cardView) {

        cardView.setX(40);
        cardView.setY(92);
        gamePane.getChildren().add(cardView);
        CardView newCardView = getCardForRivalHand(cardView.getCard());
        rivalHand.add(newCardView);
        newCardView.setX(getCardInRivalHandX(newCardView));
        newCardView.setY(getCardinRivalHandY());
        newCardView.setVisible(false);
        gamePane.getChildren().add(newCardView);

        KeyFrame createNewCardAnimation = new KeyFrame(Duration.millis(500), actionEvent -> {
            newCardView.setVisible(true);
            gamePane.getChildren().remove(cardView);
        });

        Timeline timeline = new Timeline(createNewCardAnimation);

        ParallelTransition transitions = new ParallelTransition(
                new ScaleAnimation(cardView, 0.6, 500).getAnimation(),
                new TimelineTranslate(cardView, getCardInRivalHandX(newCardView) + 15,
                        getCardinRivalHandY() + 23, 500).getAnimation(), timeline

        );


        for (CardView cardView1 : rivalHand) {
            if (cardView1 != newCardView) {
                transitions.getChildren().add(new TimelineTranslate
                        (cardView1, getCardInRivalHandX(cardView1), getCardinRivalHandY(), 500)
                        .getAnimation());
            }
        }

        return transitions;
    }

    private ParallelTransition getTransitionForAddCardFromDeckToHand(CardView cardView) {

        cardView.setX(530);
        cardView.setY(425);
        gamePane.getChildren().add(cardView);

        CardView newCardView = getCardForHand(cardView.getCard());
        selfHand.add(newCardView);
        newCardView.setX(getCardInHandX(newCardView));
        newCardView.setY(getCardinHandY());
        newCardView.setVisible(false);
        gamePane.getChildren().add(newCardView);

        KeyFrame createNewCardAnimation = new KeyFrame(new Duration(500), actionEvent -> {
            newCardView.setVisible(true);
            gamePane.getChildren().remove(cardView);
        });

        KeyFrame notifyRivalAnimation = new KeyFrame(Duration.millis(1), actionEvent ->
                rivalGameView.addCardToRivalHandFromDeck(cardView.card));

        Timeline createCardTimeline = new Timeline(createNewCardAnimation);

        Timeline notifyRivalTimeLine = new Timeline(notifyRivalAnimation);

        ParallelTransition transitions = new ParallelTransition(
                new ScaleAnimation(cardView, 0.6, 500).getAnimation(),
                new TimelineTranslate(cardView, getCardInHandX(newCardView) + 15,
                        getCardinHandY() + 23, 500).getAnimation(),
                new FlipTransition(cardView, 500).getAnimation(), createCardTimeline, notifyRivalTimeLine
        );


        for (CardView cardView1 : selfHand) {
            if (cardView1 != newCardView) {
                transitions.getChildren().add(new TimelineTranslate
                        (cardView1, getCardInHandX(cardView1), getCardinHandY(), 500)
                        .getAnimation());
            }
        }

        return transitions;
    }

    private double getCardinHandY() {
        return menuGraphic.sceneY - 80;
    }

    private double getCardinRivalHandY() {
        return -44;
    }

    private double getCardInHandX(CardView cardView) {

        double ans = menuGraphic.sceneX / 2 - 100;
        double index = selfHand.indexOf(cardView);
        double size = ((double) selfHand.size()) / 2;
        double x = (size - index) * CardView.width / 4.8;
        return ans - x;
    }

    private double getCardInRivalHandX(CardView cardView) {
        double ans = menuGraphic.sceneX / 2 - 190;
        double index = rivalHand.indexOf(cardView);
        double size = ((double) rivalHand.size()) / 2;
        double x = (size - index) * CardView.width / 4.8;
        return ans + x;
    }

    private CardView getCardForHand(Card card) {
        CardView cardView = new CardView(card, 5, false, true);

        cardView.setOnMouseEntered(mouseEvent -> {
            game.gameData.setSelectedCard(card);
            showCard(cardView);
            new Translation(cardView, cardView.getLayoutY() - 15, 150).start();
            if (cardView.canShowValidActions) {
                showValidActionForCard(cardView.getFirstValidAction(), cardView);
            }
        });

        cardView.setOnMouseExited(mouseEvent -> {
            new Translation(cardView, cardView.getLayoutY(), 150).start();
            Popup popup = cardView.tempPopup;
            if (popup != null) {
                popup.hide();
            }
            if(cardView.filter != null){
                stage.getScene().removeEventFilter(MouseEvent.MOUSE_MOVED, cardView.filter);
            }
        });

        cardView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)) {
                showValidActionForCard(cardView.getNextValidAction(), cardView);
            }
            else {
                cardOnLeftClick(cardView);
            }
        });

        return cardView;
    }

    private void cardOnLeftClick(CardView cardView){
        ArrayList<String> dataFromGameRun = game.run(cardView.getCurrentAction()).getEvents();
        if (dataFromGameRun.size() == 1){
            String response = dataFromGameRun.get(0);
            if (response.matches("summon \\d")) {
                runMovingCardFromHandToFieldGraphic(cardView, 0, 0, Integer.parseInt(response.substring(7)));
            } else if (response.matches("set spell \\d")) {
                runMovingCardFromHandToFieldGraphic(cardView, 2, 1, Integer.parseInt(response.substring(10)));
            } else if (response.matches("set monster \\d")) {
                runMovingCardFromHandToFieldGraphic(cardView, 1, 0, Integer.parseInt(response.substring(12)));
            } else if (response.equals("flip summoned successfully")) {
                runFlipSummonGraphic(cardView);
            }
        }
    }

    private CardView getCardForRivalHand(Card card) {
        return new CardView(card, 5, true, true);
    }

    public void showCard(CardView cardView) {

        Card card = cardView.getCard();

        if(card == null) {
            return;
        }
        if (rivalMonsterZoneCards.contains(cardView) || rivalSpellZoneCards.contains(cardView)) {
            if (card instanceof Monster) {
                if (CardMod.DEFENSIVE_HIDDEN.equals(((Monster) card).getCardMod())) {
                    return;
                }
            } else if(card instanceof SpellAndTraps){
                SpellAndTraps spell = (SpellAndTraps) card;
                if (SpellCardMods.HIDDEN.equals(spell.getSpellCardMod())) {
                    return;
                }
            }
        }
        cardForShow.setFill(new ImagePattern(getImageByCard(card)));
        cardDescription.setText(card.getDescription());
    }

    private void setShowCardOnMouseEntered(CardView cardView) {
        cardView.setOnMouseEntered(mouseEvent -> {
            showCard(cardView);
        });
    }

    public void showValidActionForCard(String message, CardView cardView) {

        if(cardView.tempPopup != null){
            cardView.tempPopup.hide();
        }
        if(cardView.filter != null){
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


    private void fadeCard(CardView cardView){
        new FadeAnimation(cardView, 500,1,0).getAnimation().play();
    }

    private void runSummonGraphic(CardView cardView) {
        runMovingCardFromHandToFieldGraphic(cardView, 1, 0, 3);
    }


    private CardView getCardViewForField(Card card, int mode) {
        CardView cardView = new CardView(9);
        if (mode == 0 || mode == 2) {
            cardView = new CardView(card, 9, false, true);
        } else if (mode == 1) {
            cardView = new CardView(card, 9, true, false);
        } else if (mode == 3){
            cardView = new CardView(card, 9, true, true);
        } else if (mode == 4){
            cardView = new CardView(card, 9, false, false);
        }
        setShowCardOnMouseEntered(cardView);
        return cardView;
    }

    private double getCardInFieldX(CardView cardView, int mode) {
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

    private double getCardInRivalFieldX(CardView cardView, int mode) {

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

    private double getCardInFieldY(int mode) {
        if (mode == 0) {
            return 101 + 220;
        } else if (mode == 1 || mode == 4) {
            return 111 + 220;
        } else if (mode == 2 || mode == 3) {
            return 101 + 220 + 110;
        }
        return 0;
    }


    private double getCardInRivalFieldY(int mode) {
        if (mode == 0) {
            return 101 + 100;
        } else if (mode == 1 || mode == 4) {
            return 111 + 100;
        } else if (mode == 2 || mode == 3) {
            return 101 + 100 - 100;
        }
        return 0;
    }

    private CardView searchCardInRivalHand(Card card) throws RuntimeException {
        for (CardView cardView : rivalHand) {
            if (cardView.getCard().equals(card)) {
                return cardView;
            }
        }
        throw new RuntimeException("card is not in hand");
    }

    private CardView searchCardInRivalField(Card card){
        for(CardView cardView : rivalMonsterZoneCards){
            if(cardView != null && cardView.getCard().equals(card)){
                return cardView;
            }
        }
        for(CardView cardView : rivalSpellZoneCards){
            if(cardView != null && cardView.getCard().equals(card)){
                return cardView;
            }
        }
        return null;
    }

    //hand animations

    //zone -> 0 for monsterZone and 1 for spellZone
    //mode -> 0 for summon monster and 1 for set monster and 2 for set spell and 3 for activate spell

    private void runMovingCardFromHandToFieldGraphic(CardView cardView, int mode, int zone, int index) {
        CardView newCardView = getCardViewForField(cardView.getCard(), mode);

        addCardToCorrectZone(newCardView, zone, index);

        newCardView.setVisible(false);
        gamePane.getChildren().add(newCardView);
        newCardView.setX(getCardInFieldX(newCardView, mode));
        newCardView.setY(getCardInFieldY(mode));
        selfHand.remove(cardView);

        Timeline addNewCardAnimation = new Timeline
                (new KeyFrame(new Duration(500), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        gamePane.getChildren().remove(cardView);
                        newCardView.setVisible(true);
                    }
                }));

        Timeline notifyRivalAnimation = new Timeline(new KeyFrame(Duration.millis(1), actionEvent ->
                rivalGameView.runMovingRivalCardFromHandToFiledGraphic(cardView, mode, zone, 4 - index)));

        ParallelTransition transitions;

        if (mode == 0 || mode == 2) {
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 18,
                            newCardView.getY() - 28, 500).getAnimation(),
                    addNewCardAnimation, notifyRivalAnimation);
        } else if (mode == 1) {

            Timeline hideCardAnimation = new Timeline(new KeyFrame(new Duration(250),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            cardView.hideCard();
                        }
                    }));

            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 8,
                            newCardView.getY() - 38, 500).getAnimation(),
                    new RotateAnimation(cardView, 500, 90).getAnimation(),
                    addNewCardAnimation, notifyRivalAnimation, hideCardAnimation);
        } else if (mode == 3) {
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 18,
                            newCardView.getY() - 28, 500).getAnimation(),
                    new FlipTransition(cardView, 500).getAnimation(),
                    addNewCardAnimation, notifyRivalAnimation);
        } else {
            transitions = new ParallelTransition();
        }

        for (CardView cardView1 : selfHand) {
            transitions.getChildren().add(new TimelineTranslate
                    (cardView1, getCardInHandX(cardView1), getCardinHandY(), 500)
                    .getAnimation());
        }

        transitions.play();
    }

    private void addCardToCorrectZone(CardView cardView, int zone, int index){
        if (zone == 0) {
            monsterZoneCards.set(index, cardView);
        } else if (zone == 1) {
            spellZoneCards.set(index, cardView);
        }
    }


    //rival
    private void runMovingRivalCardFromHandToFiledGraphic
    (CardView tempCardView, int mode, int zone, int index) {

        CardView cardView = searchCardInRivalHand(tempCardView.getCard());

        CardView newCardView = getCardViewForField(cardView.getCard(), mode);

        addNewCardViewToCorrectRivalZone(newCardView, zone, index);

        newCardView.setVisible(false);
        gamePane.getChildren().add(newCardView);
        newCardView.setX(getCardInRivalFieldX(newCardView, mode));
        newCardView.setY(getCardInRivalFieldY(mode));
        rivalHand.remove(cardView);

        getAnimationForMoveCardFromRivalHandToRivalField(cardView, newCardView, mode).play();
    }


    private Animation getAnimationForMoveCardFromRivalHandToRivalField
            (CardView cardView, CardView newCardView, int mode) {

        Timeline addNewCardAnimation = new Timeline
                (new KeyFrame(new Duration(500), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        gamePane.getChildren().remove(cardView);
                        newCardView.setVisible(true);
                    }
                }));


        ParallelTransition transitions;

        if (mode == 0 || mode == 2) {
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 18,
                            newCardView.getY() - 28, 500).getAnimation(),
                    new FlipTransition(cardView, 500).getAnimation(),
                    addNewCardAnimation);
        } else if (mode == 1) {
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 8,
                            newCardView.getY() - 38, 500).getAnimation(),
                    new RotateAnimation(cardView, 500, 90).getAnimation(),
                    addNewCardAnimation);
        } else if (mode == 3) {
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 18,
                            newCardView.getY() - 28, 500).getAnimation(),
                    addNewCardAnimation);
        } else {
            transitions = new ParallelTransition();
        }

        for (CardView cardView1 : rivalHand) {
            transitions.getChildren().add(new TimelineTranslate
                    (cardView1, getCardInRivalHandX(cardView1), getCardinRivalHandY(), 500)
                    .getAnimation());
        }
        return transitions;
    }

    private void addNewCardViewToCorrectRivalZone(CardView cardView, int zone, int index) {
        if (zone == 0) {
            rivalMonsterZoneCards.set(index, cardView);
        } else if (zone == 1) {
            rivalSpellZoneCards.set(index, cardView);
        }
    }

    private void runRemoveCardFromHandGraphic(CardView cardView){
        ParallelTransition transition = new ParallelTransition(
                new FadeAnimation(cardView, 500, 1, 0).getAnimation());
        selfHand.remove(cardView);
        transition.getChildren().add(getHandAnimationForCardsWasInHand());
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gamePane.getChildren().remove(cardView);
            }
        });
        transition.play();
        rivalGameView.runRemoveCardFromRivalHandGraphic(cardView.getCard());
    }

    private void runRemoveCardFromRivalHandGraphic(Card card){
        CardView cardView = searchCardInRivalHand(card);
        ParallelTransition transition = new ParallelTransition(
                new FadeAnimation(cardView, 500, 1, 0).getAnimation());
        rivalHand.remove(cardView);
        transition.getChildren().add(getHandAnimationForCardsWasInRivalHand());
        transition.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gamePane.getChildren().remove(cardView);
            }
        });
        transition.play();;
    }

    private void addCardToHand(Card card){
        CardView cardView = getCardForHand(card);
        ParallelTransition transition = new ParallelTransition(
                new FadeAnimation(cardView, 500, 0, 1).getAnimation(),
                new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        gamePane.getChildren().add(cardView);
                    }
                })));

        selfHand.add(cardView);
        cardView.setX(getCardInHandX(cardView));
        cardView.setY(getCardinHandY());
        transition.getChildren().add(getHandAnimationForCardsWasInHand());
        transition.play();
        rivalGameView.addCardToRivalHand(card);
    }

    private void addCardToRivalHand(Card card){
        CardView cardView = getCardForRivalHand(card);
        ParallelTransition transition = new ParallelTransition(
                new FadeAnimation(cardView, 500, 0, 1).getAnimation(),
                new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        gamePane.getChildren().add(cardView);
                    }
                })));

        rivalHand.add(cardView);
        cardView.setX(getCardInRivalHandX(cardView));
        cardView.setY(getCardinRivalHandY());

        transition.getChildren().add(getHandAnimationForCardsWasInRivalHand());
        transition.play();
    }

    private ParallelTransition getHandAnimationForCardsWasInHand(){
        ParallelTransition transition = new ParallelTransition();
        for (CardView cardView : selfHand) {
            transition.getChildren().add(new TimelineTranslate
                    (cardView, getCardInHandX(cardView), getCardinHandY(), 500)
                    .getAnimation());
        }
        return transition;
    }

    private ParallelTransition getHandAnimationForCardsWasInRivalHand(){
        ParallelTransition transition = new ParallelTransition();
        for (CardView cardView : rivalHand) {
            transition.getChildren().add(new TimelineTranslate
                    (cardView, getCardInRivalHandX(cardView), getCardinRivalHandY(), 500)
                    .getAnimation());
        }
        return transition;
    }
    //field animations

    //zone -> 0 for monsterZone and 1 for spellZone
    //mode -> 0 for summon monster and 1 for set monster and 2 for set spell and 3 for activate spell
    //mode -> 4 for DO monster

    private void putCardIntoFiled(Card card, int mode, int zone, int index){
        CardView cardView = getCardViewForField(card, mode);
        addCardToCorrectZone(cardView, zone, index);
        cardView.setX(getCardInFieldX(cardView, mode));
        cardView.setY(getCardInFieldY(mode));
        new FadeAnimation(cardView, 800, 0, 1).getAnimation().play();
        gamePane.getChildren().add(cardView);
        rivalGameView.putCardIntoRivalFiled(card, mode, zone, 4 - index);

    }

    private void putCardIntoRivalFiled(Card card, int mode, int zone, int index){
        CardView cardView = getCardViewForField(card, mode);
        addNewCardViewToCorrectRivalZone(cardView, zone, index);
        cardView.setX(getCardInRivalFieldX(cardView, mode));
        cardView.setY(getCardInRivalFieldY(mode));
        new FadeAnimation(cardView, 800, 0, 1).getAnimation().play();
        gamePane.getChildren().add(cardView);
    }

    //flip animations

    private void runFlipSummonGraphic(CardView cardView){

        CardView newCardView = getCardViewForField(cardView.getCard(), 0);
        monsterZoneCards.set(monsterZoneCards.indexOf(cardView), newCardView);
        newCardView.setX(getCardInFieldX(newCardView, 0));
        newCardView.setY(getCardInFieldY(0));
        newCardView.setVisible(false);
        gamePane.getChildren().add(newCardView);

        Timeline addNewCardTimeline = new Timeline(new KeyFrame(Duration.millis(400),
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gamePane.getChildren().remove(cardView);
                newCardView.setVisible(true);
            }
        }));

        Timeline notifyRival = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rivalGameView.runRivalFlipSummonGraphic(cardView.getCard());
            }
        }));
        ParallelTransition transition = new ParallelTransition(
                new RotateAnimation(cardView, 400, -90).getAnimation(),
                new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        cardView.setCardImage();
                    }
                })),
                notifyRival, addNewCardTimeline);

        transition.play();

    }

    private void runRivalFlipSummonGraphic(Card card){

        CardView cardView = searchCardInRivalField(card);
        CardView newCardView = getCardViewForField(cardView.getCard(), 0);
        rivalMonsterZoneCards.set(rivalMonsterZoneCards.indexOf(cardView), newCardView);
        newCardView.setX(getCardInRivalFieldX(newCardView, 0));
        newCardView.setY(getCardInRivalFieldY(0));
        newCardView.setVisible(false);
        gamePane.getChildren().add(newCardView);

        Timeline addNewCardTimeline = new Timeline(new KeyFrame(Duration.millis(400),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        gamePane.getChildren().remove(cardView);
                        newCardView.setVisible(true);
                    }
                }));

        ParallelTransition transition = new ParallelTransition(
                new RotateAnimation(cardView, 400, -90).getAnimation(),
                new Timeline(new KeyFrame(Duration.millis(200), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        cardView.setCardImage();
                    }
                })),
                 addNewCardTimeline);

        transition.play();
    }

    private void runFlipCardGraphic(CardView cardView){
        new FlipTransition(cardView, 500).getAnimation().play();
        rivalGameView.runFlipRivalCardGraphic(cardView.getCard());
    }

    private void runFlipRivalCardGraphic(Card card){
        CardView cardView = searchCardInRivalField(card);
        new FlipTransition(cardView, 500).getAnimation().play();
    }



    //lp animations

    private void runIncreaseLpGraphic(int lp){
        getIncreaseLpTransition(lp, true).play();
        rivalGameView.runIncreaseRivalLpGraphic(lp);
    }

    private void increaseLpInLabel(int lp){
        selfLp += lp;
        selfLpLabel.setText(selfLp + "");
    }

    private void runIncreaseRivalLpGraphic(int lp){
        getIncreaseLpTransition(lp, false).play();
    }

    private void increaseRivalLpInLabel(int lp){
        rivalLp += lp;
        rivalLpLabel.setText(rivalLp + "");
    }

    private SequentialTransition getIncreaseLpTransition(int lp, boolean isSelf){

        SequentialTransition transition = new SequentialTransition();
        int increasingSize = 2;
        for(int i = 0; i < Math.ceil((double) lp / increasingSize); i++){
            int finalI = i;
            transition.getChildren().add(new Timeline(new KeyFrame(Duration.millis(2),
                    new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            int increasingLp = increasingSize;
                            if(finalI >= lp/increasingSize){
                                increasingLp = lp % increasingSize;
                            }
                            if(isSelf){
                                increaseLpInLabel(increasingLp);
                            } else {
                                increaseRivalLpInLabel(increasingLp);
                            }
                        }
                    })));
        }
        return transition;
    }

    // MindCrush data collector
    private void getCardNameForMindCrush(){
        VBox vBox = new VBox(10);
        vBox.setMinWidth(200);
        vBox.setMaxWidth(200);
        vBox.setLayoutX(200);
        vBox.setLayoutY(270);
        vBox.setCenterShape(true);
        vBox.setAlignment(Pos.CENTER);
        vBox.setBackground(new Background(
                new BackgroundFill(Color.rgb(243,89,17), new CornerRadii(0), new Insets(-12,0,0,0))));

        Label messageLabel = new Label("sadfds saldfjl lsadjf ljsdf aslkdjf lasdkjfl jasdf ljsdf lksjdflja l jlsdf");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(150);
        messageLabel.setAlignment(Pos.CENTER);

        TextField textField = new TextField();
        textField.setMaxWidth(150);
        vBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode().getName().equals("Enter")){
                    String data = textField.getText();
                    String result = sendMindCrushDataToServer(data);
                    if(result.equals("invalid card name")){
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

    private void showPopupForMindCrush(String message){
        Popup popup = menuGraphic.showPopupMessage
                (stage, message, stage.getX() + 200 + 220,
                        stage.getY() + 400);
        popup.setAutoHide(true);
    }

    private String sendMindCrushDataToServer(String data){
        String ans = " name";
        //TODO
        return ans;
    }

    //end Mind Crush
    static int counter = 0;
    public Button testButton = new Button("test");

    private void f() {

        getCardNameForMindCrush();
//        addCardToHand(controller.Utils.getCardByName("Battle ox"));

//        addCardToSelfGraveYard(controller.Utils.getCardByName("Battle ox"));
//        runRemoveCardFromHandGraphic(selfHand.get(0));
//        fadeCard(selfHand.get(0));

//        runIncreaseLPGraphic(125);

//        counter++;
//        if(counter == 1)
//        summon(selfHand.get(0));
//        else
//        runFlipCardGraphic(monsterZoneCards.get(3));
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

}
