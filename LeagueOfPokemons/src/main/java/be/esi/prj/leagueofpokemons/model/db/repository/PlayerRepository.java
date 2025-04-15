package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Game;
import be.esi.prj.leagueofpokemons.model.core.Player;
import be.esi.prj.leagueofpokemons.util.ConnectionManager;


import java.sql.*;
import java.util.*;

public class PlayerRepository implements Repository<Integer, Player> {
    private final Connection connection;
    private final CardRepository cardRepository;

    public PlayerRepository() {
        this.connection = ConnectionManager.getConnection();
        cardRepository = new CardRepository();
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

    @Override
    public Integer save(Player player) {
        System.out.println("SAVING PLAYER");
        String sql = "INSERT INTO Player (id, name) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.setString(2, player.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error saving player", e);
        }
        return player.getId();
    }

    @Override
    public Set<Player> findAll() {
        return null;
    }

    @Override
    public void delete(Player entity) {

    }

}
