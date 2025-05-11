package be.esi.prj.leagueofpokemons.model.core;

/**
 * Enum representing the different types of elements that Pokémon can belong to.
 * Each {@link Type} has a specific relationship with other types, such as being effective
 * or weak against them. The enum defines five types: FIRE, WATER, GRASS, LIGHTNING, and FIGHTING.
 * <p>
 * Each type has an associated effectiveness and weakness against other types, which are used
 * to determine the outcome of battles between Pokémon. The relationships are as follows:
 * </p>
 * <ul>
 *     <li>{@link #FIRE} is effective against {@link #GRASS} but weak against {@link #WATER}.</li>
 *     <li>{@link #WATER} is effective against {@link #FIRE} but weak against {@link #LIGHTNING}.</li>
 *     <li>{@link #GRASS} is effective against {@link #FIGHTING} but weak against {@link #FIRE}.</li>
 *     <li>{@link #LIGHTNING} is effective against {@link #WATER} but weak against {@link #FIGHTING}.</li>
 *     <li>{@link #FIGHTING} is effective against {@link #LIGHTNING} but weak against {@link #GRASS}.</li>
 * </ul>
 */
public enum Type {
    /**
     * Represents the FIRE type.
     */
    FIRE,

    /**
     * Represents the WATER type.
     */
    WATER,

    /**
     * Represents the GRASS type.
     */
    GRASS,

    /**
     * Represents the LIGHTNING type.
     */
    LIGHTNING,

    /**
     * Represents the FIGHTING type.
     */
    FIGHTING;

    private Type effectiveAgainst;
    private Type weakAgainst;

    static {
        FIRE.effectiveAgainst = Type.GRASS;
        FIRE.weakAgainst = Type.WATER;

        WATER.effectiveAgainst = Type.FIRE;
        WATER.weakAgainst = Type.LIGHTNING;

        GRASS.effectiveAgainst = Type.FIGHTING;
        GRASS.weakAgainst = Type.FIRE;

        LIGHTNING.effectiveAgainst = Type.WATER;
        LIGHTNING.weakAgainst = Type.FIGHTING;

        FIGHTING.effectiveAgainst = Type.LIGHTNING;
        FIGHTING.weakAgainst = Type.GRASS;
    }

    /**
     * Determines if the current type is effective against the specified type.
     *
     * @param type The {@link Type} to compare against.
     * @return {@code true} if the current type is effective against the specified type, {@code false} otherwise.
     */
    public boolean isEffectiveAgainst(Type type) {
        return this.effectiveAgainst == type;
    }

    /**
     * Determines if the current type is weak against the specified type.
     *
     * @param type The {@link Type} to compare against.
     * @return {@code true} if the current type is weak against the specified type, {@code false} otherwise.
     */
    public boolean isWeakAgainst(Type type) {
        return this.weakAgainst == type;
    }

    /**
     * Checks if a given type name is a valid {@link Type}.
     *
     * @param typeName The name of the type to validate.
     * @return {@code true} if the type name is valid, {@code false} otherwise.
     */
    public static boolean isValidTypeName(String typeName) {
        try {
            Type.valueOf(typeName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
