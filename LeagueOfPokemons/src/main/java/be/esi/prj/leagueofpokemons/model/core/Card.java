package be.esi.prj.leagueofpokemons.model.core;

public record Card(String id, String name, int maxHP, String imageURL, Tier tier, Type type) {
}
