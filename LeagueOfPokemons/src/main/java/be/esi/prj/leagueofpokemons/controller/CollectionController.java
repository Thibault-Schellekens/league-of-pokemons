package be.esi.prj.leagueofpokemons.controller;

import be.esi.prj.leagueofpokemons.model.core.*;
import be.esi.prj.leagueofpokemons.util.SceneManager;
import be.esi.prj.leagueofpokemons.util.SceneView;
import be.esi.prj.leagueofpokemons.util.audio.AudioManager;
import be.esi.prj.leagueofpokemons.util.audio.AudioSound;
import be.esi.prj.leagueofpokemons.view.CardContext;
import be.esi.prj.leagueofpokemons.view.CardView;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.*;

/**
 * Controller for managing the player's collection of cards in the League of Pokemons game.
 */
public class CollectionController implements ControllerFXML {

    private Game game;
    private int page = 1;
    private Tier tier = Tier.TIER_I;
    private String name;
    private Type type;
    private static final int CARDS_PER_PAGE = 6;

    private final AudioManager audioManager;

    @FXML
    private ErrorController errorPanelController;

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
    @FXML
    private Button prevPageBtn;
    @FXML
    private Button nextPageBtn;
    @FXML
    private ImageView prevImage;
    @FXML
    private ImageView nextImage;

    @FXML
    private TextField nameField;
    @FXML
    private RadioButton fireButton;
    @FXML
    private RadioButton waterButton;
    @FXML
    private RadioButton grassButton;
    @FXML
    private RadioButton lightningButton;
    @FXML
    private RadioButton fightingButton;

    private final Map<String, CardView> collectionCardViews = new HashMap<>();

    public CollectionController() {
        game = Game.getInstance();
        audioManager = AudioManager.getInstance();
    }

    /**
     * Initializes the controller by setting up buttons, loading card views, and updating the page.
     */
    @Override
    public void init() {
        initTierButtons();
        initTypeButtons();

        loadCardViews();
        loadCollectionPage();
        loadInventory();
        updatePaginationButtons();
    }

    /**
     * Initializes the buttons for selecting card types.
     * This ensures only one type is selected at a time and changes the opacity based on selection.
     */
    private void initTypeButtons() {
        List<RadioButton> typeButtons = List.of(fireButton, waterButton, grassButton, lightningButton, fightingButton);

        for (RadioButton rb : typeButtons) {
            rb.setOnAction(e -> {
                boolean isSelected = rb.isSelected();
                for (RadioButton b : typeButtons) {
                    b.setSelected(false);
                    b.getParent().setOpacity(0.5);
                }
                if (isSelected) {
                    rb.setSelected(true);
                    rb.getParent().setOpacity(1);
                }
            });
        }
    }

    /**
     * Returns the selected card type based on the radio button selected.
     *
     * @return The selected card type, or null if none is selected.
     */
    private Type getSelectedButtonType() {
        List<RadioButton> typeButtons = List.of(fireButton, waterButton, grassButton, lightningButton, fightingButton);

        for (RadioButton rb : typeButtons) {
            if (rb.isSelected()) {
                return Type.valueOf(rb.getText());
            }
        }
        return null;
    }

    /**
     * Initializes the buttons for selecting tiers and sets the corresponding action to reload the collection.
     */
    private void initTierButtons() {
        tier1btn.setOnAction(e -> setTierAndReload(Tier.TIER_I));
        tier2btn.setOnAction(e -> setTierAndReload(Tier.TIER_II));
        tier3btn.setOnAction(e -> setTierAndReload(Tier.TIER_III));
        tier4btn.setOnAction(e -> setTierAndReload(Tier.TIER_IV));
        tier5btn.setOnAction(e -> setTierAndReload(Tier.TIER_V));
    }

    /**
     * Updates the pagination buttons to reflect the current state.
     */
    private void updatePaginationButtons() {
        updatePreviousPaginationButton();
        updateNextPaginationButton();
    }

