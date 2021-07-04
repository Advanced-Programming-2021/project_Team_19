package view.graphic;

import controller.DuelControllers.CardActionManager;
import controller.Utils;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Popup;
import model.Card.Card;
import model.Card.Monster;

import java.util.ArrayList;

public class CardView extends Rectangle {

    Card card;
    public boolean isHidden = false;
    public boolean isVertical = true;
    public static double height = 614;
    public static double width = 423;
    public double sizeInverse;
    private ArrayList<String> validActionNamesForShow;
    private ArrayList<String> validActionNames;
    private int validActionIndex = 0;
    public boolean canShowValidActions = true;
    public Popup tempPopup;
    public EventHandler filter;

    public CardView(double sizeInverse){
        super(width / sizeInverse, height / sizeInverse);
        this.sizeInverse = sizeInverse;
        hideCard();
    }

    public CardView(Card card, double sizeInverse, boolean isHidden, boolean isVertical){

        setWidth(isVertical ? width / sizeInverse : height / sizeInverse);
        setHeight(isVertical ? height / sizeInverse : width / sizeInverse);

        this.sizeInverse = sizeInverse;
        this.card = card;
        this.isHidden = isHidden;
        this.isVertical = isVertical;

        if(isHidden){
            setFill(new ImagePattern(new Image("/Assets/Cards/Monsters/Unknown.jpg")));
        } else{
            setCardImage();
        }
    }

    public void setCanShowValidActions(boolean canShowValidActions){
        this.canShowValidActions = canShowValidActions;
    }

    public void hideCard(){
        setFill(new ImagePattern(new Image("/Assets/Cards/Monsters/Unknown.jpg")));
        isHidden = true;
    }

    public void setCardImage(){
        if(isVertical){
            String model =  card instanceof Monster ? "Monsters/" : "SpellTrap/";
            ImagePattern imagePattern = new ImagePattern(new Image("/Assets/Cards/" + model +
                    Utils.getPascalCase(card.getName()) +".jpg"));
            setFill(imagePattern);
        } else{
            setCardImage2();
        }
        isHidden = false;
    }

    private void setCardImage2(){
        String model =  card instanceof Monster ? "Monsters/" : "SpellTrap/";
        ImageView imageView = new ImageView(new Image("/Assets/Cards/" + model +
                Utils.getPascalCase(card.getName()) +".jpg"));

        imageView.setRotate(90);

        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        Image rotatedImage = imageView.snapshot(params, null);

        setFill(new ImagePattern(rotatedImage));
    }

    public Card getCard(){
        return card;
    }

    public String getFirstValidAction(){

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

        //test
        validActionNames.add("summon");
        validActionNamesForShow.add("summon");
        validActionNames.add("attack");
        validActionNamesForShow.add("attack");
        //test

        validActionIndex = 0;

        if(validActionNamesForShow.size() > 0){
            return validActionNamesForShow.get(0);
        } else {
            return null;
        }
    }

    public String getNextValidAction(){
        if(validActionNamesForShow.size() == 0){
            return null;
        }
        return validActionNamesForShow.get
                (++validActionIndex % validActionNamesForShow.size());
    }


    public String getCurrentAction(){
        return validActionNamesForShow.get(validActionIndex);
    }
}

