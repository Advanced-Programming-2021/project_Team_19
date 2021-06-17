package view.graphic;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import view.Menu.Menu;
import view.graphic.Animations.FlipTransition;
import view.graphic.Animations.Translation;

import static view.graphic.test.cardView;

public class test extends Menu {

    Pane pane = new Pane();

    public static CardView cardView = new CardView(controller.Utils.getCardByName
            ("battle ox"), 2);

    public test() {
        super("test");
    }

    public void run(){

        pane.getChildren().add(cardView);
        cardView.setX(200);
        cardView.setY(300);
        stage.getScene().setRoot(pane);


        new FlipTransition(cardView).start();
        new Translation(cardView, 100,100, 1000).start();
    }
}
