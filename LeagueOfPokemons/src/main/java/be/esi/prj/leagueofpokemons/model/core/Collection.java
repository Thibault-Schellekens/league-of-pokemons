package be.esi.prj.leagueofpokemons.model.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Collection {
    private int id;

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
            pcs.firePropertyChange(CARD_ADDED_EVENT, null, card);
        }
    }


    //returns all cards
    public Set<Card> getAvailableCards(){
        return availableCards;
    }

    //returns loadedCards
    public Set<Card> getImportedCards() {
        return importedCards;
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
