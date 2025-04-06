package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.core.Collection;
import be.esi.prj.leagueofpokemons.model.core.Game;
import be.esi.prj.leagueofpokemons.model.core.Player;
import be.esi.prj.leagueofpokemons.util.ConnectionManager;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public class GameRepository implements Repository<Integer, Game> {

    private Connection connection;

    public GameRepository() {
        this.connection = ConnectionManager.getConnection();
    }


    public boolean exists(Integer id){
        String sql =" Select * from GameSaves where playerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,id);{
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()){
                    return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }




    @Override
    public Optional<Game> findById(Integer playerId) {
        Player plr;
        Collection collection;
        PlayerRepository plrRep = new PlayerRepository();
        CollectionRepository colRep = new CollectionRepository();
        String sql = "Select * from GameSaves where playerID = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, playerId);
            try(ResultSet rs = preparedStatement.executeQuery()){
                if (rs.next()){
                    plr = plrRep.findById(playerId).orElseThrow(() -> new RuntimeException("Player not found"));
                    collection = colRep.findById(playerId).orElse(null);
                    int currStage = rs.getInt(4);
                    return Optional.of(new Game(plr,collection,currStage));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Game game) {
        System.out.println("League Of Pokemons : Saving new game ...");
        String sql = "INSERT into GameSaves(playerID, collectionID, currentStage)values(?,?,?)";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,game.getPlayer().getId());
            preparedStatement.setInt(2,game.getPlayer().getId());
            preparedStatement.setInt(3,game.getCurrentStage());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void update(int playerId, int currentStage){
        System.out.println("League Of Pokemons : Updating current game save ...");
        //we only update currentStage
        //Player's inventory and collection cards are already being dealt with
        //Since we know we only update this gameSave, the playerID and collectionID wont change whatsoever
        String sql = "UPDATE GameSaves SET currentStage = ? WHERE playerID = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //deso thibault je sais que ca m'a pris longtemps :((
            preparedStatement.setInt(1,currentStage);
            preparedStatement.setInt(2,playerId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //i dont really wanna use these,
    //creating game objects, having to loadthem everytime, kinda boring and yuck
    @Override
    public Set<Game> findAll() {
        return null;
    }
}
