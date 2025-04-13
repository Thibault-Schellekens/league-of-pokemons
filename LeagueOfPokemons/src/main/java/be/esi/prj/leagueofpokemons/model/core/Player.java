package be.esi.prj.leagueofpokemons.model.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player extends CombatEntity {
    private int id;
    //no
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
    public List<Card> getInventory() {
        return inventory;
    }
    public int getId(){
        return id;
    }

    public boolean buyCard(Card card) {
        return false;
    }

    public void sellCard(Card card) {

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


    @Override
    public void performAction(ActionType actionType, CombatEntity enemy) {

    }
}
