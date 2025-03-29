package be.esi.prj.leagueofpokemons.model.core;

import java.util.Objects;

// Class or Record ?
public class Card {
    private final String id;
    private final String name;
    private final int maxHP;
    private final String imageURL;
    private Tier tier;
    private final Type type;

    // Might want to check parameters...
    public Card(String id, String name, int maxHP, String imageURL, Type type) {
        this.id = id;
        this.name = name;
        this.maxHP = maxHP;
        this.imageURL = imageURL;
        setTier(maxHP);
        this.type = type;
    }

    // Make it return Tier and static if we want final tier
    private void setTier(int maxHP) {
        if (maxHP <= 100) tier = Tier.TIER_I;
        else if (maxHP <= 140) tier = Tier.TIER_II;
        else if (maxHP <= 180) tier = Tier.TIER_III;
        else if (maxHP <= 220) tier = Tier.TIER_IV;
        else tier = Tier.TIER_V;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Tier getTier() {
        return tier;
    }

    public Type getType() {
        return type;
    }

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return maxHP == card.maxHP && Objects.equals(id, card.id) && Objects.equals(name, card.name) && Objects.equals(imageURL, card.imageURL) && tier == card.tier && type == card.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, maxHP, imageURL, tier, type);
    }
}
