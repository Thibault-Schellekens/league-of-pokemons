package be.esi.prj.leagueofpokemons.model.core;

public class Pokemon {
    private Card card;
    private int currentHP;
    Attack basicAttack;
    Attack specialAttack;
    // Might have currentEffect on him



    public AttackResult attack(boolean special, Pokemon defender) {
        return null;
    }

    public boolean isDefeated() {
        return false;
    }

    // Might replace int amount by Potion
    public void heal(int amount) {

    }
}
