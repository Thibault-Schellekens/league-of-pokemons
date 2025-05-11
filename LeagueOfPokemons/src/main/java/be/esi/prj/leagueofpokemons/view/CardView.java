package be.esi.prj.leagueofpokemons.view;

import be.esi.prj.leagueofpokemons.model.core.Card;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import be.esi.prj.leagueofpokemons.controller.CollectionController;

public class CardView extends StackPane {
    private final Card card;
    private final ImageView imageView;
    private final Button selectButton;
    private CollectionController controller;

    public CardView(Card card, CollectionController controller, CardContext context) {
        this.card = card;
        this.controller = controller;

        this.imageView = ImageCache.getInstance().getImageView(card);
        this.selectButton = new Button();

        setup(context);
    }

    public CardView(StackPane placeholder) {
        this.card = null;
        this.imageView = null;
        this.selectButton = null;
        this.controller = null;
        this.getChildren().add(placeholder);
    }

    private void setup(CardContext context) {
        selectButton.setOpacity(0);
        selectButton.setPrefSize(165, 113);
        selectButton.setStyle("-fx-cursor: hand;");

        selectButton.setOnAction(e -> {
            switch (context) {
                case INVENTORY -> controller.onInventoryCardSelected(card);
                case COLLECTION -> controller.onCollectionCardSelected(this);
            }
        });

        this.getChildren().addAll(imageView, selectButton);
        this.setPrefSize(165, 113);
    }

    public Card getCard() {
        return card;
    }

    public static CardView createEmptySlot() {
        StackPane placeholder = new StackPane();
        placeholder.setPrefSize(165, 113);
        placeholder.setStyle("-fx-background-color: #cccccc; -fx-border-color: black; -fx-border-width: 1;");

        // Optional: Add a small icon/text
        javafx.scene.text.Text emptyText = new javafx.scene.text.Text("Empty");
        emptyText.setStyle("-fx-font-size: 14px; -fx-fill: gray;");
        placeholder.getChildren().add(emptyText);

        return new CardView(placeholder);
    }


    @Override
    public String toString() {
        return "CardView{" +
                "card=" + card +
                ", imageView=" + imageView +
                ", selectButton=" + selectButton +
                ", controller=" + controller +
                '}';
    }
}
