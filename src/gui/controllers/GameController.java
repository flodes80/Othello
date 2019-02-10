package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
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
        for (int i = 0; i < 8; i++){ //Per righe
            for (int j = 0; j < 8; j++){ // Per colonne

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
        //black = new Image("img/test.png");
        //white = new Image("img/test.png");
        //empty = new Image("img/test.png");

        Node source = (Node)event.getTarget() ;
        Integer colIndex = GridPane.getColumnIndex(source);
        Integer rowIndex = GridPane.getRowIndex(source);
        System.out.printf("Mouse clicked cell [%d, %d]%n", colIndex, rowIndex);

        gridPaneGame.add(new ImageView(white), colIndex, rowIndex);

    }

}