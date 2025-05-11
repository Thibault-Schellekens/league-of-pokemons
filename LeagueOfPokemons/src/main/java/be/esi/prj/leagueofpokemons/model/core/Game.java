package be.esi.prj.leagueofpokemons.model.core;

import be.esi.prj.leagueofpokemons.model.db.dto.GameDto;
import javafx.beans.property.IntegerProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Represents a game session where a player competes against a series of opponents.
 * This class is implemented as a singleton to ensure only one game instance exists at any time.
 * It handles the game state, stages, and battles between the player and opponents.
 */
public class Game {
    private static Game instance;

    private int id;
    private Player player;
    private List<Opponent> opponents;
    private Collection collection;
    private Battle currentBattle;
    private int currentStage;
    private boolean gameOver;

    private static final int MAX_STAGE = 5;

    /**
     * Initializes the game with the specified game ID, player, and collection.
     * This method can only be called once during the lifetime of the game instance.
     *
     * @param gameId     The ID of the game.
     * @param player     The player participating in the game.
     * @param collection The collection of cards owned by the player.
     * @throws ModelException if the game has already been initialized.
     */
    public static void initialize(int gameId, Player player, Collection collection) {
        if (instance != null) {
            throw new ModelException("Game has already been initialized");
        }
        instance = new Game(gameId, player, collection);
    }

    /**
     * Retrieves the singleton instance of the game.
     *
     * @return The instance of the game.
     * @throws ModelException if the game has not been initialized.
     */
    public static Game getInstance() {
        if (instance == null) {
            throw new ModelException("Game has not been initialized");
        }
        return instance;
    }

    /**
     * Resets the singleton instance of the game, allowing for a fresh initialization.
     */
    static void resetInstance() {
        instance = null;
    }

    /**
     * Private constructor to initialize the game with the provided details.
     *
     * @param gameId     The ID of the game.
     * @param player     The player participating in the game.
     * @param collection The collection of cards owned by the player.
     */
    private Game(int gameId, Player player, Collection collection) {
        this.id = gameId;
        this.player = player;
        this.collection = collection;
        this.opponents = new ArrayList<>();
        buildOpponents();
        this.currentStage = 0;
    }

    /**
     * Builds the list of opponents for the game.
     * This will create 5 opponents with default names and stages.
     */
    private void buildOpponents() {
        for (int i = 0; i < 5; i++) {
            Opponent opponent = new Opponent("Opponent " + (i + 1), i);
            opponents.add(opponent);
        }
    }

    /**
     * Loads a saved game state with the specified game ID, player, collection, and current stage.
     *
     * @param gameId        The ID of the saved game.
     * @param newPlayer     The player to load into the game.
     * @param newCollection The collection of cards for the player.
     * @param currentStage  The stage of the game at the time of loading.
     */
    public void loadGame(int gameId, Player newPlayer, Collection newCollection, int currentStage) {
        this.id = gameId;
        this.player = newPlayer;
        this.collection = newCollection;
        this.currentStage = currentStage;
        this.gameOver = currentStage == MAX_STAGE;
    }

    /**
     * Progresses to the next stage of the game after a battle has concluded.
     * If the current battle was won by the player, the stage will increase by one.
     * The game will end if the maximum stage is reached.
     *
     * @throws ModelException if the current battle has not concluded.
     */
    public void nextStage() {
        if (!currentBattle.isOver()) {
            throw new ModelException("Current Battle must be over!");
        }

        if (currentBattle.getWinner().equals(player.getName())) {
            currentStage++;
        }
        if (currentStage >= MAX_STAGE) {
            endGame();
        }
        currentBattle = null;
    }

    /**
     * Starts a new battle between the player and the current opponent.
     *
     * @return The Battle instance representing the ongoing battle.
     * @throws ModelException if the game has already ended.
     */
    public Battle startBattle() {
        if (gameOver) {
            throw new ModelException("Game has already ended");
        }
        player.selectTeam(Tier.values()[currentStage]);
        Opponent opponent = opponents.get(currentStage);
        opponent.selectTeam(Tier.values()[currentStage]);
        currentBattle = new Battle(player, opponents.get(currentStage));

        return currentBattle;
    }

    /**
     * Ends the game, marking it as over.
     */
    public void endGame() {
        gameOver = true;
    }

    /**
     * Checks whether the game is over.
     *
     * @return {@code true} if the game is over, {@code false} otherwise.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Retrieves the player currently playing the game.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Retrieves the collection of cards owned by the player.
     *
     * @return The player's collection of cards.
     */
    public Collection getCollection() {
        return collection;
    }

    /**
     * Retrieves the current stage of the game.
     *
     * @return The current stage.
     */
    public int getCurrentStage() {
        return currentStage;
    }

    /**
     * Retrieves the ID of the game.
     *
     * @return The game ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the game and updates the associated collection.
     *
     * @param id The new game ID.
     */
    public void setId(int id) {
        this.id = id;
        collection.setId(id);
    }

    /**
     * Retrieves the list of cards in the player's inventory.
     *
     * @return The list of cards in the player's inventory.
     */
    public List<Card> getPlayerInventory() {
        return player.getInventory();
    }

    /**
     * Removes a card from the player's inventory.
     *
     * @param card The card to be removed.
     */
    public void removeCardInPlayer(Card card) {
        player.removeCard(card);
    }

    /**
     * Adds a card to the player's inventory.
     *
     * @param card The card to be added.
     */
    public void addCardInPlayer(Card card) {
        if (card.getTier().isGreater(Tier.values()[currentStage])) {
            throw new ModelException("You can't add cards with a tier higher than the current stage");
        }
        player.addCard(card);
    }

    /**
     * Creates a new GameDto instance with the provided game name and current game state.
     *
     * @param name The name of the game.
     * @return A GameDto containing the game ID, name, player ID, IDs of the player's slots,
     *         current stage, and the timestamp of the creation.
     */
    public GameDto createGameDto(String name) {
        return new GameDto(
                id,
                name,
                player.getId(),
                player.getSlotId(0),
                player.getSlotId(1),
                player.getSlotId(2),
                currentStage,
                LocalDateTime.now()
        );
    }

    /**
     * Retrieves all imported cards from the player's collection.
     *
     * @return an unmodifiable set of all imported cards in the collection
     */
    public Set<Card> getImportedCards() {
        return collection.getImportedCards();
    }
}
