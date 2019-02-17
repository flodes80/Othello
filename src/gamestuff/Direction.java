package gamestuff;

public enum Direction {

    // COLONNE / LIGNE
    /* ↑ */ NORTH(0, -1),
    /* ↓ */ SOUTH(0, 1),
    /* ← */ WEST(-1, 0),
    /* → */ EAST(1, 0),
    /* ↖ */ NORTHWEST(-1, -1),
    /* ↘ */ SOUTHEAST(1, 1),
    /* ↙ */ SOUTHWEST(-1, 1),
    /* ↗ */ NORTHEAST(1, -1);
    private int rowstep;
    private int colstep;

    Direction(int colstep, int rowstep) {
        this.rowstep = rowstep;
        this.colstep = colstep;
    }

    public int getRowstep() {
        return rowstep;
    }

    public int getColstep() {
        return colstep;
    }

}