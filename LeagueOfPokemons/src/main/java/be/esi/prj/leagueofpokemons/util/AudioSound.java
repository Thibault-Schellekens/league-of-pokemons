package be.esi.prj.leagueofpokemons.util;

public enum AudioSound {
    SCANNER_ADD,
    POKEBALL_WOBBLE,

    ;

    private static final String DIR_LOCATION = "/be/esi/prj/leagueofpokemons/sounds/";

    public String getFilePath() {
        return DIR_LOCATION + this.name().toLowerCase() + ".wav";
    }
}