    /**
     * Updates the "Next" pagination button to disable it when there are no more pages.
     */
    private void updateNextPaginationButton() {
        int totalPages = (int) Math.ceil((double) filteredCardViews().size() / CARDS_PER_PAGE);
        boolean nextDisable = page >= totalPages;
        nextPageBtn.setDisable(nextDisable);
        nextImage.setOpacity(nextDisable ? 0.5 : 1);
    }

    /**
     * Updates the "Previous" pagination button to disable it when there are no previous pages.
     */
    private void updatePreviousPaginationButton() {
        boolean prevDisable = page <= 1;
        prevPageBtn.setDisable(prevDisable);
        prevImage.setOpacity(prevDisable ? 0.5 : 1);
    }

    /**
     * Handles filtered search based on the entered name and selected type.
     * Reloads the collection page with updated filters.
     */
    @FXML
    private void filteredSearch() {
        String name = nameField.getText();
        Type type = getSelectedButtonType();

        this.name = name;
        this.type = type;
        loadCollectionPage();
    }

    /**
     * Handles the action of moving to the previous page of the collection.
     * Updates the page and reloads the collection.
     */
    @FXML
    private void prevPage() {
        if (page > 1) {
            page--;
            loadCollectionPage();
            updatePaginationButtons();
        }
    }

    /**
     * Handles the action of moving to the next page of the collection.
     * Updates the page and reloads the collection.
     */
    @FXML
    private void nextPage() {
        int totalPages = (int) Math.ceil((double) filteredCardViews().size() / CARDS_PER_PAGE);
        if (page < totalPages) {
            page++;
            loadCollectionPage();
            updatePaginationButtons();
        }
    }

    /**
     * Loads the inventory by adding all cards from the player's inventory to the inventory grid.
     */
    private void loadInventory() {
        inventoryGrid.getChildren().clear();
        int row = 0;

        for (Card card : game.getPlayerInventory()) {
            CardView cardView = new CardView(card, this, CardContext.INVENTORY);
            inventoryGrid.add(cardView, 0, row);
            row++;
        }
    }

    /**
     * Sets the selected tier and reloads the collection page with the updated tier.
     *
     * @param selectedTier The tier to be set.
     */
    private void setTierAndReload(Tier selectedTier) {
        tier = selectedTier;
        page = 1;
        loadCollectionPage();
    }

    /**
     * Loads the collection page by displaying the filtered cards in the collection grid.
     */
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

    /**
     * Filters the card views based on the selected tier, name, and type.
     *
     * @return A list of filtered card views.
     */
    private List<CardView> filteredCardViews() {
        CardFilter filter = new CardFilter.Builder()
                .tier(tier)
                .name(name)
                .type(type)
                .build();

        Set<Card> filteredCards = game.getCollection().getAvailableCards(filter);

        return collectionCardViews.values().stream()
                .filter(view -> filteredCards.contains(view.getCard()))
                .toList();
    }

    /**
     * Navigates the user back to the hub scene.
     */
    public void back() {
        SceneManager.switchScene(SceneView.HUB);
    }

    /**
     * Handles the action of selecting a card from the collection.
     * Adds the selected card to the player's inventory.
     *
     * @param card The selected card to be added.
     */
    public void onCollectionCardSelected(CardView card) {
        try {
            game.addCardInPlayer(card.getCard());
            audioManager.playSound(AudioSound.PLINK);
            loadInventory();
        } catch (ModelException e) {
            errorPanelController.displayError(e.getMessage());
        }
    }

    /**
     * Handles the action of selecting a card from the inventory.
     * Removes the selected card from the player's inventory.
     *
     * @param card The selected card to be removed.
     */
    public void onInventoryCardSelected(Card card) {
        try {
            audioManager.playSound(AudioSound.PLINK);
            game.removeCardInPlayer(card);
            loadInventory();
        } catch (ModelException e) {
            errorPanelController.displayError(e.getMessage());
        }
    }

    /**
     * Loads all card views into the collection card views map.
     */
    private void loadCardViews() {
        Set<Card> collection = game.getCollection().getAvailableCards();
        for (Card card : collection) {
            if (!collectionCardViews.containsKey(card.getId())) {
                collectionCardViews.put(card.getId(), new CardView(card, this, CardContext.COLLECTION));
            }
        }
    }
}
