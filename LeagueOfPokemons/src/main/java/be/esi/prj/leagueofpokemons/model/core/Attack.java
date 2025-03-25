package be.esi.prj.leagueofpokemons.model.core;

public class Attack {
    private Type type;
    private int damage;

    // Might be added later
    private Effect effect;

    public Attack(Type type, int damage) {
        this.type = type;
        this.damage = damage;
    }

    public int calculateDamage(Type enemyType) {
        return 0;
    }

    public void applyEffect(Pokemon toApply) {

    }
}
