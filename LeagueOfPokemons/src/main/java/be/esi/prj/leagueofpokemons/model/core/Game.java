package be.esi.prj.leagueofpokemons.model.core;

import java.util.List;

public class Game {
    private Player player;
    private List<Opponent> opponents;
    private Collection collection;
    private int currentStage;
    private Battle currentBattle;

    // new game constructor
    public Game(int newPlayerId, String playerName) {
        player = new Player(newPlayerId,playerName);
        collection = new Collection(); //there should be a CollectionManager.getDefaultCollection()
        currentStage = 0;
    }

    public void loadGame(int loadedPlayerId, String loadedPlayerName, Collection loadedCollection, List<Card> loadedInventory){
        player = new Player();
        player.loadPlayer(loadedInventory,loadedPlayerId,loadedPlayerName);
        collection = loadedCollection;
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
