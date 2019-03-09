package gamestuff.ai;

import javafx.concurrent.Task;

import java.util.List;

/**
 * Cette classe est utilisée lorsqu'on utilise le multithreading dans la recherche de l'ia
 */
public class ThreadedAiTask extends Task<Void> {

    private List<int[]> availablesMoves;
    private Node child;
    private int depth;

    public ThreadedAiTask(List<int[]> availablesMoves, Node child, int depth) {
        this.availablesMoves = availablesMoves;
        this.child = child;
        this.depth = depth;
    }


    @Override
    protected Void call() {
        int value = Ai.mtd(child, depth, Integer.MIN_VALUE);
        int[] move = {child.getCol(), child.getRow(), value};
        availablesMoves.add(move);
        return null;
    }
}
