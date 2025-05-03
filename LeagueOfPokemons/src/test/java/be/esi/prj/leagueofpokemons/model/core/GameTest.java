package be.esi.prj.leagueofpokemons.model.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
        Game.initialize(0, player, new Collection(0));
        game = Game.getInstance();
    }

    @Test
    void testStartBattlePlayerEmptyTeam() {
        assertThrows(ModelException.class, () -> game.startBattle());
    }

}