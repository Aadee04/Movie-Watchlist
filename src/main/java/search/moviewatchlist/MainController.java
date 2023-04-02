package search.moviewatchlist;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

//API
import java.io.IOException;
import java.net.*;
import java.util.*;

//JSON PARSING
import javafx.scene.layout.VBox;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
    private GridPane searchResultPane;

    @FXML
    private Button signUpButton;

    @FXML
    private BorderPane signUpPane;

    @FXML
    private Button signinButton;

    @FXML
    private StackPane stackMain;

    @FXML
    private VBox movieList = null;


    private final String search_API_link = "https://api.themoviedb.org/3/search/multi?api_key=60861577c310df46ea9a16c2bcd51716&language=en-US&query=search_query&page=1&include_adult=false";
    private final String poster_API_link = "http://image.tmdb.org/t/p/w92/";

    @FXML
    public void showSignUp(ActionEvent event) {
        stackMain.getChildren().clear();
        stackMain.getChildren().add(signUpPane);
    }

    @FXML
    public void initialize() {
        stackMain.getChildren().clear();
        stackMain.getChildren().add(loginPane);
    }

    @FXML
    public void showSearchResult(ActionEvent event) {
        stackMain.getChildren().clear();
        stackMain.getChildren().add(searchResultPane);
    }

    public void testing() throws IOException, ParseException {
        String searchBarText = (resultsSearch.getText()).trim();

        if( searchBarText.isEmpty() ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Entry");
            alert.setHeaderText ("Search for Something!");
            alert.setContentText ("Enter a Movie Title or Television Show Name in the search box");
            alert.showAndWait();
            return;
        }

        searchBarText = searchBarText.replace(" ", "%20");

        String search_link = search_API_link.replace("search_query", searchBarText);

        URL url = new URL(search_link);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            System.out.println("Error");
        }
        else {
            StringBuilder informationString = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());

            while (scanner.hasNext()) {
                informationString.append(scanner.nextLine());
            }
            //Close the scanner
            scanner.close();

            String results = String.valueOf(informationString);

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(results);
            JSONArray media_array = (JSONArray) jsonObject.get("results");

            int s = media_array.size();

            if (s == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Entry");
                alert.setHeaderText("Nothing Found!");
                alert.setContentText("No Movie Titles or Television Shows found!");
                alert.showAndWait();
            }
            else {
                List<String> titles = new ArrayList<>(); //contains name
                List<String> overviews = new ArrayList<>(); //contains overview
                List<String> types = new ArrayList<>(); //contains media type - tv or movie
                List<String> posters = new ArrayList<>(); //contains poster links
                List<Long> ids = new ArrayList<>(); //contains imdb id of the media item
                List<Double> rating = new ArrayList<>(); //contains media item rating

                Node[] movieItems = new Node[s];

                movieList.getChildren().clear();
                for (int i = 0; i < s; i++) {
                    JSONObject media_object = (JSONObject) media_array.get(i);
                    if (media_object.get("media_type").equals("tv") || media_object.get("media_type").equals("movie")) {



                        types.add((String) media_object.get("media_type"));

                        if (Objects.equals((String) media_object.get("media_type"), "tv")) {
                            titles.add((String) media_object.get("name"));
                        } else {
                            titles.add((String) media_object.get("title"));
                        }


                        overviews.add((String) media_object.get("overview"));
                        posters.add(poster_API_link + (String) media_object.get("poster_path"));
                        ids.add((long) media_object.get("id"));
                        rating.add((double) (media_object.get("vote_average")));


                        FXMLLoader loader = new FXMLLoader(getClass().getResource("movieListItem.fxml"));
                        movieItems[i] = loader.load();
                        movieItemController controller = loader.getController();
                        controller.setMovieInfo(titles.get(i), overviews.get(i), rating.get(i), posters.get(i), ids.get(i));
                        if(i%2 == 1)
                            movieItems[i].setStyle("-fx-background-color: #1565C0");

                        movieList.getChildren().add(movieItems[i]);
                    }
                }


            }
        }
    }
}
