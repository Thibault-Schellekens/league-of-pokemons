package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.db.dto.GameDto;
import be.esi.prj.leagueofpokemons.util.GameManager;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import be.esi.prj.leagueofpokemons.view.ImageCache;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
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

public class LoadGameController {

    @FXML
    private Pane loadGamePane;

    @FXML
    private TableView<GameDto> savedGamesTable;

    @FXML
    private TableColumn<GameDto, String> gameNameColumn;

    @FXML
    private TableColumn<GameDto, LocalDateTime> dateColumn;

    @FXML
    private TableColumn<GameDto, ImageView> pokemon1Column;

    @FXML
    private TableColumn<GameDto, ImageView> pokemon2Column;

    @FXML
    private TableColumn<GameDto, ImageView> pokemon3Column;

    @FXML
    private TableColumn<GameDto, Integer> stageColumn;


    private ObservableList<GameDto> savedGames;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void initialize() {
        savedGames = FXCollections.observableArrayList();
    }

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

    private void loadSavedGames() {
        savedGames.clear();

        List<GameDto> games = GameManager.loadGames();
        savedGames.addAll(games);
    }

    private void initTable() {
        gameNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name()));
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().date()));
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

    public void showLoadGame() {
        initTable();

        loadGamePane.setVisible(true);
        loadGamePane.getParent().toFront();
    }

    public void hideLoadGame() {
        loadGamePane.setVisible(false);
        if (loadGamePane.getParent() != null) {
            loadGamePane.getParent().toBack();
        }
    }

    @FXML
    private void loadGame() {
        GameDto selectedGame = savedGamesTable.getSelectionModel().getSelectedItem();
        if (selectedGame == null) {
            return;
        }
        System.out.println(selectedGame);
        int gameId = selectedGame.gameID();
        GameManager.loadGame(gameId);
        SceneManager.switchScene(SceneView.HUB);
    }

    @FXML
    private void deleteGame() {
        GameDto selectedGame = savedGamesTable.getSelectionModel().getSelectedItem();
        if (selectedGame == null) {
            return;
        }
        int gameId = selectedGame.gameID();
        GameManager.deleteGame(gameId);
        savedGames.remove(selectedGame);
    }
}
