package be.esi.prj.leagueofpokemons.model.core;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
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

    @AfterEach
    void reset() {
        Game.resetInstance();
    }

    @Test
    void testStartBattlePlayerEmptyTeam() {
        assertThrows(ModelException.class, () -> game.startBattle());
    }

    @Test
    void testStartBattleGameOver() {
        game.endGame();
        assertThrows(ModelException.class, () -> game.startBattle());
    }

    @Test
    void testStartBattleSuccess() {
        player.addCard(new Card("test1", "test1", 100, "url", Type.FIRE));
        player.addCard(new Card("test2", "test2", 100, "url", Type.FIRE));
        player.addCard(new Card("test3", "test3", 100, "url", Type.FIRE));

        assertNotNull(game.startBattle());
    }

    @Test
    void testNextStageBattleNotOver() {
        player.addCard(new Card("test1", "test1", 100, "url", Type.FIRE));
        player.addCard(new Card("test2", "test2", 100, "url", Type.FIRE));
        player.addCard(new Card("test3", "test3", 100, "url", Type.FIRE));
        game.startBattle();

        assertThrows(ModelException.class, () -> game.nextStage());
    }

    @Test
    void testSameStageIfPlayerLost() {
        player.addCard(new Card("test1", "test1", 100, "url", Type.FIRE));
        player.addCard(new Card("test2", "test2", 100, "url", Type.FIRE));
        player.addCard(new Card("test3", "test3", 100, "url", Type.FIRE));

        Battle battle = game.startBattle();

        player.team.getPokemon(0).takeDamage(99);
        player.team.getPokemon(1).takeDamage(1000);
        player.team.getPokemon(2).takeDamage(1000);

        battle.start();
        // This will end the battle
        battle.prepareTurn(ActionType.BASIC_ATTACK, ActionType.BASIC_ATTACK);
        battle.playTurn();
        battle.playTurn();


        int expected = game.getCurrentStage();
        game.nextStage();


        int actual = game.getCurrentStage();

        assertEquals(expected, actual);
    }

    @Test
    void testNextStageIfPlayerWins() {
        player.addCard(new Card("test1", "test1", 100, "url", Type.FIRE));
        player.addCard(new Card("test2", "test2", 100, "url", Type.FIRE));
        player.addCard(new Card("test3", "test3", 100, "url", Type.FIRE));

        Battle battle = game.startBattle();
        battle.getOpponent().team.getPokemon(0).takeDamage(50);
        battle.getOpponent().team.getPokemon(1).takeDamage(1000);

        battle.start();
        battle.prepareTurn(ActionType.BASIC_ATTACK, ActionType.BASIC_ATTACK);
        battle.playTurn();

        game.nextStage();

        int expected = 1;
        int actual = game.getCurrentStage();

        assertEquals(expected, actual);
    }

}