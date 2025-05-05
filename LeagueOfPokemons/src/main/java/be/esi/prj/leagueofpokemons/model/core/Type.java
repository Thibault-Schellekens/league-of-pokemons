package be.esi.prj.leagueofpokemons.model.core;

import java.util.EnumSet;

public enum Type {
    FIRE,
    WATER,
    GRASS,
    LIGHTNING,
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

    public boolean isEffectiveAgainst(Type type) {
        return this.effectiveAgainst == type;
    }

    public boolean isWeakAgainst(Type type) {
        return this.weakAgainst == type;
    }

    public static boolean isValidTypeName(String typeName) {
        try {
            Type.valueOf(typeName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
