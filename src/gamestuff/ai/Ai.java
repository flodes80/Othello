package gamestuff.ai;

import gamestuff.BoardGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public static int[] move(byte valueToPlay, int col, int row, BoardGame boardGame, boolean multithreaded) {
        int[] bestMove = new int[3];
        bestMove[2] = Integer.MIN_VALUE;  // On cherche à maximiser les gains de l'ordinateur donc on initialise le score à -INFINI

        // Noeud de départ qui correspond à la situation actuelle du jeu lors de la demande de coup pour l'ordinateur
        Node nodeRoot = new Node(valueToPlay, col, row, boardGame);

        if (multithreaded) {
            // Création d'une liste "Thread Safe"
            List<int[]> availablesMoves = Collections.synchronizedList(new ArrayList<>());

            // Création d'un tableau de thread pour paralléliser le calcul
            Thread[] threadArray = new Thread[nodeRoot.getChildrensNodes().size()];
            int index = 0;
            for (Node child : nodeRoot.getChildrensNodes()) {
                // Création d'un thread pour chaque coups possibles
                Thread thread = new Thread(new ThreadedAiTask(availablesMoves, child, difficulty.getDepth() - 1));

                // Lancement du thread
                thread.start();

                // Ajout du thread dans le tableau de thread
                threadArray[index] = thread;

                index++;
            }

            // On attend que tous les threads soient terminés
            for (Thread thread : threadArray) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // On cherche le meilleur move parmis les moves retournés par les différents threads
            for (int[] move : availablesMoves) {
                //System.out.println("Possible move: " + move[0] + " " + move[1] + " " + move[2]);
                if (move[2] > bestMove[2]) {
                    // On remplie le tableau du bestMove
                    bestMove[0] = move[0];   // Colonne à jouer
                    bestMove[1] = move[1];   // Ligne à jouer
                    bestMove[2] = move[2];   // Score du move
                }
            }
        } else {
            // On parcours coups possibles pour l'ordinateur
            for (Node child : nodeRoot.getChildrensNodes()) {
                // On attribue un score à ces coups
                int alpha = mtd(child, difficulty.getDepth() - 1, Integer.MIN_VALUE); // MTD

                // Si le coup possède un meilleur score que précédemment on le choisi
                if (alpha > bestMove[2]) {
                    // On remplie le tableau du bestMove
                    bestMove[0] = child.getCol();   // Colonne à jouer
                    bestMove[1] = child.getRow();   // Ligne à jouer
                    bestMove[2] = alpha;            // Score du move
                }
            }
        }

        // Pour finir on retourne le meilleur coup
        return bestMove;
    }


    /**
     * Algorithme MTD basé sur Alpha Beta
     *
     * @param node   Noeud à partir duquel l'algo
     * @param depth  Profondeur maximale pour limiter le temps de recherche
     * @param init_g borne actuelle
     * @return La valeur du noeud testé
     */
    public static int mtd(Node node, int depth, int init_g) {
        int g = init_g;
        int haut = Integer.MAX_VALUE;
        int bas = Integer.MIN_VALUE;
        while (haut > bas) {
            int beta;
            if (g == bas)
                beta = g + 1;
            else
                beta = g;
            g = alphaBeta(node, depth, beta - 1, beta);
            if (g < beta)
                haut = g;
            else
                bas = g;
        }
        return g;
    }

    /**
     * Algorithme Alpha Beta permettant d'obtenir le meilleur coup à jouer
     *
     * @param node  Noeud à partir duquel l'algo
     * @param depth  Profondeur maximale pour limiter le temps de recherche
     * @param alpha Borne inférieure
     * @param beta  Borne supérieure
     * @return La valeur du noeud testé
     */
    public static int alphaBeta(Node node, int depth, int alpha, int beta) {
        int score;
        // Si le noeud est une feuille ou que la profondeur maximale est atteinte on retourne une évaluation de la situation
        if (node.isALeaf() || depth == 0) {
            return node.getSelfEvaluation();
        }

        if (!node.hasAnyMoves()) {
            return alphaBeta(node, depth - 1, alpha, beta);
        }

        // Sinon on applique l'algorithme alpha beta
        else {

            // Si c'est un noeud max
            if (node.isMaxNode()) {
                // Noeud max donc on cherche à maximiser la valeur du noeud
                score = Integer.MIN_VALUE;

                // On parcourt les enfants de ce noeud
                for (Node children : node.getChildrensNodes()) {
                    // Obtention valeurs des noeuds enfants
                    int childScore = alphaBeta(children, depth - 1, alpha, beta);
                    // Meilleur score ?
                    if (childScore > score) score = childScore;

                    // Mise à jour d'alpha si nécessaire
                    if (score > alpha) alpha = score;

                    // Coupure beta ?
                    if (beta <= alpha) break;
                }

            }

            // Sinon c'est un noeud min
            else {
                // Noeud min donc on cherche à minimiser la valeur du noeud
                score = Integer.MAX_VALUE;

                // On parcourt les enfants de ce noeud
                for (Node children : node.getChildrensNodes()) {
                    // Obtention valeurs des noeuds enfants
                    int childScore = alphaBeta(children, depth - 1, alpha, beta);
                    // Meilleur score ?
                    if (childScore < score) score = childScore;

                    // Mise à jour d'alpha si nécessaire
                    if (score < beta) beta = score;

                    // Coupure beta ?
                    if (beta <= alpha) break;
                }

            }
        }
        return score;
    }

}
