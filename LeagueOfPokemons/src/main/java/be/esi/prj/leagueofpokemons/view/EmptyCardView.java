package be.esi.prj.leagueofpokemons.view;

import be.esi.prj.leagueofpokemons.model.core.Card;
import be.esi.prj.leagueofpokemons.model.core.Type;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;

public class EmptyCardView {

    public static Image create(Card card) {
        Image baseImage = new Image(Objects.requireNonNull(EmptyCardView.class.getResourceAsStream(
                "/be/esi/prj/leagueofpokemons/pics/common/emptyCard.png")));

        Canvas canvas = new Canvas(baseImage.getWidth(), baseImage.getHeight());
        GraphicsContext gc = canvas.getGraphicsContext2D();

        gc.drawImage(baseImage, 0, 0);

        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial", 100));

        double middleX = baseImage.getWidth() - 420;

        gc.fillText(card.getName(), middleX - 100, baseImage.getHeight() - 700);
        gc.fillText(card.getMaxHP() + " HP", middleX - 100, baseImage.getHeight() - 550);
        gc.fillText("Loading...", middleX - 100, baseImage.getHeight() - 140);

        Type type = card.getType();
        String imageUrl = type.name() + "_type.png";
        Image typeIcon = new Image(Objects.requireNonNull(EmptyCardView.class.getResourceAsStream(
                "/be/esi/prj/leagueofpokemons/pics/common/" + imageUrl)));
        gc.drawImage(typeIcon, middleX + 250, baseImage.getHeight() - 650);

        WritableImage result = new WritableImage((int) baseImage.getWidth(), (int) baseImage.getHeight());
        canvas.snapshot(new SnapshotParameters(), result);

        return result;
    }
}
