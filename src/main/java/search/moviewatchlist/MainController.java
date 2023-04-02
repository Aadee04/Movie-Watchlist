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
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;

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

    //For database validation
    @FXML
    private Label leftblankerror;


    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private TextField email;

    @FXML
    private TextField confirmpassword;

    //For sign in
    @FXML
    private TextField userid;

    @FXML
    private TextField passid;

    @FXML
    private Label validationlabel;

    int flag = 0;


    //sign in button click function
    @FXML
    public void signIn(ActionEvent event) {

        if ((userid.getText().isBlank() == false) || (passid.getText().isBlank() == false)){
            validateLogin();
            if (flag == 1){
                stackMain.getChildren().clear();
                stackMain.getChildren().add(searchResultPane);
            }
            else {
                validationlabel.setText("Enter correct details!");
            }
        }
        else {
            validationlabel.setText("Enter username and password!");
        }
    }


    //for validating sign in
    public void validateLogin(){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM user_info WHERE Userdb = '" + userid.getText() + "' AND Passdb = '" + passid.getText() + "'";

        try{
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()) {
                if (queryResult.getInt(1) == 1){
                    flag = 1;
                }
                else {
                    break;
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }



    //sign up button click function
    @FXML
    public void showSearchResult(ActionEvent event) {

        if ((username.getText().isBlank() == false) || (password.getText().isBlank() == false) || (email.getText().isBlank() == false) || (confirmpassword.getText().isBlank() == false)){

                storeData();
                stackMain.getChildren().clear();
                stackMain.getChildren().add(searchResultPane);
        }
        else {
            leftblankerror.setText("Enter username and password!");
        }
    }


    //For storing data when sign up is clicked
    public void storeData() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String userName = username.getText();
        String passWord = password.getText();

        String insertData = "INSERT INTO user_info (Userdb, Passdb) " +
                "VALUES ('" + userName + "', '" + passWord + "')";

        try {
            Statement statement = connectDB.createStatement();
            int rowsInserted = statement.executeUpdate(insertData);

            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully!");
            } else {
                System.out.println("Failed to insert data");
            }

            statement.close();
            connectDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



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
