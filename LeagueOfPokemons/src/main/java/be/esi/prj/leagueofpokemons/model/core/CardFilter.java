package be.esi.prj.leagueofpokemons.model.core;

public class CardFilter {
    private final String name;
    private final Type type;
    private final Tier tier;

    private CardFilter(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.tier = builder.tier;
    }

    public boolean matches(Card card) {
        if (name != null && !card.getName().toLowerCase().contains(name.toLowerCase())) {
            return false;
        }

        if (type != null && card.getType() != type) {
            return false;
        }

        if (tier != null && card.getTier() != tier) {
            return false;
        }

        return true;
    }

    public static class Builder {
        private String name;
        private Type type;
        private Tier tier;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder tier(Tier tier) {
            this.tier = tier;
            return this;
        }

        public CardFilter build() {
            return new CardFilter(this);
        }
    }
}
