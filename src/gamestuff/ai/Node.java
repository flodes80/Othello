package gamestuff.ai;

import gamestuff.BoardGame;

import java.util.ArrayList;

/**
 * Un noeud correspond à un état du jeu
 */
public class Node {

    private final static int[][] vMap = {
            {20, -3, 11, 8, 8, 11, -3, 20},
            {-3, -7, -4, 1, 1, -4, -7, -3},
            {11, -4, 2, 2, 2, 2, -4, 11},
            {8, 1, 2, -3, -3, 2, 1, 8},
            {8, 1, 2, -3, -3, 2, 1, 8},
            {11, -4, 2, 2, 2, 2, -4, 11},
            {-3, -7, -4, 1, 1, -4, -7, -3},
            {20, -3, 11, 8, 8, 11, -3, 20}};
    private ArrayList<int[]> stableDisks = new ArrayList<>();

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

        // Permet de remplir la liste des pions étants stables
        computeAllStablesDisks();
    }

    /**
     * Permet d'obtenir les noeuds fils du noeud actuel.
     * Pour se faire on va simplement parcourir les mouvements adverses possibles à partir de la situation actuelle
     *
     * @return Les noeuds fils du noeud actuel
     */
    public ArrayList<Node> getChildrensNodes() {
        ArrayList childrens = new ArrayList();
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
        if (boardGame.calculEmptyCase() <= 5)
            return 100 * (boardGame.calculPiecePlayer2() - boardGame.calculPiecePlayer1()) / (boardGame.calculPiecePlayer2() + boardGame.calculPiecePlayer1());

        // On initialise d'abord la valeur avec la parité sur le plateau
        int value = getInitialValue();

        // On ajoute la valeur de "mobilité"
        value += getMobility();


        // On ajoute la valeur des "coins"
        value += getCornerValue();
        return value;
    }

    /**
     * Fonction déterminant la valeur intiale à partir de la parité, de la map value ainsi que de la stabilité
     *
     * @return la valeur initiale du noeud
     */
    private int getInitialValue() {
        int initialValue = 0, maxDiskCount = 0, minDiskCount = 0;
        boolean stabilityPossibility = getStabilityPossiblity();

        // On parcout le plateau actuel en entier
        for (int i = 0; i < boardGame.getBoard().length; i++) {
            for (int j = 0; j < boardGame.getBoard().length; j++) {

                // Si ce n'est pas une case vide
                if (boardGame.getBoard()[i][j] != (byte) -1) {

                    // Si c'est une case adverse on soustrait la valeur de la map
                    if (boardGame.getBoard()[i][j] == (byte) 0) {
                        initialValue -= vMap[j][i];
                        minDiskCount++;
                        if (stabilityPossibility)
                            initialValue -= getDiskStability(i, j);
                    }
                    // On ajoute dans le cas inverse (si c'est notre pion)
                    else {
                        initialValue += vMap[j][i];
                        maxDiskCount++;
                        if (stabilityPossibility)
                            initialValue += getDiskStability(i, j);
                    }
                }

            }
        }
        initialValue += 100 * (maxDiskCount - minDiskCount) / (maxDiskCount + minDiskCount);
        return initialValue;
    }


    /**
     * Fonction permettant de rechercher tous les pions stables sur le plateau
     */
    private void computeAllStablesDisks() {
        ArrayList<Integer> filledCol = new ArrayList<>();
        ArrayList<Integer> filledRow = new ArrayList<>();
        boolean filled;

        // Recherche des colonnes pleines
        for (int i = 0; i < boardGame.getBoard().length; i++) {
            filled = true;
            for (int j = 0; j < boardGame.getBoard().length; j++) {
                if (boardGame.getBoard()[i][j] == (byte) -1) {
                    filled = false;
                    break;
                }
            }
            if (filled)
                filledCol.add(i);
        }

        // Recherche des lignes pleines
        for (int i = 0; i < boardGame.getBoard().length; i++) {
            filled = true;
            for (int j = 0; j < boardGame.getBoard().length; j++) {
                if (boardGame.getBoard()[j][i] == (byte) -1) {
                    filled = false;
                    break;
                }
            }
            if (filled)
                filledRow.add(i);
        }

        // On va vérifier si des pions sont entièrement stables via leur ligne, leur colonne et leur diagonale.
        for (int i = 0; i < boardGame.getBoard().length; i++) {
            for (int j = 0; j < boardGame.getBoard().length; j++) {
                if (filledCol.contains(i) && filledRow.contains(j)) {
                    boolean isFullyStable = true;

                    for (int diagCol = i, diagRow = j; diagCol < boardGame.getBoard().length && diagRow < boardGame.getBoard().length; diagCol++, diagRow++) {
                        if (boardGame.getBoard()[diagCol][diagRow] == (byte) -1) {
                            isFullyStable = false;
                            break;
                        }
                    }
                    if (isFullyStable) {
                        for (int diagCol = i, diagRow = j; diagCol > 0 && diagRow > 0; diagCol--, diagRow--) {
                            if (boardGame.getBoard()[diagCol][diagRow] == (byte) -1) {
                                isFullyStable = false;
                                break;
                            }
                        }
                    }
                    if (isFullyStable)
                        stableDisks.add(new int[]{i, j});
                }
            }
        }
    }

    /**
     * Fonction permettant de checker si il y a des possibilités de stabilités sur le plateau
     *
     * @return true si il y a une possibilité false sinon
     */
    private boolean getStabilityPossiblity() {
        if (boardGame.getBoard()[0][0] != (byte) -1 || boardGame.getBoard()[1][0] != (byte) -1 || boardGame.getBoard()[0][1] != (byte) -1)
            return true;
        if (boardGame.getBoard()[7][0] != (byte) -1 || boardGame.getBoard()[7][1] != (byte) -1 || boardGame.getBoard()[6][0] != (byte) -1)
            return true;
        if (boardGame.getBoard()[0][7] != (byte) -1 || boardGame.getBoard()[1][7] != (byte) -1 || boardGame.getBoard()[0][6] != (byte) -1)
            return true;
        return boardGame.getBoard()[7][7] != (byte) -1 || boardGame.getBoard()[7][6] != (byte) -1 || boardGame.getBoard()[6][7] != (byte) -1;
    }

    /**
     * Fonction retournant la stabilité d'un pion
     *
     * @param col colonne du pion à tester
     * @param row ligne du pion à tester
     * @return 0 si le pion est instable et 2 si il est stable
     */
    private int getDiskStability(int col, int row) {
        for (int[] stableDisk : stableDisks) {
            if (stableDisk[0] == col && stableDisk[1] == row)
                return 2;
        }

        return 0;
    }

    /**
     * Fonction permettant de calculer la mobilité d'une situaiton
     *
     * @return un nombre entre -100 et 100 jugeant la possiblité de mouvement de l'ia
     */
    private int getMobility() {
        int mobilityValue;
        int maxMoves = boardGame.getAvailablesMovesAmount((byte) 1);
        int minMoves = boardGame.getAvailablesMovesAmount((byte) 0);

        if (maxMoves + minMoves != 0)
            mobilityValue = 100 * (maxMoves - minMoves) / (maxMoves + minMoves);
        else
            mobilityValue = 0;
        return mobilityValue;
    }

    /**
     * Fonction permettant de calculer la valeur des coins du plateau
     *
     * @return un nombre indiquant la valeur des coins capturés par l'ia
     */
    private int getCornerValue() {
        int cornerValue = 0;

        if (boardGame.getBoard()[0][0] == (byte) 1)
            cornerValue += 20;
        else if (boardGame.getBoard()[0][0] == (byte) 0)
            cornerValue += -20;

        if (boardGame.getBoard()[7][0] == (byte) 1)
            cornerValue += 20;
        else if (boardGame.getBoard()[7][0] == (byte) 0)
            cornerValue += -20;

        if (boardGame.getBoard()[0][7] == (byte) 1)
            cornerValue += 20;
        else if (boardGame.getBoard()[0][7] == (byte) 0)
            cornerValue += -20;

        return cornerValue;
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
