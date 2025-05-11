package be.esi.prj.leagueofpokemons.animation;

import be.esi.prj.leagueofpokemons.util.SceneManager;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Random;

/**
 * Utility class responsible for animating a leaf curtain scene transition in the League of Pokémons application.
 * <p>
 * The JavaFX leaves have been created by AI.
 */
public class SwapSceneAnimation {
    private static final Random RANDOM = new Random();
    private static final int WIDTH = SceneManager.WIDTH;
    private static final int HEIGHT = SceneManager.HEIGHT;
    private static final int LEAF_COUNT = 300;
    private static final Color[] LEAF_COLORS = {
            // Green
            Color.web("#4CAF50"), Color.web("#388E3C"), Color.web("#2E7D32"), Color.web("#1B5E20"),
            Color.web("#66BB6A"), Color.web("#81C784"), Color.web("#43A047"), Color.web("#558B2F"),
            // Autumn colors
            Color.web("#FFB300"), Color.web("#FFA000"), Color.web("#FF8F00"), // Ambre
            Color.web("#FBC02D"), Color.web("#F9A825"), Color.web("#F57F17")  // Jaunes dorés
    };

    private SwapSceneAnimation() {}

    /**
     * Initiates a curtain-style scene transition using animated leaves.
     *
     * @param scene        the {@link Scene} where the transition will take place
     * @param mainRoot     the current main {@link Pane} containing the old content
     * @param nextRoot     the {@link Pane} for the new scene to transition into
     * @param previousRoot the {@link Node} of the previous content being transitioned out
     * @param speed        the animation speed multiplier
     */
    public static void swapSceneTransition(Scene scene, Pane mainRoot, Pane nextRoot, Node previousRoot, double speed) {
        mainRoot.getChildren().add(previousRoot);

        Group leftCurtain = new Group();
        Group rightCurtain = new Group();
        leftCurtain.setTranslateX(0);
        rightCurtain.setTranslateX(WIDTH);

        createLeaves(leftCurtain, rightCurtain, speed);

        mainRoot.getChildren().addAll(leftCurtain, rightCurtain);
        scene.setRoot(mainRoot);

        playHorizontalCurtainAnimation(scene, leftCurtain, rightCurtain, nextRoot, speed);
    }

    private static void playHorizontalCurtainAnimation(Scene scene, Group leftCurtain, Group rightCurtain, Pane nextRoot, double speed) {
        TranslateTransition leftTransition = createHorizontalTransition(leftCurtain, WIDTH * 0.2, speed);
        TranslateTransition rightTransition = createHorizontalTransition(rightCurtain, -WIDTH * 0.2, speed);

        TranslateTransition downTransitionLeft = createVerticalTransition(leftCurtain, speed);
        TranslateTransition downTransitionRight = createVerticalTransition(rightCurtain, speed);

        setupAnimationChain(scene, leftCurtain, rightCurtain, nextRoot,
                leftTransition, rightTransition,
                downTransitionLeft, downTransitionRight);

        leftTransition.play();
        rightTransition.play();
    }

