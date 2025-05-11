package be.esi.prj.leagueofpokemons.util;

import be.esi.prj.leagueofpokemons.model.core.*;
import be.esi.prj.leagueofpokemons.model.core.Collection;
import be.esi.prj.leagueofpokemons.model.db.dto.GameDto;
import be.esi.prj.leagueofpokemons.model.db.repository.*;
import be.esi.prj.leagueofpokemons.view.ImageCache;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Manages the overall game operations, including creating, saving, loading, and deleting games.
 * The class interacts with various repositories to handle game data and player information.
 * It also initializes and registers the game environment and player data for each session.
 * This class provides functionality for game lifecycle management.
 */
public class GameManager {

    private static final CardRepository cardRepository = new CardRepository();
    private static final CollectionRepository collectionRepository = new CollectionRepository();
    private static final PlayerRepository playerRepository = new PlayerRepository();
    private static final GameRepository gameRepository = new GameRepository();

    /**
     * Initializes a new game with the provided player name.
     * A new game instance is created, including a collection of cards and a new player.
     *
     * @param name the name of the player for the new game.
     */
    public static void newGame(String name) {
        System.out.println("League of Legends: Initializing game");
        Collection currCollection = new Collection(-1);
        ImageCache.getInstance().registerToCollection(currCollection);
        currCollection.loadCards(collectionRepository.loadBaseSet(), Collections.emptySet());
        Player currPlayer = new Player(0, name);

        Game.initialize(-1, currPlayer, currCollection);
        System.out.println("GameID : " + Game.getInstance().getId()
                + " PlayerID : " + Game.getInstance().getPlayer().getId()
                + " CollectionID : " + Game.getInstance().getCollection().getId());
    }

    /**
     * Saves the current game with the specified name.
     * It stores the game's data, player details, and collection of cards to the repository.
     *
     * @param name the name to associate with the saved game.
     * @throws ModelException if the game name is null or there is an issue during saving.
     */
    public static void saveGame(String name) {
        if (name == null) {
            throw new ModelException("Game name cannot be empty");
        }
        Game game = Game.getInstance();
        GameDto gameDto = new GameDto(
                game.getId(),
                name,
                game.getPlayer().getId(),
                game.getPlayer().getSlot(0).getId(),
                game.getPlayer().getSlot(1).getId(),
                game.getPlayer().getSlot(2).getId(),
                game.getCurrentStage(),
                LocalDateTime.now()
        );

        try {
            for (Card card : game.getCollection().getImportedCards()) {
                cardRepository.save(card);
            }

            System.out.println("LOP : saving with id : " + game.getId());
            System.out.println("Saving with team : " + game.getPlayer().getSlot(2).getName());

            int gameId = gameRepository.save(gameDto);
            game.setId(gameId);
            collectionRepository.save(game.getCollection());
            playerRepository.save(game.getPlayer());
        } catch (RepositoryException e) {
            throw new ModelException("Error while saving game");
        }
    }

    /**
     * Loads a game from the repository by the given game ID.
     * Retrieves game data, player details, and the collection associated with the game.
     *
     * @param gameId the ID of the game to load.
     * @throws ModelException if the game is not found or there is an error during loading.
     */
    public static void loadGame(int gameId) {
        System.out.println("GameID before loading : " + gameId);
        GameDto gameDto = gameRepository.findById(gameId).orElse(null);
        if (gameDto == null) {
            throw new ModelException("Game not found with ID: " + gameId);
        }

        Collection collection = collectionRepository.findById(gameDto.gameID()).orElse(null);
        Player player = playerRepository.findById(gameDto.playerID()).orElse(null);

        Card slot1 = cardRepository.findById(gameDto.slot1ID()).orElse(null);
        Card slot2 = cardRepository.findById(gameDto.slot2ID()).orElse(null);
        Card slot3 = cardRepository.findById(gameDto.slot3ID()).orElse(null);

        List<Card> playerInventory = new ArrayList<>(List.of(slot1, slot2, slot3));
        player.loadPlayerInventory(playerInventory);

        Game.getInstance().loadGame(gameDto.gameID(), player, collection, gameDto.currentStage());
        ImageCache.getInstance().registerToCollection(collection);
        System.out.println("GameID after loading : " + gameId);
    }

    /**
     * Deletes the game from the repository by the given game ID.
     *
     * @param gameId the ID of the game to delete.
     * @throws ModelException if there is an error during game deletion.
     */
    public static void deleteGame(int gameId) {
        try {
            gameRepository.delete(gameId);
        } catch (RepositoryException e) {
            throw new ModelException("Error while deleting game: " + gameId);
        }
    }

    /**
     * Retrieves all games from the repository.
     *
     * @return a list of all saved games.
     */
    public static List<GameDto> loadGames() {
        return gameRepository.findAll();
    }

    /**
     * Finds a card by its ID.
     *
     * @param id the ID of the card to find.
     * @return an Optional containing the card if found, otherwise empty.
     */
    public static Optional<Card> findCardById(String id) {
        return cardRepository.findById(id);
    }
}
