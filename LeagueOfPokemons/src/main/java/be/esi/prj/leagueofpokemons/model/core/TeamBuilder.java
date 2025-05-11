package be.esi.prj.leagueofpokemons.model.core;

/**
 * The {@link TeamBuilder} class is responsible for constructing Pokémon teams based on difficulty and maximum tier.
 * It provides methods to create teams with varying difficulty levels, each containing a different set of Pokémon.
 */
public class TeamBuilder {

    /**
     * Builds a team based on the specified difficulty and maximum tier.
     * The difficulty determines the strength and number of Pokémon in the team.
     *
     * @param difficulty The difficulty level (0-4) to define the strength of the team.
     * @param maxTier    The maximum tier that the Pokémon in the team can have.
     * @return The constructed {@link Team} for the given difficulty and maximum tier.
     * @throws ModelException If an invalid difficulty level is provided.
     */
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

    /**
     * Constructs a team for difficulty level 0, with weaker Pokémon.
     *
     * @param maxTier The maximum tier for the Pokémon in the team.
     * @return A {@link Team} with Pokémon suited for difficulty level 0.
     */
    private Team buildTeamDifficulty0(Tier maxTier) {
        Team team = new Team(maxTier);

        Pokemon pokemon1 = new Pokemon(new Card("swsh1-10", "Grookey", 60, "https://assets.tcgdex.net/en/swsh/swsh1/10/high.png", Type.GRASS));
        Pokemon pokemon2 = new Pokemon(new Card("swsh1-65", "Pikachu", 60, "https://assets.tcgdex.net/en/swsh/swsh1/65/high.png", Type.LIGHTNING));

        team.addPokemon(pokemon1);
        team.addPokemon(pokemon2);
        return team;
    }

    /**
     * Constructs a team for difficulty level 1, with moderately strong Pokémon.
     *
     * @param maxTier The maximum tier for the Pokémon in the team.
     * @return A {@link Team} with Pokémon suited for difficulty level 1.
     */
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

    /**
     * Constructs a team for difficulty level 2, with stronger Pokémon.
     *
     * @param maxTier The maximum tier for the Pokémon in the team.
     * @return A {@link Team} with Pokémon suited for difficulty level 2.
     */
    private Team buildTeamDifficulty2(Tier maxTier) {
        Team team = new Team(maxTier);

        Pokemon pokemon1 = new Pokemon(new Card("swsh2-30", "Magmortar", 140, "https://assets.tcgdex.net/en/swsh/swsh2/30/high.png", Type.FIRE));
        Pokemon pokemon2 = new Pokemon(new Card("swsh2-107", "Coalossal", 160, "https://assets.tcgdex.net/en/swsh/swsh2/107/high.png", Type.FIGHTING));

        team.addPokemon(pokemon1);
        team.addPokemon(pokemon2);

        return team;
    }

    /**
     * Constructs a team for difficulty level 3, with even stronger Pokémon.
     *
     * @param maxTier The maximum tier for the Pokémon in the team.
     * @return A {@link Team} with Pokémon suited for difficulty level 3.
     */
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

    /**
     * Constructs a team for difficulty level 4, with the strongest Pokémon.
     *
     * @param maxTier The maximum tier for the Pokémon in the team.
     * @return A {@link Team} with Pokémon suited for difficulty level 4.
     */
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
