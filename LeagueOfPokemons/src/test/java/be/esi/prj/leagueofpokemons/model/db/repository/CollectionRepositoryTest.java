package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Collection;
import be.esi.prj.leagueofpokemons.model.core.Type;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CollectionRepositoryTest {

    private static Connection connection;
    private CollectionRepository collectionRepository;
    private CardRepository cardRepository;
    // Test cards
    private static Card tyranitar = new Card("swsh3-88", "Tyranitar", 180,
            "https://assets.tcgdex.net/en/swsh/swsh3/88/high.png", Type.FIGHTING);
    private static Card pikachu = new Card("swsh4-43", "Pikachu", 70,
            "https://assets.tcgdex.net/en/swsh/swsh4/43/high.png", Type.LIGHTNING);
    private static Card charizard = new Card("swsh5-25", "Charizard", 170,
            "https://assets.tcgdex.net/en/swsh/swsh5/25/high.png", Type.FIRE);

    private static Collection collectionTest;

    @BeforeAll
    static void setupDatabase() throws SQLException {

        // Using memory database for testing, we don't want to mess up the good Card/Collection Database

        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        Set<Card> baseSet =  new HashSet<>(List.of(tyranitar));
        Set<Card> importedCards = new HashSet<>(List.of(charizard,pikachu));

        collectionTest = new Collection(1);
        collectionTest.loadCards(baseSet,importedCards);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                    CREATE TABLE IF NOT EXISTS Cards (
                        pokID VARCHAR PRIMARY KEY,
                        pokName TEXT NOT NULL,
                        pokType TEXT NOT NULL,
                        pokMaxHP INTEGER NOT NULL,
                        pokTier TEXT NOT NULL,
                        pokUrl VARCHAR NOT NULL
                    )
                """);
            stmt.execute("""
                    CREATE TABLE "Collection" (
                    	"colID"	INTEGER NOT NULL,
                    	"pokemonID"	VARCHAR NOT NULL,
                    	UNIQUE("colID","pokemonID"),
                    	FOREIGN KEY("pokemonID") REFERENCES "Cards"
                    )
                """);
            stmt.executeUpdate("""
                        INSERT INTO Cards (pokID, pokName, pokType, pokMaxHP, pokTier, pokUrl) VALUES
                        ('swsh3-88', 'Tyranitar', 'FIGHTING', 180, 1, 'https://assets.tcgdex.net/en/swsh/swsh3/88/high.png'),
                        ('swsh4-43', 'Pikachu', 'LIGHTNING', 70, 1, 'https://assets.tcgdex.net/en/swsh/swsh4/43/high.png'),
                        ('swsh5-25', 'Charizard', 'FIRE', 170, 1, 'https://assets.tcgdex.net/en/swsh/swsh5/25/high.png');
                """);
        }

    }

    @BeforeEach
    void setup() {
        collectionRepository = new CollectionRepository(connection);
        cardRepository = new CardRepository(connection);
    }

    @AfterEach
    void cleanDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Collection");
        }
    }

    @AfterAll
    static void closeDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }



    @Test
    void testSave_SaveCollection() throws SQLException { //Saving a collection's importedCards.
        //Saving collection (has a baseSet and an importedSet)
        int collectionAddedId = collectionRepository.save(collectionTest);
        //collectionTest has 2 imported cards, we should only see 2
        List<Card> actualCards = returnCardsOfCollection(collectionAddedId);
        Set<Card> expectedCards = collectionTest.getImportedCards();
        assertEquals(expectedCards.size(),actualCards.size());
        System.out.print("Expected : ");
        expectedCards.forEach(e -> System.out.print (" | name : " + e.getName()));
        System.out.println();
        System.out.print("Actual : ");
        actualCards.forEach(e -> System.out.print (" | name : " + e.getName()));
        System.out.println();
        assertTrue(expectedCards.containsAll(actualCards));
        System.out.println("testSave_SaveCollection : only imported cards of collection was saved");

    }
    @Test
    void testSave_ignoreDuplicatesCollection() { //Saving a collection's importedCards but ignoring cards already added.
        //Saving collection (has a baseSet and an importedSet)
        insertCardIntoCollectionDB(collectionTest.getId(), charizard);
        //already adding one of the collections cards in DB to see if save will add it again
        int collectionAddedId = collectionRepository.save(collectionTest);

        //collectionTest has 2 imported cards, we should only see 2
        List<Card> actualCards = returnCardsOfCollection(collectionAddedId);
        Set<Card> expectedCards = collectionTest.getImportedCards();
        System.out.print("Expected : ");
        expectedCards.forEach(e -> System.out.print (" | name : " + e.getName()));
        System.out.println();
        System.out.print("Actual : ");
        actualCards.forEach(e -> System.out.print (" | name : " + e.getName()));
        System.out.println();
        assertTrue(expectedCards.containsAll(actualCards));
        System.out.println("testSave_ignoreDuplicatesCollection : imported cards were not added twice ");

    }

    @Test
    void testFindAll_ReturnsAllCollections() {
        Collection secondCollection = new Collection(2);
        Set<Card> baseSet =  new HashSet<>(List.of(tyranitar,charizard));
        Set<Card> importedCards = new HashSet<>(List.of(pikachu));
        secondCollection.loadCards(baseSet,importedCards);

        collectionRepository.save(collectionTest);
        collectionRepository.save(secondCollection);

        List<Collection> all = collectionRepository.findAll();
        assertEquals(2, all.size());
        assertTrue(
                all.get(0).getId() == collectionTest.getId() &&
                        all.get(1).getId() == secondCollection.getId()
        );
        System.out.println("Expected : 2 collections | Actual : " + all.size());
        System.out.println("testFindAll_ReturnsAllCollections : returns all collections ");

    }

    @Test
    void testDelete_RemovesCollectionEntries() {
        int coladdedId = collectionRepository.save(collectionTest);
        System.out.println(collectionTest.getImportedCards().size());
        System.out.println(coladdedId + " " + collectionTest.getId());
        collectionRepository.delete(coladdedId);
        Optional<Collection> found = collectionRepository.findById(coladdedId);
        assertTrue(found.get().getImportedCards().isEmpty());

    }


    // -----------------------HELPER METHOD------------------------

    private List<Card> returnCardsOfCollection(int colId){
        List<Card> cards = new ArrayList<>();
        String sql = "SELECT * FROM Collection WHERE colID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, colId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String cardId = rs.getString("pokemonID");
                    cardRepository.findById(cardId).ifPresent(cards::add);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cards;
    }

    private void insertCardIntoCollectionDB(int colID, Card card) {
        String sqlInsert = "INSERT INTO Collection (colID, pokemonID) VALUES (?, ?)";
        String sqlCheck = "SELECT COUNT(*) FROM Collection WHERE pokemonID = ? AND colID = ?";

        try (
                PreparedStatement checkStmt = connection.prepareStatement(sqlCheck);
                PreparedStatement insertStmt = connection.prepareStatement(sqlInsert)
        ) {
            checkStmt.setString(1, card.getId());
            checkStmt.setInt(2, colID);

            try (ResultSet rs = checkStmt.executeQuery()) {
                boolean exists = rs.next() && rs.getInt(1) > 0;

                if (!exists) {
                    insertStmt.setInt(1, colID);
                    insertStmt.setString(2, card.getId());
                    insertStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error inserting collection", e);
        }
    }


}
