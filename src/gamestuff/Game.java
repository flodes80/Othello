package gamestuff;

import gui.controllers.GameController;
import javafx.scene.paint.Color;

public class Game {

    private Player player1, player2;
    private BoardGame boardGame;
    private Player currentPlayer;
    private GameController gameController;

    public Game(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        boardGame = new BoardGame();
        currentPlayer = player1;
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
    }

    public void play(int colonne, int ligne){
        boolean placed;
        if(currentPlayer.getColor() == Color.WHITE)
            placed = boardGame.add((byte) 0, colonne, ligne);
        else
            placed = boardGame.add((byte) 1, colonne, ligne);

        if(placed)  // Si le pion a été placé
            switchCurrentPlayer();
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

}
