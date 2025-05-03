package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Collection;
import be.esi.prj.leagueofpokemons.model.core.Game;
import be.esi.prj.leagueofpokemons.model.core.Type;
import be.esi.prj.leagueofpokemons.model.db.repository.CardRepository;
import be.esi.prj.leagueofpokemons.model.db.repository.Repository;
import be.esi.prj.leagueofpokemons.model.db.repository.RepositoryException;
import be.esi.prj.leagueofpokemons.util.ConnectionManager;

import java.sql.*;
import java.util.*;

public class CollectionRepository implements Repository<Integer, Collection> {
    private final Connection connection;

    public CollectionRepository() {
        this.connection = ConnectionManager.getConnection();
    }

    @Override
    public Optional<Collection> findById(Integer id) {
        CardRepository cardRepository = new CardRepository();
        Set<Card> cards = new HashSet<>();
        String sql = "SELECT * FROM Collection WHERE colId = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String cardId = rs.getString("pokemonID");
                    cardRepository.findById(cardId).ifPresent(cards::add);
                }
                Collection collection = new Collection(id);
                collection.loadCards(loadBaseSet(), cards);
                return Optional.of(collection);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error retrieving collection with id: " + id, e);
        }
    }
    @Override
    public Integer save(Collection collection) {
        String sql = "INSERT INTO Collection (colId, pokemonID) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Card card : collection.getImportedCards()) {
                stmt.setInt(1, collection.getId());
                stmt.setString(2, card.getId());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error saving collection", e);
        }
        return collection.getId();
    }


    @Override
    public Set<Collection> findAll() {
        Set<Collection> collections = null;
        String sql = "Select * from Collection";
        try(Statement statement= connection.createStatement()){
            try(ResultSet rs = statement.executeQuery(sql)){
                while(rs.next()){
                    // todo
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return collections;
    }

    @Override
    public void delete(Collection collection) {
        String sql = "DELETE FROM Collection WHERE colId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, collection.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting collection with id: " + collection.getId(), e);
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


