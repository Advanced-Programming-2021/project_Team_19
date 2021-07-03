package view.graphic;

import controller.DuelControllers.CardActionManager;
import controller.Utils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import model.Card.Card;
import model.Card.Monster;

import java.util.ArrayList;

public class CardView extends Rectangle {

    Card card;
    public boolean isHidden = false;
    public static double height = 614;
    public static double width = 423;
    public double sizeInverse;
    public Label actionDisplayLabel = new Label();
    private ArrayList<String> validActionNamesForShow;
    private ArrayList<String> validActionNames;
    private int validActionIndex = 0;
    public boolean myBool = true;

    public CardView(double sizeInverse){
        super(width / sizeInverse, height / sizeInverse);
        this.sizeInverse = sizeInverse;
        hideCard();
    }

    public CardView(Card card, double sizeInverse, boolean isHidden){
        super(width / sizeInverse, height / sizeInverse);
        this.sizeInverse = sizeInverse;
        this.card = card;
        this.isHidden = isHidden;
        if(isHidden){
            setFill(new ImagePattern(new Image("/Assets/Cards/Monsters/Unknown.jpg")));
        } else{
            setCardImage();
        }
    }

    public void setMyBool(boolean myBool){
        this.myBool = myBool;
    }

    public void hideCard(){
        setFill(new ImagePattern(new Image("/Assets/Cards/Monsters/Unknown.jpg")));
        isHidden = true;
    }

    public void setCardImage(){
        String model =  card instanceof Monster ? "Monsters/" : "SpellTrap/";
        setFill(new ImagePattern(new Image("/Assets/Cards/" + model +
                Utils.getPascalCase(card.getName()) +".jpg")));
        isHidden = false;
    }

    public Card getCard(){
        return card;
    }

    public boolean wasShow = false;

    public Label getShowLabel(){

        actionDisplayLabel.setLayoutX(getX() + 10);
        actionDisplayLabel.setLayoutY(getY() - 35);
        actionDisplayLabel.setTextAlignment(TextAlignment.CENTER);
        actionDisplayLabel.getStyleClass().add("actions");

        validActionNamesForShow = new ArrayList<>();
        validActionNames = CardActionManager.getInstance(card).getValidActions();

        for(String validAction : validActionNames){
            if(validAction.startsWith("summon")){
                validActionNamesForShow.add("summon");
            } else if (validAction.startsWith("attack")){
                validActionNamesForShow.add("attack");
            } else if (validAction.startsWith("set position")){
                validActionNamesForShow.add("change position");
            } else if (validAction.equals("set")){
                validActionNamesForShow.add("set");
            } else if (validAction.equals("activate spell normally")){
                validActionNamesForShow.add("activate");
            } else if (validAction.equals("activate effect monster")){
                validActionNamesForShow.add("activate");
            } else if (validAction.equals("select")){
                validActionNamesForShow.add("select");
            } else {
                validActionNamesForShow.add(validAction);
            }
        }

        validActionNames.add("summon");
        validActionNamesForShow.add("summon");
        validActionNames.add("attack");
        validActionNamesForShow.add("attack");

        validActionIndex = 0;

        if(validActionNamesForShow.size() > 0){
            actionDisplayLabel.setText(validActionNamesForShow.get(validActionIndex));
            return actionDisplayLabel;
        } else {
            return null;
        }
    }

    public void setNextValidAction(){
        actionDisplayLabel.setText(validActionNamesForShow.get
                (++validActionIndex % validActionNamesForShow.size()));
    }

    public String getCurrentAction(){
        return validActionNamesForShow.get(validActionIndex);
    }


    public Label clearLabel(){
        validActionIndex = 0;
        actionDisplayLabel.setText("");
        return actionDisplayLabel;
    }
}

