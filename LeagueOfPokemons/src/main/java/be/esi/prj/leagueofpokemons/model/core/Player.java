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

        team = new Team();
        team.addPokemon(new Pokemon(new Card("swsh1-43", "Krabby", 80, "https://assets.tcgdex.net/en/swsh/swsh1/43/high.png", Type.WATER)));
        team.addPokemon(new Pokemon(new Card("swsh9-020", "Margmortar", 140, "https://assets.tcgdex.net/en/swsh/swsh9/020/high.png", Type.FIRE)));
        team.addPokemon(new Pokemon(new Card("swsh1-71", "Galvantula", 100, "https://assets.tcgdex.net/en/swsh/swsh1/71/high.png", Type.LIGHTNING)));

        activePokemon = team.getPokemon(0);
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
    public AttackResult performAction(ActionType actionType, CombatEntity enemy) {
        Pokemon enemyPokemon = enemy.getActivePokemon();
        switch (actionType) {
            case BASIC_ATTACK -> {
                activePokemon.attack(false, enemyPokemon);
            }
            case SPECIAL_ATTACK -> {
                activePokemon.attack(true, enemyPokemon);
            }
        }

        return null;
    }
}
