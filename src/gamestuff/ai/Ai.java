package gamestuff.ai;

public class Ai {

    public static ScoredMove alphaBeta(Node node, int prof, int alpha, int beta) {
        int nbr;
        ScoredMove move = new ScoredMove();
        if (node.isALeaf() || prof == 0)
            return move;
        else {
            if (node.isMaxNode()) {

                move.setValue(Integer.MIN_VALUE);
                nbr = node.getChildrensNodesAmount();
                Node f = node.getFirstChildrenNode();

                while (move.getValue() < beta && nbr != 0) {
                    if (f == null)
                        break;
                    move.setCol(f.getCol());
                    move.setRow(f.getRow());
                    move.setValue(Integer.max(move.getValue(), alphaBeta(f, prof - 1, alpha, beta).getValue()));
                    alpha = Integer.max(alpha, move.getValue());
                    nbr--;
                    f = node.getBrotherNode();
                }
            } else {

                move.setValue(Integer.MAX_VALUE);
                nbr = node.getChildrensNodesAmount();
                Node f = node.getFirstChildrenNode();

                while (move.getValue() > alpha && nbr != 0) {
                    if (f == null)
                        break;
                    move.setCol(f.getCol());
                    move.setRow(f.getRow());
                    move.setValue(Integer.min(move.getValue(), alphaBeta(f, prof - 1, alpha, beta).getValue()));
                    beta = Integer.min(beta, move.getValue());
                    nbr--;
                    f = node.getBrotherNode();
                }
            }
        }
        return move;
    }
}
