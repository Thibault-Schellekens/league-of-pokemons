package be.esi.prj.leagueofpokemons.model.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
    }

    @Test
    void testAddCardInventoryFull() {
        Card card1 = new Card("test1", "test1", 100, "url", Type.FIRE);
        Card card2 = new Card("test2", "test2", 100, "url", Type.FIRE);
        Card card3 = new Card("test3", "test3", 100, "url", Type.FIRE);

        player.addCard(card1);
        player.addCard(card2);
        player.addCard(card3);

        Card card4 = new Card("test4", "test4", 100, "url", Type.FIRE);

        assertThrows(ModelException.class, () -> player.addCard(card4));
    }

    @Test
    void testAddCardSameCard() {
        Card card1 = new Card("test1", "test1", 100, "url", Type.FIRE);

        player.addCard(card1);

        assertThrows(ModelException.class, () -> player.addCard(card1));
    }

    @Test
    void testAddCardSuccess() {
        Card card1 = new Card("test1", "test1", 100, "url", Type.FIRE);

        player.addCard(card1);

        Card expected = card1;
        Card actual = player.getInventory().getFirst();

        assertEquals(expected, actual);
    }

    @Test
    void testGetSlotNegativeIndex() {
        assertThrows(ModelException.class, () -> player.getSlot(-1));
    }

    @Test
    void testGetSlotOutOfBoundsIndex() {
        Card card1 = new Card("test1", "test1", 100, "url", Type.FIRE);

        player.addCard(card1);

        assertThrows(ModelException.class, () -> player.getSlot(2));
    }

    @Test
    void testGetSlotSuccess() {
        Card card1 = new Card("test1", "test1", 100, "url", Type.FIRE);
        player.addCard(card1);

        Card expected = card1;
        Card actual = player.getSlot(0);

        assertEquals(expected, actual);
    }

    @Test
    void testSelectTeamEmpty() {
        assertThrows(ModelException.class, () -> player.selectTeam(Tier.TIER_V));
    }

    @Test
    void testSelectTeamLessThan3() {
        Card card1 = new Card("test1", "test1", 100, "url", Type.FIRE);
        Card card2 = new Card("test2", "test2", 100, "url", Type.FIRE);

        player.addCard(card1);
        player.addCard(card2);

        assertThrows(ModelException.class, () -> player.selectTeam(Tier.TIER_V));
    }

    @Test
    void selectTeamSuccessActivePokemon() {
        Card card1 = new Card("test1", "test1", 100, "url", Type.FIRE);
        Card card2 = new Card("test2", "test2", 100, "url", Type.FIRE);
        Card card3 = new Card("test3", "test3", 100, "url", Type.FIRE);

        player.addCard(card1);
        player.addCard(card2);
        player.addCard(card3);
        player.selectTeam(Tier.TIER_V);

        Pokemon expected = new Pokemon(card1);
        Pokemon actual = player.getActivePokemon();

        System.out.println(actual);
        System.out.println(expected);

        assertEquals(expected, actual);
    }

    @Test
    void selectTeamSuccess() {
        Card card1 = new Card("test1", "test1", 100, "url", Type.FIRE);
        Card card2 = new Card("test2", "test2", 100, "url", Type.FIRE);
        Card card3 = new Card("test3", "test3", 100, "url", Type.FIRE);

        player.addCard(card1);
        player.addCard(card2);
        player.addCard(card3);
        player.selectTeam(Tier.TIER_V);

        Pokemon expected1 = new Pokemon(card1);
        Pokemon actual1 = player.team.getPokemon(0);
        assertEquals(expected1, actual1);

        Pokemon expected2 = new Pokemon(card2);
        Pokemon actual2 = player.team.getPokemon(1);
        assertEquals(expected2, actual2);

        Pokemon expected3 = new Pokemon(card3);
        Pokemon actual3 = player.team.getPokemon(2);
        assertEquals(expected3, actual3);

    }

}