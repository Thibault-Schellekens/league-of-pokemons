package be.esi.prj.leagueofpokemons.model.db.dao;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Collection;
import be.esi.prj.leagueofpokemons.model.core.Type;
import be.esi.prj.leagueofpokemons.model.db.repository.CardRepository;
import be.esi.prj.leagueofpokemons.model.db.repository.RepositoryException;
import be.esi.prj.leagueofpokemons.util.ConnectionManager;

import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import java.sql.*;
import java.util.*;

public class CollectionDao implements Dao<Integer, Collection> {
    private final Connection connection;
    private final CardRepository cardRepository;

    public CollectionDao(Connection connection) {
        this.connection = Objects.requireNonNull(connection, "Connection cannot be null");
        this.cardRepository = new CardRepository(connection);
    }

    public CollectionDao() {
        this.connection = ConnectionManager.getConnection();
        this.cardRepository = new CardRepository(connection);
    }

    @Override
    public Optional<Collection> findById(Integer id) {
        Set<Card> cards = new HashSet<>();
        String sql = "SELECT * FROM Collection WHERE colId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String cardId = rs.getString("pokemonID");
                    cardRepository.findById(cardId).ifPresent(cards::add);
                }
                Collection collection = new Collection();
                collection.loadBaseSet(loadBaseSet());
                collection.loadCardSet(cards);
                return Optional.of(collection);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error retrieving collection with id: " + id, e);
        }
    }


    public void save(int colID, Set<Card> cardsToSave){
        String sql = "INSERT INTO Collection (colId, pokemonID) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Card card : cardsToSave) {
                stmt.setInt(1, colID);
                stmt.setString(2, card.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error saving collection", e);
        }
    }


    @Override
    public void save(Collection collection) {
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Collection WHERE colId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting collection with id: " + id, e);
        }
    }


    public Set<Card> loadBaseSet() {
        Set<Card> cards = new HashSet<>();
        String sql = "SELECT * FROM BaseSet";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Card card = Optional.of(new Card(
                                rs.getString("pokID"),
                                rs.getString("pokName"),
                                rs.getInt("pokMaxHP"),
                                rs.getString("pokUrl"),
                                Type.valueOf(rs.getString("pokType"))
                        )).orElse(null);
                cards.add(card);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cards;
    }


}


