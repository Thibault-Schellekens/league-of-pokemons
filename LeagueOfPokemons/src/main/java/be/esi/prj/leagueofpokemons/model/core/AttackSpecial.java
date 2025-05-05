package be.esi.prj.leagueofpokemons.model.core;

public class AttackSpecial extends Attack {
    private Type type;

    public AttackSpecial(Tier tier, Type type) {
        super(tier);
        this.type = type;
        remainingUse.set(3);
    }

    @Override
    public int calculateDamage(Type enemyType) {
        if (type.isEffectiveAgainst(enemyType)) {
            return damage * 2;
        } else if (type.isWeakAgainst(enemyType)) {
            return (int) (damage * 0.5);
        } else {
            return damage;
        }
    }

    @Override
    public void applyEffect(Pokemon attacker, Pokemon defender) {
        Effect effect = new Effect(type, tier);
        Effect.EffectType effectType = effect.getEffectType();
        switch (effectType) {
            case DODGE, DRAIN, CRIT -> attacker.setCurrentEffect(effect);
            case BURN, PARALYZE -> defender.setCurrentEffect(effect);
        }
    }

}
