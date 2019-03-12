package gamestuff.ai;

import gamestuff.BoardGame;

/**
 * GamePhase permettant de savoir dans quelle partie du jeu nous sommes;
 * Début de partie
 * Milieu de partie
 * Fin de partie
 */
public enum GamePhase {

    EARLY_GAME,
    MID_GAME,
    LATE_GAME;

    public static GamePhase getGamePhase(BoardGame boardGame) {
        int emptyCells = boardGame.calculEmptyCase();
        if (emptyCells > 44) return GamePhase.EARLY_GAME;
        else if (emptyCells >= 6) return GamePhase.MID_GAME;
        else return GamePhase.LATE_GAME;
    }
}
