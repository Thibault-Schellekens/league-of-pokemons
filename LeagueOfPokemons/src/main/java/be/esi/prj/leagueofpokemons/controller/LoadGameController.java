package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.db.dto.GameDto;
import be.esi.prj.leagueofpokemons.util.GameManager;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import be.esi.prj.leagueofpokemons.util.audio.AudioManager;
import be.esi.prj.leagueofpokemons.util.audio.AudioSound;
import be.esi.prj.leagueofpokemons.view.ImageCache;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Controller for managing the load game view in the League of Pokemons game.
 * <p>
 * This controller handles the display and management of saved game data, including loading and deleting saved games.
 * </p>
 */
public class LoadGameController {

    private final AudioManager audioManager;

    @FXML
    private Pane loadGamePane;

    @FXML
    private TableView<GameDto> savedGamesTable;

    @FXML
    private TableColumn<GameDto, String> gameNameColumn;

    @FXML
    private TableColumn<GameDto, String> dateColumn;

    @FXML
    private TableColumn<GameDto, ImageView> pokemon1Column;

    @FXML
    private TableColumn<GameDto, ImageView> pokemon2Column;

    @FXML
    private TableColumn<GameDto, ImageView> pokemon3Column;

    @FXML
    private TableColumn<GameDto, Integer> stageColumn;

    private ObservableList<GameDto> savedGames;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /**
     * Constructor for LoadGameController.
     * Initializes the audio manager.
     */
    public LoadGameController() {
        audioManager = AudioManager.getInstance();
    }

    /**
     * Initializes the saved games table when the controller is first loaded.
     * <p>
     * This method sets up the table columns and loads the saved games.
     * </p>
     */
    @FXML
    private void initialize() {
        savedGames = FXCollections.observableArrayList();
    }

    /**
     * Retrieves an {@link ObservableValue} containing an {@link ImageView} for a given card ID.
     * <p>
     * If the card exists, it will return an image view; otherwise, it will return a default empty card image.
     * </p>
     *
     * @param id the ID of the card.
     * @return the image view for the card.
     */
    private ObservableValue<ImageView> getImageViewFromCard(String id) {
        Optional<Card> card = GameManager.findCardById(id);
        if (card.isPresent()) {
            ImageView imageView = ImageCache.getInstance().getImageView(card.get());
            return new ReadOnlyObjectWrapper<>(imageView);
        } else {
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/be/esi/prj/leagueofpokemons/pics/common/emptyCard.png")));
            ImageView imageView = new ImageView(image);
            return new ReadOnlyObjectWrapper<>(imageView);
        }
    }

    /**
     * Loads the list of saved games into the table.
     * <p>
     * This method clears any previous games and loads new data from the {@link GameManager}.
     * </p>
     */
    private void loadSavedGames() {
        savedGames.clear();
        List<GameDto> games = GameManager.loadGames();
        savedGames.addAll(games);
    }

    /**
     * Initializes the table with the appropriate cell value factories for each column.
     * <p>
     * This method binds the table columns to the relevant data from the {@link GameDto} objects.
     * </p>
     */
    private void initTable() {
        gameNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name()));
        dateColumn.setCellValueFactory(cellData -> {
            LocalDateTime date = cellData.getValue().date();
            return new SimpleStringProperty(date.format(dateFormatter));
        });
        stageColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().currentStage()).asObject());

        pokemon1Column.setCellValueFactory(cellData -> {
            String id = cellData.getValue().slot1ID();
            return getImageViewFromCard(id);
        });
        pokemon2Column.setCellValueFactory(cellData -> {
            String id = cellData.getValue().slot2ID();
            return getImageViewFromCard(id);
        });
        pokemon3Column.setCellValueFactory(cellData -> {
            String id = cellData.getValue().slot3ID();
            return getImageViewFromCard(id);
        });

        loadSavedGames();
        savedGamesTable.setItems(savedGames);
    }

    /**
     * Displays the load game pane and brings it to the front.
     * <p>
     * This method is used to show the load game screen, allowing the user to select a saved game.
     * </p>
     */
    public void showLoadGame() {
        initTable();
        loadGamePane.setVisible(true);
        loadGamePane.getParent().toFront();
    }

    /**
     * Hides the load game pane and sends it to the back.
     * <p>
     * This method is triggered when the user chooses to close the load game screen.
     * </p>
     */
    @FXML
    private void hideLoadGame() {
        loadGamePane.setVisible(false);
        if (loadGamePane.getParent() != null) {
            loadGamePane.getParent().toBack();
        }
    }

    /**
     * Loads the selected game and switches to the hub scene.
     * <p>
     * This method is triggered when the user selects a saved game and chooses to load it.
     * </p>
     */
    @FXML
    private void loadGame() {
        GameDto selectedGame = savedGamesTable.getSelectionModel().getSelectedItem();
        if (selectedGame == null) {
            return;
        }
        audioManager.playSound(AudioSound.PLINK);
        int gameId = selectedGame.gameID();
        GameManager.loadGame(gameId);
        SceneManager.switchScene(SceneView.HUB);
    }

    /**
     * Deletes the selected saved game.
     * <p>
     * This method is triggered when the user selects a saved game and chooses to delete it.
     * </p>
     */
    @FXML
    private void deleteGame() {
        GameDto selectedGame = savedGamesTable.getSelectionModel().getSelectedItem();
        if (selectedGame == null) {
            return;
        }
        audioManager.playSound(AudioSound.PLINK);
        int gameId = selectedGame.gameID();
        GameManager.deleteGame(gameId);
        savedGames.remove(selectedGame);
    }
}
