package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.core.Card;
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
                Optional<Card> slot1 = cardRepository.findById(rs.getString(3));
                Optional<Card> slot2 = cardRepository.findById(rs.getString(4));
                Optional<Card> slot3 = cardRepository.findById(rs.getString(5));

                List<Card> playerCards = List.of(
                        slot1.orElse(null),
                        slot2.orElse(null),
                        slot3.orElse(null)
                );
                Player player = new Player();
                player.loadPlayer(id, rs.getString(2),playerCards);
                return Optional.of(player);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error retrieving player by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public void save(Player player) {
        System.out.println("SAVING PLAYER");
        String sql = "INSERT INTO Player (id, name, slot1ID, slot2ID, slot3ID) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, player.getId());
            stmt.setString(2, player.getName());
            List<Card> playerCards = player.getInventory();
            System.out.println("Player's Inventory Size : " + playerCards.size());
            for (int i = 0; i < 3; i++) {
                if (i < playerCards.size()) {
                    stmt.setString(3 + i, playerCards.get(i).getId());
                } else {
                    stmt.setNull(3 + i, java.sql.Types.VARCHAR);
                }
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error saving player", e);
        }
    }

    @Override
    public Set<Player> findAll() {
        return null;
    }

    public void updatePlayer(Player player) {
        System.out.println("UPDATING PLAYER");
        String sql = "UPDATE Player SET slot1ID = ?, slot2ID = ?, slot3ID = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            List<Card> playerCards = player.getInventory();
            System.out.println("Player's Inventory Size : " + playerCards.size());

            for (int i = 0; i < 3; i++) {
                if (i < playerCards.size()) {
                    stmt.setString(i + 1, playerCards.get(i).getId());
                } else {
                    stmt.setNull(i + 1, java.sql.Types.VARCHAR);
                }
            }

            stmt.setInt(4, player.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



    public int getNewPlayerID(){
        String sql = "Select count(*) from Player";
        try(Statement stmt = connection.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()){
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}
