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
        if (card == null){
            return null;
        }
        String sql = "SELECT COUNT(*) FROM Cards WHERE pokID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, card.getId());
            ResultSet rs = stmt.executeQuery();
            boolean exists = rs.next() && rs.getInt(1) > 0;

            // A card in database, it should never be updated.
            if (exists) {
                return card.getId();
            } else {
                return insert(card);
            }

        } catch (SQLException e) {
            throw new RepositoryException("Error saving card: " + card, e);
        }
    }

    private String insert(Card card) {
        String sql = "INSERT INTO Cards VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, card.getId());
            stmt.setString(2, card.getName());
            stmt.setString(3, card.getType().toString());
            stmt.setInt(4, card.getMaxHP());
            stmt.setString(5, card.getTier().toString());
            stmt.setString(6, card.getImageURL());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0 ){
                return card.getId();
            } else{
                return null;
            }


        } catch (SQLException e) {
            throw new RepositoryException("Error inserting card: " + card, e);
        }
    }


    @Override
    public List<Card> findAll() {
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * from Cards";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
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
    public void delete(String id) {
        String sql = "DELETE FROM Cards WHERE pokID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting collection with id: " + id, e);
        }
    }


}



