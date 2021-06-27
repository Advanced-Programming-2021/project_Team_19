package view.graphic;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import view.Menu.Menu;
import view.graphic.Animations.FlipTransition;
import view.graphic.Animations.ScaleAnimation;
import view.graphic.Animations.Translation;

import static view.graphic.test.cardView;

public class test extends Menu {

    Pane pane = new Pane();

    public static CardView cardView = new CardView(controller.Utils.getCardByName
            ("battle ox"), 2, true);

    public test() {
        super("test");
    }

    public void run(){


        pane.getChildren().add(cardView);
        cardView.setX(200);
        cardView.setY(300);
        stage.getScene().setRoot(pane);

        new FlipTransition(cardView, 700).start();
//        new Translation(cardView, 100,100, 1000).start();


//        new ScaleAnimation(cardView, 1, 1000).start();
//        ScaleTransition st = new ScaleTransition(Duration.millis(900), cardView);
//        st.setFromX(1);
//        st.setFromY(1);
//        st.setToX(0.1);
//        st.setToY(0.1);
//        st.play();
    }
}
