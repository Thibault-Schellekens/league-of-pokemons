package be.esi.prj.leagueofpokemons.model.core;

/**
 * Represents a special type of attack that deals damage based on effectiveness against the enemy type
 * and applies a specific effect to either the attacker or the defender.
 * <p>
 * This attack can have different effects depending on the type and can deal additional damage
 * if the attack is effective against the enemy's type, or reduced damage if it's weak.
 * </p>
 */
public class AttackSpecial extends Attack {
    private Type type;

    /**
     * Constructs an AttackSpecial with the specified tier and type.
     * The number of uses is set to 3 by default.
     *
     * @param tier the tier of the attack, determining its base damage
     * @param type the type of the attack, determining its effectiveness against enemy types
     */
    public AttackSpecial(Tier tier, Type type) {
        super(tier);
        this.type = type;
        remainingUse.set(3);
    }

    /**
     * Calculates the damage dealt by the attack based on the enemy's type.
     * The damage is adjusted if the attack is effective or weak against the enemy's type.
     *
     * @param enemyType the type of the defending Pokémon
     * @return the calculated damage based on the type effectiveness
     */
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

    /**
     * Applies the effect of the attack to the appropriate Pokémon (either the attacker or defender).
     * The effect is determined based on the attack's type and tier.
     *
     * @param attacker the Pokémon using the attack
     * @param defender the Pokémon being attacked
     */
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
