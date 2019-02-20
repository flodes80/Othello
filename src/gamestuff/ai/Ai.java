package gamestuff.ai;

import gamestuff.BoardGame;
import gamestuff.Game;

public class Ai {

    private Game game;
    private BoardGame boardGame;

    public Ai(Game game) {
        this.game = game;
        this.boardGame = game.getBoardGame();
    }

}
