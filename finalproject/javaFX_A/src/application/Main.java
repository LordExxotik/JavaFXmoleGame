package application;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Pos;
import javafx.geometry.Insets;


import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.*;
import javafx.collections.*;
import javafx.util.*;
import javafx.stage.*;

import javafx.concurrent.*;
import java.lang.Thread;
public class Main extends Application {
	public static boolean isGameActive = false;
	public static double UserHighScore = 0;
	public static String difficulty = "Easy";
	final static Color COLOR_GRAY = Color.GRAY;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			// Step 1, set background color, add game title, add computer math image.
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root, 375, 225);
			
			
		  //  scene.getStylesheets().add("/application.css"); // Stylesheet!
		    
			// Title
			Label titleLabel = new Label();
			titleLabel.setText("JavaFX Mole Game!");
			titleLabel.setFont(Font.font("Arial", 30));
			titleLabel.setTextFill(Color.WHITE);
			
			root.setTop(titleLabel);
			BorderPane.setAlignment(titleLabel, Pos.CENTER);
			
			// Title Image
			Label titleImageLabel = new Label();
			Image img = new Image("/whackpicture.jpg");
		    ImageView view = new ImageView(img);
		    
		    view.setFitHeight(125);
		    view.setPreserveRatio(true);
		    titleImageLabel.setGraphic(view);
		    
		    // Title Image Positioning
		    BorderPane.setAlignment(view, Pos.TOP_CENTER);
		    root.setCenter(view);
		    
		    // Create List of Options
		    	// Available Options: Play, Exit.
		    
		    // Create Exit Button
		    Button exit_button = new Button();
		    exit_button.setText("Exit!");
		    exit_button.setOnAction(event -> exit());
		    exit_button.setMinWidth(75);
		    exit_button.setAlignment(Pos.CENTER);
		    
		    // Create Play Button
		    Button play_button = new Button();
		    play_button.setText("Play!");
		   // play_button.setOnAction(event -> exit());
		    play_button.setMinWidth(75);
		    play_button.setAlignment(Pos.CENTER);
		    play_button.setOnAction(event -> select_difficulty());
		    
		    
		    // Create and configure ListView
		    ObservableList<Button> listview_buttons = FXCollections.observableArrayList(play_button, exit_button);
		    
		    ListView<Button> optionsList = new ListView<Button>(listview_buttons);
		    optionsList.setStyle("-fx-control-inner-background: gray;"); // Set gray background for ListView
		    
		    optionsList.setCellFactory(new Callback<ListView<Button>, ListCell<Button>>() { // Overwite the ENTIRE cell creation function JUST to center cells in the middle of the ListView.

		        @Override
		        public ListCell<Button> call(ListView<Button> list) {
		            ListCell<Button> cell = new ListCell<Button>() {
		                @Override
		                public void updateItem(Button item, boolean empty) {
		                	
		                    super.updateItem(item, empty);
		                   
		                    
		                    setGraphic(item); // even though it says "Graphic" actually populates the cell with the button (graphic = button)
		                }
		            };
		            cell.setAlignment(Pos.CENTER); // All of this, just for this one line. definitely a java moment.
		            return cell;
		        }
		    });
		    
		    root.setBottom(optionsList);
		    // Set gray background.
			root.setBackground(new Background(new BackgroundFill(COLOR_GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
			
			primaryStage.setScene(scene);
			primaryStage.setTitle("JavaFX Click-A-Mole Game!");
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	public static void exit() {
		if (isGameActive == false) {
			Platform.exit();
		}
	}
	public static String change_difficulty(Label update) {
		// Make a cycling difficulty change function.
		if (difficulty == "Easy") {
			difficulty = "Medium";
		} else if (difficulty == "Medium") {
			difficulty = "Hard";
		} else if (difficulty == "Hard") {
			difficulty = "Expert";
		} else if(difficulty == "Expert") {
			difficulty = "God.";
		} else if (difficulty == "God.") {
			difficulty = "Easy";
		}
		
		update.setText("current difficulty: " + difficulty);
		
		return difficulty;
	}
	
	private static void startGame() {
		try {
			MainGame.startGame(difficulty);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void select_difficulty() {
		// Allow user to select difficulty.
		// Create new scene.
		if (isGameActive == false) { // Don't let users click if the game is active!
			Stage difficulty_Stage = new Stage();
			BorderPane borderpane = new BorderPane();
			Scene newScene = new Scene(borderpane, 300, 200);
			
			Label difficulty_Label = new Label(); // Display Difficulty
			difficulty_Label.setText("current difficulty: " + difficulty);
			difficulty_Label.setFont(Font.font("Arial", 18));
			difficulty_Label.setTextFill(Color.WHITE);
			BorderPane.setAlignment(difficulty_Label, Pos.BOTTOM_CENTER);
			borderpane.setTop(difficulty_Label);
			
			// Now create buttons.
			// Change difficulty.
			Button change_difficulty = new Button();
			change_difficulty.setText("Change difficulty!");
			change_difficulty.setMinWidth(75);
			change_difficulty.setAlignment(Pos.CENTER);
			change_difficulty.setOnAction(event -> change_difficulty(difficulty_Label));
			
			// Create Play Button
			Button play_button = new Button();
			play_button.setText("Play!");
			play_button.setMinWidth(75);
			play_button.setAlignment(Pos.CENTER);
			play_button.setOnAction(event -> startGame());
			
			
			  // Create and configure ListView
		    ObservableList<Button> listview_buttons = FXCollections.observableArrayList(play_button, change_difficulty);
		    
		    ListView<Button> optionsList = new ListView<Button>(listview_buttons);
		    optionsList.setStyle("-fx-control-inner-background: gray;"); // Set gray background for ListView
		    
		    optionsList.setCellFactory(new Callback<ListView<Button>, ListCell<Button>>() { // Overwite the ENTIRE cell creation function JUST to center cells in the middle of the ListView.

		        @Override
		        public ListCell<Button> call(ListView<Button> list) {
		            ListCell<Button> cell = new ListCell<Button>() {
		                @Override
		                public void updateItem(Button item, boolean empty) {
		                	
		                    super.updateItem(item, empty);
		                   
		                    
		                    setGraphic(item); // even though it says "Graphic" actually populates the cell with the button (graphic = button)
		                }
		            };
		            cell.setAlignment(Pos.CENTER); // All of this, just for this one line. definitely a java moment.
		            return cell;
		        }
		    });
		    
		    borderpane.setBottom(optionsList);
		    
			borderpane.setBackground(new Background(new BackgroundFill(COLOR_GRAY, CornerRadii.EMPTY, Insets.EMPTY))); // set color
			difficulty_Stage.setScene(newScene);
			difficulty_Stage.setTitle("Select Difficulty!");
			difficulty_Stage.show();
		} else {
			return;
		}
		
	}
	
}
