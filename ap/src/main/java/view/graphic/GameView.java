package view.graphic;

import controller.DuelControllers.Game;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import model.Card.Card;
import model.Gamer;
import view.graphic.Animations.FlipTransition;
import view.graphic.Animations.ScaleAnimation;
import view.graphic.Animations.TimelineTranslate;
import view.graphic.Animations.Translation;

import java.util.ArrayList;

import static view.Printer.Printer.print;
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

    }

    public void setRivalGameView(GameView gameView){
        rivalGameView = gameView;
    }

    private void setGamePane(){
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
                new Insets(0,0,0,0))));

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

    private void setSelfDeck(){
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

    private void addCardToSelfGraveYard(Card card){
        CardView cardView = getCardForGraveyard(card, true);
        selfGraveyardCards.add(cardView);
        selfGraveYard.getChildren().add(cardView);
        rivalGameView.addCardToRivalGraveYard(card);
    }

    public void addCardToRivalGraveYard(Card card){
        CardView cardView = getCardForGraveyard(card, false);
        rivalGraveyardCards.add(cardView);
        rivalGraveYard.getChildren().add(cardView);
    }


    private CardView getCardForGraveyard(Card card, boolean isSelf) {
        CardView cardView = new CardView(card, 9, false);

        cardView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showGraveYard(isSelf);
            }
        });

        return cardView;
    }

    private void showGraveYard(boolean isSelf) {

        ScrollPane graveyardScrollPane = new ScrollPane();

        graveyardScrollPane.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    mainPane.getChildren().remove(graveyardScrollPane);
                }
            }
        });

        graveyardScrollPane.setId("graveyard");
        mainPane.getChildren().add(graveyardScrollPane);

        graveyardScrollPane.setLayoutX(250);
        graveyardScrollPane.setLayoutY(200);

        HBox box = new HBox(5);

        ArrayList<CardView> cards = isSelf ? selfGraveyardCards : rivalGraveyardCards;

        for (CardView cardView : cards) {
            CardView temp = new CardView(cardView.getCard(), 4, false);
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

        ArrayList<Card> tempHand = new ArrayList<>();
        for(int i = 0; i < 5; i++){
            tempHand.add(self.getGameBoard().getHand().getCardsInHand().get(i));
        }

        getHandAnimation(tempHand, 0).play();
    }

    private Animation getHandAnimation(ArrayList<Card> cardViews, int index){

        CardView cardView = new CardView(cardViews.get(index), 8, true);

        Animation tr = getTransitionForAddCardFromDeckToHand(cardView);

        tr.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(index < 4){
                    getHandAnimation(cardViews, index + 1).play();
                }
            }
        });

        return tr;
    }


    public void addCardToRivalHand(Card card){
        getTransitionForAddCardFromRivalDeckToRivalHand(new CardView(card, 8, true)).play();
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

        KeyFrame createNewCardAnimation = new KeyFrame(new Duration(700), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                newCardView.setVisible(true);
                gamePane.getChildren().remove(cardView);
            }
        });

        Timeline timeline = new Timeline(createNewCardAnimation);

        ParallelTransition transitions = new ParallelTransition(
                new ScaleAnimation(cardView, 0.6, 700).getAnimation(),
                new TimelineTranslate(cardView, getCardInRivalHandX(newCardView) + 15,
                        getCardinRivalHandY() + 23, 700).getAnimation(), timeline

        );


        for(CardView cardView1 : rivalHand){
            if(cardView1 != newCardView){
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

        KeyFrame createNewCardAnimation = new KeyFrame(new Duration(700), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                newCardView.setVisible(true);
                gamePane.getChildren().remove(cardView);
            }
        });

        KeyFrame notifyRivalAnimation = new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rivalGameView.addCardToRivalHand(cardView.card);
            }
        });

        Timeline createCardTimeline = new Timeline(createNewCardAnimation);

        Timeline notifyRivalTimeLine = new Timeline(notifyRivalAnimation);

        ParallelTransition transitions = new ParallelTransition(
                new ScaleAnimation(cardView, 0.6, 700).getAnimation(),
                new TimelineTranslate(cardView, getCardInHandX(newCardView) + 15,
                        getCardinHandY() + 23, 700).getAnimation(),
                new FlipTransition(cardView, 700).getAnimation(), createCardTimeline, notifyRivalTimeLine
        );


        for(CardView cardView1 : selfHand){
            if(cardView1 != newCardView){
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

    private double getCardinRivalHandY(){
        return -44;
    }

    private double getCardInHandX(CardView cardView) {

        double ans = menuGraphic.sceneX / 2 - 100;
        double index = selfHand.indexOf(cardView);
        double size = ((double) selfHand.size()) / 2;
        double x = (size - index) * CardView.width / 4.8;
        return ans - x;
    }

    private double getCardInRivalHandX(CardView cardView){
        double ans = menuGraphic.sceneX / 2 - 190;
        double index = rivalHand.indexOf(cardView);
        double size = ((double) rivalHand.size()) / 2;
        double x = (size - index) * CardView.width / 4.8;
        return ans + x;
    }

    private CardView getCardForHand(Card card) {
        CardView cardView = new CardView(card, 5, false);

        cardView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showCard(card);
                new Translation(cardView, cardView.getLayoutY() - 15, 150).start();
            }
        });

        cardView.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                new Translation(cardView, cardView.getLayoutY(), 150).start();
            }
        });

        return cardView;
    }

    private CardView getCardForRivalHand(Card card) {
        CardView cardView = new CardView(card, 5, true);
        return cardView;
    }

    private CardView getCardViewByCardForMonsterAndSpellZone(Card card, int i, int j) {
        CardView cardView = new CardView(card, 9, false);

        cardView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showCard(card);
            }
        });

        cardView.setX(122 + i * 82);
        cardView.setY(101 + 100 * j);
        return cardView;
    }

    public void showCard(Card card) {
        cardForShow.setFill(new ImagePattern(getImageByCard(card)));
        cardDescription.setText(card.getDescription());
    }

    private void setShowCardOnMouseEntered(CardView cardView){
        cardView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showCard(cardView.card);
            }
        });
    }

}
