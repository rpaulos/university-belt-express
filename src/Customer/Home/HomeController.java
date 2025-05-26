package Customer.Home;

import java.io.IOException;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.image.Image;

public class HomeController {

    @FXML
    private Button btn_Search;

    @FXML
    private Button btn_account;

    @FXML
    private Button btn_grocery;

    @FXML
    private GridPane cardGrid;


    private Stage stage;
    private Scene scene; 
    private Parent root;

    @FXML
    public void toProfilePageHandler(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Profile.fxml"));

        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    // Change this so that it dynamically gets restaurant details from the database
    private List<FoodItem> foodList = List.of(
        new FoodItem("Taco", "150", "Taco Bell"),
        new FoodItem("Fried Chicken", "180", "Chowking"),
        new FoodItem("Nuggets", "110", "McDonald's"),
        new FoodItem("Spaghetti", "130", "Jollibee"),
        new FoodItem("Dimsum", "140", "North Park"),
        new FoodItem("Ramen", "290", "Ramen Nagi"),
        new FoodItem("Burger", "125", "Wendy's"),
        new FoodItem("Pizza", "270", "Greenwich"),
        new FoodItem("Ice Cream", "80", "Selecta"),
        new FoodItem("Hotdog", "90", "Minute Burger"),
        new FoodItem("Takoyaki", "100", "Ohayo"),
        new FoodItem("Donut", "75", "Dunkin'"),
        new FoodItem("Milk Tea", "120", "Macao Imperial"),
        new FoodItem("Fries", "60", "Potato Corner"),
        new FoodItem("Sundae", "70", "McDonald's"),
        new FoodItem("Siomai", "55", "Siomai House"),
        new FoodItem("Pasta", "160", "Amici"),
        new FoodItem("Rice Bowl", "150", "Teriyaki Boy"),
        new FoodItem("Curry", "200", "Coco Ichibanya"),
        new FoodItem("Pancakes", "130", "Pancake House"),
        new FoodItem("BBQ Skewers", "110", "Mang Inasal"),
        new FoodItem("Burger Steak", "145", "Jollibee"),
        new FoodItem("Tempura", "190", "Tokyo Tokyo"),
        new FoodItem("Shawarma", "100", "Turks")
    );


    public void initialize() {
        int columns = 1;
        int col = 0;
        int row = 0;

        try {
            // For every
            for (FoodItem food : foodList) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Card.fxml"));
                AnchorPane card = fxmlLoader.load();

                CardController controller = fxmlLoader.getController();
                controller.setData(food.getName(), food.getPrice(), food.getRestaurant());

                cardGrid.add(card, col, row);
                col++;
                if (col == columns) {
                    col = 0;
                    row++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

