package gamestuff.ai;

import gamestuff.BoardGame;

import java.util.ArrayList;

/**
 * Un noeud correspond à un état du jeu
 */
public class Node {

    private byte[][] availablesMoves;
    private BoardGame boardGame;
    private int col;
    private int row;
    private byte valueToPlay, ennemyValue;

    /**
     * Constructeur d'un noeud
     *
     * @param valueToPlay Valeur du pion à jouer
     * @param col         Colonne du pion à jouer
     * @param row         Ligne du pion à jouer
     * @param boardGame   boardGame avant avoir joué le pion
     */
    public Node(byte valueToPlay, int col, int row, BoardGame boardGame) {
        this.valueToPlay = valueToPlay;
        this.ennemyValue = valueToPlay == 0 ? (byte) 1 : 0; // On set simplement le pion adverse en fonction du pion que l'on vient de jouer
        this.col = col;
        this.row = row;

        // On récupère le BoardGame passé en paramètre
        this.boardGame = new BoardGame(boardGame.getBoard());

        // Puis on l'update en ajoutant la valeur jouée afin de ne pas se retrouver avec le même tableau de jeu
        // Ce qui n'aurait aucun sens dans le sens inverse
        this.boardGame.add(valueToPlay, col, row);

        // Puis on obtient les mouvements adverses disponibles après avoir joué notre coup
        availablesMoves = this.boardGame.getAvailablesMoves(ennemyValue);
    }

    /**
     * Permet d'obtenir les noeuds fils du noeud actuel.
     * Pour se faire on va simplement parcourir les mouvements adverses possibles à partir de la situation actuelle
     * @return Les noeuds fils du noeud actuel
     */
    public ArrayList<Node> getChildrensNodes() {
        ArrayList<Node> childrens = new ArrayList();
        for (int i = 0; i < availablesMoves.length; i++) {
            for (int j = 0; j < availablesMoves.length; j++) {
                // Si la case est égale à 1, c'est un coup possible donc on ajoute ce nouveau noeud à la liste
                if (availablesMoves[i][j] == 1) {
                    childrens.add(new Node(ennemyValue, i, j, boardGame));
                }
            }
        }
        return childrens;
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
     * Retourne l'évaluation du noeud lui même, donc de la situation du plateau actuelle
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
        return valueToPlay == 1;
    }

    /**
     * Sert au debug pour afficher un état du jeu
     */
    public void debug() {
        boardGame.debug();
    }
}
