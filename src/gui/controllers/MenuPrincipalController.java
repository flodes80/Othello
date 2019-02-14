package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuPrincipalController{

    @FXML
    AnchorPane anchorPaneMain;
    @FXML
    Button buttonBackArrow;
    private MainController mainController;

    @FXML
    private void handleButtonLocalAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/interfaces/MenuJvsJ.fxml"));
        Pane registerPane = fxmlLoader.load();
        mainController.setSousMenuController(fxmlLoader.getController());
        try {
            anchorPaneMain.getChildren().clear();
            anchorPaneMain.getChildren().add(registerPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonIAAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/interfaces/MenuJvsIA.fxml"));
        Pane registerPane = fxmlLoader.load();
        mainController.setSousMenuController(fxmlLoader.getController());
        try {
            anchorPaneMain.getChildren().clear();
            anchorPaneMain.getChildren().add(registerPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonChargerAction(ActionEvent event) throws IOException {
        System.out.println("Clic chargement!");

    }

    public void setMainController(MainController mainController){
        if(this.mainController == null)
            this.mainController = mainController;
    }

}
