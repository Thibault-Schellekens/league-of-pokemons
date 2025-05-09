package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.core.Player;
import be.esi.prj.leagueofpokemons.util.ConnectionManager;


import java.sql.*;
import java.util.*;

public class PlayerRepository implements Repository<Integer, Player> {
    private final Connection connection;

    public PlayerRepository() {
        this.connection = ConnectionManager.getConnection();
    }

    @Override
    public Optional<Player> findById(Integer id) {
        String sql = "SELECT * FROM Player WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Player player = new Player(id, rs.getString(2));
                return Optional.of(player);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error retrieving player by ID: " + id, e);
        }
        return Optional.empty();
    }

    // TODO: The save method for the Player should only be called via a login system, not via the Game save.

    @Override
    public Integer save(Player player) {
        String sql = "SELECT COUNT(*) FROM Player WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next() && rs.getInt(1) > 0;

            if (exists) {
                int updatedRows = update(player);
                return updatedRows > 0 ? player.getId() : -1;
            } else {
                return insert(player);
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error saving player: " + player, e);
        }
    }

    private int insert(Player player) {
        String sql = "INSERT INTO Player (id, name) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.setString(2, player.getName());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error inserting player: " + player, e);
        }
    }

    private int update(Player player) {
        String sql = "UPDATE Player SET name = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, player.getName());
            stmt.setInt(2, player.getId());

            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error updating player: " + player, e);
        }
    }

    @Override
    public Set<Player> findAll() {
        return null;
    }

    @Override
    public void delete(Player entity) {

    }

}
