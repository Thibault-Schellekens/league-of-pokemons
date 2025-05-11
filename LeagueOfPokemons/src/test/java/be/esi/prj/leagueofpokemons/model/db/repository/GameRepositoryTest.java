package be.esi.prj.leagueofpokemons.model.db.repository;

import be.esi.prj.leagueofpokemons.model.core.Game;
import be.esi.prj.leagueofpokemons.model.core.Player;
import be.esi.prj.leagueofpokemons.model.db.dto.GameDto;
import be.esi.prj.leagueofpokemons.util.GameManager;
import org.junit.jupiter.api.*;

import javax.swing.text.html.Option;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameRepositoryTest {
    private static Connection connection;
    private GameRepository gameRepository;
    private static GameDto gameDtoTest = new GameDto(1,"GameSave", 1,"randomPokId","randomPokId","randomPokId",0, LocalDateTime.now());
    private static GameDto gameDtoTest2 = new GameDto(2, "AnotherGameSave", 2,"randomPokId","randomPokId","randomPokId",0, LocalDateTime.now());

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeAll
    static void setupDatabase() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite::memory:");

        try (Statement stmt = connection.createStatement()){
            stmt.execute("""
                    CREATE TABLE "GameSaves"
                (
                    gameID       INTEGER
                        primary key autoincrement,
                    name         TEXT    not null
                        unique,
                    playerID     INTEGER not null,
                    slot1ID      TEXT,
                    slot2ID      TEXT,
                    slot3ID      TEXT,
                    currentStage INTEGER not null,
                    date         TEXT    not null
                )
                """);
        }
    }

    @BeforeEach
    void setup() {
        gameRepository = new GameRepository(connection);

    }

    @AfterEach
    void cleanDatabase() throws SQLException {

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM GameSaves");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='GameSaves'");
//            stmt.executeQuery("SELECT * FROM GameSaves");
            System.out.println("AfterEach");
            System.out.println("---------------------------------------------------------");

        }

    }

    @AfterAll
    static void closeDatabase() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    @Test
    void testFindBy_ExistingId() {
        //Manually adding the gameDto
        int addedGamesaveId = safeInsert(gameDtoTest);

        Optional<GameDto> foundGameSave = gameRepository.findById(addedGamesaveId);
        assertFalse(foundGameSave.isEmpty());
        assertEquals(foundGameSave.get().gameID(), gameDtoTest.gameID());
        System.out.println(foundGameSave.get().name() + " | " + gameDtoTest.name());
        System.out.println("TestFindBy_ExistingId : The exact game save was found");
    }


    @Test
    void testFindBy_NonExistingId() {
        //Manually adding the gameDto
        Optional<GameDto> foundGameSave = gameRepository.findById(-56);
        assertTrue(foundGameSave.isEmpty());
        System.out.println("TestFindBy_ExistingId : The exact game save was found");
    }
    @Test
    void testSave_newGameSave() {
        int addedSaveId = gameRepository.save(gameDtoTest);
        int foundId = safeFind(addedSaveId);
        assertEquals(foundId, 1);
        System.out.println("TestSave_newGameSave : the gameSave was found after inserting");
    }

    @Test
    void testSave_NotDuplicatingGameSave() {
        int addedSaveId = gameRepository.save(gameDtoTest);
        gameRepository.save(gameDtoTest);
        int foundRows = safeFind(addedSaveId);
        System.out.println(foundRows);
        assertEquals(foundRows, 1);
        System.out.println("TestSave_NotDuplicatingGameSave : the gameSave was not duplicated");
    }
    @Test
    void testSave_UpdatingInsteadOfDuplicating() {
        int addedSaveId = gameRepository.save(gameDtoTest);
        System.out.println(gameDtoTest.slot1ID());
        GameDto gameDtoTesting2 = new GameDto(addedSaveId, gameDtoTest.name(), 1,"changedSlot","randomPokId","randomPokId",0, LocalDateTime.now());
        gameRepository.save(gameDtoTesting2);

        String sql = "Select slot1ID from GameSaves where gameID = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1,addedSaveId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                String updatedSlot = rs.getString(1);
                // updatedName should be
                System.out.println(updatedSlot);
                assertFalse(rs.next()); //if this fails, that means 2 gameSaves were added
                assertFalse(updatedSlot.matches(gameDtoTest.slot1ID()));
                System.out.println("TestSave_UpdatingInsteadOfDuplicating : The row was updated as we can see for the slotID1");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindAll_ExistingGameSaves() {
        safeInsert(gameDtoTest);
        GameDto gameDtoTesting2 = new GameDto(2, "AnotherGameSave", 1,"randomPokId","randomPokId","randomPokId",0, LocalDateTime.now());
        safeInsert(gameDtoTesting2);

        List<GameDto> gameSaves = gameRepository.findAll();
        System.out.println(gameSaves.size());
        assertEquals(gameSaves.size(),2);
        System.out.println("Expected ids: " + gameDtoTest.gameID() + ", "+ gameDtoTesting2.gameID());
        System.out.println("Actual ids: " + gameSaves.get(0).gameID() + ", "+ gameSaves.get(1).gameID());

        assertEquals(gameSaves.get(0).gameID() , gameDtoTest.gameID());
        assertEquals(gameSaves.get(1).gameID() , gameDtoTesting2.gameID());
        System.out.println("TestFindAll_ExistingGameSaves : all gameSaves were found");
    }

    @Test
    void testFindAll_EmptyDB() {
        List<GameDto> gameSaves = gameRepository.findAll();
        assertTrue(gameSaves.isEmpty());
        System.out.println("TestFindAll_EmptyDB : 0 games were found");
    }

    @Test
    void testDelete_ExistingGameSave() {
        int addedGameSaveId = safeInsert(gameDtoTest);
        assertEquals(safeFind(addedGameSaveId), 1);
        System.out.println("Player was added. Lets continue with the Deleting");
        gameRepository.delete(addedGameSaveId);
        assertEquals(safeFind(addedGameSaveId), -1);
        System.out.println("TestDelete_ExistingGameSave : gameSave was safely deleted");

    }


//-----------------HELPER METHODS----------------------

    private int safeInsert(GameDto gameDto) {
        String sql = "INSERT into GameSaves(playerID, name, slot1ID, slot2ID, slot3ID, currentStage, date)values(?, ? , ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, gameDto.playerID());
            stmt.setString(2, gameDto.name());
            stmt.setString(3, gameDto.slot1ID());
            stmt.setString(4, gameDto.slot2ID());
            stmt.setString(5, gameDto.slot3ID());
            stmt.setInt(6, gameDto.currentStage());
            stmt.setString(7, gameDto.date().format(formatter));

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : -1;
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error saving", e);
        }
    }

    public int safeFind(Integer gameId) {
        String sql = "Select count(*) from GameSaves where gameID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, gameId);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()){
                    int rowsAffected = rs.getInt(1);
                    if (rowsAffected > 0){
                        return rowsAffected;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Error finding by id", e);
        }
        return -1;
    }



}