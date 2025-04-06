package be.esi.prj.leagueofpokemons.model.core;

import java.util.HashSet;
import java.util.Set;

// Singleton Pattern ?
public class Collection {
    private Set<Card> availableCards;
    //removed tier form collection

    public Collection(){
        availableCards = new HashSet<>();
    }



    public boolean addCard(Card card) {
        return false;
    }

    public boolean getCard(Card card) {
        return false;
    }

    public void loadBaseSet(Set<Card> baseSet){
        availableCards = baseSet;
    }
    public void loadCardSet(Set<Card> loadedCards){
        availableCards.addAll(loadedCards);
    }

    public Set<Card> getAvailableCards(){
        return availableCards;
    }

    public void updateCollection() {

    }
}
