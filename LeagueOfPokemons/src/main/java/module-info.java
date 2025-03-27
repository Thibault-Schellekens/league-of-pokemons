module be.esi.prj.leagueofpokemons {
    requires javafx.controls;
    requires javafx.fxml;
    requires tess4j;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens be.esi.prj.leagueofpokemons to javafx.fxml;
    opens be.esi.prj.leagueofpokemons.controller to javafx.fxml;
    exports be.esi.prj.leagueofpokemons;
}