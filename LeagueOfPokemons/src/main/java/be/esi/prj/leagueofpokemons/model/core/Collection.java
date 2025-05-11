package be.esi.prj.leagueofpokemons.model.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a collection of Pokémon cards, including both the base set and imported cards.
 * <p>
 * This class allows for managing the collection of available cards, adding new cards, and filtering
 * cards based on specific criteria. It also supports property change notifications for card addition.
 * </p>
 */
public class Collection {
    private int id;

    // Contains every card, including base set + imported cards
    private Set<Card> availableCards;
    private Set<Card> importedCards;

    private final PropertyChangeSupport pcs;

    public static final String CARD_ADDED_EVENT = "cardAdded";

    /**
     * Constructs a new Collection instance.
     *
     * @param gameID the ID of the game to associate with this collection
     */
    public Collection(int gameID){
        id = gameID;
        availableCards = new HashSet<>();
        importedCards = new HashSet<>();
        pcs = new PropertyChangeSupport(this);
    }

    /**
     * Adds a card to both the available cards and the imported cards sets.
     *
     * @param card the card to be added
     */
    public void addCard(Card card) {
        availableCards.add(card);
        importedCards.add(card);

        pcs.firePropertyChange(CARD_ADDED_EVENT, null, card);
    }

    /**
     * Loads cards into the collection, both base set and loaded cards, and notifies listeners
     * of the additions.
     *
     * @param baseSetCards the set of base set cards to add
     * @param loadedCards the set of loaded cards to add
     */
    public void loadCards(Set<Card> baseSetCards, Set<Card> loadedCards) {
        for (Card card : baseSetCards) {
            availableCards.add(card);
            pcs.firePropertyChange(CARD_ADDED_EVENT, null, card);
        }

        for (Card card : loadedCards) {
            availableCards.add(card);
            pcs.firePropertyChange(CARD_ADDED_EVENT, null, card);
        }
    }

    /**
     * Gets all available cards in the collection.
     *
     * @return an unmodifiable set of all available cards
     */
    public Set<Card> getAvailableCards(){
        return Collections.unmodifiableSet(availableCards);
    }

    /**
     * Gets all available cards that match the specified filter criteria.
     *
     * @param filter the filter criteria to apply
     * @return a set of cards that match the filter
     */
    public Set<Card> getAvailableCards(CardFilter filter) {
        return availableCards.stream().filter(filter::matches).collect(Collectors.toSet());
    }

    /**
     * Gets all imported cards in the collection.
     *
     * @return an unmodifiable set of all imported cards
     */
    public Set<Card> getImportedCards() {
        return Collections.unmodifiableSet(importedCards);
    }

    /**
     * Gets the ID of the collection.
     *
     * @return the collection's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the collection.
     *
     * @param id the new ID for the collection
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Adds a property change listener for a specific property.
     *
     * @param propertyName the name of the property to listen to
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    /**
     * Removes a property change listener for a specific property.
     *
     * @param propertyName the name of the property to stop listening to
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }
}
