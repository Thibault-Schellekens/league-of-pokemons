package be.esi.prj.leagueofpokemons.model.core;

public class TeamBuilder {

    public Team buildTeam(int difficulty, Tier maxTier) {
        return switch (difficulty) {
            case 0 -> buildTeamDifficulty0(maxTier);
            case 1 -> buildTeamDifficulty1(maxTier);
            case 2 -> buildTeamDifficulty2(maxTier);
            case 3 -> buildTeamDifficulty3(maxTier);
            case 4 -> buildTeamDifficulty4(maxTier);
            default -> throw new ModelException("Unexpected value: " + difficulty);
        };
    }

    private Team buildTeamDifficulty0(Tier maxTier) {
        Team team = new Team(maxTier);

        Pokemon pokemon1 = new Pokemon(new Card("swsh1-10", "Grookey", 60, "https://assets.tcgdex.net/en/swsh/swsh1/10/high.png", Type.GRASS));
        Pokemon pokemon2 = new Pokemon(new Card("swsh1-65", "Pikachu", 60, "https://assets.tcgdex.net/en/swsh/swsh1/65/high.png", Type.LIGHTNING));

        team.addPokemon(pokemon1);
        team.addPokemon(pokemon2);
        return team;
    }

    private Team buildTeamDifficulty1(Tier maxTier) {
        Team team = new Team(maxTier);

        Pokemon pokemon1 = new Pokemon(new Card("swsh1-64", "Frosmoth", 90, "https://assets.tcgdex.net/en/swsh/swsh1/64/high.png", Type.WATER));
        Pokemon pokemon2 = new Pokemon(new Card("swsh2-4", "Scyther", 80, "https://assets.tcgdex.net/en/swsh/swsh2/4/high.png", Type.GRASS));
        Pokemon pokemon3 = new Pokemon(new Card("swsh2-28", "Arcanine", 130, "https://assets.tcgdex.net/en/swsh/swsh2/28/high.png", Type.FIRE));

        team.addPokemon(pokemon1);
        team.addPokemon(pokemon2);
        team.addPokemon(pokemon3);

        return team;
    }

    private Team buildTeamDifficulty2(Tier maxTier) {
        Team team = new Team(maxTier);

        Pokemon pokemon1 = new Pokemon(new Card("swsh2-30", "Magmortar", 140, "https://assets.tcgdex.net/en/swsh/swsh2/30/high.png", Type.FIRE));
        Pokemon pokemon2 = new Pokemon(new Card("swsh2-107", "Coalossal", 160, "https://assets.tcgdex.net/en/swsh/swsh2/107/high.png", Type.FIGHTING));

        team.addPokemon(pokemon1);
        team.addPokemon(pokemon2);

        return team;
    }

    private Team buildTeamDifficulty3(Tier maxTier) {
        Team team = new Team(maxTier);

        Pokemon pokemon1 = new Pokemon(new Card("swsh3-24", "Blaziken", 170, "https://assets.tcgdex.net/en/swsh/swsh3/24/high.png", Type.FIRE));
        Pokemon pokemon2 = new Pokemon(new Card("swsh3-16", "Tsareena", 150, "https://assets.tcgdex.net/en/swsh/swsh3/16/high.png", Type.GRASS));
        Pokemon pokemon3 = new Pokemon(new Card("swsh3-65", "Dracozolt", 160, "https://assets.tcgdex.net/en/swsh/swsh3/65/high.png", Type.LIGHTNING));

        team.addPokemon(pokemon1);
        team.addPokemon(pokemon2);
        team.addPokemon(pokemon3);

        return team;
    }

    private Team buildTeamDifficulty4(Tier maxTier) {
        Team team = new Team(maxTier);


        Pokemon pokemon1 = new Pokemon(new Card("swsh3-95", "Rhyperior V", 230, "https://assets.tcgdex.net/en/swsh/swsh3/95/high.png", Type.FIGHTING));
        Pokemon pokemon2 = new Pokemon(new Card("swsh3-19", "Charizard V", 220, "https://assets.tcgdex.net/en/swsh/swsh3/19/high.png", Type.FIRE));
        Pokemon pokemon3 = new Pokemon(new Card("swsh2-18", "Rillaboom VMAX", 330, "https://assets.tcgdex.net/en/swsh/swsh2/18/high.png", Type.GRASS));

        team.addPokemon(pokemon1);
        team.addPokemon(pokemon2);
        team.addPokemon(pokemon3);

        return team;
    }

}
