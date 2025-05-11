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

/**
 * Singleton class for managing and caching images associated with Pokémon cards.
 * <p>
 * The {@link ImageCache} preloads and caches images for cards, creating image views for them,
 * and allows efficient image rendering in the UI.
 */
public class ImageCache implements PropertyChangeListener {
    private final Map<String, Image> croppedImages = new HashMap<>();
    private final Map<String, List<ImageView>> activeViews = new HashMap<>();
    private final Map<String, ImageView> imageViews = new HashMap<>();

    private static ImageCache instance;

    private final ExecutorService executor;

    /**
     * Private constructor to initialize the {@link ImageCache} with a fixed thread pool.
     */
    private ImageCache() {
        executor = Executors.newFixedThreadPool(4);
    }

    /**
     * Gets the singleton instance of the {@link ImageCache}.
     *
     * @return the singleton instance of the {@link ImageCache}
     */
    public static ImageCache getInstance() {
        if (instance == null) {
            instance = new ImageCache();
        }
        return instance;
    }

    /**
     * Retrieves a cropped image for the specified Pokémon card.
     * The image is preloaded if it hasn't been cached already.
     *
     * @param card the Pokémon card for which to retrieve the image
     * @return the cropped image for the card
     */
    private Image getCroppedImage(Card card) {
        return croppedImages.computeIfAbsent(card.getId(),
                id -> EmptyCardView.create(card));
    }

    /**
     * Retrieves an {@link ImageView} for the specified Pokémon card.
     * If the image has not been loaded, a new image view is created and the image is preloaded.
     *
     * @param card the Pokémon card for which to retrieve the image view
     * @return the {@link ImageView} for the card
     */
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

    /**
     * Preloads the image for a given Pokémon card asynchronously.
     * The image is fetched, cropped, and stored in the cache.
     *
     * @param card the Pokémon card for which to preload the image
     */
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

    /**
     * Registers a {@link Collection} to listen for card additions and preload images for new cards.
     *
     * @param collection the {@link Collection} to register
     */
    public void registerToCollection(Collection collection) {
        collection.addPropertyChangeListener(Collection.CARD_ADDED_EVENT, this);
    }

    /**
     * Shuts down the executor service used for preloading images.
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * Handles property changes for the {@link Collection}, specifically when a card is added.
     * This triggers the image preloading for the new card.
     *
     * @param evt the property change event
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();

        if (Collection.CARD_ADDED_EVENT.equals(evt.getPropertyName())) {
            preloadCardImage((Card) newValue);
        }
    }
}
