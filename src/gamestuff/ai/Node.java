package gamestuff.ai;

import gamestuff.BoardGame;

public class Node {

    byte[][] enemyAvailablesMoves;
    private BoardGame boardGame;
    private int col, row;
    private byte value, enemyValue;

    // Sert à connaitre le dernier enfant parcouru pour obtenir le noeud frère
    private int lastColChildren, lastRowChildren;

    public Node(byte value, int col, int row, BoardGame boardGame) {
        this.value = value;
        this.enemyValue = value == 0 ? (byte) 1 : (byte) 0;
        this.col = col;
        this.row = row;
        this.boardGame = new BoardGame(boardGame.getBoard());
        enemyAvailablesMoves = this.boardGame.getAvailablesMoves(enemyValue);
    }

    /**
     * Obtenir le nombre de noeuds enfants
     *
     * @return le nombre d'enfants du noeud
     */
    public int getChildrensNodesAmount() {
        // Pour obtenir le nombre d'enfant d'un noeud il faut d'abord déterminer si ce dernier est un noeud max ou min
        // Ensuite on retourne le nombre de mouvements adverses possibles pour ce noeud
        // Il faut retourner le nombre de mouvements possibles adverses car on change à chaque tour de joueur, ça n'aurait pas de sens sinon
        return boardGame.getAvailablesMovesAmount(enemyValue);
    }

    /**
     * Obtenir le noeud "frère" du précédent noeud sélectionné via lastColChildren et lastRowChildren
     *
     * @return le noeud frère du précédent noeud
     */
    public Node getBrotherNode() {
        for (int i = lastColChildren; i < enemyAvailablesMoves.length; i++) {
            for (int j = lastRowChildren + 1; j < enemyAvailablesMoves.length; j++) {
                if (enemyAvailablesMoves[i][j] == 1) {
                    boardGame.add(enemyValue, i, j);
                    lastColChildren = i;
                    lastRowChildren = j;
                    return new Node(enemyValue, i, j, boardGame);
                }
            }
        }
        return null;
    }

    /**
     * Obtenir le premier noeud enfant du noeud actuel
     * Pour cela on renvoie un noeud avec le premier emplacement adverse "jouable"
     *
     * @return le premier noeud enfant
     */
    public Node getFirstChildrenNode() {
        for (int i = 0; i < enemyAvailablesMoves.length; i++) {
            for (int j = 0; j < enemyAvailablesMoves.length; j++) {
                if (enemyAvailablesMoves[i][j] == 1) {
                    boardGame.add(enemyValue, i, j);
                    lastColChildren = i;
                    lastRowChildren = j;
                    return new Node(enemyValue, i, j, boardGame);
                }
            }
        }
        return null;
    }

    /**
     * Savoir si le noeud est une feuille ou non
     *
     * @return Retourne vrai si c'est une feuille sinon faux
     */
    public boolean isALeaf() {
        return boardGame.calculEmptyCase() == 0;
    }

    /**
     * Retourne l'évaluation de lui même
     *
     * @return La différence de ses propres pions et de celle de l'adversaire
     */
    public int getSelfEvaluation() {
        return boardGame.calculPiecePlayer2() - boardGame.calculPiecePlayer1();
    }

    /**
     * Obtenir la colonne jouée pour obtenir ce noeud
     *
     * @return colonne jouée
     */
    public int getCol() {
        return col;
    }

    /**
     * Obtenir la ligne jouée pour obtenir ce noeud
     *
     * @return ligne jouée
     */
    public int getRow() {
        return row;
    }

    /**
     * Le noeud est-il un noeud max ?
     * Cela siginifie que c'est au tour de l'ordinateur
     *
     * @return vrai si c'est un noeud max et faux si c'en est pas un
     */
    public boolean isMaxNode() {
        return value == 1;
    }
}
