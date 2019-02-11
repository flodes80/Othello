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
    GridPane gridPaneGame;

   private Image black, white, empty;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        black = new Image("img/blackDisk.png");
        white = new Image("img/whiteDisk.png");
        empty = new Image("img/emptyDisk.png");
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){

                gridPaneGame.add(new ImageView(empty), i, j);
            }
        }
        gridPaneGame.add(new ImageView(white), 3, 3);
        gridPaneGame.add(new ImageView(black), 3, 4);
        gridPaneGame.add(new ImageView(black), 4, 3);
        gridPaneGame.add(new ImageView(white), 4, 4);

    }

    @FXML
    private void clickGrid(MouseEvent event) {
        Node source = (Node)event.getTarget() ;
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        System.out.printf("Mouse clicked cell [%d, %d]%n", colIndex, rowIndex);

        gridPaneGame.add(new ImageView(white), colIndex, rowIndex);

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


}