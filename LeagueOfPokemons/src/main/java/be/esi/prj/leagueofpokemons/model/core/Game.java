package be.esi.prj.leagueofpokemons.model.core;

import javafx.beans.property.IntegerProperty;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static Game instance;

    private int id;
    private Player player;
    private List<Opponent> opponents;
    private Collection collection;
    private Battle currentBattle;
    private int currentStage;
    private boolean gameOver;

    private final int MAX_STAGE = 5;

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
        for (int i = 0; i < 5; i++) {
            Opponent opponent = new Opponent("Opponent " + (i + 1), i);
            opponent.selectTeam(Tier.values()[i]);
            opponents.add(opponent);
        }
    }

    public void loadGame(int gameId, Player newPlayer, Collection newCollection, int currentStage) {
        this.id = gameId;
        this.player = newPlayer;
        this.collection = newCollection;
        this.currentStage = currentStage;
    }

    public void nextStage() {
        if (!currentBattle.isOver()) {
            throw new ModelException("Current Battle must be over!");
        }

        if (currentBattle.getWinner().equals(player.getName())) {
            currentStage++;
            if (currentStage >= MAX_STAGE) {
                endGame();
            }
        }
        currentBattle = null;
    }

    public Battle startBattle() {
        if (gameOver) {
            throw new ModelException("Game has already ended");
        }
        player.selectTeam(Tier.values()[currentStage]);
        currentBattle = new Battle(player, opponents.get(currentStage));

        return currentBattle;
    }

    public GameResult endGame() {
        gameOver = true;
        return null;
    }

    public boolean isGameOver() {
        return gameOver;
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

    public void setId(int id) {
        this.id = id;
        collection.setId(id);
    }

    public List<Card> getPlayerInventory(){
        return player.getInventory();
    }

    public void removeCardInPlayer(Card card){
        player.removeCard(card);
    }

    public void addCardInPlayer(Card card){
        player.addCard(card);
    }
}
