package be.esi.prj.leagueofpokemons.model.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Collection {
    private int id;

    // Contains every card, including base set + imported cards
    private Set<Card> availableCards;
    private Set<Card> importedCards;

    private final PropertyChangeSupport pcs;

    public static final String CARD_ADDED_EVENT = "cardAdded";

    public Collection(int gameID){
        id = gameID;
        availableCards = new HashSet<>();
        importedCards = new HashSet<>();
        pcs = new PropertyChangeSupport(this);
    }

    public void addCard(Card card) {
        availableCards.add(card);
        importedCards.add(card);

        pcs.firePropertyChange(CARD_ADDED_EVENT, null, card);
    }

    public void loadCards(Set<Card> baseSetCards, Set<Card> loadedCards) {
        for (Card card : baseSetCards) {
            availableCards.add(card);
            pcs.firePropertyChange(CARD_ADDED_EVENT, null, card);
        }

        for (Card card : loadedCards) {
            availableCards.add(card);
            importedCards.add(card);
            pcs.firePropertyChange(CARD_ADDED_EVENT, null, card);
        }
    }

    public Set<Card> getAvailableCards(){
        return Collections.unmodifiableSet(availableCards);
    }

    public Set<Card> getAvailableCards(CardFilter filter) {
        return availableCards.stream().filter(filter::matches).collect(Collectors.toSet());
    }

    public Set<Card> getImportedCards() {
        return Collections.unmodifiableSet(importedCards);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void updateCollection() {

    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(propertyName, listener);
    }
}
