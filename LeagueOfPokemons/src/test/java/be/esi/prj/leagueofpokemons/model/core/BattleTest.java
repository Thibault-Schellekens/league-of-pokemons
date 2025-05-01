package be.esi.prj.leagueofpokemons.model.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleTest {

    private Battle battle;

    @BeforeEach
    void setUp() {
        Player player = new Player();
        Opponent opponent = new Opponent();
        opponent.createTeam();
        battle = new Battle(player, opponent);
    }
    
}