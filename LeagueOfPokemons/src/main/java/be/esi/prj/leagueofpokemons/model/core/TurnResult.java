package be.esi.prj.leagueofpokemons.model.core;

public record TurnResult(CombatEntity attacker, CombatEntity defender, int defenderHP, boolean isPokemonDefeated, Effect.EffectType effectType) {

}
