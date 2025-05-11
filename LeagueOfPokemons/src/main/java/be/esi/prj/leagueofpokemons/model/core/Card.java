package be.esi.prj.leagueofpokemons.model.core;

import java.util.Objects;

/**
 * Represents a Pokémon card.
 * <p>
 * Each card has an ID, name, maximum HP, image URL, tier, and type.
 * The tier is determined based on the Pokémon's maximum HP.
 * </p>
 */
public class Card {
    private final String id;
    private final String name;
    private final int maxHP;
    private final String imageURL;
    private Tier tier;
    private final Type type;

    /**
     * Constructs a new Card object.
     *
     * @param id        the card's unique ID
     * @param name      the name of the Pokémon
     * @param maxHP     the maximum HP of the Pokémon
     * @param imageURL  the URL for the Pokémon's image
     * @param type      the type of the Pokémon
     */
    public Card(String id, String name, int maxHP, String imageURL, Type type) {
        this.id = id;
        this.name = name;
        this.maxHP = maxHP;
        this.imageURL = imageURL;
        setTier(maxHP);
        this.type = type;
    }

    /**
     * Sets the tier of the card based on the maximum HP.
     *
     * @param maxHP the maximum HP of the Pokémon
     */
    private void setTier(int maxHP) {
        if (maxHP <= 100) tier = Tier.TIER_I;
        else if (maxHP <= 140) tier = Tier.TIER_II;
        else if (maxHP <= 180) tier = Tier.TIER_III;
        else if (maxHP <= 220) tier = Tier.TIER_IV;
        else tier = Tier.TIER_V;
    }

    /**
     * Gets the unique ID of the card.
     *
     * @return the card's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the Pokémon.
     *
     * @return the Pokémon's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the maximum HP of the Pokémon.
     *
     * @return the Pokémon's max HP
     */
    public int getMaxHP() {
        return maxHP;
    }

    /**
     * Gets the URL of the Pokémon's image.
     *
     * @return the Pokémon's image URL
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Gets the tier of the card.
     *
     * @return the tier of the card
     */
    public Tier getTier() {
        return tier;
    }

    /**
     * Gets the type of the Pokémon.
     *
     * @return the Pokémon's type
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns a string representation of the card.
     *
     * @return a string representation of the card
     */
    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", maxHP=" + maxHP +
                ", imageURL='" + imageURL + '\'' +
                ", tier=" + tier +
                ", type=" + type +
                '}';
    }

    /**
     * Checks if two cards are equal based on their properties.
     *
     * @param o the object to compare this card with
     * @return true if the cards are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return maxHP == card.maxHP && Objects.equals(id, card.id) && Objects.equals(name, card.name) && Objects.equals(imageURL, card.imageURL) && tier == card.tier && type == card.type;
    }

    /**
     * Returns the hash code for this card.
     *
     * @return the hash code of the card
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, maxHP, imageURL, tier, type);
    }
}
