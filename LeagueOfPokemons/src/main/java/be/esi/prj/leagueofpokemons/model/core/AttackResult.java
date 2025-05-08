package be.esi.prj.leagueofpokemons.model.core;

public record AttackResult(int damage, Effect.EffectType effectType) {
    public AttackResult(int damage) {
        this(damage, null);
    }
}
