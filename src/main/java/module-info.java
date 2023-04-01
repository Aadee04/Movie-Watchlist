module search.moviewatchlist {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens search.moviewatchlist to javafx.fxml;
    exports search.moviewatchlist;
}