package be.esi.prj.leagueofpokemons.model.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player extends CombatEntity {
    private int id;
    private Pokemon nextPokemon;
    //no
    private List<Card> inventory;

    //if new game, inventory empty/ player id = size of PlayerDB +  1 / name hard coded for now
    public Player(int id, String name){
        super(name);
        this.id = id;
        inventory = new ArrayList<>();
    }

    public Player(){
        this(0, "Player");
    }

    public void loadPlayerInventory(List<Card> inventory){
        this.inventory = inventory;
    }

    public Card getSlot(int index) {
        if (index >= inventory.size() || index < 0){
            throw new ModelException("Index out of bounds: [" + index + "]");
        }
        return inventory.get(index);
    }

    public int getId(){
        return id;
    }

    public void addCard(Card card) {
        if (inventory.size() >= 3){
            throw new ModelException("You already have 3 cards sin inventory");
        }

        else if (inventory.contains(card)){
            throw new ModelException("Can't have duplicates in inventory");
        }
        inventory.add(card);

    }

    public void removeCard(Card card) {
        inventory.remove(card);
    }

    public int getInventorySize(){
        return inventory.size();
    }

    public List<Card> getInventory() {
        return Collections.unmodifiableList(inventory);
    }


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

    public void setNextPokemon(Pokemon nextPokemon) {
        this.nextPokemon = nextPokemon;
    }

    @Override
    public Pokemon getNextPokemon(Pokemon enemyPokemon) {
        if (nextPokemon == null) {
            throw new ModelException("You must select the next Pokémon!");
        }
        return nextPokemon;
    }

}
