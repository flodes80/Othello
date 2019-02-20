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

    public int alphaBeta(Node node, int prof, int alpha, int beta) {
        int m, nbr;
        if (node.isALeaf())
            return node.getSelfEvaluation();
        else {
            if (node.isMaxNode()) {
                m = Integer.MIN_VALUE;
                nbr = node.getChildrensNodesAmount();
                Node f = node.getFirstChildrenNode();
                while (m < beta && nbr != 0) {
                    m = Integer.max(m, alphaBeta(f, prof - 1, alpha, beta));
                    alpha = Integer.max(alpha, m);
                    nbr--;
                    f = node.getBrotherNode();
                }
            } else {
                m = Integer.MAX_VALUE;
                nbr = node.getChildrensNodesAmount();
                Node f = node.getFirstChildrenNode();
                while (m > alpha && nbr != 0) {
                    m = Integer.min(m, alphaBeta(f, prof - 1, alpha, beta));
                    beta = Integer.min(beta, m);
                    nbr--;
                    f = node.getBrotherNode();
                }
            }
        }
        return m;
    }
}
