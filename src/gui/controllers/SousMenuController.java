package gui.controllers;

import gamestuff.Game;
import gamestuff.Player;
import gamestuff.ai.Ai;
import gamestuff.ai.Difficulty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

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
    @FXML
    private Label labelErrorMessage;

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
    private void handleButtonBackArrow() throws IOException {
        backToMainMenu(anchorPaneJvsIA);
    }

    @FXML
    private void handleButtonBackArrow2() throws IOException {
        backToMainMenu(anchorPaneJvsJ);
    }

    @FXML
    private void handleButtonValidateJvsJAction() {
        if ((textFieldJ1.getText().equals("") || textFieldJ2.getText().equals(""))) {
            labelErrorMessage.setText("Veuillez remplir tous les champs");
            labelErrorMessage.setAlignment(Pos.CENTER);
            labelErrorMessage.setVisible(true);
        }
        else if (textFieldJ1.getText().equals(textFieldJ2.getText())){
            labelErrorMessage.setText("Veuillez choisir des noms différents");
            labelErrorMessage.setAlignment(Pos.CENTER);
            labelErrorMessage.setVisible(true);
        }
        else if (textFieldJ1.getText().length() > 15 || textFieldJ2.getText().length() > 15){
            labelErrorMessage.setText("Veuillez inscrire des noms de moins de 15 caractères");
            labelErrorMessage.setAlignment(Pos.CENTER);
            labelErrorMessage.setVisible(true);
        }
        else {
            mainController.launchGame(new Game(
                    new Player(textFieldJ1.getText(), Color.WHITE, false),
                    new Player(textFieldJ2.getText(), Color.BLACK, false)));
        }
    }

    @FXML
    private void handleButtonValidateJvsIAAction() {
        mainController.launchGame(new Game(
                new Player(textFieldJ1.getText(), Color.WHITE, false),
                new Player("Ordinateur", Color.BLACK, true)));
        Ai.difficulty = Difficulty.getDifficulty((int) sliderIA.getValue());
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
