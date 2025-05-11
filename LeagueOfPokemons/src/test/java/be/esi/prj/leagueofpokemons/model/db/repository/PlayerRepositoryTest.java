package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.core.Player;
import org.junit.jupiter.api.*;

import javax.swing.plaf.nimbus.State;
import javax.swing.text.html.Option;
import java.sql.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PlayerRepositoryTest {

    private static Connection connection;
    private PlayerRepository playerRepository;

    private static Player playerTest = new Player(1,"Marian");
    private static Player playerTest2 = new Player(2,"Lebron");

    @BeforeAll
    static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        try (Statement stmt = connection.createStatement()){
            stmt.execute("""
                    CREATE TABLE "Player" (
                        "id"	INTEGER,
                        "name"	TEXT NOT NULL,
                        PRIMARY KEY("id" AUTOINCREMENT)
                    )
                """);
        }
    }

    @BeforeEach
    void setup() {
        playerRepository = new PlayerRepository(connection);
    }

    @AfterEach
    void cleanDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM Player");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='player'");
        }
    }

    @AfterAll
    static void closeDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }


    @Test
    void testFindBy_ExistingId(){
        //Adding player manually
        int generatedId = 0;
        try(PreparedStatement stmt = connection.prepareStatement("INSERT INTO Player(name) VALUES (?)")) {
            stmt.setString(1,playerTest.getName());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
                System.out.println("Player was added, id : " + generatedId);
            }
            Optional<Player> found = playerRepository.findById(generatedId);
            System.out.println(found.get().getName());
            System.out.println(playerTest.getName());
            //Same player was found
            assertEquals(found.get().getName(),playerTest.getName());
            System.out.println("testFindBy_ExistingId : Player was found" );
            System.out.println("-------------------------------------------------------");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindBy_NonExistingId(){
        //Using non-existing id to test findById returning empty
        Optional<Player> player = playerRepository.findById(-56);
        assertTrue(player.isEmpty());
        System.out.println("TestFindBy_NonExistingId : Returned Empty due to player not found" );
        System.out.println("-------------------------------------------------------");
    }

    @Test
    void testSave_newPlayer(){
        //saving player
        int addedPlayerId = playerRepository.save(playerTest);
        // manually checking if player exists
        String sql = "Select count(*) from Player where id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1,addedPlayerId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                assertTrue(rs.getInt(1) == 1);
                System.out.println("TestSave_ExistingPlayer : Player Was found");
                System.out.println("-------------------------------------------------------");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSave_NotDuplicating(){
        //saving player
        int addedPlayerId = playerRepository.save(playerTest);
        playerRepository.save(playerTest);
        // manually checking if player exists
        String sql = "Select count(*) from Player where id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1,addedPlayerId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                assertTrue(rs.getInt(1) == 1);
                System.out.println("TestSave_NotDuplicating: Player was only added once");
                System.out.println("-------------------------------------------------------");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testSave_UpdatingInsteadOfDuplicating(){
        // saving player
        int addedPlayerId = playerRepository.save(playerTest);
        // saving the exact same player again but with different name
        // this should update the firstly added player instead of adding it again
        int addedAgainId = playerRepository.save(new Player(addedPlayerId,"CopiedMarian"));
        // manually checking if player exists
        String sql = "Select * from Player where id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1,addedAgainId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                String updatedName = rs.getString(2);
                // updatedName should be CopiedMarian
                System.out.println(updatedName);
                assertFalse(rs.next()); //if this fails, that means 2 players were added
                assertTrue(updatedName.matches("CopiedMarian"));
                System.out.println("TestSave_UpdatingInsteadOfDuplicating : Player Was found");
            }        System.out.println("-------------------------------------------------------");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindAll_ExistingPlayers(){
        //manually adding players
        try(PreparedStatement stmt = connection.prepareStatement("INSERT INTO Player(name) VALUES (?),(?)")) {
            stmt.setString(1,playerTest.getName());
            stmt.setString(2,playerTest2.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //Checkingif found players match the names of the manually added ones
        List<Player> foundPlayers = playerRepository.findAll();
        for (Player plr : foundPlayers){
            System.out.println(plr.getName());
        }
        assertTrue(foundPlayers.size() == 2);
        System.out.println("TestFindAll_ExistingPlayers : Players were found");
        System.out.println("-------------------------------------------------------");
    }

    @Test
    void testFindAll_EmptyDB(){
        //should be empty if no players inside the DB
        List<Player> foundPlayers = playerRepository.findAll();
        assertTrue(foundPlayers.isEmpty());
        System.out.println("TestFindAll_EmptyDB: No players found");
        System.out.println("-------------------------------------------------------");
    }

    @Test
    void testDelete_ExistingPlayer(){
        int addedPlayerId = 0;
        try(PreparedStatement stmt = connection.prepareStatement("INSERT INTO Player(name) VALUES (?)")) {
            stmt.setString(1,playerTest.getName());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()){
                addedPlayerId = rs.getInt(1);
            }
            System.out.println(addedPlayerId);
            assertTrue(addedPlayerId > 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        playerRepository.delete(addedPlayerId);
        String sql = "Select COUNT(*) from Player where id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1,addedPlayerId);
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            System.out.println(rs.getInt(1));
            assertTrue(rs.getInt(1) == 0);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("TestDelete_ExistingPlayer: Player was deleted");
        System.out.println("-------------------------------------------------------");
    }
}