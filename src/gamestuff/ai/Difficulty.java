package gamestuff.ai;

/**
 * La difficulté changera la profondeur maximale de recherche de l'IA
 */
public enum Difficulty {

    FACILE(4),
    NORMAL(8),
    DIFFICILE(12);

    private final int depth;

    Difficulty(int depth) {
        this.depth = depth;
    }

    public static Difficulty getDifficulty(int value){
        if (value == 0)
            return Difficulty.FACILE;
        else if (value == 1)
            return Difficulty.NORMAL;
        else
            return Difficulty.DIFFICILE;
    }

    public int getDepth() {
        return depth;
    }
}
