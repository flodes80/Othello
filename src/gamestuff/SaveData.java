package gamestuff;

import java.io.Serializable;

public class SaveData implements Serializable {

    private static final long serialVersionUID = -1941598707357105953L;
    private String player1;
    private String player2;
    private String currentPlayer;
    private byte[][] board;
    private boolean ai;

    public SaveData(String player1, String player2, String currentPlayer, byte[][] board, boolean ai) {
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = currentPlayer;
        this.board = board;
        this.ai = ai;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public byte[][] getBoard() {
        return board;
    }

    public boolean isAi() {
        return ai;
    }
}
