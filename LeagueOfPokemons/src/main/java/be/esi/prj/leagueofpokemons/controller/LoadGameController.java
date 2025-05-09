package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.db.dto.GameDto;
import be.esi.prj.leagueofpokemons.util.GameManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
    private TableColumn<GameDto, String> pokemon1Column;

    @FXML
    private TableColumn<GameDto, String> pokemon2Column;

    @FXML
    private TableColumn<GameDto, String> pokemon3Column;

    @FXML
    private TableColumn<GameDto, Integer> stageColumn;


    private ObservableList<GameDto> savedGames;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void initialize() {
        savedGames = FXCollections.observableArrayList();

        gameNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().name()));
        dateColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().date()));
        stageColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().currentStage()).asObject());

        pokemon1Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().slot1ID()));
        pokemon2Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().slot2ID()));
        pokemon3Column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().slot3ID()));


        loadSavedGames();

        savedGamesTable.setItems(savedGames);
    }

    private void loadSavedGames() {
        savedGames.clear();

        List<GameDto> games = GameManager.loadGames();
        savedGames.addAll(games);
    }


    public void showLoadGame() {
        loadGamePane.setVisible(true);
        loadGamePane.getParent().toFront();
    }

    public void hideLoadGame() {
        loadGamePane.setVisible(false);
        if (loadGamePane.getParent() != null) {
            loadGamePane.getParent().toBack();
        }
    }
}
