package gamestuff;

import gamestuff.ai.AiService;
import gui.controllers.GameController;
import javafx.scene.paint.Color;

public class Game {

    private Player player1, player2, currentPlayer;
    private BoardGame boardGame;
    private GameController gameController;

    public Game(Player player1, Player player2, byte[][] board, Player currentPlayer) {
        this.player1 = player1;
        this.player2 = player2;
        boardGame = new BoardGame();
        boardGame.setBoard(board);
        this.currentPlayer = currentPlayer;
    }

    public Game(Player player1, Player player2){
        this.player1 = player1;
        this.player2 = player2;
        boardGame = new BoardGame();
        currentPlayer = player1;
    }

    /**
     * Jouer un pion
     *
     * @param colonne colonne jouée
     * @param ligne   ligne jouée
     */
    public void play(int colonne, int ligne){
        // Booléen permettant de savoir si le pion à été placé ou non
        boolean placed;

        // Détection du joueur qui joue en fonction de sa couleur
        if(currentPlayer.getColor() == Color.WHITE)
            placed = boardGame.add((byte) 0, colonne, ligne);
        else
            placed = boardGame.add((byte) 1, colonne, ligne);
        // Si le pion a été placé
        if (placed) {
            // Mise à jour des scores des joueurs
            player1.setScore(boardGame.calculPiecePlayer1());
            player2.setScore(boardGame.calculPiecePlayer2());

            // Requête d'affichage des scores
            gameController.getLabelScoreJ1().setText(String.valueOf(player1.getScore()));
            gameController.getLabelScoreJ2().setText(String.valueOf(player2.getScore()));

            // La partie est-elle finie ?
            if (isGameOver()) {
                gameOver();
            }

            // Sinon on continue le déroulement normal
            else {
                // Changement de joueur
                switchCurrentPlayer();

                // On fait jouer l'ia
                if (currentPlayer == player2 && player2.isAi()) {
                    gameController.removeOldHint();
                    // On indique que l'ia est en train de chercher un coup
                    gameController.getAiIndicator().setVisible(true);

                    AiService aiService = new AiService(colonne, ligne, this, gameController, true);
                    aiService.start();
                } else
                    // Requête d'affichage des mouvements disponibles
                    gameController.showAvailablesMoves(boardGame.getAvailablesMoves(getPlayerValue(currentPlayer)), getPlayerValue(currentPlayer));
            }

        }

    }

    /**
     * Détection de fin de partie si il ne reste plus de cases vides
     *
     * @return Vrai si la partie est finie
     */
    private boolean isGameOver() {
        return boardGame.calculEmptyCase() == 0;
    }

    /**
     * Procédure de fin de partie
     */
    private void gameOver() {
        String winner;

        // Determine le gagnant
        if (boardGame.calculPiecePlayer1() > boardGame.calculPiecePlayer2()) {
            winner = player1.getName();
        }
        else if (boardGame.calculPiecePlayer1() < boardGame.calculPiecePlayer2()) {
            winner = player2.getName();
        } else
            winner = "Egalité";

        // Requête d'affichage du vainqueur
        gameController.showWinFrame(winner);

    }

    /**
     * Procédure de changement de joueur
     */
    private void switchCurrentPlayer() {
        // Si le joueur actuel est le joueur 1 et que le joueur 2 peut jouer (mouvements possibles > 0)
        if (currentPlayer == player1 && boardGame.getAvailablesMovesAmount(getPlayerValue(player2)) > 0) {
            currentPlayer = player2;
            gameController.getRectangleJoueur1().setVisible(false);
            gameController.getRectangleJoueur2().setVisible(true);
        } else if (currentPlayer == player2 && boardGame.getAvailablesMovesAmount(getPlayerValue(player1)) > 0) {
            currentPlayer = player1;
            gameController.getRectangleJoueur1().setVisible(true);
            gameController.getRectangleJoueur2().setVisible(false);
        } else if (currentPlayer == player2 && boardGame.getAvailablesMovesAmount(getPlayerValue(player1)) > 0 && boardGame.getAvailablesMovesAmount(getPlayerValue(player2)) > 0) {
            boardGame.fillEmptyCell(getPlayerValue(currentPlayer));
        }
    }

    private byte getPlayerValue(Player player) {
        return player == player1 ? (byte) 0 : (byte) 1;
    }


    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public BoardGame getBoardGame() {
        return boardGame;
    }


    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        boardGame.setGameController(gameController);
        gameController.showAvailablesMoves(boardGame.getAvailablesMoves(getPlayerValue(currentPlayer)), getPlayerValue(currentPlayer));
    }

}
