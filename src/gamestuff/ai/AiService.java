package gamestuff.ai;

import gamestuff.Game;
import gui.controllers.GameController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Cette classe service va permettre de créer une tâche dans un thread dédié afin de ne pas figer l'interface utilisateur
 */
public class AiService extends Service<int[]> {

    private int colonne, ligne;
    private Game game;
    private GameController gameController;
    private long time, endTime;
    private boolean debug;

    public AiService(int colonne, int ligne, Game game, GameController gameController, boolean debug) {
        this.colonne = colonne;
        this.ligne = ligne;
        this.game = game;
        this.gameController = gameController;
        this.debug = debug;

        // Méthode appellée lorsque la tâche est finie
        setOnSucceeded(event -> {
            // Debug
            if (debug) System.out.println("Took " + (endTime - time) + "ms");

            // Obtention résultat
            int[] move = getValue();

            // On cache le progress indicator
            gameController.getAiIndicator().setVisible(false);

            // On joue le coup
            game.play(move[0], move[1]);

            // Debug
            if (debug) System.out.println("Final move: " + move[0] + " " + move[1] + " " + move[2]);
        });
    }

    /**
     * Création de la tâche
     *
     * @return Le mouvement à jouer
     */
    @Override
    protected Task<int[]> createTask() {

        return new Task<int[]>() {

            /**
             * Méthode appellée lors du lancement de la tâche
             * @return le move à jouer
             */
            @Override
            protected int[] call() {
                time = System.currentTimeMillis();

                // Recherche du coup
                int[] move = Ai.move((byte) 0, colonne, ligne, game.getBoardGame(), Ai.difficulty.getDepth() > 6);

                endTime = System.currentTimeMillis();

                // Permet de faire un délai de 2 secondes minimum avant que l'ia joue
                if (System.currentTimeMillis() - time < 2000 && !debug) {
                    try {
                        Thread.sleep(2000 - (System.currentTimeMillis() - time));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return move;
            }
        };
    }
}
