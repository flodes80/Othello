package gamestuff;

import gamestuff.ai.Ai;
import gamestuff.ai.Node;
import gamestuff.ai.ScoredMove;
import javafx.scene.paint.Color;

public class Player {

    private String name;
    private boolean ai;
    private Color color;
    private int score;
    private int wins;

    public Player(String name, Color color, boolean ai) {
        this.name = name;
        this.color = color;
        this.ai = ai;
    }

    public ScoredMove getAiPlay(int colPlayerPlayed, int rowPlayerPlayed, BoardGame boardGame) {
        byte playerValue = (byte) 0;
        Node node = new Node(playerValue, colPlayerPlayed, rowPlayerPlayed, boardGame);
        return Ai.alphaBeta(node, 30, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public boolean isAi() {
        return ai;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAi(boolean ai) {
        this.ai = ai;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }
}
