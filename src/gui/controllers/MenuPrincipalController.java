package gui.controllers;

import gamestuff.Game;
import gamestuff.Player;
import gamestuff.ResourceManager;
import gamestuff.SaveData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class MenuPrincipalController {

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

    // Chargement d'une partie
    @FXML
    private void handleButtonChargerAction(ActionEvent event) throws IOException {
        byte[][] loadedBoard = new byte[0][];
        String s_player1 = null, s_player2 = null, s_currentPlayer = null;

        //On choisit le fichier et on récupère les données
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        try {
            SaveData data = (SaveData) ResourceManager.load(file.toString());
            loadedBoard = data.board;
            s_player1 = data.player1;
            s_player2 = data.player2;
            s_currentPlayer = data.currentPlayer;
        } catch (Exception e) {
            System.out.println("error loading : " + e.getMessage());
        }

        // Parametrage de la partie
        Player currentPlayer;
        Player player1 = new Player(s_player1, Color.WHITE, false);
        Player player2 = new Player(s_player2, Color.BLACK, false);

        // On determine quel joueur doit commencer
        currentPlayer = (s_currentPlayer.equals(s_player1)) ? player1 : player2;

        // On lance la partie
        mainController.launchGame(new Game(player1, player2, loadedBoard, currentPlayer));

        //On supprime tous les disks
        mainController.getGame().getGameController().removeAll();

        // On ajoute ceux correpondant au plateau
        byte[][] gameBoard = mainController.getGame().getBoardGame().getBoard();
        mainController.getGame().getGameController().placeDisksAccordingToBoard(gameBoard);

        // Recalcule du score et affichage
        if (currentPlayer.equals(player1)) {
            mainController.getGame().getGameController().getRectangleJoueur1().setVisible(true);
            mainController.getGame().getGameController().getRectangleJoueur2().setVisible(false);
        } else {
            mainController.getGame().getGameController().getRectangleJoueur1().setVisible(false);
            mainController.getGame().getGameController().getRectangleJoueur2().setVisible(true);
        }
        int scoreJ1 = mainController.getGame().getBoardGame().calculPiecePlayer1();
        int scoreJ2 = mainController.getGame().getBoardGame().calculPiecePlayer2();
        mainController.getGame().getGameController().getLabelScoreJ1().setText(String.valueOf(scoreJ1));
        mainController.getGame().getGameController().getLabelScoreJ2().setText(String.valueOf(scoreJ2));
    }


    public void setMainController(MainController mainController) {
        if (this.mainController == null)
            this.mainController = mainController;
    }

}
