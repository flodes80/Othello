package gamestuff;

import gamestuff.ai.Ai;
import javafx.scene.paint.Color;

public class Player {

    private String name;
    private Ai ai;
    private Color color;
    private int score;
    private int wins;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public byte[] getAiPlay(BoardGame boardGame) {
        byte[] move = new byte[2];

        return move;
    }

    public boolean isAi() {
        return ai != null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAi(Ai ai) {
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
