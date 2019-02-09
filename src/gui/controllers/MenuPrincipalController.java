package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MenuPrincipalController{

    @FXML
    AnchorPane anchorPaneMain;
    @FXML
    Button buttonBackArrow;

    @FXML
    private void handleButtonLocalAction(ActionEvent event) throws IOException {
        goToMenu("/gui/interfaces/MenuJvsJ.fxml");
    }

    @FXML
    private void handleButtonIAAction(ActionEvent event) throws IOException {
        goToMenu("/gui/interfaces/MenuJvsIA.fxml");
    }

    @FXML
    private void handleButtonChargerAction(ActionEvent event) throws IOException {
        System.out.println("Clic chargement!");

    }

    private void goToMenu(String p_menu) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(p_menu));
        Pane registerPane = fxmlLoader.load();
        try {
            anchorPaneMain.getChildren().clear();
            anchorPaneMain.getChildren().add(registerPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
