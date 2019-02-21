package gamestuff.ai;

public class ScoredMove {

    private int col, row, value;

    public ScoredMove(int col, int row, int value) {
        this.col = col;
        this.row = row;
        this.value = value;
    }

    public ScoredMove() {
        this.value = 0;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
