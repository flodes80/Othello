package gamestuff.ai;

public enum Difficulty {

    FACILE(0),
    NORMAL(1),
    DIFFICILE(2);

    private final int value;

    private Difficulty(int value){
        this.value = value;
    }

    public static Difficulty getDifficulty(int value){
        for(Difficulty v : values())
            if(v.value == value)
                return v;
        throw new IllegalArgumentException();
    }
}
