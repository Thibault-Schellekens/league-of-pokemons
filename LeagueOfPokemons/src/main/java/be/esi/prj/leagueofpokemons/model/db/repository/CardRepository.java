package be.esi.prj.leagueofpokemons.model.db.repository;
import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Game;
import be.esi.prj.leagueofpokemons.model.core.Type;
import be.esi.prj.leagueofpokemons.util.ConnectionManager;
import be.esi.prj.leagueofpokemons.util.GameManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    public void save(Card card) {
        if (!cardExists(card.getId())) {
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
                throw new RepositoryException("Error saving card", e);
            }
            System.out.println("Card already exists in the encyclopedia");
        }
    }

    public boolean cardExists(String id){
        String sql =" Select count(*) from Cards where pokID = ?";
        boolean found = false;
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                found = rs.getInt(1) > 1;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return found;
    }

    @Override
    public Set<Card> findAll() {
        return null;
    }


}



