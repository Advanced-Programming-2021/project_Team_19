package view.graphic;

import controller.DataBaseControllers.UserDataBaseController;
import controller.Utils;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.Card.Card;
import model.User;
import view.Menu.Menu;

public class GameGraphic extends Menu {

    User user1 = UserDataBaseController.getUserByUsername("mohammad");
    User user2 = UserDataBaseController.getUserByUsername("reza");

    Pane gamePane = new Pane();
    Pane cardShowPane = new Pane();
    Pane mainPane = new Pane();
    CardView cardForShow;
    ScrollPane cardDescription = new ScrollPane();

    public GameGraphic() {
        super("Game Menu");
    }

    public void setCardShowPane(){

        Rectangle rectangle = new Rectangle(200, 600, Color.rgb(230, 180, 40));
        cardShowPane.getChildren().add(rectangle);
        cardForShow = new CardView(null, 2.3);
        cardShowPane.getChildren().add(cardForShow);
        cardForShow.setX(9);
        cardForShow.setY(110);
        cardShowPane.getChildren().add(cardDescription);

    }

    public void run() {


        setCardShowPane();

        Image image = new Image("Assets/Field/fie_normal.bmp");

        Rectangle rectangle0 = new Rectangle(600, 600);

        rectangle0.setFill(new ImagePattern(image));
        gamePane.getChildren().add(rectangle0);


        Rectangle monster1 = getSpellAndTrapCardView(Utils.getCardByName("battle OX"), 0,1);
        gamePane.getChildren().add(monster1);

        HBox box = new HBox(cardShowPane, gamePane);
        mainPane.getChildren().add(box);
        box.setLayoutY((GraphicMenu.sceneY  - 600) / 2);


        mainPane.getChildren().add(hand);
        hand.setCenterShape(true);
        hand.setSpacing(10);

        hand.getChildren().add(getRectangle());
        hand.getChildren().add(getRectangle());
        hand.getChildren().add(getRectangle());

        hand.setLayoutX(GraphicMenu.sceneX / 2);
        hand.setLayoutY(GraphicMenu.sceneY - 100);
        stage.getScene().setRoot(mainPane);
    }

    HBox hand = new HBox();

    private Rectangle getRectangle(){
        Rectangle rectangle =  new Rectangle(CardView.width / 6.3, CardView.height / 6.3, Color.YELLOW);
        return rectangle;
    }

    private CardView getSpellAndTrapCardView(Card card, int i , int j) {
        CardView cardView = new CardView(card, 9);

        cardView.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                showCard(card);
            }
        });

        cardView.setX(122 + i * 82); cardView.setY(101 + 100 * j);
        return cardView;
    }

    public void showCard(Card card){

    }

}
