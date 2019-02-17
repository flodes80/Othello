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
        board[3][3] = 0;                  // Pions blancs = 0
        board[4][3] = 1;                  // Pions noirs = 1
        board[3][4] = 1;
        board[4][4] = 0;
        black = new Image("img/blackDisk.png");
        white = new Image("img/whiteDisk.png");
    }

    /**
     * Ajout d'un pion sur le plateau
     *
     * @param value   valeur du pion
     * @param colonne Colonne jouée
     * @param ligne Ligne jouée
     * @return True si le pion a été posé
     */
    public boolean add(byte value, int colonne, int ligne){
        if(isEmpty(board[colonne][ligne])){
            if (value == 0 && isAnAvailableMove(value, colonne, ligne)) {
                board[colonne][ligne] = value;
                gameController.replaceNodeGridPane(colonne, ligne, new ImageView(white));
                revertPions(value, colonne, ligne);
                return true;
            } else if (value == 1 && isAnAvailableMove(value, colonne, ligne)) {
                board[colonne][ligne] = value;
                gameController.replaceNodeGridPane(colonne, ligne, new ImageView(black));
                revertPions(value, colonne, ligne);
                return true;
            }
        }
        return false;
    }

    private void revertPions(byte value, int colonne, int ligne) {
        byte[][] pionsToRevert = getPionsToRevert(value, colonne, ligne);
        for (int i = 0; i < pionsToRevert.length; i++) {
            for (int j = 0; j < pionsToRevert.length; j++) {
                if (pionsToRevert[i][j] == 1) {
                    board[i][j] = value;
                    if (value == 0)
                        gameController.replaceNodeGridPane(i, j, new ImageView(white));
                    else
                        gameController.replaceNodeGridPane(i, j, new ImageView(black));
                }
            }
        }
    }

    /**
     * Obtenir les pions à retourner une fois une pièce placée
     *
     * @param value   valeur jouée
     * @param colonne colonne jouée
     * @param ligne   ligne jouée
     * @return Retourne une tableau de byte marqué de 1 pour les pions à retourner
     */
    private byte[][] getPionsToRevert(byte value, int colonne, int ligne) {
        byte[][] pionsToRevert = new byte[8][8];
        for (Direction direction : Direction.values()) {
            int col = colonne;
            int row = ligne;
            byte currentValue = value;
            boolean flag1 = false;  // J'ai trouvé au moins un pion adverse
            boolean flag2 = false;  // J'ai trouvé un pion "ami" qui n'est pas voisin du pion joué

            // J'essaie de trouver le motif correspondant à une capture
            do {
                col += direction.getColstep();
                row += direction.getRowstep();
                if (col > 7 || row > 7 || col < 0 || row < 0 || currentValue == -1)
                    break;
                currentValue = board[col][row];
                if (currentValue == getEnnemyValue(value) && !flag1)
                    flag1 = true;
                if (flag1 && currentValue == value)
                    flag2 = true;
            } while (col <= board.length && row <= board.length && col >= 0 && row >= 0 && !flag2);

            if (flag1 && flag2) {
                do {
                    col -= direction.getColstep();
                    row -= direction.getRowstep();
                    pionsToRevert[col][row] = 1;
                    currentValue = board[col][row];
                } while (currentValue != value);
            }
        }
        return pionsToRevert;
    }

    /**
     * Check si la position choisie est une position "légale"
     *
     * @param value Valeur du pion joué
     * @param colonne Colonne jouée
     * @param ligne Ligne jouée
     * @return
     */
    private boolean isAnAvailableMove(byte value, int colonne, int ligne) {
        byte[][] availableMoves = getAvailablesMoves(value);
        return availableMoves[colonne][ligne] == 1;
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
                        byte previousValue = -2;
                        byte currentValue = value;
                        do{
                            col += direction.getColstep();
                            row += direction.getRowstep();
                            if (col > 7 || row > 7 || col < 0 || row < 0)
                                break;
                            previousValue = currentValue;
                            currentValue = board[col][row];
                        } while (col <= board.length && row <= board.length && col >= 0 && row >= 0 && currentValue != -1);
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
     * @return true si vide
     */
    private boolean isEmpty(byte value){
        return value == -1;
    }

    private byte getEnnemyValue(byte value) {
        return value == 0 ? (byte) 1 : (byte) 0;
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
