package be.esi.prj.leagueofpokemons.util;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Collection;
import be.esi.prj.leagueofpokemons.model.core.Game;
import be.esi.prj.leagueofpokemons.model.core.Player;
import be.esi.prj.leagueofpokemons.model.db.repository.CardRepository;
import be.esi.prj.leagueofpokemons.model.db.repository.CollectionRepository;
import be.esi.prj.leagueofpokemons.model.db.repository.GameRepository;
import be.esi.prj.leagueofpokemons.model.db.repository.PlayerRepository;

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
    private Collection currCollection;
    private int currPlayerId;

    public GameManager(){
        cardRepository = new CardRepository();
        collectionRepository = new CollectionRepository();
        playerRepository = new PlayerRepository();
        gameRepository = new GameRepository();
        currCollection = new Collection();
        currCollection.loadBaseSet(collectionRepository.loadBaseSet());
        currPlayerId = playerRepository.getNewPlayerID();
//        currPlayer =  new Player(playerRepository.getNewPlayerID(), playerName);
    }

    public void newGame(String name){
        Player player = new Player(currPlayerId, name);
        game = new Game(player, currCollection,0);
    }

    //SaveGame is fairly tested, i need to
    public void saveGame(){
        //Step 1 : add all collection cards to the Card table
        for (Card card : collectionRepository.getCache()){
            cardRepository.save(card);
        }
        //Step 2
        //We add the collection(if any cards imported) to the Collection Table
        //Here again it doesnt matter, since we will not update a line, only add
        collectionRepository.save(currPlayerId);
        //Step 3
        //if there already exists a gameSave for this player then update player
        if (gameRepository.exists(currPlayerId)){
            gameRepository.update(currPlayerId,game.getCurrentStage());
            playerRepository.updatePlayer(game.getPlayer());
        }else {
            gameRepository.save(game);
            collectionRepository.save(currPlayerId);
            playerRepository.save(game.getPlayer());

        }
    }

    public int getCurrPlayerId() {
        return currPlayerId;
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

}
