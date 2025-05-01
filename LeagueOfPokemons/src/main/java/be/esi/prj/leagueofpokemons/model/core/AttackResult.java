package be.esi.prj.leagueofpokemons.model.core;

public record AttackResult(int damage, String message) {
    public AttackResult(int damage) {
        this(damage, "");
    }
}
