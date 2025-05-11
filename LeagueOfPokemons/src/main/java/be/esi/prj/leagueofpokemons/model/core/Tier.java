package be.esi.prj.leagueofpokemons.model.core;

/**
 * The {@link Tier} enum represents the different tiers of Pokémon.
 * Each tier indicates the level of power or strength that a Pokémon can have.
 * Tiers range from {@link #TIER_I} (lowest) to {@link #TIER_V} (highest).
 */
public enum Tier {
    /**
     * Represents the first tier of Pokémon.
     */
    TIER_I,

    /**
     * Represents the second tier of Pokémon.
     */
    TIER_II,

    /**
     * Represents the third tier of Pokémon.
     */
    TIER_III,

    /**
     * Represents the fourth tier of Pokémon.
     */
    TIER_IV,

    /**
     * Represents the fifth tier of Pokémon (the highest tier).
     */
    TIER_V;

    /**
     * Compares the current tier to another tier to determine if the current tier is greater.
     * This is based on the ordinal values of the tiers.
     *
     * @param tier The tier to compare against.
     * @return {@code true} if the current tier is greater than the provided tier, {@code false} otherwise.
     */
    public boolean isGreater(Tier tier) {
        return this.ordinal() > tier.ordinal();
    }
}
