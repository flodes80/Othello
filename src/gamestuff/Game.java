package gamestuff;

import gui.controllers.GameController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

public class Game {

    private Player player1, player2;
    private Player currentPlayer;
    private BoardGame boardGame;
    private GameController gameController;
    private ImageView black, white;

    public Game(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        boardGame = new BoardGame();
        currentPlayer = player1;
        black = new ImageView(new Image("img/blackDisk.png"));
        white = new ImageView(new Image("img/whiteDisk.png"));
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setGameController(GameController gameController){
        this.gameController = gameController;
        boardGame.setGameController(gameController);
        gameController.showAvailablesMoves(boardGame.getAvailablesMoves(getPlayerValue(currentPlayer)), getPlayerValue(currentPlayer));
    }

    public void play(int colonne, int ligne){
        boolean placed;
        if(currentPlayer.getColor() == Color.WHITE)
            placed = boardGame.add((byte) 0, colonne, ligne);
        else
            placed = boardGame.add((byte) 1, colonne, ligne);

        // Si le pion a été placé
        if (placed) {
            // Requête d'affichage des scores
            gameController.getLabelScoreJ1().setText(String.valueOf(boardGame.calculPiecePlayer1()));
            gameController.getLabelScoreJ2().setText(String.valueOf(boardGame.calculPiecePlayer2()));

            // Changement de joueur
            switchCurrentPlayer();

            // Requête d'affichage des mouvements disponibles
            gameController.showAvailablesMoves(boardGame.getAvailablesMoves(getPlayerValue(currentPlayer)), getPlayerValue(currentPlayer));
        }

    }

    private void switchCurrentPlayer(){
        if(currentPlayer == player1) {
            currentPlayer = player2;
            gameController.getRectangleJoueur1().setVisible(false);
            gameController.getRectangleJoueur2().setVisible(true);
        }
        else {
            currentPlayer = player1;
            gameController.getRectangleJoueur1().setVisible(true);
            gameController.getRectangleJoueur2().setVisible(false);
        }
    }

    private byte getPlayerValue(Player player) {
        return player == player1 ? (byte) 0 : (byte) 1;
    }

}
