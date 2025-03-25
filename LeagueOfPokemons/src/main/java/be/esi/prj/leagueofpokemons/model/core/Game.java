package be.esi.prj.leagueofpokemons.model.core;

import java.util.List;

public class Game {
    private Player player;
    private List<Opponent> opponents;
    private Collection collection;
    private int currentStage;
    private Battle currentBattle;

    public void nextStage() {

    }

    public void startBattle() {

    }

    public GameResult endGame() {
        return null;
    }

    public boolean isGameOver() {
        return false;
    }
}
