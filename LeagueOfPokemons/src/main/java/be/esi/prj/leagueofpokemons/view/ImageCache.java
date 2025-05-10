package be.esi.prj.leagueofpokemons.view;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Collection;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageCache implements PropertyChangeListener {
    private final Map<String, Image> croppedImages = new HashMap<>();
    // Stores all the clones, so we can update them when the real image is loaded
    private final Map<String, List<ImageView>> activeViews = new HashMap<>();
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
        return croppedImages.computeIfAbsent(card.getId(),
                id -> EmptyCardView.create(card));
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

        ImageView original = imageViews.get(card.getId());
        ImageView clone = new ImageView(original.getImage());
        clone.setFitWidth(original.getFitWidth());
        clone.setFitHeight(original.getFitHeight());
        clone.setPreserveRatio(original.isPreserveRatio());

        activeViews.computeIfAbsent(card.getId(), k -> new ArrayList<>()).add(clone);

        return clone;
    }

    private void preloadCardImage(Card card) {
        if (!croppedImages.containsKey(card.getId())) {
            System.out.println("Preloading " + card.getName());

            executor.submit(() -> {
                try {
                    Image fullImage = new Image(card.getImageURL(), false);
                    WritableImage croppedImage = new WritableImage(fullImage.getPixelReader(), 0, 0, 600, 420);

                    Platform.runLater(() -> {
                        croppedImages.put(card.getId(), croppedImage);

                        if (imageViews.containsKey(card.getId())) {
                            imageViews.get(card.getId()).setImage(croppedImage);
                        }

                        if (activeViews.containsKey(card.getId())) {
                            List<ImageView> views = activeViews.get(card.getId());
                            List<ImageView> validViews = new ArrayList<>();

                            for (ImageView view : views) {
                                view.setImage(croppedImage);
                                validViews.add(view);
                            }

                            activeViews.put(card.getId(), validViews);
                        }

                        System.out.println("Finished preloading " + card.getName());
                    });
                } catch (Exception e) {
                    System.err.println("Error preloading image for " + card.getName() + ": " + e.getMessage());
                }
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