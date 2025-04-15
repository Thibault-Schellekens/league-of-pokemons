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
    private final Image cropped;
    private final CollectionController controller;

    public CardView(Card card, CollectionController controller, CardContext context) {
        this.card = card;
        this.controller = controller;

        Image fullImage = new Image(card.getImageURL(), false);
        this.cropped = new WritableImage(fullImage.getPixelReader(), 0, 0, 600, 425);
        this.imageView = new ImageView(cropped);
        this.selectButton = new Button();

        setup(context);
    }

    public CardView(CardView other, CollectionController controller, CardContext context) {
        this.card = other.card;
        this.controller = controller;
        this.cropped = other.cropped;
        this.imageView = new ImageView(cropped);
        this.selectButton = new Button();

        setup(context);
    }

    private void setup(CardContext context) {
        imageView.setFitWidth(165);
        imageView.setFitHeight(113);
        imageView.setPreserveRatio(false);

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
}
