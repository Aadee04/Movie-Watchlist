package search.moviewatchlist;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.text.DecimalFormat;

public class movieItemController {

    @FXML
    private Button movieAddButton;

    @FXML
    private Label movieDesc;

    @FXML
    private ImageView movieImage;

    @FXML
    private Label movieRating;

    @FXML
    private Label movieTitle;
    long ID;
    private final File file = new File("src/main/resources/images/no-movie-poster.jpeg");
    private final Image image = new Image(file.toURI().toString());

    public void setMovieInfo(String Title, String Desc, double Rating, String imageURL, long ID)
    {
//        System.out.println("Title = " + Title + ", Desc = " + Desc + ", Rating = " + Rating + ", image = " + imageURL + ", ID = " + ID);

        movieTitle.setText(Title);
        movieDesc.setText(Desc);

        DecimalFormat df = new DecimalFormat("#.#");

        movieRating.setText("Rating: " + (df.format(Rating).equals("0") ? "None" : (df.format(Rating)) + "/10.0"));

        if(imageURL.endsWith("null"))
            movieImage.setImage(this.image);
        else
            movieImage.setImage(new Image(imageURL));
        this.ID = ID;
    }
}
