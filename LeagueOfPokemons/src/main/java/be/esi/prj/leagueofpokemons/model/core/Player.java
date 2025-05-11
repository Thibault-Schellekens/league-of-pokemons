package be.esi.prj.leagueofpokemons.model.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a player in the game, extending {@link CombatEntity}.
 * The player has a unique ID, a set of Pokémon in their inventory (represented by {@link Card}),
 * and can select a team of Pokémon to battle with.
 */
public class Player extends CombatEntity {
    private int id;
    private Pokemon nextPokemon;
    private List<Card> inventory;

    /**
     * Constructs a {@link Player} with the specified ID and name.
     * The player's inventory starts empty.
     *
     * @param id   The unique identifier for the player.
     * @param name The name of the player.
     */
    public Player(int id, String name){
        super(name);
        this.id = id;
        inventory = new ArrayList<>();
    }

    /**
     * Default constructor for the player with an ID of -1 and the name "Player".
     * Used for tests.
     */
    Player(){
        this(-1, "Player");
    }

    /**
     * Loads the player's inventory with the given list of {@link Card} objects.
     *
     * @param inventory The list of cards to be added to the player's inventory.
     */
    public void loadPlayerInventory(List<Card> inventory){
        this.inventory = inventory;
    }

    /**
     * Retrieves the {@link Card} at the specified index in the player's inventory.
     *
     * @param index The index of the card in the inventory.
     * @return The card at the specified index.
     * @throws ModelException If the index is out of bounds.
     */
    public Card getSlot(int index) {
        if (index >= inventory.size() || index < 0){
            throw new ModelException("Index out of bounds: [" + index + "]");
        }
        return inventory.get(index);
    }

    /**
     * Retrieves the unique ID of the card at the specified index in the player's inventory.
     *
     * @param index The index of the card in the inventory.
     * @return The ID of the card at the specified index.
     * @throws ModelException If the index is out of bounds.
     */
    public String getSlotId(int index) {
        return getSlot(index).getId();
    }

    /**
     * Gets the unique ID of the player.
     *
     * @return The player's ID.
     */
    public int getId(){
        return id;
    }

    /**
     * Adds a {@link Card} to the player's inventory. A player can have a maximum of 3 cards.
     *
     * @param card The card to be added to the inventory.
     * @throws ModelException If the inventory already contains 3 cards or if the card is a duplicate.
     */
    public void addCard(Card card) {
        if (inventory.size() >= 3){
            throw new ModelException("You already have 3 cards in inventory");
        } else if (inventory.contains(card)){
            throw new ModelException("Can't have duplicates in inventory");
        }
        inventory.add(card);
    }

    /**
     * Removes a {@link Card} from the player's inventory.
     *
     * @param card The card to be removed.
     */
    public void removeCard(Card card) {
        inventory.remove(card);
    }

    /**
     * Retrieves the number of cards in the player's inventory.
     *
     * @return The number of cards in the inventory.
     */
    public int getInventorySize(){
        return inventory.size();
    }

    /**
     * Gets an unmodifiable list of the player's cards in the inventory.
     *
     * @return An unmodifiable list of cards in the player's inventory.
     */
    public List<Card> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    /**
     * Selects the player's team of Pokémon based on their inventory.
     * The player must have exactly 3 cards in their inventory to create a team.
     * The active Pokémon is set to the first Pokémon in the team.
     *
     * @param maxTier The maximum tier for the team of Pokémon.
     * @throws ModelException If the player doesn't have exactly 3 cards in their inventory.
     */
    @Override
    public void selectTeam(Tier maxTier) {
        if (inventory.isEmpty()) {
            throw new ModelException("You have no cards in inventory");
        }
        if (inventory.size() != 3) {
            throw new ModelException("You need 3 cards in inventory");
        }

        team = new Team(maxTier);
        for (Card card : inventory) {
            Pokemon pokemon = new Pokemon(card);
            team.addPokemon(pokemon);
        }
        activePokemon = team.getPokemon(0);
    }

    /**
     * Sets the next Pokémon to be used by the player in battle.
     *
     * @param nextPokemon The next Pokémon to be used in battle.
     */
    public void setNextPokemon(Pokemon nextPokemon) {
        this.nextPokemon = nextPokemon;
    }

    /**
     * Retrieves the player's next Pokémon to engage in battle.
     *
     * @param enemyPokemon The enemy's current Pokémon.
     * @return The player's next Pokémon.
     * @throws ModelException If the next Pokémon has not been selected.
     */
    @Override
    public Pokemon getNextPokemon(Pokemon enemyPokemon) {
        if (nextPokemon == null) {
            throw new ModelException("You must select the next Pokémon!");
        }
        return nextPokemon;
    }
}
