package view.graphic;

import controller.DataBaseControllers.UserDataBaseController;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
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

    public GameGraphic() {
        super("Game Menu");
    }

    public void run() {


        Rectangle rectangle = new Rectangle(200, 600, Color.rgb(230, 180, 40));
        cardShowPane.getChildren().add(rectangle);

        Image image = new Image("Assets/Field/fie_normal.bmp");

        Rectangle rectangle0 = new Rectangle(600, 600);
        rectangle0.setFill(new ImagePattern(image));
        gamePane.getChildren().add(rectangle0);

        Rectangle monster1 = getRectangle(0,1);
        gamePane.getChildren().add(monster1);

        Rectangle monster2 = getRectangle(1,1);
        gamePane.getChildren().add(monster2);

        Rectangle monster3 = getRectangle(2,1);
        gamePane.getChildren().add(monster3);

        Rectangle monster4 = getRectangle(3,1);
        gamePane.getChildren().add(monster4);

        Rectangle monster5 = getRectangle(4,1);
        gamePane.getChildren().add(monster5);

        Rectangle deck = getRectangle(5,0);
        gamePane.getChildren().add(deck);

        Rectangle spell1 = getRectangle(0,0);
        gamePane.getChildren().add(spell1);

        Rectangle spell2 = getRectangle(1,0);
        gamePane.getChildren().add(spell2);

        Rectangle spell3 = getRectangle(2,0);
        gamePane.getChildren().add(spell3);

        Rectangle spell4 = getRectangle(3,0);
        gamePane.getChildren().add(spell4);

        Rectangle spell5 = getRectangle(4,0);
        gamePane.getChildren().add(spell5);

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
        Rectangle rectangle =  new Rectangle(Card.width / 6.3, Card.height / 6.3, Color.YELLOW);
        return rectangle;
    }

    private Rectangle getRectangle(int i , int j) {
        Rectangle rectangle =  new Rectangle(Card.width / 9, Card.height / 9, Color.YELLOW);
        rectangle.setX(122 + i * 82); rectangle.setY(101 + 100 * j);
        return rectangle;
    }

}
