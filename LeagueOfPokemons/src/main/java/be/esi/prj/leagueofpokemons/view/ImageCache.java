package be.esi.prj.leagueofpokemons.view;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Collection;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageCache implements PropertyChangeListener {
    private final Map<String, Image> croppedImages = new HashMap<>();

    private final Map<String, ImageView> imageViews = new HashMap<>();

    private static ImageCache instance;

    private final ExecutorService executor;

    private ImageCache() {
        executor = Executors.newFixedThreadPool(4);
    }

    public static ImageCache getInstance() {
        if (instance == null) {
            instance = new ImageCache();
        }
        return instance;
    }

    private Image getCroppedImage(Card card) {
        return croppedImages.computeIfAbsent(card.getId(), id -> {
            Image fullImage = new Image(card.getImageURL(), false);
            return new WritableImage(fullImage.getPixelReader(), 0, 0, 600, 420);
        });
    }

    public ImageView getImageView(Card card) {
        if (!imageViews.containsKey(card.getId())) {
            Image croppedImage = getCroppedImage(card);
            ImageView view = new ImageView(croppedImage);
            view.setFitWidth(165);
            view.setFitHeight(113);
            view.setPreserveRatio(false);
            imageViews.put(card.getId(), view);
        }

        // Return a clone
        ImageView original = imageViews.get(card.getId());
        ImageView clone = new ImageView(original.getImage());
        clone.setFitWidth(original.getFitWidth());
        clone.setFitHeight(original.getFitHeight());
        clone.setPreserveRatio(original.isPreserveRatio());

        return clone;
    }

    private void preloadCardImage(Card card) {
        if (!croppedImages.containsKey(card.getId())) {
            System.out.println("Preloading " + card.getName());

            executor.submit(() -> {
                Image fullImage = new Image(card.getImageURL(), false);
                WritableImage croppedImage = new WritableImage(fullImage.getPixelReader(), 0, 0, 600, 420);

                Platform.runLater(() -> {
                    croppedImages.put(card.getId(), croppedImage);
                    System.out.println("Finished preloading " + card.getName());
                });

            });

        }
    }

    public void registerToCollection(Collection collection) {
        collection.addPropertyChangeListener(Collection.CARD_ADDED_EVENT, this);
    }

    public void shutdown() {
        executor.shutdown();
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();

        if (Collection.CARD_ADDED_EVENT.equals(evt.getPropertyName())) {
            preloadCardImage((Card) newValue);
        }
    }
}
