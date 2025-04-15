package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Game;
import be.esi.prj.leagueofpokemons.model.core.ModelException;
import be.esi.prj.leagueofpokemons.model.core.Tier;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import be.esi.prj.leagueofpokemons.view.CardContext;
import be.esi.prj.leagueofpokemons.view.CardView;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CollectionController {
    Game game = Game.getInstance();
    private int page = 1;
    private int inventoryIndex;
    private Tier tier = Tier.TIER_I;
    private final int CARDS_PER_PAGE = 6;

    @FXML
    GridPane collectionGrid;
    @FXML
    GridPane inventoryGrid;
    @FXML
    Button tier1btn;
    @FXML
    Button tier2btn;
    @FXML
    Button tier3btn;
    @FXML
    Button tier4btn;
    @FXML
    Button tier5btn;
    private List<Card> collectionCards;
    private List<Card> filteredCards;
    // will be needed when filtered by type or name

    private List<Card> inventoryCards;

    public void initialize() {
        System.out.println("Initializing Collection Controller");
        collectionCards = new ArrayList<>(game.getCollection().getAvailableCards());
        System.out.println("Card List Size : " + collectionCards.size());
        inventoryCards = new ArrayList<>();

        for (int i = 0; i < game.getPlayer().getInventorySize(); i++) {
            inventoryCards.add(Objects.requireNonNullElse(game.getPlayer().getSlot(i), null));
        }
        if (!inventoryCards.isEmpty()) {
            System.out.println("Initialized inventory with cards : "
                    + inventoryCards.get(0).getName() + " | "
                    + inventoryCards.get(1).getName() + " | "
                    + inventoryCards.get(2).getName());
        }
        if (game.getPlayer().getInventorySize() == 0) {
            inventoryIndex = 0;
        } else {
            inventoryIndex = game.getPlayer().getInventorySize() - 1;
        }
        tier1btn.setOnAction(e -> setTierAndReload(Tier.TIER_I));
        tier2btn.setOnAction(e -> setTierAndReload(Tier.TIER_II));
        tier3btn.setOnAction(e -> setTierAndReload(Tier.TIER_III));
        tier4btn.setOnAction(e -> setTierAndReload(Tier.TIER_IV));
        tier5btn.setOnAction(e -> setTierAndReload(Tier.TIER_V));
        
        loadCollectionPage(); 
        loadInventory();
        

    }
    // change this in future so that it creates cardView by grabing the selected cardView's cropped image
    // -> skip image processing for better performance
    private void loadInventory() {
        inventoryGrid.getChildren().clear();
        int row = 0;
        for (Card card : inventoryCards) {
            if (card == null) continue;
            CardView cardView = new CardView(card, this, CardContext.INVENTORY);
            inventoryGrid.add(cardView, 0, row);
            row++;
        }
    }

    private void setTierAndReload(Tier selectedTier) {
        tier = selectedTier;
        page = 1;
        loadCollectionPage();
    }

    public void loadCollectionPage() {
        collectionGrid.getChildren().clear();
        collectionCards = filteredCards();
        System.out.println("Filtered Card List Size : " + collectionCards.size());
        int start = (page - 1) * CARDS_PER_PAGE;
        int end = Math.min(start + CARDS_PER_PAGE, collectionCards.size());

        int col = 0, row = 0;
        for (int i = start; i < end; i++) {
            Card card = collectionCards.get(i);
            CardView cardView = new CardView(card, this, CardContext.COLLECTION);
            collectionGrid.add(cardView, col, row);

            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }

    }


    private List<Card> filteredCards() {
        return game.getCollection()
                .getAvailableCards()
                .stream()
                .filter(card -> card.getTier() == tier)
                .toList();
    }

    public void back() {
        SceneManager.switchScene(SceneView.HUB);
    }

    public void onCollectionCardSelected(CardView card) {
        try {
            game.getPlayer().addCard(inventoryIndex, card.getCard());
            inventoryCards.add(inventoryIndex,card.getCard());
            inventoryIndex++;
            loadInventory();
            System.out.println("Added Card " + card.getCard().getName());
        } catch (ModelException e) {
            System.out.println("Cannot add card: " + e.getMessage());
        }
    }

    public void onInventoryCardSelected(Card card) {
        try{
            inventoryIndex = game.getPlayer().removeCard(card.getId());
            inventoryCards.remove(inventoryIndex);
            loadInventory();
            System.out.println("Removed Card " + card.getName());
        } catch (ModelException e){
            System.out.println("Cannot remove card: " + e.getMessage());
        }
    }
}
