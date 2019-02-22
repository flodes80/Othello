package gamestuff.ai;

import gamestuff.BoardGame;

public class Ai {

    public static Difficulty difficulty;

    /**
     * Cette fonction permet de retourner le "meilleur mouvement" selon l'ia
     *
     * @param valueToPlay Valeur à jouer sur le plateau passé en paramètre
     * @param col         Colonne à jouer sur le plateau passé en paramètre
     * @param row         Ligne à jouer sur le plateau passé en paramètre
     * @param boardGame   Situation de plateau "actuelle" sur laquelle doit se baser l'algo
     * @return Un tableau de trois entiers comprenant en 1ère position la colonne à jouer, en 2ème la ligne à jouer et en 3ème le score attribué à ce "move" par l'ia
     */
    public static int[] move(byte valueToPlay, int col, int row, BoardGame boardGame) {
        int[] bestMove = new int[3];
        bestMove[2] = Integer.MIN_VALUE;  // On cherche à maximiser les gains de l'ordinateur donc on initialise le score à -INFINI

        // Noeud de départ qui correspond à la situation actuelle du jeu lors de la demande de coup pour l'ordinateur
        Node nodeRoot = new Node(valueToPlay, col, row, boardGame);

        // On parcours coups possibles pour l'ordinateur
        for (Node child : nodeRoot.getChildrensNodes()) {
            // On attribue un score à ces coups
            int alpha = alphaBeta(child, difficulty.getDepth() - 1, bestMove[2], Integer.MAX_VALUE);

            // Si le coup possède un meilleur score que précédemment on le choisi
            if (alpha > bestMove[2]) {
                // On remplie le tableau du bestMove
                bestMove[0] = child.getCol();   // Colonne à jouer
                bestMove[1] = child.getRow();   // Ligne à jouer
                bestMove[2] = alpha;            // Score du move
            }
        }

        // Pour finir on retourne le meilleur coup
        return bestMove;
    }

    /**
     * Algorithme Alpha Beta permettant d'obtenir le meilleur coup à jouer
     *
     * @param node  Noeud à partir duquel l'algo
     * @param depth  Profondeur maximale pour limiter le temps de recherche
     * @param alpha Borne inférieure
     * @param beta  Borne supérieure
     * @return Un Node comprenant le mouvement "sélectionné" ainsi que sa valeur attribuée.
     */
    public static int alphaBeta(Node node, int depth, int alpha, int beta) {
        int m;
        // Si le noeud est une feuille ou que la profondeur maximale est atteinte on retourne une évaluation de la situation
        if (node.isALeaf() || depth == 0) {
            return node.getSelfEvaluation();
        }

        // Sinon on applique l'algorithme alpha beta
        else {

            // Si c'est un noeud max
            if (node.isMaxNode()) {
                // Noeud max donc on cherche à maximiser la valeur du noeud
                m = Integer.MIN_VALUE;

                // On parcourt les enfants de ce noeud
                for (Node children : node.getChildrensNodes()) {

                    // Nous sommes dans un noeud max donc nous cherchons la plus haute valeur dans ses noeuds fils
                    m = Integer.max(m, alphaBeta(children, depth - 1, alpha, beta));

                    // La valeur trouvée pour un fils est supérieure au beta que le noeud parent à transmis ici
                    // On arrête la recherche ici car le noeud parent cherche à minimiser, et l'on vient de trouver une valeur supérieure
                    if (beta <= m) {
                        return m;
                    }

                    // Pas de coupure, donc on cherche à obtenir la valeur la plus haute entre alpha et la valeur actuelle
                    alpha = Integer.max(alpha, m);
                }

            }

            // Sinon c'est un noeud min
            else {
                // Noeud min donc on cherche à minimiser la valeur du noeud
                m = Integer.MAX_VALUE;

                // On parcourt les enfants de ce noeud
                for (Node children : node.getChildrensNodes()) {

                    // Nous sommes dans un noeud min donc nous cherchons la plus petite valeur dans ses noeuds fils
                    m = Integer.min(m, alphaBeta(children, depth - 1, alpha, beta));

                    // La valeur trouvée pour un fils est inférieure au alpha que le noeud parent à transmis ici
                    // On arrête la recherche ici car le noeud parent cherche à maximiser, et l'on vient de trouver une valeur inférieure
                    if (alpha >= m) {
                        return m;
                    }

                    // Pas de coupure, donc on cherche à obtenir la valeur la plus petite entre beta et la valeur actuelle
                    beta = Integer.min(beta, m);
                }

            }
        }
        return m;
    }
}
