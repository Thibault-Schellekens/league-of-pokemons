package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Collection;
import be.esi.prj.leagueofpokemons.model.core.Type;
import be.esi.prj.leagueofpokemons.util.ConnectionManager;

import java.sql.*;
import java.util.*;

public class CollectionRepository implements Repository<Integer, Collection> {
    private final Connection connection;
    private CardRepository cardRepository;

    public CollectionRepository() {
        this.connection = ConnectionManager.getConnection();
    }

    public CollectionRepository(Connection connection) {
        this.connection = connection;
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
        String sqlInsert = "INSERT INTO Collection (colID, pokemonID) VALUES (?, ?)";
        String sqlCheck = "SELECT COUNT(*) FROM Collection WHERE pokemonID = ? AND colID = ?";

        try (
                PreparedStatement checkStmt = connection.prepareStatement(sqlCheck);
                PreparedStatement insertStmt = connection.prepareStatement(sqlInsert)
        ) {
            for (Card card : collection.getImportedCards()) {
                checkStmt.setString(1, card.getId());
                checkStmt.setInt(2, collection.getId());

                try (ResultSet rs = checkStmt.executeQuery()) {
                    boolean exists = rs.next() && rs.getInt(1) > 0;

                    if (!exists) {
                        insertStmt.setInt(1, collection.getId());
                        insertStmt.setString(2, card.getId());
                        insertStmt.executeUpdate();
                    }
                }
            }
            return collection.getId();
        } catch (SQLException e) {
            throw new RepositoryException("Error inserting collection", e);
        }
    }


    @Override
    public List<Collection> findAll() {
        Map<Integer, Collection> collectionMap = new HashMap<>();
        String sql = "SELECT colId, pokemonID FROM Collection";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                int collectionId = rs.getInt("colId");
                String pokemonId = rs.getString("pokemonID");
                // Get or create the Collection
                Collection collection = collectionMap.computeIfAbsent(collectionId, Collection::new);
                // Find the card and add to the Collection
                cardRepository.findById(pokemonId).ifPresent(collection::addCard);
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding all collections", e);
        }
        return new ArrayList<>(collectionMap.values());
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM Collection WHERE colID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryException("Error deleting collection with id: " + id, e);
        }
    }


    //This uses a fixed BaseSet Table with filled with card data. Not to be tampered with.
    public Set<Card> loadBaseSet() {
        Set<Card> cards = new HashSet<>();
        String sql = "SELECT * FROM BaseSet";
        try (Statement stmt = ConnectionManager.getConnection().createStatement()) {
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
            throw new RepositoryException("Error loading BaseSet", e);
        }
        return cards;
    }


}


