package view.graphic.CardViewAnimations;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import view.graphic.CardView;

public class TimelineTranslate implements CardViewAnimationMaker{
    Timeline animation;

    public TimelineTranslate(CardView cardView, double x, double y, double time){

        animation = new Timeline();
        KeyFrame kf = new KeyFrame(Duration.millis(time),
                new KeyValue(cardView.xProperty(), x),
                new KeyValue(cardView.yProperty(), y));

        animation.getKeyFrames().add(kf);
    }

    public Timeline getAnimation(){
        return animation;
    }
}
