package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.db.dto.GameDto;
import be.esi.prj.leagueofpokemons.util.ConnectionManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GameRepository implements Repository<Integer, GameDto> {

    private Connection connection;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public GameRepository() {
        this.connection = ConnectionManager.getConnection();
    }

    @Override
    public Optional<GameDto> findById(Integer gameId) {
        String sql = "Select * from GameSaves where playerID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, gameId);
            try (ResultSet rs = preparedStatement.executeQuery()) {

                if (rs.next()) {
                    GameDto gameDto = mapResultSetToDto(rs);

                    return Optional.of(gameDto);
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding by id", e);
        }
        return Optional.empty();
    }

    @Override
    public Integer save(GameDto gameDto) {
        String sql = "SELECT COUNT(*) FROM GameSaves WHERE gameId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gameDto.gameID());
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next() && rs.getInt(1) > 0;

            if (exists) {
                int updatedRows = update(gameDto);
                return updatedRows > 0 ? gameDto.gameID() : -1;
            } else {
                return insert(gameDto);
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error saving game", e);
        }
    }

    private int insert(GameDto gameDto) {
        String sql = "INSERT into GameSaves(playerID, name, slot1ID, slot2ID, slot3ID, currentStage, date)values(?, ? , ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gameDto.playerID());
            stmt.setString(2, gameDto.name());
            stmt.setString(3, gameDto.slot1ID());
            stmt.setString(4, gameDto.slot2ID());
            stmt.setString(5, gameDto.slot3ID());
            stmt.setInt(6, gameDto.currentStage());
            stmt.setString(7, gameDto.date().format(formatter));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error saving", e);
        }
    }

    private int update(GameDto gameDto) {
        String sql = "UPDATE GameSaves SET name = ?, slot1ID = ?, slot2ID = ?, slot3ID = ?, currentStage = ?, date = ? WHERE gameID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, gameDto.name());
            stmt.setString(2, gameDto.slot1ID());
            stmt.setString(3, gameDto.slot2ID());
            stmt.setString(4, gameDto.slot3ID());
            stmt.setInt(5, gameDto.currentStage());
            stmt.setString(6, gameDto.date().format(formatter));

            stmt.setInt(7, gameDto.gameID());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<GameDto> findAll() {
        List<GameDto> gameSaves = new ArrayList<>();
        String sql = "Select * from GameSaves";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                GameDto gameDto = mapResultSetToDto(rs);

                gameSaves.add(gameDto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return gameSaves;
    }

    private GameDto mapResultSetToDto(ResultSet rs) throws SQLException {
        int gameId = rs.getInt("gameID");
        String name = rs.getString("name");
        int playerId = rs.getInt("playerID");
        String slot1Id = rs.getString("slot1ID");
        String slot2Id = rs.getString("slot2ID");
        String slot3Id = rs.getString("slot3ID");
        int stage = rs.getInt("currentStage");
        String date = rs.getString("date");

        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

        return new GameDto(gameId, name, playerId, slot1Id, slot2Id, slot3Id, stage, dateTime);
    }

    @Override
    public void delete(GameDto entity) {

    }
}
