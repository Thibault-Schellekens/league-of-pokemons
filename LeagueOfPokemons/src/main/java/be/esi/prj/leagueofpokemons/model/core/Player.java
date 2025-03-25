package be.esi.prj.leagueofpokemons.model.core;

import java.util.List;

public class Player extends CombatEntity {
    private List<Card> cards;

    public boolean buyCard(Card card) {
        return false;
    }

    public void sellCard(Card card) {

    }

    public boolean selectTeam(List<Card> cards) {
        return false;
    }

    @Override
    public void performAction(ActionType actionType, CombatEntity enemy) {

    }
}
