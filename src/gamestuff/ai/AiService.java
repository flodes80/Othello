package gamestuff.ai;

import gamestuff.Game;
import gui.controllers.GameController;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Cette classe service va permettre de créer une tâche dans un thread dédié afin de ne pas figer l'interface utilisateur
 */
public class AiService extends Service<int[]> {

    private byte value;
    private int colonne, ligne, depth;
    private Game game;
    private GameController gameController;

    public AiService(byte value, int colonne, int ligne, int depth, Game game, GameController gameController) {
        this.value = value;
        this.colonne = colonne;
        this.ligne = ligne;
        this.depth = depth;
        this.game = game;
        this.gameController = gameController;

        // Méthode appellée lorsque la tâche est finie
        setOnSucceeded(event -> {
            int[] move = getValue();
            gameController.getAiIndicator().setVisible(false);
            game.play(move[0], move[1]);
        });
    }

    /**
     * Création de la tâche
     *
     * @return
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
                gameController.getAiIndicator().setVisible(true);
                return Ai.move((byte) 0, colonne, ligne, game.getBoardGame());
            }
        };
    }
}
