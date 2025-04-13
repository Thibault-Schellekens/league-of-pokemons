package be.esi.prj.leagueofpokemons.model.core;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

// Singleton Pattern ?
public class Collection {
    private int id;

    private Set<Card> availableCards;
    private Set<Card> importedCards;

    public Collection(int gameID){
        id = gameID;
        availableCards = new HashSet<>();
        importedCards = new HashSet<>();

    }

    public void addCard(Card card) {
        availableCards.add(card);
        importedCards.add(card);
    }

    public void loadCards(Set<Card> baseSetCards, Set<Card> loadedCards){
        availableCards = baseSetCards;
        availableCards.addAll(loadedCards);
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

    public void updateCollection() {

    }
}
