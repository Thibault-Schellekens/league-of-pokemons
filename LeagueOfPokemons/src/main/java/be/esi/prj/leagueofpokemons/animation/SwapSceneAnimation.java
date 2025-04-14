package be.esi.prj.leagueofpokemons.animation;

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

// todo: get WIDTH/HEIGHT FROM A GLOBAL SCOPE
public class SwapSceneAnimation {
    private static final int WIDTH = 1061;
    private static final int HEIGHT = 660;
    private static final int LEAF_COUNT = 300;
    private static final Color[] LEAF_COLORS = {
            // Green
            Color.web("#4CAF50"), Color.web("#388E3C"), Color.web("#2E7D32"), Color.web("#1B5E20"),
            Color.web("#66BB6A"), Color.web("#81C784"), Color.web("#43A047"), Color.web("#558B2F"),
            // Autumn colors
//            Color.web("#FFA000"), Color.web("#FF8F00"), Color.web("#FF6F00"), // Oranges
//            Color.web("#F57F17"), Color.web("#F57C00"), Color.web("#EF6C00"), // Orange foncé
            Color.web("#FFB300"), Color.web("#FFA000"), Color.web("#FF8F00"), // Ambre
//            Color.web("#A1887F"), Color.web("#8D6E63"), Color.web("#795548"), // Bruns
//            Color.web("#FF5722"), Color.web("#E64A19"), Color.web("#D84315"), // Orange-rouge
            Color.web("#FBC02D"), Color.web("#F9A825"), Color.web("#F57F17")  // Jaunes dorés
    };

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

        downTransitionLeft.setOnFinished(e -> {
            nextRoot.getChildren().removeAll(leftCurtain, rightCurtain);
        });
    }

    private static void createLeaves(Group leftCurtain, Group rightCurtain, double speed) {
        Random random = new Random();

        for (int i = 0; i < LEAF_COUNT; i++) {
            createLeafWithDelay(leftCurtain, rightCurtain, i, random, speed);
        }
    }

    private static void createLeafWithDelay(Group leftCurtain, Group rightCurtain, int index, Random random, double speed) {
        double creationDelay = random.nextDouble(0, 2 / speed);

        PauseTransition delay = new PauseTransition(Duration.seconds(creationDelay));
        delay.setOnFinished(event -> {
            Group leaf = createAndPositionLeaf(random);

            if (index % 2 == 0) {
                leftCurtain.getChildren().add(leaf);
            } else {
                rightCurtain.getChildren().add(leaf);
            }

            animateLeafAppearance(leaf);
        });

        delay.play();
    }

    private static Group createAndPositionLeaf(Random random) {
        double size = random.nextDouble() * 80 + 160;
        Color leafColor = LEAF_COLORS[random.nextInt(LEAF_COLORS.length)];
        Color darkColor = random.nextBoolean() ?
                Color.web("#5D4037") : leafColor.darker();
        double posX = random.nextDouble() * WIDTH * 0.6 - WIDTH * 0.3;
        double posY = random.nextDouble() * HEIGHT;

        Group leaf = createOptimizedLeaf(size, leafColor, darkColor, random);

        leaf.setTranslateX(posX);
        leaf.setTranslateY(posY);
        leaf.setRotate(random.nextDouble() * 360);

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

    private static Group createOptimizedLeaf(double size, Color color, Color darkColor, Random random) {
        Group leaf = new Group();

        Polygon mainLeaf = createMainLeafShape(size, color, darkColor);
        Rectangle centralVein = createCentralVein(size, darkColor);
        Group veins = createSecondaryVeins(size, darkColor, random);

        if (random.nextBoolean()) {
            addHighlight(veins, size, random);
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

    private static Group createSecondaryVeins(double size, Color darkColor, Random random) {
        Group veins = new Group();
        int numVeins = 2;

        for (int i = 1; i <= numVeins; i++) {
            double yPos = -size * 0.3 + (i * size * 0.6 / (numVeins + 1));

            Rectangle rightVein = createVein(0, yPos - size * 0.01, size * 0.15, size * 0.03,
                    darkColor, random.nextDouble() * 10 + 10);

            Rectangle leftVein = createVein(-size * 0.15, yPos - size * 0.01, size * 0.15, size * 0.03,
                    darkColor, -random.nextDouble() * 10 - 10);

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

    private static void addHighlight(Group veins, double size, Random random) {
        double xPos = random.nextDouble() * size * 0.3 - size * 0.15;
        double yPos = random.nextDouble() * size * 0.6 - size * 0.3;

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