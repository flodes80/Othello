package gamestuff;

import gui.controllers.GameController;

import java.util.Arrays;

public class BoardGame {

    //Colonne / Ligne
    private byte[][] board;
    private GameController gameController;

    public BoardGame(){
        this.board = new byte[8][8];
        for(byte[] ligne : board)
            Arrays.fill(ligne, (byte)-1); // Les cases vides sont égales à -1
        board[3][3] = 0;                  // Pions blancs = 0
        board[4][3] = 1;                  // Pions noirs = 1
        board[3][4] = 1;
        board[4][4] = 0;
    }

    public BoardGame(byte[][] board) {
        this.board = new byte[8][8];
        for (int i = 0; i < board.length; i++) {
            this.board[i] = board[i].clone();
        }
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
        if (isEmpty(board[colonne][ligne]) && isAnAvailableMove(value, colonne, ligne)) {
            board[colonne][ligne] = value;
            if (!isAiBoardGame())
                gameController.addNewDisk(value, colonne, ligne);
            revertPions(value, colonne, ligne);
            return true;
        }
        return false;
    }

    private void revertPions(byte value, int colonne, int ligne) {
        byte[][] pionsToRevert = getPionsToRevert(value, colonne, ligne);
        for (int i = 0; i < pionsToRevert.length; i++) {
            for (int j = 0; j < pionsToRevert.length; j++) {
                if (pionsToRevert[i][j] == 1) {
                    board[i][j] = value;
                    if (!isAiBoardGame())
                        gameController.flipDisk(value, i, j);
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
                pionsToRevert[col][row] = 0;
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
     * Obtenir le nombre de mouvements légaux disponibles
     *
     * @param value valeur jouée
     * @return nombre de mouvements légaux disponibles
     */
    public int getAvailablesMovesAmount(byte value) {
        int moves = 0;
        byte[][] availablesMoves = getAvailablesMoves(value);
        for (int i = 0; i < availablesMoves.length; i++) {
            for (int j = 0; j < availablesMoves.length; j++)
                if (availablesMoves[i][j] == 1)
                    moves++;
        }
        return moves;
    }


    /**
     * Remplir les cellules vides par une valeur
     * (Utile lorsque aucun joueur ne peut jouer)
     *
     * @param value Valeur du pion à jouer
     */
    public void fillEmptyCell(byte value) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (isEmpty(board[i][j])) {
                    board[i][j] = value;
                    gameController.addNewDisk(value, i, j);
                }
            }
        }
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

    private boolean isAiBoardGame() {
        return gameController == null;
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

    public int calculEmptyCase() {
        int emptyCase = 0;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if (isEmpty(board[i][j]))
                    emptyCase++;
            }
        }
        return emptyCase;
    }

    public byte[][] getBoard() {
        return board;
    }

    /**
     * Debug
     */
    public void show() {
        System.out.println("-----");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                String str = String.valueOf(board[j][i]);
                if (str.length() == 1)
                    str = " " + str;
                System.out.print(" " + str);
            }
            System.out.print("\n");
        }
        System.out.println("-----");
    }
}
