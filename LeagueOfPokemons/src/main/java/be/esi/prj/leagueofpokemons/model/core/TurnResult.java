package be.esi.prj.leagueofpokemons.model.core;

public record TurnResult(CombatEntity attacker, CombatEntity defender, int damage, int defenderHP, int attackerHP, boolean isPokemonDefeated, Effect.EffectType effectType) {

}
