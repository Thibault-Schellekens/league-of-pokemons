package be.esi.prj.leagueofpokemons.model.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BattleTest {

    private Battle battle;
    private Player player;
    private Opponent opponent;

    @BeforeEach
    void setUp() {
        player = new Player();
        Card card1 = new Card("test1", "test1", 100, "url", Type.FIRE);
        Card card2 = new Card("test2", "test2", 100, "url", Type.FIRE);
        Card card3 = new Card("test3", "test3", 100, "url", Type.FIRE);

        player.addCard(card1);
        player.addCard(card2);
        player.addCard(card3);
        player.selectTeam(Tier.TIER_V);

        opponent = new Opponent();
        opponent.selectTeam(Tier.TIER_V);
        battle = new Battle(player, opponent);
    }

    @Test
    void testSwapBattleNotStarted() {
        assertThrows(ModelException.class, () -> battle.swap(player.getActivePokemon()));
    }

    @Test
    void testPlayTurnBattleNotStarted() {
        assertThrows(ModelException.class, () -> battle.playTurn(ActionType.SPECIAL_ATTACK));
    }

    @Test
    void testStartBattleAlreadyStarted() {
        battle.start();
        assertThrows(ModelException.class, () -> battle.start());
    }

    @Test
    void testCannotSwapToDeadPokemon() {
        battle.start();

        Pokemon pokemon = player.getSlot1Pokemon();
        pokemon.takeDamage(pokemon.getMaxHP());

        assertThrows(ModelException.class, () -> battle.swap(pokemon));
    }

    @Test
    void testSwapToNextPokemonSuccess() {
        battle.start();

        Pokemon pokemon = player.getSlot1Pokemon();
        battle.swap(pokemon);

        Pokemon expected = pokemon;
        Pokemon actual = player.getActivePokemon();

        assertEquals(expected, actual);
    }

    @Test
    void testCannotSwapToSamePokemon() {
        battle.start();

        assertThrows(ModelException.class, () -> battle.swap(player.getActivePokemon()));
    }

    @Test
    void testSwapSwitchingTurnPlayerToOpponent() {
        battle.start();

        battle.swap(player.getSlot1Pokemon());

        String expected = opponent.getName();
        String actual = battle.getCurrentTurnName();

        assertEquals(expected, actual);
    }

    @Test
    void testSwapSwitchingTurnOpponentToPlayer() {
        battle.start();

        battle.swap(player.getSlot1Pokemon());

        battle.swap(opponent.getSlot1Pokemon());

        String expected = player.getName();
        String actual = battle.getCurrentTurnName();

        assertEquals(expected, actual);
    }

    @Test
    void testSwapNotSwitchingTurnIfPlayerPokemonDead() {
        battle.start();

        Pokemon pokemon = player.getActivePokemon();
        pokemon.takeDamage(pokemon.getMaxHP());

        // Turn is already over, to get to the next one, no need to switch turn
        battle.swap(player.getSlot1Pokemon());

        // Player should already be the next one
        String expected = player.getName();
        String actual = battle.getCurrentTurnName();

        assertEquals(expected, actual);
    }

    @Test
    void testSwapSwitchingTurnIfOpponentPokemonDead() {
        battle.start();

        Pokemon pokemon = opponent.getActivePokemon();
        pokemon.takeDamage(pokemon.getMaxHP());

        // Switch to opponent's turn
        battle.swap(player.getSlot1Pokemon());

        // Opponent has dead Pokémon, he swaps
        battle.swap(opponent.getSlot1Pokemon());

        // Turn is over, currentTurn should be the player
        String expected = player.getName();
        String actual = battle.getCurrentTurnName();

        assertEquals(expected, actual);

    }

    @Test
    void testPlayTurnAttackActivePokemonDefeated() {
        battle.start();

        player.getActivePokemon().takeDamage(1000);

        assertThrows(ModelException.class, () -> battle.playTurn(ActionType.SPECIAL_ATTACK));
    }

    @Test
    void testBattleOverPlayerWon() {
        battle.start();

        opponent.getSlot1Pokemon().takeDamage(1000);
        opponent.getActivePokemon().takeDamage(opponent.getActivePokemon().getMaxHP() - 1);

        battle.playTurn(ActionType.BASIC_ATTACK);

        assertTrue(battle.isOver());

        String expected = player.getName();
        String actual = battle.getWinner();

        assertEquals(expected, actual);
    }

    @Test
    void testBattleOverOpponentWon() {
        battle.start();

        player.getSlot1Pokemon().takeDamage(1000);
        player.getSlot2Pokemon().takeDamage(1000);

        player.getActivePokemon().takeDamage(player.getActivePokemon().getMaxHP() - 1);

        battle.playTurn(ActionType.BASIC_ATTACK);
        battle.playTurn(ActionType.BASIC_ATTACK);

        assertTrue(battle.isOver());

        String expected = opponent.getName();
        String actual = battle.getWinner();

        assertEquals(expected, actual);
    }

    @Test
    void testBattleSwitchCurrentTurnPokemonDefeated() {
        battle.start();

        opponent.getActivePokemon().takeDamage(opponent.getActivePokemon().getMaxHP() - 1);

        battle.playTurn(ActionType.BASIC_ATTACK);

        assertEquals(opponent.getName(), battle.getCurrentTurnName());
    }
}