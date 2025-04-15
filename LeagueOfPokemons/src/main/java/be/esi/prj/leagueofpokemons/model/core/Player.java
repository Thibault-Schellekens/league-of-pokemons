package be.esi.prj.leagueofpokemons.model.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player extends CombatEntity {
    private int id;
    private List<Card> inventory;

    //if new game, inventory empty/ player id = size of PlayerDB +  1 / name hard coded for now
    public Player(int id, String name){
        inventory = new ArrayList<>();
        this.id = id;
        this.name = name;
    }

    public Player(){

    }
    public void loadPlayerInventory(List<Card> inventory){
        this.inventory = inventory;
    }

    public String getName(){
        return name;
    }
    public Card getSlot(int index) {
        if (index >= 3 || index < 0){
            throw new IndexOutOfBoundsException("out of bounds broski");
        }
        return inventory.get(index);
    }
    public int getId(){
        return id;
    }

    public void addCard(int index, Card card) {
        if (inventory.size() == 3){
            throw new ModelException("You already have 3 cards sin inventory");
        }

        else if (inventory.contains(card)){
            throw new ModelException("Can't have duplicates in inventory");
        }
        inventory.add(index, card);

    }

    public int removeCard(String id) {
        Card toRemove = inventory.stream().filter(c -> c.getId().equals(id)).findFirst().orElseThrow(() -> new ModelException("There's no card with id: " + id));
        int pos = inventory.indexOf(toRemove);
        inventory.remove(toRemove);
        return pos;
    }

    public boolean selectTeam() {
        if (inventory.size() < 3){
            System.out.println("Player doesnt have the required number of pokemons in his team");
            return false;
        } else {
            for (Card card : inventory){
                // too
            }
        }
        return true;
    }

    public int getInventorySize(){
        return inventory.size();
    }

    @Override
    public void performAction(ActionType actionType, CombatEntity enemy) {

    }
}
