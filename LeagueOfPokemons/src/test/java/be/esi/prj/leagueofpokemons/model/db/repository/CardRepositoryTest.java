package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Type;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CardRepositoryTest {

    private static Connection connection;
    private CardRepository cardRepository;

    // Test cards
    private Card tyranitar = new Card("swsh3-88", "Tyranitar", 180,
            "https://assets.tcgdex.net/en/swsh/swsh3/88/high.png", Type.FIGHTING);
    private Card pikachu = new Card("swsh4-43", "Pikachu", 70,
            "https://assets.tcgdex.net/en/swsh/swsh4/43/high.png", Type.LIGHTNING);
    private Card charizard = new Card("swsh5-25", "Charizard", 170,
            "https://assets.tcgdex.net/en/swsh/swsh5/25/high.png", Type.FIRE);

    @BeforeAll
    static void setupDatabase() throws SQLException {

        // Using memory database for testing, we don't want to mess up the good Card Database

        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

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
        }
    }

    @BeforeEach
    void setup() {
        cardRepository = new CardRepository(connection);
    }

    @AfterEach
    void cleanDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Cards");
        }
    }

    @AfterAll
    static void closeDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void testSave_NewCardAdded() throws SQLException { //Saving new card. We don't use findById directly because that needs to be tested too.
        // Saving card
        String addedCardId = cardRepository.save(tyranitar);

        // We now use a select statement to find the recently added card
        // FindById replacement until tested
        String sql = "SELECT * FROM Cards WHERE pokID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, addedCardId);
            ResultSet rs = stmt.executeQuery();
            boolean hasResults = rs.next();
            assertTrue(hasResults, "Card should exist in database");
            System.out.println("TestSave_NewCardAdded : a new card was successfully added.");
        }
    }

    @Test
    void testSave_CardOnlyAddedOnce() throws SQLException { //Not saving a card mutliple times. We don't use findById directly because that needs to be tested too.
        // Saving card
        String addedCardId = cardRepository.save(tyranitar);
        cardRepository.save(tyranitar);
        // We now use a select statement to find the recently added card
        // FindById replacement until tested
        String sql = "SELECT COUNT(*) FROM Cards WHERE pokID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, addedCardId);
            ResultSet rs = stmt.executeQuery();

            assertTrue(rs.next());
            //Checking if card was only added once
            assertTrue(rs.getInt(1) == 1);
            System.out.println("TestSave_CardOnlyAddedOnce : the card was only added once.");
        }
    }

    @Test
    void testSave_nullCardNotAdded() { // Not saving a null card.
        // Saving null card
        String addedCardId = cardRepository.save(null);
        assertNull(addedCardId);
        System.out.println("TestSave_NullCardNotAdded : the null card was not added");
    }


    @Test
    void testFindCardByID_Found() { //Finding an existing card in the DB by id
        // saving card
        String addedCardId = testSave(tyranitar);
        // Here we test the findById() method
        Optional<Card> result = cardRepository.findById(addedCardId);

        // Then it should be found
        assertTrue(result.isPresent(), "Card should be found");
        Card foundCard = result.get();
        // And thhe 2 cards should match
        assertTrue(tyranitar.equals(foundCard));
        System.out.println("TestFindCardById_Found : card was successfully retrieved.");

    }

    @Test
    void testFindCardById_NotFound() { // Not finding a card that doesn't exist
        // When we try to find a card that doesn't exist
        Optional<Card> result = cardRepository.findById("non-existent-id");

        // Then it should not be found
        assertFalse(result.isPresent(), "Card should not be found");
        System.out.println("TestFindCardById_NotFound : card was successfully not found.");
    }


    @Test
    void testFindAll_ExistingCards() { // Finding all cards added inside the DB.
        // We're saving multiple cards inside the DB
        testSave(tyranitar);
        testSave(pikachu);
        testSave(charizard);

        // When we find all cards
        List<Card> cards = cardRepository.findAll();

        // Then all cards should be returned
        assertEquals(3, cards.size(), "All cards should be returned");

        // 'cards' should contain all the cards we've added
        assertTrue(cards.stream().anyMatch(c -> c.getId().equals(tyranitar.getId())),
                "Result should contain tyranitar");
        assertTrue(cards.stream().anyMatch(c -> c.getId().equals(pikachu.getId())),
                "Result should contain pikachu");
        assertTrue(cards.stream().anyMatch(c -> c.getId().equals(charizard.getId())),
                "Result should contain charizard");
        System.out.println("TestFindAll_ExistingCard : all cards have been successfully retrieved.");
    }

    @Test
    void testDelete_ExistingCard() throws SQLException { // Deleting existing card from the DB
        // Saving card
        testSave(tyranitar);

        // Deleting card
        cardRepository.delete(tyranitar.getId());

        // Then it should no longer be in the database
        String sql = "SELECT COUNT(*) FROM Cards WHERE pokID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, tyranitar.getId());
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            assertEquals(0, rs.getInt(1), "Card should be deleted");
        }

        // And findById should return empty
        Optional<Card> result = cardRepository.findById(tyranitar.getId());
        assertFalse(result.isPresent(), "Card should not be found after deletion");
        System.out.println("TestDelete_ExistingCard : the card has been successfully deleted.");

    }

    @Test
    void testFindAll_EmptyDatabase() { // Not Finding any cards in an empty DB
        // Not adding anything

        // When we find all cards
        List<Card> cards = cardRepository.findAll();

        // Then an empty list should be returned
        assertNotNull(cards, "Result should not be null");
        assertTrue(cards.isEmpty(), "Result should be empty");

        System.out.println("TestFindAll_EmptyDatabase : list 'cards' should be empty, but not null.");

    }

    // -----------------------HELPER METHOD------------------------
    private String testSave(Card card) {
        String sql = "INSERT INTO Cards VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, card.getId());
            stmt.setString(2, card.getName());
            stmt.setString(3, card.getType().toString());
            stmt.setInt(4, card.getMaxHP());
            stmt.setString(5, card.getTier().toString());
            stmt.setString(6, card.getImageURL());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return card.getId();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Couldn't add card to DB", e);
        }
    }
}