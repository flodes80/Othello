package gamestuff;

import gui.controllers.GameController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;

public class BoardGame {

    //Colonne / Ligne
    private byte[][] board;

    private GameController gameController;
    private Image black, white;

    public BoardGame(){
        this.board = new byte[8][8];
        for(byte[] ligne : board)
            Arrays.fill(ligne, (byte)-1); // Les cases vides sont égales à -1
        board[3][3] = 0;            // Pions blancs = 0
        board[4][3] = 1;            // Pions noirs = 1
        board[3][4] = 1;
        board[4][4] = 0;
        black = new Image("img/blackDisk.png");
        white = new Image("img/whiteDisk.png");
    }

    /**
     * Ajout d'un pion sur le plateau
     *
     * @param value   valeur du pion
     * @param colonne
     * @param ligne
     * @return
     */
    public boolean add(byte value, int colonne, int ligne){
        if(isEmpty(board[colonne][ligne])){
            board[colonne][ligne] = value;
            if (value == 0 && isAnAvailableMove(value, colonne, ligne)) {
                gameController.replaceNodeGridPane(colonne, ligne, new ImageView(white));
                return true;
            } else if (value == 1 && isAnAvailableMove(value, colonne, ligne)) {
                gameController.replaceNodeGridPane(colonne, ligne, new ImageView(black));
                return true;
            }
        }
        return false;
    }

    /**
     * Check si la position choisie est une position "légale"
     *
     * @param value Valeur du pion joué
     * @param col   Colonne jouée
     * @param ligne Ligne jouée
     * @return
     */
    private boolean isAnAvailableMove(byte value, int col, int ligne) {
        byte[][] availableMoves = getAvailablesMoves(value);
        return availableMoves[col][ligne] == 1;
    }

    /**
     * Obtenir les mouvements possible pour un joueur à partir de la valeur d'un pion
     *
     * @param value valeur des pions du joueur
     * @return Tableau de byte contenant les mouvements disponibles marqués par un "1"
     */
    public byte[][] getAvailablesMoves(byte value) {
        byte[][] moves = new byte[8][8];
        // Parcours de tout le plateau
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                // Check si couleur est celle recherchée
                if (board[i][j] == value) {
                    // Parcours de toutes les directions possibles
                    for (Direction direction : Direction.values()) {
                        int col = i;
                        int row = j;
                        byte previousValue = value;
                        byte currentValue = board[col][row];
                        while (col <= 7 && row <= 7 && col >= 0 && row >= 0 && currentValue != -1) {
                            col += direction.getColstep();
                            row += direction.getRowstep();
                            previousValue = currentValue;
                            currentValue = board[col][row];
                            if (currentValue == value)
                                break;
                        }
                        if (currentValue == -1 && previousValue != value)
                            moves[col][row] = 1;
                    }
                }
            }
        }
        return moves;
    }

    /**
     * La valeur de la case est-elle vide ?
     *
     * @param value
     * @return
     */
    private boolean isEmpty(byte value){
        return value == -1;
    }

    public void setGameController(GameController gameController){
        this.gameController = gameController;
    }

    public int calculPiecePlayer1(){
        int score = 0;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if (board[i][j] == 0)
                    score++;
            }
        }
        return score;
    }

    public int calculPiecePlayer2(){
        int score = 0;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if (board[i][j] == 1)
                    score++;
            }
        }
        return score;
    }
}
