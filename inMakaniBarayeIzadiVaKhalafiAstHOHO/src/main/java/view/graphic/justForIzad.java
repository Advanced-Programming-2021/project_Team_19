package view.graphic;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;


public class justForIzad extends menuGraphic {

    public void run(){//اhoho
        Pane pane = new Pane();

        ImageView imageView = new ImageView(new Image("/Pictures/atk_icon.png"));
        pane.getChildren().add(imageView);
        imageView.setX(250);
        imageView.setY(100);
        imageView.maxHeight(50);


        pane.setOnMouseMoved(mouseEvent -> {

            double temp1 = (mouseEvent.getX() - imageView.getBoundsInParent().getCenterX());
            double temp2 = (mouseEvent.getY() - imageView.getBoundsInParent().getCenterY());
            double ans = (-temp1/temp2);
            double temp = temp2 > 0 ? 180 : 0;
            imageView.setRotate(temp + Math.toDegrees(Math.atan(ans)));
        });

        pane.setOnMouseClicked(mouseEvent -> {
            Timeline animation = new Timeline();
            KeyFrame kf = new KeyFrame(Duration.millis(400),
                    new KeyValue(imageView.xProperty(), mouseEvent.getX() - imageView.getFitWidth() / 2),
                    new KeyValue(imageView.yProperty(), mouseEvent.getY()));

            animation.getKeyFrames().add(kf);
            animation.play();
        });


        stage.getScene().setRoot(pane);
    }
}
