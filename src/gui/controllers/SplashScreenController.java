package gui.controllers;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import gamestuff.Main;
import gui.misc.AnimationGenerator;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


public class SplashScreenController implements Initializable{

    private double xOffset = 0;
    private double yOffset = 0;
    AnimationGenerator animationGenerator = null;
    @FXML
    private AnchorPane parent;
    @FXML
    private ImageView imageViewSplashScreen;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Image image = new Image("/img/splashscreen.jpg");
            imageViewSplashScreen.setImage(image);

            Parent fxml = FXMLLoader.load(getClass().getResource("/gui/interfaces/Menu.fxml"));       //Chargement de la nouvelle page
            //makeStageDrageable();
            animationGenerator = new AnimationGenerator();
            animationGenerator.applyFadeAnimationOn02(parent, 500, 0.2, 1, 1, (e2) -> {
            animationGenerator.applyFadeAnimationOn01(parent, 500, 1, 1, 1, (e) -> {
                    parent.getChildren().removeAll();
                    parent.getChildren().setAll(fxml);
                });
            });
        } catch (IOException ex) {
            Logger.getLogger(SplashScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*public void makeStageDrageable() {
        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Main.stage.setX(event.getScreenX() - xOffset);
                Main.stage.setY(event.getScreenY() - yOffset);
                Main.stage.setOpacity(0.7f);
            }
        });
        parent.setOnDragDone((e) -> {
            Main.stage.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((e) -> {
            Main.stage.setOpacity(1.0f);
        });
    }*/

}



