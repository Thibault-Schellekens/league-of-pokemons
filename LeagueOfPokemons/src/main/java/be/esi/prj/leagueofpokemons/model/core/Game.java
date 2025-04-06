package be.esi.prj.leagueofpokemons.model.core;

import java.util.List;
import java.util.Set;

public class Game {
    private Player player;
    private List<Opponent> opponents;
    private Collection collection;
    private int currentStage;
    private Battle currentBattle;

    // new game constructor
    public Game(Player newPlayer, Collection newCollection, int currentStage) {
        this.player = newPlayer;
        this.collection = newCollection;
        this.currentStage = currentStage;
    }
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





    //SETTERS AND GETTERS
    public Player getPlayer() {
        return player;
    }

    public Collection getCollection() {
        return collection;
    }

    public int getCurrentStage() {
        return currentStage;
    }

}
