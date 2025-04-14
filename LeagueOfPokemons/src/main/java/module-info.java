module be.esi.prj.leagueofpokemons {
    requires javafx.controls;
    requires javafx.fxml;
    requires tess4j;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.sdk;
    requires javafx.media;
    requires java.sql;
    requires javafx.swing;
    requires javafx.graphics;

    opens be.esi.prj.leagueofpokemons to javafx.fxml;
    opens be.esi.prj.leagueofpokemons.controller to javafx.fxml;
    exports be.esi.prj.leagueofpokemons;
}


