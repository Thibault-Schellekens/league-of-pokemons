module be.esi.prj.leagueofpokemons {
    requires javafx.controls;
    requires javafx.fxml;


    opens be.esi.prj.leagueofpokemons to javafx.fxml;
    exports be.esi.prj.leagueofpokemons;
}