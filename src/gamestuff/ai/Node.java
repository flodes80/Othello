package gamestuff.ai;

import gamestuff.BoardGame;
import gamestuff.Direction;

import java.util.ArrayList;

/**
 * Un noeud correspond à un état du jeu
 */
public class Node {

    private final static int[][] pMap = {
            {100, -10, 8, 6, 6, 8, -10, 100},
            {-10, -25, -4, -4, -4, -4, -25, -10},
            {8, -4, 6, 4, 4, 6, -4, 8},
            {6, -4, 4, 0, 0, 4, -4, 6},
            {6, -4, 4, 0, 0, 4, -4, 6},
            {8, -4, 6, 4, 4, 6, -4, 8},
            {-10, -25, -4, -4, -4, -4, -25, -10},
            {100, -10, 8, 6, 6, 8, -10, 100}};
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
        if (isStabilityPossiblity()) computeAllStablesDisks();
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
        // Etat terminal
        if (isALeaf()) {
            return 1000 * evalDiscDiff();
        }

        // En cours de jeu evaluation différentes
        switch (GamePhase.getGamePhase(boardGame)) {
            case EARLY_GAME:
                return 100 * evalCorner() + 75 * evalStability() + 50 * evalMobility() + 50 * evalFrontier() + evalPlacement();
            case MID_GAME:
                return 100 * evalCorner() + 75 * evalStability() + 50 * evalFrontier() + 35 * evalDiscDiff() + evalPlacement();
            case LATE_GAME:
                return 100 * evalCorner() + 75 * evalStability() + 100 * evalDiscDiff() + evalPlacement();
            default:
                return 0;
        }
    }

    /**
     * Evalue la parité entre les deux joueurs
     *
     * @return une valeur entre -100 et 100.
     */
    private int evalDiscDiff() {
        int min = boardGame.calculPiecePlayer1();
        int max = boardGame.calculPiecePlayer2();

        return 100 * (max - min) / (max + min);
    }

    private int evalPlacement() {
        int min = 0;
        int max = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (boardGame.getBoard()[i][j] == (byte) 0)
                    min += pMap[j][i];
                else if (boardGame.getBoard()[i][j] == (byte) 1)
                    max += pMap[j][i];
            }
        }
        return max - min;
    }

    /**
     * Fonction permettant de calculer la mobilité d'une situaiton
     *
     * @return un nombre entre -100 et 100 jugeant la possiblité de mouvement de l'ia
     */
    private int evalMobility() {
        int max = boardGame.getAvailablesMovesAmount((byte) 1);
        int min = boardGame.getAvailablesMovesAmount((byte) 0);

        if (max + min == 0) return 0;
        return 100 * (max - min) / (max + min);
    }

    /**
     * Fonction permettant de calculer les coins capturés
     *
     * @return un nombre entre -100 et 100 portant sur les coins capturés.
     */
    private int evalCorner() {
        int max = 0;
        int min = 0;

        if (boardGame.getBoard()[0][0] == (byte) 1) max++;
        else if (boardGame.getBoard()[0][0] == (byte) 0) min++;

        if (boardGame.getBoard()[7][0] == (byte) 1) max++;
        else if (boardGame.getBoard()[7][0] == (byte) 0) min++;

        if (boardGame.getBoard()[0][7] == (byte) 1) max++;
        else if (boardGame.getBoard()[0][7] == (byte) 0) min++;

        if (boardGame.getBoard()[7][7] == (byte) 1) max++;
        else if (boardGame.getBoard()[7][7] == (byte) 0) min++;

        if (max + min == 0) return 0;
        return 100 * (max - min) / (max + min);
    }

    /**
     * Fonction permettant de retourner le nombre de pions étant à la frontière de pions vides
     *
     * @return un nombre entre -100 et 100 portant sur les pions étant possiblements capturables
     */
    private int evalFrontier() {
        ArrayList<int[]> maxFrontierSquare = new ArrayList<>();
        ArrayList<int[]> minFrontierSquare = new ArrayList<>();

        for (int i = 0; i < boardGame.getBoard().length; i++) {
            for (int j = 0; j < boardGame.getBoard().length; j++) {

                if (boardGame.getBoard()[i][j] != (byte) -1) {

                    // On commence les checks dans toutes les directions
                    for (Direction direction : Direction.values())

                        // On vérifie si on est pas "hors map"
                        if (i + direction.getColstep() > 0 && i + direction.getColstep() < 8 && j + direction.getRowstep() > 0 && j + direction.getRowstep() < 8) {

                            boolean redundant = false;
                            // On check la nature du pion et si il est déjà présent dans la liste ou non
                            if (boardGame.getBoard()[i][j] == (byte) 0) {
                                for (int[] square : maxFrontierSquare) {
                                    if (square[0] == i + direction.getColstep() && square[1] == j + direction.getRowstep()) {
                                        redundant = true;
                                        break;
                                    }
                                }
                                if (!redundant)
                                    maxFrontierSquare.add(new int[]{i + direction.getColstep(), j + direction.getRowstep()});
                            } else {
                                for (int[] square : minFrontierSquare) {
                                    if (square[0] == i + direction.getColstep() && square[1] == j + direction.getRowstep()) {
                                        redundant = true;
                                        break;
                                    }
                                }
                                if (!redundant)
                                    minFrontierSquare.add(new int[]{i + direction.getColstep(), j + direction.getRowstep()});
                            }

                        }

                }
            }
        }
        if (maxFrontierSquare.size() + minFrontierSquare.size() == 0) return 0;
        return 100 * (maxFrontierSquare.size() - minFrontierSquare.size()) / (maxFrontierSquare.size() + minFrontierSquare.size() + 1);
    }

    private int evalStability() {
        int max = 0;
        int min = 0;
        for (int[] stableDisk : stableDisks) {
            if (boardGame.getBoard()[stableDisk[0]][stableDisk[1]] == (byte) 0)
                min++;
            else if (boardGame.getBoard()[stableDisk[0]][stableDisk[1]] == (byte) 1)
                max++;
        }

        if (max + min == 0) return 0;
        return 100 * (max - min) / (max + min);
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
    private boolean isStabilityPossiblity() {
        if (boardGame.getBoard()[0][0] != (byte) -1 || boardGame.getBoard()[1][0] != (byte) -1 || boardGame.getBoard()[0][1] != (byte) -1)
            return true;
        if (boardGame.getBoard()[7][0] != (byte) -1 || boardGame.getBoard()[7][1] != (byte) -1 || boardGame.getBoard()[6][0] != (byte) -1)
            return true;
        if (boardGame.getBoard()[0][7] != (byte) -1 || boardGame.getBoard()[1][7] != (byte) -1 || boardGame.getBoard()[0][6] != (byte) -1)
            return true;
        return boardGame.getBoard()[7][7] != (byte) -1 || boardGame.getBoard()[7][6] != (byte) -1 || boardGame.getBoard()[6][7] != (byte) -1;
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
