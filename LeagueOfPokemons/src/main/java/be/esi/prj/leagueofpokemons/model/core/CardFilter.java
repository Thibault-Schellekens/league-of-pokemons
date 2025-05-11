package be.esi.prj.leagueofpokemons.model.core;

/**
 * Represents a filter that can be used to filter Pokémon cards based on their name, type, and tier.
 * <p>
 * This class provides a builder pattern to construct a filter that can be used to match cards against
 * specific criteria like the card's name, type, and tier.
 * </p>
 */
public class CardFilter {
    private final String name;
    private final Type type;
    private final Tier tier;

    /**
     * Constructs a new CardFilter using the Builder.
     *
     * @param builder the builder that constructs the CardFilter
     */
    private CardFilter(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.tier = builder.tier;
    }

    /**
     * Checks if a given card matches the filter's criteria.
     *
     * @param card the card to check
     * @return true if the card matches the filter criteria, false otherwise
     */
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

    /**
     * Builder class for constructing a CardFilter.
     * <p>
     * The builder allows you to specify the name, type, and tier criteria for the filter.
     * </p>
     */
    public static class Builder {
        private String name;
        private Type type;
        private Tier tier;

        /**
         * Sets the name filter for the CardFilter.
         *
         * @param name the name to filter cards by
         * @return the builder instance for method chaining
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the type filter for the CardFilter.
         *
         * @param type the type to filter cards by
         * @return the builder instance for method chaining
         */
        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        /**
         * Sets the tier filter for the CardFilter.
         *
         * @param tier the tier to filter cards by
         * @return the builder instance for method chaining
         */
        public Builder tier(Tier tier) {
            this.tier = tier;
            return this;
        }

        /**
         * Builds a CardFilter using the specified criteria.
         *
         * @return the constructed CardFilter
         */
        public CardFilter build() {
            return new CardFilter(this);
        }
    }
}
