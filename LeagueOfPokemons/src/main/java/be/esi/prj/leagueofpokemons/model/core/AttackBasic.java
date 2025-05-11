package be.esi.prj.leagueofpokemons.model.core;

/**
 * Represents a basic attack in the game.
 * <p>
 * This class defines the behavior of a basic attack, which calculates damage based on the attack's tier and does not apply any additional effects.
 * </p>
 */
public class AttackBasic extends Attack {

    /**
     * Constructs a basic attack with the specified tier.
     * The base damage is calculated according to the tier, and the attack is initialized with 20 uses.
     *
     * @param tier the tier of the attack
     */
    public AttackBasic(Tier tier) {
        super(tier);
        remainingUse.set(20);
    }

    /**
     * Calculates the damage of the basic attack.
     * <p>
     * Since it is a basic attack, the damage is simply the base damage calculated from the tier.
     * </p>
     *
     * @param enemyType the type of the enemy Pokémon (not used in basic attack)
     * @return the damage calculated from the base damage
     */
    @Override
    public int calculateDamage(Type enemyType) {
        return damage;
    }

    /**
     * Applies the effect of the basic attack to the defender.
     * <p>
     * Since basic attacks do not apply any special effects, this method does nothing.
     * </p>
     *
     * @param attacker the attacking Pokémon (not used in basic attack)
     * @param defender the defending Pokémon (not used in basic attack)
     */
    @Override
    public void applyEffect(Pokemon attacker, Pokemon defender) {
        // Basic attack doesn't apply any effect
    }
}
