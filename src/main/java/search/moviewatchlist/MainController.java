package search.moviewatchlist;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
//API
import java.io.IOException;
import java.net.*;
import java.util.*;
//JSON PARSING
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

    private String search_API_link = "https://api.themoviedb.org/3/search/multi?api_key=60861577c310df46ea9a16c2bcd51716&language=en-US&query=search_query&page=1&include_adult=false";
    private String poster_API_link = "http://image.tmdb.org/t/p/w92/";
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

    public void testing() throws IOException, ParseException {
        String searchBarText = (resultsSearch.getText()).trim();



        searchBarText = searchBarText.replace(" ","%20");

        String search_link = search_API_link.replace("search_query",searchBarText);

        URL url = new URL(search_link);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            System.out.println("Error");
        } else {
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

            long s = (long) jsonObject.get("total_results");

            if(s == 0){
                System.out.println("Nothing Found");
            }

            else{
                List <String> titles = new ArrayList<String>(); //contains name
                List <String> overviews = new ArrayList<String>(); //contains overview
                List <String> types = new ArrayList<String>(); //contains media type - tv or movie
                List <String> posters = new ArrayList<String>(); //contains poster links
                List <Long> ids = new ArrayList<Long>(); //contains tmdb id of the media item
                List <Double> rating = new ArrayList<Double>(); //contains media item rating

                for(int i = 0 ; i < s ; i++){
                    JSONObject media_object = (JSONObject) media_array.get(i);
                    types.add((String) media_object.get("media_type"));

                    if(Objects.equals((String) media_object.get("media_type"), "tv")){
                        titles.add((String) media_object.get("name"));
                    } else {
                        titles.add((String) media_object.get("title"));
                    }

                    overviews.add((String) media_object.get("overview"));
                    posters.add(poster_API_link + (String) media_object.get("poster_path"));
                    ids.add((long) media_object.get("id"));
                    rating.add((double) media_object.get("vote_average"));
                }
            }


        }
    }
}