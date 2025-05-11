package be.esi.prj.leagueofpokemons.util.audio;

/**
 * Enum representing various audio sound effects used in the game.
 * Each enum constant corresponds to a specific sound effect or background music.
 * The sounds are expected to be in WAV format and located in the "/resources/be/esi/prj/leagueofpokemons/sounds/" directory.
 */
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

    /**
     * Directory location where the audio files are stored.
     */
    private static final String DIR_LOCATION = "/be/esi/prj/leagueofpokemons/sounds/";

    /**
     * Gets the file path of the audio file associated with this {@link AudioSound}.
     * The file is expected to be a WAV file located in the specified directory.
     *
     * @return the file path of the audio file in the format "/be/esi/prj/leagueofpokemons/sounds/{sound}.wav"
     */
    public String getFilePath() {
        return DIR_LOCATION + this.name().toLowerCase() + ".wav";
    }
}
