package be.esi.prj.leagueofpokemons.util;

import be.esi.prj.leagueofpokemons.model.core.*;
import be.esi.prj.leagueofpokemons.model.core.Collection;
import be.esi.prj.leagueofpokemons.model.db.dto.GameDto;
import be.esi.prj.leagueofpokemons.model.db.repository.*;
import be.esi.prj.leagueofpokemons.view.ImageCache;

import java.time.LocalDateTime;
import java.util.*;

public class GameManager {

    private static final CardRepository cardRepository = new CardRepository();
    private static final CollectionRepository collectionRepository = new CollectionRepository();
    private static final PlayerRepository playerRepository = new PlayerRepository();
    private static final GameRepository gameRepository = new GameRepository();

    public static void newGame(String name) {
        System.out.println("League of legends : Initializing game");
        Collection currCollection = new Collection(-1);
        ImageCache.getInstance().registerToCollection(currCollection);
        currCollection.loadCards(collectionRepository.loadBaseSet(), Collections.emptySet());
        Player currPlayer = new Player(0, name);

        Game.initialize(-1, currPlayer, currCollection);
        System.out.println("GameID : " + Game.getInstance().getId()
                + " PlayerID : " + Game.getInstance().getPlayer().getId()
                + " CollectionID : " + Game.getInstance().getCollection().getId());
    }

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

    public static void deleteGame(int gameId) {
        try {
            gameRepository.delete(gameId);
        } catch (RepositoryException e) {
            throw new ModelException("Error while deleting game: " + gameId);
        }
    }


    // TODO: only return game from that player name
    public static List<GameDto> loadGames() {
        return gameRepository.findAll();
    }

    public static Optional<Card> findCardById(String id) {
        return cardRepository.findById(id);
    }

}
