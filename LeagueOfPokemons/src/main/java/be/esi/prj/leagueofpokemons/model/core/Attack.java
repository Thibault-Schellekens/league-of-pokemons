package be.esi.prj.leagueofpokemons.model.core;

public class Attack {
    private Type type;
    private int damage;

    // Might be added later
    private Effect effect;

    public Attack(Type type, Tier tier, boolean special) {
        this.type = type;
        this.damage = 50;
        if (special) {
            this.damage = 0;
        }
    }

    public int calculateDamage(Type enemyType) {
        return damage;
    }

    public boolean hasEffect(boolean special, Type defenderType) {
        return false;
    }

    public void applyEffect(Pokemon toApply) {

    }
}
