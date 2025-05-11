package be.esi.prj.leagueofpokemons;


import be.esi.prj.leagueofpokemons.util.GameManager;
import be.esi.prj.leagueofpokemons.util.SceneManager;

public class LeagueOfPokemon {
    public static void main(String[] args) {
        SceneManager.start();
        GameManager.newGame("Marian");

    }
}