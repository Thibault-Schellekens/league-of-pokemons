package be.esi.prj.leagueofpokemons.model.db.repository;
import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Type;
import be.esi.prj.leagueofpokemons.util.ConnectionManager;

import java.sql.*;
import java.util.*;

public class CardRepository implements Repository<String, Card> {
    private final Connection connection;

    public CardRepository(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "Connection not valid");
    }
    public CardRepository() {
        this.connection = ConnectionManager.getConnection();
    }

    @Override
    public Optional<Card> findById(String id) {
        String sql = "SELECT * FROM Cards WHERE pokID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Card(
                            rs.getString("pokID"),
                            rs.getString("pokName"),
                            rs.getInt("pokMaxHP"),
                            rs.getString("pokUrl"),
                            Type.valueOf(rs.getString("pokType"))
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error retrieving card by ID: " + id, e);
        }
        return Optional.empty();
    }

    @Override
    public String save(Card card) {
        String sql = "INSERT INTO Cards (pokID, pokName, pokType, pokMaxHP, pokTier, pokUrl) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, card.getId());
            stmt.setString(2, card.getName());
            stmt.setString(3, card.getType().toString());
            stmt.setInt(4, card.getMaxHP());
            stmt.setString(5, card.getTier().toString());
            stmt.setString(6, card.getImageURL());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException(" Card : " + card.getId()+ " already exists ", e);
        }
        return card.getId();
    }


    @Override
    public List<Card> findAll() {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * from Cards";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                cards.add(
                        new Card(
                                rs.getString("pokID"),
                                rs.getString("pokName"),
                                rs.getInt("pokMaxHP"),
                                rs.getString("pokUrl"),
                                Type.valueOf(rs.getString("pokType"))
                        ));
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding all", e);
        }
        return cards;
    }

    @Override
    public void delete(Card card) {
        String sql = "DELETE FROM Card WHERE pokID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, card.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting collection with id: " + card.getId(), e);
        }
    }


}



