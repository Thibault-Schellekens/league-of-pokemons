package be.esi.prj.leagueofpokemons.model.core;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Abstract class representing an attack in the game.
 * <p>
 * This class defines the basic properties and behavior of an attack, including its damage calculation and usage.
 * The attack is associated with a tier that determines its base damage. Specific types of attacks
 * should extend this class and provide their own implementation for calculating damage and applying effects.
 * </p>
 */
public abstract class Attack {
    protected int damage;
    protected Tier tier;
    protected IntegerProperty remainingUse = new SimpleIntegerProperty();

    /**
     * Constructs an attack with the specified tier.
     * The tier is used to calculate the base damage of the attack.
     *
     * @param tier the tier of the attack
     */
    protected Attack(Tier tier) {
        this.tier = tier;
        this.damage = calculateBaseDamage(tier);
    }

    /**
     * Calculates the base damage based on the attack's tier.
     *
     * @param tier the tier of the attack
     * @return the calculated base damage
     */
    private int calculateBaseDamage(Tier tier) {
        return switch (tier) {
            case TIER_I -> 30;
            case TIER_II -> 50;
            case TIER_III -> 70;
            case TIER_IV -> 90;
            case TIER_V -> 110;
        };
    }

    /**
     * Uses the attack on an enemy Pokémon and returns the resulting damage.
     * <p>
     * This method reduces the remaining uses of the attack by one and calculates the damage based on the enemy's type.
     * </p>
     *
     * @param enemyType the type of the enemy Pokémon
     * @return the calculated damage
     * @throws ModelException if there are no remaining uses of the attack
     */
    public int use(Type enemyType) {
        if (remainingUse.get() <= 0) {
            throw new ModelException("You can not perform this attack anymore!");
        }
        remainingUse.set(remainingUse.get() - 1);
        return calculateDamage(enemyType);
    }

    /**
     * Calculates the damage dealt by this attack based on the enemy's type.
     *
     * @param enemyType the type of the enemy Pokémon
     * @return the calculated damage
     */
    protected abstract int calculateDamage(Type enemyType);

    /**
     * Determines if the attack has an effect based on whether it is special.
     *
     * @param special      whether the attack is a special attack
     * @return true if the attack has an effect, false otherwise
     */
    public boolean hasEffect(boolean special) {
        return special;
    }

    /**
     * Applies the effect of the attack to the defender.
     *
     * @param attacker the attacking Pokémon
     * @param defender the defending Pokémon
     */
    public abstract void applyEffect(Pokemon attacker, Pokemon defender);

    /**
     * Returns the property for the remaining uses of the attack.
     *
     * @return the remaining use property
     */
    public IntegerProperty remainingUseProperty() {
        return remainingUse;
    }
}
