package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ExitController implements Initializable {

    MainController mainController;
    Stage exitStage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    private void noExitAction() {
        exitStage.close();
    }

    @FXML
    private void yesExitAction() {
        exitStage.close();
        mainController.getGameStage().close();
    }


    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setExitStage(Stage exitStage) {
        this.exitStage = exitStage;
    }
}
