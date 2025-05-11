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
    void testPlayTurnTurnsNotPrepared() {
        battle.start();
        assertThrows(ModelException.class, () -> battle.playTurn());
    }

    @Test
    void testPlayTurnBattleNotStarted() {
        assertThrows(ModelException.class, () -> battle.playTurn());
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

        player.setNextPokemon(pokemon);

        battle.prepareTurn(ActionType.SWAP, ActionType.SWAP);

        assertThrows(ModelException.class, () -> battle.playTurn());
    }

    @Test
    void testSwapToNextPokemonSuccess() {
        battle.start();

        Pokemon pokemon = player.getSlot1Pokemon();
        player.setNextPokemon(pokemon);
        battle.prepareTurn(ActionType.SWAP, ActionType.SWAP);
        battle.playTurn();

        Pokemon expected = pokemon;
        Pokemon actual = player.getActivePokemon();

        assertEquals(expected, actual);
    }

    @Test
    void testCannotSwapToSamePokemon() {
        battle.start();

        Pokemon pokemon = player.getActivePokemon();
        player.setNextPokemon(pokemon);
        battle.prepareTurn(ActionType.SWAP, ActionType.SWAP);

        assertThrows(ModelException.class, () -> battle.playTurn());
    }

    @Test
    void testSwapSwitchingTurnPlayerToOpponent() {
        battle.start();

        Pokemon pokemon = player.getSlot1Pokemon();
        player.setNextPokemon(pokemon);
        battle.prepareTurn(ActionType.SWAP, ActionType.SWAP);
        battle.playTurn();

        String expected = opponent.getName();
        String actual = battle.getCurrentTurnName();

        assertEquals(expected, actual);
    }

    @Test
    void testSwapSwitchingTurnOpponentToPlayer() {
        battle.start();

        Pokemon pokemon = player.getSlot1Pokemon();
        player.setNextPokemon(pokemon);
        battle.prepareTurn(ActionType.SWAP, ActionType.SWAP);

        battle.playTurn();

        battle.playTurn();

        String expected = player.getName();
        String actual = battle.getCurrentTurnName();

        assertEquals(expected, actual);
    }

    @Test
    void testSwapNotSwitchingTurnIfPlayerPokemonDead() {
        battle.start();

        Pokemon pokemon = player.getActivePokemon();
        pokemon.takeDamage(pokemon.getMaxHP());
        player.setNextPokemon(player.getSlot1Pokemon());
        battle.prepareTurn(ActionType.SWAP, ActionType.SWAP);

        // Turn is already over, to get to the next one, no need to switch turn
        battle.playTurn();

        // Player should already be the next one
        String expected = player.getName();
        String actual = battle.getCurrentTurnName();

        assertEquals(expected, actual);
    }

    @Test
    void testSwapSwitchingTurnIfOpponentPokemonDead() {
        battle.start();

        Pokemon pokemon = opponent.getActivePokemon();
        pokemon.takeDamage(pokemon.getMaxHP() - 1);

        battle.prepareTurn(ActionType.BASIC_ATTACK, ActionType.BASIC_ATTACK);

        // Switch to opponent's turn
        battle.playTurn();

        // Opponent has dead Pokémon, he swaps

        // Turn is over, currentTurn should be the player
        String expected = player.getName();
        String actual = battle.getCurrentTurnName();

        assertEquals(expected, actual);

    }

    @Test
    void testPlayTurnAttackActivePokemonDefeated() {
        battle.start();

        player.getActivePokemon().takeDamage(1000);
        battle.prepareTurn(ActionType.SPECIAL_ATTACK, ActionType.BASIC_ATTACK);

        assertThrows(ModelException.class, () -> battle.playTurn());
    }

    @Test
    void testBattleOverPlayerWon() {
        battle.start();

        opponent.getSlot1Pokemon().takeDamage(1000);
        opponent.getActivePokemon().takeDamage(opponent.getActivePokemon().getMaxHP() - 1);
        battle.prepareTurn(ActionType.BASIC_ATTACK, ActionType.BASIC_ATTACK);

        battle.playTurn();

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

        battle.prepareTurn(ActionType.BASIC_ATTACK, ActionType.BASIC_ATTACK);
        battle.playTurn();
        battle.playTurn();

        assertTrue(battle.isOver());

        String expected = opponent.getName();
        String actual = battle.getWinner();

        assertEquals(expected, actual);
    }

    @Test
    void testInTurnAfterPlayerAttack() {
        battle.start();

        battle.prepareTurn(ActionType.BASIC_ATTACK, ActionType.BASIC_ATTACK);
        battle.playTurn();

        boolean expected = true;
        boolean actual = battle.isInTurn();

        assertEquals(expected, actual);
    }

    @Test
    void testInTurnAfterPlayerSwap() {
        battle.start();

        player.setNextPokemon(player.getSlot1Pokemon());

        battle.prepareTurn(ActionType.SWAP, ActionType.BASIC_ATTACK);
        battle.playTurn();

        boolean expected = true;
        boolean actual = battle.isInTurn();

        assertEquals(expected, actual);
    }

    @Test
    void testInTurnAfterOpponentSwap() {
        battle.start();

        battle.prepareTurn(ActionType.BASIC_ATTACK, ActionType.SWAP);
        battle.playTurn();

        boolean expected = true;
        boolean actual = battle.isInTurn();

        assertEquals(expected, actual);
    }

    @Test
    void testNotInTurnAfterOpponentAttack() {
        battle.start();

        player.setNextPokemon(player.getSlot1Pokemon());

        battle.prepareTurn(ActionType.SWAP, ActionType.BASIC_ATTACK);
        battle.playTurn();
        battle.playTurn();


        boolean expected = false;
        boolean actual = battle.isInTurn();

        assertEquals(expected, actual);
    }

}