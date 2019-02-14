package gamestuff;

import javafx.scene.paint.Color;

public class Player {

    private String name;
    private boolean ai;
    private Color color;
    private int score;
    private int wins;

    public Player(String name, boolean ai, Color color){
        this.name = name;
        this.ai = ai;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAi() {
        return ai;
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
