package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Game;
import be.esi.prj.leagueofpokemons.model.db.dto.GameDto;
import be.esi.prj.leagueofpokemons.util.ConnectionManager;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class GameRepository implements Repository<Integer, GameDto> {

    private Connection connection;

    public GameRepository() {
        this.connection = ConnectionManager.getConnection();
    }

    @Override
    public Optional<GameDto> findById(Integer gameId) {
        PlayerRepository plrRep = new PlayerRepository();
        CollectionRepository colRep = new CollectionRepository();
        String sql = "Select * from GameSaves where playerID = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, gameId);
            try(ResultSet rs = preparedStatement.executeQuery()){

                if (rs.next()){
                    return Optional.of(
                            new GameDto(
                                    gameId,
                                    rs.getInt(2),
                                    rs.getString(3),
                                    rs.getString(4),
                                    rs.getString(5),
                                    rs.getInt(6),
                                    rs.getInt(7)
                            )
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }


    @Override
    public void save(GameDto gameDto) {
        String findGame = "Select * from GameSaves where id = " + gameDto.gameID();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(findGame);
            if (rs.next()) {
                System.out.println("League Of Pokemons : updating save ...");
                update(gameDto);
            } else{
                System.out.println("League Of Pokemons : Saving new game ...");
                String sql = "INSERT into GameSaves(playerID, collectionID, currentStage)values(?,?,?)";
                try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                    preparedStatement.setInt(1,gameDto.playerID());
                    preparedStatement.setInt(2,gameDto.gameID());
                    preparedStatement.setInt(3,gameDto.currentStage());
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private void update(GameDto gameDto){
        System.out.println("League Of Pokemons : Updating current gameDto save ...");
        //Player's inventory and collection cards are already being dealt with
        //Since we know we only update this gameSave, the playerID and collectionID won't change whatsoever
        String sql = "UPDATE GameSaves SET slot1ID = ?, slot2ID = ?, slot3ID = ?, currentStage = ? WHERE gameID = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            //deso thibault je sais que ca m'a pris longtemps :((
            preparedStatement.setString(1,gameDto.slot1ID());
            preparedStatement.setString(2,gameDto.slot2ID());
            preparedStatement.setString(3,gameDto.slot3ID());
            preparedStatement.setInt(4,gameDto.currentStage());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Set<GameDto> findAll() {
        Set<GameDto> gameSaves = new HashSet<>();
        String sql = "Select * from GameSaves";
        try(Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                gameSaves.add(
                        new GameDto(
                                rs.getInt(1),
                                rs.getInt(2),
                                rs.getString(3),
                                rs.getString(4),
                                rs.getString(5),
                                rs.getInt(6),
                                rs.getInt(7)
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gameSaves;
    }


    public int getNewGameId(){
        String sql = "SELECT max(gameID) from GameSaves";
        try(Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
    @Override
    public void delete(GameDto entity) {

    }
}