    private static TranslateTransition createHorizontalTransition(Group curtain, double offsetX, double speed) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(3000 / speed), curtain);
        transition.setByX(offsetX);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.setDelay(Duration.millis(1000 / speed));
        return transition;
    }

    private static TranslateTransition createVerticalTransition(Group curtain, double speed) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(3000 / speed), curtain);
        transition.setByY(HEIGHT + 150);
        transition.setInterpolator(Interpolator.EASE_BOTH);
        transition.setDelay(Duration.seconds(0.25));
        return transition;
    }

    // Could only give leftTransition
    private static void setupAnimationChain(Scene scene, Group leftCurtain, Group rightCurtain,
                                            Pane nextRoot, TranslateTransition leftTransition,
                                            TranslateTransition rightTransition,
                                            TranslateTransition downTransitionLeft,
                                            TranslateTransition downTransitionRight) {
        leftTransition.setOnFinished(event -> {
            downTransitionLeft.play();
            nextRoot.getChildren().addAll(leftCurtain, rightCurtain);
            scene.setRoot(nextRoot);
        });

        rightTransition.setOnFinished(event -> downTransitionRight.play());

        downTransitionLeft.setOnFinished(event -> nextRoot.getChildren().removeAll(leftCurtain, rightCurtain));
    }

    private static void createLeaves(Group leftCurtain, Group rightCurtain, double speed) {

        for (int i = 0; i < LEAF_COUNT; i++) {
            createLeafWithDelay(leftCurtain, rightCurtain, i, speed);
        }
    }

    private static void createLeafWithDelay(Group leftCurtain, Group rightCurtain, int index, double speed) {
        double creationDelay = RANDOM.nextDouble(0, 2 / speed);

        PauseTransition delay = new PauseTransition(Duration.seconds(creationDelay));
        delay.setOnFinished(event -> {
            Group leaf = createAndPositionLeaf();

            if (index % 2 == 0) {
                leftCurtain.getChildren().add(leaf);
            } else {
                rightCurtain.getChildren().add(leaf);
            }

            animateLeafAppearance(leaf);
        });

        delay.play();
    }

    private static Group createAndPositionLeaf() {
        double size = RANDOM.nextDouble() * 80 + 160;
        Color leafColor = LEAF_COLORS[RANDOM.nextInt(LEAF_COLORS.length)];
        Color darkColor = RANDOM.nextBoolean() ?
                Color.web("#5D4037") : leafColor.darker();
        double posX = RANDOM.nextDouble() * WIDTH * 0.6 - WIDTH * 0.3;
        double posY = RANDOM.nextDouble() * HEIGHT;

        Group leaf = createOptimizedLeaf(size, leafColor, darkColor);

        leaf.setTranslateX(posX);
        leaf.setTranslateY(posY);
        leaf.setRotate(RANDOM.nextDouble() * 360);

        leaf.setOpacity(0);
        leaf.setCache(true);

        return leaf;
    }

    private static void animateLeafAppearance(Group leaf) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), leaf);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private static Group createOptimizedLeaf(double size, Color color, Color darkColor) {
        Group leaf = new Group();

        Polygon mainLeaf = createMainLeafShape(size, color, darkColor);
        Rectangle centralVein = createCentralVein(size, darkColor);
        Group veins = createSecondaryVeins(size, darkColor);

        if (RANDOM.nextBoolean()) {
            addHighlight(veins, size);
        }

        addShadowEffect(mainLeaf, size);

        leaf.getChildren().addAll(mainLeaf, centralVein, veins);

        return leaf;
    }

    private static Polygon createMainLeafShape(double size, Color color, Color darkColor) {
        Polygon mainLeaf = new Polygon(
                0, -size * 0.5,
                size * 0.25, -size * 0.25,
                size * 0.3, 0,
                size * 0.25, size * 0.25,
                0, size * 0.5,
                -size * 0.25, size * 0.25,
                -size * 0.3, 0,
                -size * 0.25, -size * 0.25
        );

        mainLeaf.setFill(color);
        mainLeaf.setStroke(darkColor);
        mainLeaf.setStrokeWidth(size * 0.02);

        return mainLeaf;
    }

    private static Rectangle createCentralVein(double size, Color darkColor) {
        Rectangle centralVein = new Rectangle(-size * 0.02, -size * 0.5, size * 0.04, size);
        centralVein.setFill(darkColor);
        centralVein.setOpacity(0.7);
        return centralVein;
    }

    private static Group createSecondaryVeins(double size, Color darkColor) {
        Group veins = new Group();
        int numVeins = 2;

        for (int i = 1; i <= numVeins; i++) {
            double yPos = -size * 0.3 + (i * size * 0.6 / (numVeins + 1));

            Rectangle rightVein = createVein(0, yPos - size * 0.01, size * 0.15, size * 0.03,
                    darkColor, RANDOM.nextDouble() * 10 + 10);

            Rectangle leftVein = createVein(-size * 0.15, yPos - size * 0.01, size * 0.15, size * 0.03,
                    darkColor, -RANDOM.nextDouble() * 10 - 10);

            veins.getChildren().addAll(leftVein, rightVein);
        }

        return veins;
    }

    private static Rectangle createVein(double x, double y, double width, double height,
                                        Color color, double rotation) {
        Rectangle vein = new Rectangle(0, 0, width, height);
        vein.setFill(color);
        vein.setOpacity(0.6);
        vein.setX(x);
        vein.setY(y);
        vein.setRotate(rotation);
        return vein;
    }

    private static void addHighlight(Group veins, double size) {
        double xPos = RANDOM.nextDouble() * size * 0.3 - size * 0.15;
        double yPos = RANDOM.nextDouble() * size * 0.6 - size * 0.3;

        Rectangle highlight = new Rectangle(xPos, yPos, size * 0.05, size * 0.05);
        highlight.setFill(Color.WHITE);
        highlight.setOpacity(0.3);

        veins.getChildren().add(highlight);
    }

    private static void addShadowEffect(Polygon mainLeaf, double size) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.4));
        dropShadow.setOffsetX(size * 0.03);
        dropShadow.setOffsetY(size * 0.03);
        dropShadow.setRadius(3);
        mainLeaf.setEffect(dropShadow);
    }
}