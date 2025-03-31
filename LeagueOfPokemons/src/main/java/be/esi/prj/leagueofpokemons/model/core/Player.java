package be.esi.prj.leagueofpokemons.model.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Player extends CombatEntity {
    //no
    private List<Card> inventory;

    //if new game, inventory empty/ player id = size of PlayerDB +  1 / name hard coded for now
    public Player(int id, String name){
        inventory = new ArrayList<>();
        this.id = id;
        this.name = name;
    }

    public Player(){
        //nothing here
    }

    public void loadPlayer(List<Card> loadedInventory, int loadedId, String loadedName){
        inventory = loadedInventory;
        this.id = loadedId;
        this.name = loadedName;
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

    public boolean selectTeam(List<Card> cards) {
        return false;
    }


    @Override
    public void performAction(ActionType actionType, CombatEntity enemy) {

    }
}
