package gui.controllers;

import gamestuff.Game;
import gamestuff.Player;
import gamestuff.ai.Difficulty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SousMenuController implements Initializable{

    @FXML
    private AnchorPane  anchorPaneJvsIA, anchorPaneJvsJ;
    @FXML
    private Button buttonBackArrow;
    @FXML
    private TextField textFieldJ1, textFieldJ2;
    @FXML
    private Slider sliderIA;
    @FXML
    private Text labelDifficulty;

    private MainController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Bouton retour au menu
        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("/img/backarrow.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        Button button = buttonBackArrow;
        button.setBackground(background);

        //Slider + label
        if(sliderIA != null){
            sliderIA.valueProperty().addListener((observable, oldValue, newValue) -> labelDifficulty.setText(Difficulty.getDifficulty(newValue.intValue()).name()));
        }
    }

    @FXML
    private void handleButtonBackArrow(ActionEvent event) throws IOException {
        backToMainMenu(anchorPaneJvsIA);
    }

    @FXML
    private void handleButtonBackArrow2(ActionEvent event) throws IOException {
        backToMainMenu(anchorPaneJvsJ);
    }

    @FXML
    private void handleButtonValidateJvsJAction(ActionEvent event) throws IOException {
        if ((textFieldJ1.getText().equals("") || textFieldJ2.getText().equals(""))) {
            System.out.println("vide");
        } else {
            mainController.launchGame(new Game(new Player(textFieldJ1.getText(), false, Color.WHITE),
                    new Player(textFieldJ2.getText(), false, Color.BLACK)));
        }
    }

    @FXML
    private void handleButtonValidateJvsIAAction(ActionEvent event) throws IOException {
        mainController.launchGame(new Game(new Player(textFieldJ1.getText(), false, Color.WHITE),   //maincontroller = null
                new Player("Ordinateur", true, Color.BLACK)));
    }

    private void backToMainMenu(AnchorPane p_anchorPane) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/interfaces/Menu.fxml"));
        Pane registerPane = fxmlLoader.load();
        MenuPrincipalController menuPrincipalController = fxmlLoader.getController();
        menuPrincipalController.setMainController(mainController);
        try {
            p_anchorPane.getChildren().clear();
            p_anchorPane.getChildren().add(registerPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

}
