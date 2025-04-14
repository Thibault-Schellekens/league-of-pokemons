package be.esi.prj.leagueofpokemons.model.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Game {
    private static Game instance;

    private int id;
    private Player player;
    private List<Opponent> opponents;
    private Collection collection;
    private int currentStage;
    private Battle currentBattle;

    public static void initialize(int gameId, Player player, Collection collection) {
        if (instance != null) {
            throw new ModelException("Game has already been initialized");
        }
        instance = new Game(gameId, player, collection);
    }

    public static Game getInstance() {
        if (instance == null) {
            throw new ModelException("Game has not been initialized");
        }
        return instance;
    }

    private Game(int gameId, Player player, Collection collection) {
        this.id = gameId;
        this.player = player;
        this.collection = collection;
        this.opponents = new ArrayList<>();
        buildOpponents();
        this.currentStage = 0;
    }

    private void buildOpponents() {
        Opponent first = new Opponent();
        first.createTeam();

        opponents.add(first);
    }

    public void loadGame(int gameId, Player newPlayer, Collection newCollection, int currentStage) {
        this.id = gameId;
        this.player = newPlayer;
        this.collection = newCollection;
        this.currentStage = currentStage;
    }

    public void nextStage() {
    }

    public Battle startBattle() {
        currentBattle = new Battle(player, opponents.get(currentStage));

        return currentBattle;
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

    public int getId(){
        return id;
    }

}
