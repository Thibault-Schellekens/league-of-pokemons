package be.esi.prj.leagueofpokemons.model.core;

public enum Tier {
    TIER_I,
    TIER_II,
    TIER_III,
    TIER_IV,
    TIER_V,
    ;

    public boolean isGreater(Tier tier) {
        return this.ordinal() > tier.ordinal();
    }
}
