package gamestuff;

public class Player  {

    private String name;
    private boolean ai;
    private int score;

    public Player(String name, boolean ai) {
        this.name = name;
        this.ai = ai;
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}
