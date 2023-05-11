package com.lovelaced;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PlayerCustomizationController {

    private HashMap<String,String> selectedColors = new HashMap<String,String>();
    private HashMap<String,ImageView> selectedFlasks = new HashMap<String,ImageView>();

    @FXML
    private AnchorPane pane2;

    @FXML
    private ImageView arrow1;

    @FXML
    private ImageView arrow2;

    @FXML
    private ImageView arrow3;

    @FXML
    private ImageView arrow4;


    @FXML
    private ImageView exitButton;


    @FXML
    private ImageView leftAddButton;

    @FXML
    private AnchorPane pane3;

    @FXML
    private AnchorPane pane1;


    @FXML
    private ImageView rightAddButton;

    @FXML
    private AnchorPane pane4;


    @FXML
    private ImageView firstPlayer;

    @FXML
    private ImageView secondPlayer;

    @FXML
    private ImageView thirdPlayer;

    @FXML
    private ImageView fourthPlayer;


    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    void exitClicked(MouseEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("StartScreen-view.fxml"));

        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.setFullScreen(true);
        stage.setTitle("Start Screen");
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.show();
    }

    @FXML
    void leftClicked(MouseEvent event) {
        pane3.setVisible(true);
    }
    @FXML
    void rightClicked(MouseEvent event) {
        pane4.setVisible(true);
    }
    private ImageView arrow = new ImageView();
    private String pane = null;
    private String color = null;
    @FXML
    void arrowClicked(MouseEvent event) throws FileNotFoundException {
        if(pane != null)
            selectedFlasks.get(pane).setStyle(null);
        boolean exist = false;
        ImageView flask =  (ImageView) event.getSource();
        colorPicker(flask);

        for ( String Tempcolor : selectedColors.keySet() ) {
            if ( selectedColors.get( Tempcolor ).equals( color ) ) { exist = true; flask.setStyle( null ); break; }
        }

        if ( !exist ){

            if(pane != null && !pane.equals(flask.getParent().getId()) && color != null)
                selectedFlasks.get(pane).setStyle("-fx-effect: dropShadow(three-pass-box, " + selectedColors.get(pane) + ", 10, 0, 0, 0)");

            pane = flask.getParent().getId();

            selectedColors.put( pane, color );

            animateArrow(flask, 1000);

            flask.setStyle("-fx-effect: dropShadow(three-pass-box, " + color + ", 30, 0, 0, 0)");

            selectedFlasks.put( pane, flask );
            flask = selectedFlasks.get(pane);

            setPlayerImage(pane);
        }

    }

    private void colorPicker(ImageView flask) {
        String colorUrl = flask.getImage().getUrl();

        int temp = colorUrl.lastIndexOf("flask" );
        color = colorUrl.substring( temp );

        switch ( color ) {
            case "flaskYellow.png": color = "yellow"; break;
            case "flaskRed.png":    color = "red"; break;
            case "flaskBlue.png":   color = "blue"; break;
            case "flaskGreen.png":  color = "green"; break;
        }
    }

    private void animateArrow(ImageView flask, int duration) {

        arrow.setVisible(false);

        switch (pane) {
            case "pane1": arrow = arrow1; break;
            case "pane2": arrow = arrow2; break;
            case "pane3": arrow = arrow3; break;
            case "pane4": arrow = arrow4; break;
        }

        double numberY;
        double numberX;
        numberX = arrow.getLayoutX();
        numberY = arrow.getLayoutY() - 55;
        arrow.setVisible( true );

        TranslateTransition translate = new TranslateTransition();
        if ( arrow.getX() == 0 )
            translate.setToX(flask.getLayoutX() - numberX);
        else
            translate.setToX(flask.getLayoutX());
        if ( arrow.getY() == 0 )
            translate.setToY(flask.getLayoutY() - numberY);
        else
            translate.setToY(flask.getLayoutY());

        translate.setDuration(Duration.millis(duration));
        translate.setAutoReverse(true);
        translate.setNode(arrow);
        translate.play();
    }

    public void setPlayerImage(String pane) throws FileNotFoundException {
        String path = "src/main/resources/assets/playerCustomizationScreen/playerAvatars/Character_Window_";
        switch (pane) {
            case "pane1": {
                firstPlayer.setImage(new Image(new FileInputStream(path + selectedColors.get(pane) + ".png")));
                break;
            }
            case "pane2": {
                secondPlayer.setImage(new Image(new FileInputStream(path + selectedColors.get(pane) + ".png")));
                break;
            }
            case "pane3": {
                thirdPlayer.setImage(new Image(new FileInputStream(path + selectedColors.get(pane) + ".png")));
                break;
            }
            case "pane4": {
                fourthPlayer.setImage(new Image(new FileInputStream(path + selectedColors.get(pane) + ".png")));
                break;
            }
        }
    }

    @FXML
    void initialize() {
        assert arrow1 != null : "fx:id=\"arrow1\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert arrow2 != null : "fx:id=\"arrow2\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert arrow3 != null : "fx:id=\"arrow3\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert arrow4 != null : "fx:id=\"arrow4\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert exitButton != null : "fx:id=\"exitButton\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert firstPlayer != null : "fx:id=\"firstPlayer\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert fourthPlayer != null : "fx:id=\"fourthPlayer\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert leftAddButton != null : "fx:id=\"leftAddButton\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert pane1 != null : "fx:id=\"pane1\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert pane2 != null : "fx:id=\"pane2\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert pane3 != null : "fx:id=\"pane3\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert pane4 != null : "fx:id=\"pane4\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert rightAddButton != null : "fx:id=\"rightAddButton\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert secondPlayer != null : "fx:id=\"secondPlayer\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";
        assert thirdPlayer != null : "fx:id=\"thirdPlayer\" was not injected: check your FXML file 'PlayerCustomizationScreen-view.fxml'.";

    }

}
