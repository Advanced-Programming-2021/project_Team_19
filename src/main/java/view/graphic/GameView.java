package view.graphic;

import controller.DuelControllers.Game;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Card.Card;
import model.Gamer;
import view.graphic.Animations.*;

import java.util.ArrayList;

import static view.graphic.Utils.getImageByCard;

public class GameView {


    public Button testButton = new Button("test");
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

    ArrayList<CardView> selfHand = new ArrayList<>();
    ArrayList<CardView> selfGraveyardCards = new ArrayList<>();
    StackPane selfGraveYard = new StackPane();

    ArrayList<CardView> rivalHand = new ArrayList<>();
    ArrayList<CardView> rivalGraveyardCards = new ArrayList<>();
    StackPane rivalGraveYard = new StackPane();

    CardView cardForShow = null;
    Text cardDescription = new Text();
    ScrollPane descriptionScrollPane = new ScrollPane();

    Label selfLpLabel = new Label();
    Label rivalLpLabel = new Label();

    int selfLp;
    int rivalLp;

    Label selfUsernameLabel = new Label();
    Label rivalUsernameLabel = new Label();


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

    public void setRivalGameView(GameView gameView) {
        rivalGameView = gameView;
    }

    private void setGamePane() {
        Image image = new Image("Assets/Field/fie_normal.bmp");
        Rectangle field = new Rectangle(600, 600);
        field.setFill(new ImagePattern(image));
        gamePane.getChildren().add(field);
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

    private void showGraveYard(boolean isSelf) {

        ScrollPane graveyardScrollPane = new ScrollPane();

        graveyardScrollPane.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                mainPane.getChildren().remove(graveyardScrollPane);
            }
        });

        graveyardScrollPane.setId("graveyard");
        mainPane.getChildren().add(graveyardScrollPane);

        graveyardScrollPane.setLayoutX(250);
        graveyardScrollPane.setLayoutY(200);

        HBox box = new HBox(5);

        ArrayList<CardView> cards = isSelf ? selfGraveyardCards : rivalGraveyardCards;

        for (CardView cardView : cards) {
            CardView temp = new CardView(cardView.getCard(), 4, false, true);
            setShowCardOnMouseEntered(temp);
            box.getChildren().add(temp);
        }

        graveyardScrollPane.setContent(box);
        graveyardScrollPane.setMaxWidth(450);
        graveyardScrollPane.setMinWidth(450);
        graveyardScrollPane.setMinHeight(170);

    }


    public void run() {
        stage.getScene().setRoot(mainPane);
        stage.show();
        initHand();
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

    private void setMyBool(ArrayList<CardView> cardViews, boolean myBool) {
        for (CardView cardView : cardViews) {
            cardView.setMyBool(myBool);
        }
    }

    private void runHandAnimation(ArrayList<CardView> cardViews) {

        setMyBool(selfHand, false);
        getHandAnimation(cardViews, 0).play();
    }

    private Animation getHandAnimation(ArrayList<CardView> cardViews, int index) {

        CardView cardView = cardViews.get(index);

        Animation tr = getTransitionForAddCardFromDeckToHand(cardView);

        tr.setOnFinished(actionEvent -> {
            if (index < cardViews.size() - 1) {
                setMyBool(selfHand, false);
                getHandAnimation(cardViews, index + 1).play();
            } else {
                setMyBool(selfHand, true);
            }
        });

        return tr;
    }


    public void addCardToRivalHand(Card card) {
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

        KeyFrame createNewCardAnimation = new KeyFrame(Duration.millis(700), actionEvent -> {
            newCardView.setVisible(true);
            gamePane.getChildren().remove(cardView);
        });

        Timeline timeline = new Timeline(createNewCardAnimation);

        ParallelTransition transitions = new ParallelTransition(
                new ScaleAnimation(cardView, 0.6, 700).getAnimation(),
                new TimelineTranslate(cardView, getCardInRivalHandX(newCardView) + 15,
                        getCardinRivalHandY() + 23, 700).getAnimation(), timeline

        );


        for (CardView cardView1 : rivalHand) {
            if (cardView1 != newCardView) {
                transitions.getChildren().add(new TimelineTranslate
                        (cardView1, getCardInRivalHandX(cardView1), getCardinRivalHandY(), 700)
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

        KeyFrame createNewCardAnimation = new KeyFrame(new Duration(700), actionEvent -> {
            newCardView.setVisible(true);
            gamePane.getChildren().remove(cardView);
        });

        KeyFrame notifyRivalAnimation = new KeyFrame(Duration.millis(1), actionEvent ->
                rivalGameView.addCardToRivalHand(cardView.card));

        Timeline createCardTimeline = new Timeline(createNewCardAnimation);

        Timeline notifyRivalTimeLine = new Timeline(notifyRivalAnimation);

        ParallelTransition transitions = new ParallelTransition(
                new ScaleAnimation(cardView, 0.6, 700).getAnimation(),
                new TimelineTranslate(cardView, getCardInHandX(newCardView) + 15,
                        getCardinHandY() + 23, 700).getAnimation(),
                new FlipTransition(cardView, 700).getAnimation(), createCardTimeline, notifyRivalTimeLine
        );


        for (CardView cardView1 : selfHand) {
            if (cardView1 != newCardView) {
                transitions.getChildren().add(new TimelineTranslate
                        (cardView1, getCardInHandX(cardView1), getCardinHandY(), 700)
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

    //just use for cards in hand
    private boolean isCursorOut(CardView cardView, double mouseX, double mouseY) {

        double minX = cardView.getBoundsInParent().getMinX();
        double maxX = cardView.getBoundsInParent().getMaxX();


        double minY = cardView.getBoundsInParent().getMinY() + 15;
        double maxY = cardView.getBoundsInParent().getMaxY();

        return (mouseY > maxY || mouseY < minY) || (mouseX > maxX || mouseX < minX);
    }

    private CardView getCardForHand(Card card) {
        CardView cardView = new CardView(card, 5, false, true);

        cardView.setOnMouseEntered(mouseEvent -> {
            showCard(card);
            new Translation(cardView, cardView.getLayoutY() - 15, 150).start();
            if (cardView.myBool) {
                Label cardActionsLabel = cardView.getShowLabel("actions");
                if (cardActionsLabel != null) {
                    gamePane.getChildren().add(cardActionsLabel);
                }
            }
        });

        cardView.setOnMouseExited(mouseEvent -> {
            new Translation(cardView, cardView.getLayoutY(), 150).start();
            gamePane.getChildren().remove(cardView.clearLabel());
        });

        cardView.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY))
                cardView.setNextValidAction();
            else {
//                    startAction(cardView.getCurrentAction());
            }
        });

        return cardView;
    }

    private CardView getCardForRivalHand(Card card) {
        return new CardView(card, 5, true, true);
    }

    public void showCard(Card card) {
        cardForShow.setFill(new ImagePattern(getImageByCard(card)));
        cardDescription.setText(card.getDescription());
    }

    private void setShowCardOnMouseEntered(CardView cardView) {
        cardView.setOnMouseEntered(mouseEvent -> {
            showCard(cardView.card);
            gamePane.getChildren().add(cardView.getShowLabel("actions"));
        });
    }


    private void summon(CardView cardView){
        summonGraphic(cardView, 3);
    }
    private void summonGraphic(CardView cardView, int mode) {

        CardView newCardView = getCardViewForMonsterZoneForSummonOrSet(cardView.getCard(), mode);

        monsterZoneCards.set(0, newCardView);
        newCardView.setVisible(false);
        gamePane.getChildren().add(newCardView);
        newCardView.setX(getCardInMonsterZoneX(newCardView, mode));
        newCardView.setY(getCardInMonsterZoneY(mode));
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
                rivalGameView.summonRivalCardFromHand(cardView, mode == 0)));

        ParallelTransition transitions;

        if(mode == 0 || mode == 2){
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 18,
                            newCardView.getY() - 28, 500).getAnimation(),
                    addNewCardAnimation, notifyRivalAnimation);
        } else if(mode == 1){

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
        } else if (mode == 3){
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 18,
                    newCardView.getY() - 28, 500).getAnimation(),
                    new FlipTransition(cardView, 500).getAnimation(),
                    addNewCardAnimation, notifyRivalAnimation);
        }
        else{
            transitions = new ParallelTransition();
        }

        for (CardView cardView1 : selfHand) {
            transitions.getChildren().add(new TimelineTranslate
                    (cardView1, getCardInHandX(cardView1), getCardinHandY(), 700)
                    .getAnimation());
        }

        transitions.play();
    }

    private CardView getCardViewForMonsterZoneForSummonOrSet(Card card, boolean isHidden, boolean isVertical) {
        CardView cardView = new CardView(card, 9, isHidden, isVertical);
        setShowCardOnMouseEntered(cardView);
        return cardView;
    }
    private CardView getCardViewForMonsterZoneForSummonOrSet(Card card, int mode) {
        CardView cardView = new CardView(9);

        if(mode == 0 || mode == 2){
            cardView = new CardView(card, 9, false, true);
        } else if(mode == 1){
            cardView = new CardView(card, 9, true, false);
        }
        setShowCardOnMouseEntered(cardView);
        return cardView;
    }

    private double getCardInMonsterZoneX(CardView cardView){
        int i = monsterZoneCards.indexOf(cardView);
        return 122 + i * 82;
    }

    private double getCardInMonsterZoneX(CardView cardView, int mode){
        if(mode == 0 || mode == 2 || mode == 3){
            int i = monsterZoneCards.indexOf(cardView);
            return 122 + i * 82;
        } else if (mode == 1){
            int i = monsterZoneCards.indexOf(cardView);
            return 122 + i * 82 - 10;
        }
        return 0;
    }

    private double getCardInRivalMonsterZoneX(CardView cardView){
        int i = rivalMonsterZoneCards.indexOf(cardView);
        return 122 + i * 82;
    }

    private double getCardInMonsterZoneY(CardView cardView){
        return 101 + 220;
    }

    private double getCardInMonsterZoneY(int mode){
        if(mode == 0){
            return 101 + 220;
        }else if(mode == 1){
            return 111 + 220;
        } else if (mode == 2 || mode == 3){
            return 101 + 220 + 110;
        }
        return 0;
    }

    private double getCardInRivalMonsterZoneY(CardView cardView){
        return 101 + 100;
    }

    private void summonRivalCardFromHand(CardView tempCardView, boolean isSummon){

        CardView cardView = null;

        for(CardView cardView1 : rivalHand){
            if(cardView1.getCard().equals(tempCardView.getCard())){
                cardView = cardView1;
                break;
            }
        }
        
        CardView newCardView = getCardViewForMonsterZoneForSummonOrSet(cardView.getCard(), !isSummon, isSummon);
        rivalMonsterZoneCards.set(4, newCardView);

        newCardView.setVisible(false);
        gamePane.getChildren().add(newCardView);
        newCardView.setX(getCardInRivalMonsterZoneX(newCardView) + (isSummon ? 0: -10));
        newCardView.setY(getCardInRivalMonsterZoneY(newCardView) + (isSummon ? 0 : 10));
        rivalHand.remove(cardView);

        CardView finalCardView = cardView;
        Timeline addNewCardAnimation = new Timeline
                (new KeyFrame(new Duration(500), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gamePane.getChildren().remove(finalCardView);
                newCardView.setVisible(true);
            }
        }));


        ParallelTransition transitions;

        if (isSummon) {
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 18,
                            newCardView.getY() - 28, 500).getAnimation(),
                    new FlipTransition(cardView, 500).getAnimation(),
                    addNewCardAnimation);
        } else{
            transitions = new ParallelTransition(
                    new ScaleAnimation(cardView, -(0.45555), 500).getAnimation(),
                    new TimelineTranslate(cardView, newCardView.getX() - 8,
                            newCardView.getY() - 38, 500).getAnimation(),
                    new RotateAnimation(cardView, 500, 90).getAnimation(),
                    addNewCardAnimation);
        }



        for (CardView cardView1 : rivalHand) {
            transitions.getChildren().add(new TimelineTranslate
                    (cardView1, getCardInRivalHandX(cardView1), getCardinRivalHandY(), 700)
                    .getAnimation());
        }

        transitions.play();
    }

    private ArrayList<CardView> monsterZoneCards = new ArrayList<>();
    private ArrayList<CardView> rivalMonsterZoneCards = new ArrayList<>();

    {
        for (int i = 0; i < 5; i++) {
            monsterZoneCards.add(null);
            rivalMonsterZoneCards.add(null);
        }
    }

    private void f(){
        summon(selfHand.get(0));
    }

    private CardView getCardViewByCardForMonsterAndSpellZone(Card card, int i, int j) {
        CardView cardView = new CardView(card, 9, false, true);

        cardView.setOnMouseEntered(mouseEvent -> showCard(card));

        cardView.setX(122 + i * 82);
        cardView.setY(101 + 100 * j);
        return cardView;
    }

}
