package gui.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

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

    private Image white, black, hint;

    private MainController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        white = new Image("img/whiteDisk.png");
        black = new Image("img/blackDisk.png");
        hint = new Image("img/hintDisk.png");
        gridPaneGame.add(new ImageView(white), 3, 3);
        gridPaneGame.add(new ImageView(black), 4, 3);
        gridPaneGame.add(new ImageView(black), 3, 4);
        gridPaneGame.add(new ImageView(white), 4, 4);
        rectangleJoueur1.setVisible(true);
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

    public void showAvailablesMoves(byte[][] availablesMoves, byte valuePlayer) {
        removeOldHint();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (availablesMoves[i][j] == (byte) 1) {
                    if (valuePlayer == 0) {
                        replaceNodeGridPane(i, j, new ImageView(hint));
                    } else {
                        replaceNodeGridPane(i, j, new ImageView(hint));
                    }
                }
            }
        }
    }

    private void removeOldHint() {
        Iterator<Node> iter = gridPaneGame.getChildren().iterator();
        while (iter.hasNext()) {
            Node node = iter.next();
            if (node instanceof ImageView) {
                ImageView image = (ImageView) node;
                if (image.getImage().equals(hint)) {
                    iter.remove();
                }
            }
        }
    }

    public void replaceNodeGridPane(int column, int row, Node newNode) {
        // On supprimme d'abord ce qu'il y avait avant à cet emplacement
        ObservableList<Node> childrens = gridPaneGame.getChildren();
        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                gridPaneGame.getChildren().remove(node);
                break;
            }
        }
        // Puis on ajoute
        gridPaneGame.add(newNode, column, row);
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