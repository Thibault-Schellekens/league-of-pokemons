package be.esi.prj.leagueofpokemons.model.core;

public class AttackBasic extends Attack {

    public AttackBasic(Tier tier) {
        super(tier);
        remainingUse.set(20);
    }

    @Override
    public int calculateDamage(Type enemyType) {
        return damage;
    }

    @Override
    public void applyEffect(Pokemon attacker, Pokemon defender) {
        // Basic attack doesn't apply any effect
    }
}
