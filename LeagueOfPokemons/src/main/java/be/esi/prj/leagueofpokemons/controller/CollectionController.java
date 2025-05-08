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
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.util.*;

public class CollectionController implements ControllerFXML {
    private Game game;
    private int page = 1;
    private Tier tier = Tier.TIER_I;
    private final int CARDS_PER_PAGE = 6;

    @FXML
    private GridPane collectionGrid;
    @FXML
    private GridPane inventoryGrid;
    @FXML
    private Button tier1btn;
    @FXML
    private Button tier2btn;
    @FXML
    private Button tier3btn;
    @FXML
    private Button tier4btn;
    @FXML
    private Button tier5btn;
    private static Map<String, CardView> collectionCardViews = new HashMap<>();


    @Override
    public void init() {
        System.out.println("Initializing Collection Controller");
        game = Game.getInstance();
        System.out.println("Initialized with inventory size : " + game.getPlayer().getInventorySize());

        tier1btn.setOnAction(e -> setTierAndReload(Tier.TIER_I));
        tier2btn.setOnAction(e -> setTierAndReload(Tier.TIER_II));
        tier3btn.setOnAction(e -> setTierAndReload(Tier.TIER_III));
        tier4btn.setOnAction(e -> setTierAndReload(Tier.TIER_IV));
        tier5btn.setOnAction(e -> setTierAndReload(Tier.TIER_V));
        loadCardViews();
        loadCollectionPage();
        loadInventory();
    }

    // change this in the future so that it creates cardView by grabing the selected cardView's cropped image
    // -> skip image processing for better performance
    private void loadInventory() {
        System.out.println("UPDATING INVENTORY");
        System.out.println("collections view card static");


        inventoryGrid.getChildren().clear();
        int row = 0;
        System.out.print("Cards in inventory : ");

        for (Card card : game.getPlayerInventory()) {
            CardView cardView = new CardView(collectionCardViews.get(card.getId()), this, CardContext.INVENTORY);;
            System.out.println();
            System.out.println(cardView + "cardview");
            System.out.println("row: " + row);

            inventoryGrid.add(cardView, 0, row);
            System.out.print(card.getName() + ", ");
            row++;
        }
        System.out.println(" .");
        System.out.println(inventoryGrid.getChildren());
    }

    private void setTierAndReload(Tier selectedTier) {
        tier = selectedTier;
        page = 1;
        loadCollectionPage();
    }

    public void loadCollectionPage() {
        collectionGrid.getChildren().clear();
        List<CardView> filtered = filteredCardViews();
        int start = (page - 1) * CARDS_PER_PAGE;
        int end = Math.min(start + CARDS_PER_PAGE, filtered.size());
        List<CardView> pageCards = filtered.subList(start, end);
        int col = 0, row = 0;
        for (CardView cardview : pageCards) {
            collectionGrid.add(cardview, col, row);
            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }
    }


    private List<CardView> filteredCardViews() {
        return collectionCardViews.values()
                .stream()
                .filter(view -> view.getCard().getTier() == tier)
                .toList();
    }

    public void back() {
        SceneManager.switchScene(SceneView.HUB);
    }

    public void onCollectionCardSelected(CardView card) {
        try {
            game.addCardInPlayer(card.getCard());
            loadInventory();
            System.out.println("Added Card " + card.getCard().getName());
        } catch (ModelException e) {
            System.out.println("Cannot add card: " + e.getMessage());
        }
    }

    public void onInventoryCardSelected(Card card) {
        try{
            game.removeCardInPlayer(card);
            System.out.println(card.getName() + " got removed");
            loadInventory();
        } catch (ModelException e){
            System.out.println("Cannot remove card: " + e.getMessage());
        }
    }


    private void loadCardViews (){
        System.out.println("LOADING ALL CARDVIEWS");
        Set<Card> collection = game.getCollection().getAvailableCards();
        for (Card card : collection ){
            if (!collectionCardViews.containsKey(card.getId())) {
                System.out.println("Card : " + card.getId() + " | name : " + card.getName() + " is getting added");
                collectionCardViews.put(card.getId(), new CardView(card, this, CardContext.COLLECTION));
            }
        }
    }
}
