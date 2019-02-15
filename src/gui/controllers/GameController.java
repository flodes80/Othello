package gui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static javafx.scene.control.ContentDisplay.CENTER;

public class GameController implements Initializable {

    @FXML
    Label labelJoueur1;

    @FXML
    Label labelJoueur2;

    @FXML
    GridPane gridPaneGame;

    @FXML
    Rectangle rectangleJoueur1, rectangleJoueur2;

    @FXML
    Label labelScoreJ1, labelScoreJ2;

    private MainController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void clickGrid(MouseEvent event) {
        Node source = (Node)event.getTarget() ;
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        if(colIndex != null && rowIndex != null)
            mainController.getGame().play(colIndex, rowIndex);
    }


    @FXML
    private void clickRegles(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/gui/interfaces/Rules.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Othello : Règles");
            stage.getIcons().add(new Image("img/icon.png"));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("erreur");
        }
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        labelJoueur1.setText(mainController.getGame().getPlayer1().getName());
        labelJoueur2.setText(mainController.getGame().getPlayer2().getName());
    }

    public GridPane getGridPaneGame() {
        return gridPaneGame;
    }

    public Rectangle getRectangleJoueur1() {
        return rectangleJoueur1;
    }

    public Rectangle getRectangleJoueur2() {
        return rectangleJoueur2;
    }

    public Label getLabelScoreJ1() {
        return labelScoreJ1;
    }

    public Label getLabelScoreJ2() {
        return labelScoreJ2;
    }
}