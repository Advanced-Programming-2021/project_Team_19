package view.graphic;

import controller.DuelControllers.CardActionManager;
import controller.Utils;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import model.Card.Card;
import model.Card.Monster;

import java.util.ArrayList;

public class CardView extends Rectangle {

    Card card;
    public boolean isHidden = false;
    public static double height = 614;
    public static double width = 423;
    public double sizeInverse;
    private Label actionDisplay = new Label();
    private ArrayList<String> validActions;
    private int validActionIndex = 0;

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

    public void setHidden(){
        isHidden = true;
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

    public Label showLabel(){
        actionDisplay.setLayoutX(getLayoutX() + 100);
        actionDisplay.setLayoutY(getLayoutY() + 200);
        actionDisplay.setTextFill(Color.GREEN);

        String validActionsInStringForm = new CardActionManager(card).getValidActions();

        validActionsInStringForm = "attack monster attack monster attack direct no normal summon normal summon";

        String[] actionNames = {"attack monster", "attack direct", "normal summon", "set", "set position"};

        validActions = new ArrayList<>();

        for (String actionName : actionNames) {
            if (validActionsInStringForm.matches(".*" + actionName + " " + actionName + ".*")){
                validActions.add(actionName);
            }
        }

        validActionIndex = 0;

        actionDisplay.setText(validActions.get(validActionIndex));

        return actionDisplay;
    }

    public void setNextValidAction(){
        actionDisplay.setText(validActions.get(++validActionIndex % validActions.size()));
    }

    public String getCurrentAction(){
        return validActions.get(validActionIndex);
    }


    public Label clearLabel(){
        validActionIndex = 0;
        actionDisplay.setText("");
        return actionDisplay;
    }
}

