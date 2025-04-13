package be.esi.prj.leagueofpokemons.model.core;

public class Effect {
    private EffectType type;
    private int duration;
    private int power;

    public enum EffectType {
        BURN,
        DODGE,
        DRAIN,
        PARALYZE,
        RESISTANCE
    }

    public Effect(EffectType type, int duration, int power) {
        this.type = type;
        this.duration = duration;
        this.power = power;
    }

    public void applyTurn(Pokemon pokemon) {
        switch (type) {
            case BURN -> {

            }
            case DODGE -> {

            }
            case DRAIN -> {

            }
            case PARALYZE -> {

            }
            case RESISTANCE -> {

            }
        }

        duration--;
    }

}
