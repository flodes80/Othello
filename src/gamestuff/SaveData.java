package gamestuff;

import java.io.Serializable;

public class SaveData implements Serializable {

    private static final long serialVersionUID = -1941598707357105953L;
    public String player1;
    public String player2;
    public String currentPlayer;
    public byte[][] board;

}
