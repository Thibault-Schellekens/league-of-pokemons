package be.esi.prj.leagueofpokemons.model.core;

public record AttackResult(int damage, Effect.EffectType effectType, String message) {
    public AttackResult(int damage) {
        this(damage, null, "");
    }
}
