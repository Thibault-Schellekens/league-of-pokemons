package be.esi.prj.leagueofpokemons.model.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class   AttackTest {

    @Test
    void testBasicAttackNoRemainingUse() {
        Attack basicAttack = new AttackBasic(Tier.TIER_I);
        for (int i = 0; i < 20; i++) {
            basicAttack.use(Type.GRASS);
        }

        assertThrows(ModelException.class, () -> basicAttack.use(Type.GRASS));
    }

    @Test
    void testSpecialAttackNoRemainingUse() {
        Attack specialAttack = new AttackSpecial(Tier.TIER_I, Type.FIRE);
        for (int i = 0; i < 3; i++) {
            specialAttack.use(Type.GRASS);
        }

        assertThrows(ModelException.class, () -> specialAttack.use(Type.GRASS));
    }

}