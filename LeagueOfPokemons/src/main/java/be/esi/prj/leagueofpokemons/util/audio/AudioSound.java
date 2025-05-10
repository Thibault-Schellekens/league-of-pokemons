package be.esi.prj.leagueofpokemons.util.audio;

public enum AudioSound {
    ATTACK_FIGHTING,
    ATTACK_FIRE,
    ATTACK_GRASS,
    ATTACK_LIGHTNING,
    ATTACK_WATER,
    BATTLE_WON,
    EFFECT_BURN,
    EFFECT_CRIT,
    EFFECT_DODGE,
    EFFECT_DRAIN,
    EFFECT_PARALYZE,
    IN_BATTLE,
    KO_EFFECT,
    PLINK,
    POKEBALL_WOBBLE,
    SAVE_GAME,
    SCANNER_ADD
    ;

    private static final String DIR_LOCATION = "/be/esi/prj/leagueofpokemons/sounds/";

    public String getFilePath() {
        return DIR_LOCATION + this.name().toLowerCase() + ".wav";
    }
}