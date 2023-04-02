package search.moviewatchlist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class MainController {
    @FXML
    private BorderPane loginPane;

    @FXML
    private Button loginSignupButton;

    @FXML
    private TextField resultsSearch;

    @FXML
    private Button resultsSearchButton;

    @FXML
    private SplitPane searchResultPane;

    @FXML
    private Button signUpButton;

    @FXML
    private BorderPane signUpPane;

    @FXML
    private Button signinButton;

    @FXML
    private StackPane stackMain;

    @FXML
    private ListView<String> watchlist;

    @FXML
    private ListView<String> movieList;

    @FXML
    public void showSignUp(ActionEvent event) {
        stackMain.getChildren().clear();
        stackMain.getChildren().add(signUpPane);
    }

    @FXML
    public void showLogin(ActionEvent event) {
        stackMain.getChildren().clear();
        stackMain.getChildren().add(loginPane);
    }

    @FXML
    public void showSearchResult(ActionEvent event) {
        stackMain.getChildren().clear();
        stackMain.getChildren().add(searchResultPane);
    }
}