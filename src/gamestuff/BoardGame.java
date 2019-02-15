package gamestuff;

import gui.controllers.GameController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;

public class BoardGame {

    //Colonne / Ligne
    private byte[][] board;

    private GameController gameController;
    private Image black, white, empty;

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
        empty = new Image("img/emptyDisk.png");
    }

    public boolean add(byte value, int colonne, int ligne){
        if(isEmpty(board[colonne][ligne])){
            board[colonne][ligne] = value;
            if(value == 0)
                gameController.getGridPaneGame().add(new ImageView(white), colonne, ligne);
            else
                gameController.getGridPaneGame().add(new ImageView(black), colonne, ligne);
            return true;
        }
        return false;
    }

    private boolean isEmpty(byte value){
        return value == -1;
    }

    public void setGameController(GameController gameController){
        this.gameController = gameController;
        initialize();

    }

    private void initialize(){
        gameController.getGridPaneGame().add(new ImageView(white), 3, 3);
        gameController.getGridPaneGame().add(new ImageView(black), 3, 4);
        gameController.getGridPaneGame().add(new ImageView(black), 4, 3);
        gameController.getGridPaneGame().add(new ImageView(white), 4, 4);
        gameController.getRectangleJoueur1().setVisible(true);
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
