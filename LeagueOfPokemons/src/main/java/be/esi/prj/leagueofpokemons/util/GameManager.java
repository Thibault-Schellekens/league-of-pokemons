package be.esi.prj.leagueofpokemons.util;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Collection;
import be.esi.prj.leagueofpokemons.model.core.Game;
import be.esi.prj.leagueofpokemons.model.core.Player;
import be.esi.prj.leagueofpokemons.model.db.dto.GameDto;
import be.esi.prj.leagueofpokemons.model.db.repository.*;

import java.util.*;

public class GameManager {
    private static GameManager instance;
    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }


    CardRepository cardRepository;
    CollectionRepository collectionRepository;
    PlayerRepository playerRepository;
    GameRepository gameRepository;
    private Game game;

    public GameManager(){
        cardRepository = new CardRepository();
        collectionRepository = new CollectionRepository();
        playerRepository = new PlayerRepository();
        gameRepository = new GameRepository();


    }
    public void newGame(String name){
        Collection currCollection = new Collection(0);
        currCollection.loadCards(collectionRepository.loadBaseSet(),null);
        Player currPlayer = new Player(0, name);
        game = new Game(0, currPlayer, currCollection,1);
    }

    //SaveGame is fairly tested
    public void saveGame(){

        GameDto gameDto = new GameDto(
                game.getId(),
                game.getPlayer().getId(),
                game.getPlayer().getInventory().get(0).getId(),
                game.getPlayer().getInventory().get(1).getId(),
                game.getPlayer().getInventory().get(2).getId(),
                game.getCollection().getId(),
                game.getCurrentStage()
                );

        for (Card card : game.getCollection().getImportedCards()){
            cardRepository.save(card);
        }
        collectionRepository.save(game.getCollection());
        gameRepository.save(gameDto);
        playerRepository.save(game.getPlayer());

    }

    public void loadGame(int gameId) {
        List<Card> playerInventory = new ArrayList<>();
        GameDto gameDto = gameRepository.findById(gameId).orElse(null);
        Collection collection = collectionRepository.findById(gameDto.collectionID()).orElse(null);
        Player player = playerRepository.findById(gameDto.playerID()).orElse(null);

        Card slot1 = cardRepository.findById(gameDto.slot1ID()).orElse(null);
        Card slot2 = cardRepository.findById(gameDto.slot2ID()).orElse(null);
        Card slot3 = cardRepository.findById(gameDto.slot3ID()).orElse(null);

        playerInventory.addAll(List.of(slot1,slot2,slot3));
        player.loadPlayerInventory(playerInventory);
        game = new Game(gameDto.gameID(),player,collection,gameDto.currentStage());
;    }
}

    /*
    TODO:
        Make simple use case diagram?
        Load logic mostly done in all of the Repository classes
        Need an LoadGameController setup and LoadGame-view.fxml
        -----------------------------------------
        Make Collection show the game's collection
        Make FilterMethod(name, type maybe?, and tier of course)
        The Cards available are stored in a gridPane, after filtering, get the target of the player's mouse click
            to select/deselect card
        -----------------------------------------

     */
