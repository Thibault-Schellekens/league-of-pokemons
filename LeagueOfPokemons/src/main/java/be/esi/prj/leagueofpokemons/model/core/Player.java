package be.esi.prj.leagueofpokemons.model.core;

import java.util.ArrayList;
import java.util.Collections;
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
        this(0, "Player");
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

    public void addCard(Card card) {
        if (inventory.size() == 3){
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

    public List<Card> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    @Override
    public AttackResult performAction(ActionType actionType, CombatEntity enemy) {
        AttackResult attackResult = new AttackResult(0);
        switch (actionType) {
            case SWAP -> {

            }
            case BASIC_ATTACK, SPECIAL_ATTACK -> {
                boolean isSpecial = actionType == ActionType.SPECIAL_ATTACK;
                attackResult = activePokemon.attack(isSpecial, enemy.getActivePokemon());
            }
        }

        return attackResult;
    }
}
