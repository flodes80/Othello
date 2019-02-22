package gui.controllers;

import gamestuff.Game;
import gamestuff.Player;
import gamestuff.ResourceManager;
import gamestuff.SaveData;
import javafx.animation.RotateTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    Label labelJoueur1, labelJoueur2, labelWinnerName, labelScoreJ1, labelScoreJ2;;

    @FXML
    ImageView imageGameOver;

    @FXML
    GridPane gridPaneGame;

    @FXML
    Rectangle rectangleJoueur1, rectangleJoueur2;

    @FXML
    Button buttonRejouer, buttonQuitter;

    @FXML
    AnchorPane anchorPane;

    @FXML
    ProgressIndicator aiIndicator;

    private Image hint;

    private MainController mainController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hint = new Image("img/hintDisk.png");

        gridPaneGame.add(createDisk(false), 3, 3);
        gridPaneGame.add(createDisk(true), 4, 3);
        gridPaneGame.add(createDisk(true), 3, 4);
        gridPaneGame.add(createDisk(false), 4, 4);
        rectangleJoueur1.setVisible(true);
    }

    @FXML
    private void clickGrid(MouseEvent event) {
        // Si on essaie de jouer à la place de l'ia
        if (mainController.getGame().getCurrentPlayer() == mainController.getGame().getPlayer2() && mainController.getGame().getCurrentPlayer().isAi())
            return;

        double xSource = event.getSceneX();
        double ySource = event.getSceneY();
        for (Node children : gridPaneGame.getChildren()) {
            if (children instanceof ImageView) {
                Bounds bounds = children.localToScene(children.getBoundsInLocal());
                if (bounds.contains(xSource, ySource)) {
                    Integer colIndex = GridPane.getColumnIndex(children);
                    Integer rowIndex = GridPane.getRowIndex(children);
                    mainController.getGame().play(colIndex, rowIndex);
                    break;
                }
            }
        }
    }


    @FXML
    private void clickRegles() {
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

    public void addNewDisk(byte value, int colonne, int ligne) {
        boolean blackSideUp = value != 0;
        gridPaneGame.add(createDisk(blackSideUp), colonne, ligne);
        soundFlipDisk();
    }

    private Group createDisk(boolean blackSideUp) {
        Group group = new Group();

        group.setRotationAxis(Rotate.Y_AXIS);


        PhongMaterial blackMaterial = new PhongMaterial(Color.WHITE);
        PhongMaterial whiteMaterial = new PhongMaterial(Color.WHITE);

        Image imageWhite = new Image("img/whiteDisk.png");
        Image imageBlack = new Image("img/blackDisk.png");

        whiteMaterial.setDiffuseMap(imageWhite);
        blackMaterial.setDiffuseMap(imageBlack);

        Cylinder blackPart = new Cylinder();
        blackPart.setRadius(31);
        blackPart.setHeight(1.0f);
        blackPart.setTranslateZ(-29.0f);
        blackPart.setRotationAxis(Rotate.X_AXIS);
        blackPart.setRotate(90.0f);
        blackPart.setMaterial(blackMaterial);

        Cylinder whitePart = new Cylinder();
        whitePart.setRadius(31);
        whitePart.setHeight(1.0f);
        whitePart.setTranslateZ(-32.0f);
        whitePart.setRotationAxis(Rotate.X_AXIS);
        whitePart.setRotate(90.0f);
        whitePart.setMaterial(whiteMaterial);

        if (blackSideUp)
            group.setRotate(180.0f);

        group.getChildren().add(blackPart);
        group.getChildren().add(whitePart);

        return (group);
    }

    /**
     * Retourner un pion
     *
     * @param value   nouvelle valeur du pion
     * @param colonne colonne du pion
     * @param ligne   ligne du pion
     */
    public void flipDisk(byte value, int colonne, int ligne) {
        Group disk = getGroupFromGridPane(colonne, ligne);
        boolean toBlack = value == 0;

        // Effectuer la rotation du pion
        RotateTransition rotation = new RotateTransition(Duration.millis(1000), disk);
        rotation.setFromAngle(toBlack ? 180.0f : 0.0f);
        rotation.setToAngle(toBlack ? 0.0f : 180.0f);

        rotation.play();
    }

    public void showWinFrame(String winner) {
        imageGameOver.setVisible(true);
        labelWinnerName.setText(winner);
        labelWinnerName.setVisible(true);
        labelWinnerName.setAlignment(Pos.CENTER);
        buttonQuitter.setVisible(true);
        buttonRejouer.setVisible(true);
    }

    public void showAvailablesMoves(byte[][] availablesMoves, byte valuePlayer) {
        removeOldHint();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
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

    public void removeAll() {
        gridPaneGame.getChildren().removeIf(node -> node instanceof Group);
    }

    public void placeDisksAccordingToBoard(byte[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != -1) {
                    byte value = board[i][j];
                    addNewDisk(value, i, j);
                }
            }
        }
    }

    private void replaceNodeGridPane(int column, int row, Node newNode) {
        // On supprimme d'abord ce qu'il y avait avant à cet emplacement
        ObservableList<Node> childrens = gridPaneGame.getChildren();
        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                gridPaneGame.getChildren().remove(node);
                break;
            }
        }
        gridPaneGame.add(newNode, column, row);
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        labelJoueur1.setText(mainController.getGame().getPlayer1().getName());
        labelJoueur2.setText(mainController.getGame().getPlayer2().getName());
        // Indicateur pour l'IA
        if (mainController.getGame().getPlayer2().isAi()) {
            aiIndicator.setDisable(false);
            aiIndicator.setVisible(false);
        }
    }

    private Group getGroupFromGridPane(int col, int ligne) {
        for (Node node : gridPaneGame.getChildren()) {
            if (node instanceof Group && GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == ligne) {
                return (Group) node;
            }
        }
        return null;
    }

    public ProgressIndicator getAiIndicator() {
        return aiIndicator;
    }

    // Sauvegarder une partie
    @FXML
    private void clickSave() {
        String player1 = mainController.getGame().getPlayer1().getName();
        String player2 = mainController.getGame().getPlayer2().getName();
        String currentPlayer = mainController.getGame().getCurrentPlayer().getName();
        byte[][] board = mainController.getGame().getBoardGame().getBoard();

        SaveData data = new SaveData();
        data.board = board;
        data.currentPlayer = currentPlayer;
        data.player1 = player1;
        data.player2 = player2;

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);

        try {
            ResourceManager.save(data, file.toString());
        } catch (Exception e) {
            System.out.println("error :" + e.getMessage());
        }
    }

    @FXML
    private void buttonRejouerAction() {
        mainController.getGameStage().close();
        mainController.launchGame(new Game(
                new Player(labelJoueur1.getText(), Color.WHITE, false),
                new Player(labelJoueur2.getText(), Color.BLACK, false)));
    }

    @FXML
    private void buttonQuitterAction() {
        mainController.getGameStage().close();
    }

    private void soundFlipDisk() {
        AudioClip sound = new AudioClip(this.getClass().getResource("/sound/piece_pose_flip.mp3").toExternalForm());
        sound.play();
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