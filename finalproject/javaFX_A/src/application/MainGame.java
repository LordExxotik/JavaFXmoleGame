package application;
import javafx.application.Application;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

import javafx.beans.*;
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
import javafx.scene.layout.Pane;
import java.util.Random;
import javafx.scene.input.*;

import java.io.*;
import javafx.concurrent.*;
import java.lang.Thread;

public class MainGame {
	
	private static String game_difficulty;
	public static int points;
	public static int squares_remaining;
	public static int layouts_remaining;
	private static boolean tricks_Enabled = false;
	private static Label userPoints;
	public static Label AIPoints;
	public static Pane root_pane;
	
	private static void setGameStatsBasedOnDifficulty() throws Exception  {
		switch (game_difficulty){ // Set amount of squares per layout, layout amount, tricks enabled, etc.
		
			case "God.": // Use of "fall through" behavior of switch!
				squares_remaining += 25;
				layouts_remaining += 5;
				tricks_Enabled = true;
				
			
			case "Expert":
				squares_remaining += 20;
				layouts_remaining += 4;
				tricks_Enabled = true;
				
			
			case "Hard":
				squares_remaining += 15;
				layouts_remaining += 3;
				tricks_Enabled = false;
				
			
			case "Medium":
				squares_remaining += 12;
				layouts_remaining += 2;
				tricks_Enabled = false;
				
			
			case "Easy":
				squares_remaining += 10;
				layouts_remaining += 1;
				tricks_Enabled = false;
				
		}
	}
	
	private static void bombClicked(Pane root_pane, boolean clickedCorrectly) { // Lose points and subtract squares.
		
		
		if (clickedCorrectly == false) {
		points -= 1;
		} else {
			points += 1;
		}
		
		squares_remaining -= 1;
		
		if (layouts_remaining == 0 && squares_remaining == 0) {
			EndGame();
		}
		
		userPoints.setText("Player Points: " + points);
		// System.out.println(MainGame.class.getClassLoader().getResource(musicFile).toString());
		
		if (squares_remaining == 0 && layouts_remaining > 0) {
			// Generate more!!! this time with bombs! (if applicable);
			int layouts_remaining_save = layouts_remaining - 1;
			try {
				setGameStatsBasedOnDifficulty();
			} catch (Exception e) {
				System.out.println("Invalid difficulty!");
				
				e.printStackTrace();
			} // Set game stats!
			
			layouts_remaining = layouts_remaining_save; // Set layouts remaining back to what it was.
			
			for (int mole_Number = 0; mole_Number < squares_remaining; mole_Number++) { // Possible Bombs this time (with a 10% chance);
				
				Random randomObject = new Random();
				int nextInt = randomObject.nextInt(11); // Doesnt include 11
				
				if (nextInt <= 7 ) { // Make a mole 70% of the time.
					Button mole = new Button();
					mole.setMinWidth(40); // Set button size.
					// Give Button mole image!
					
					Image moleImg = new Image("/mole.png");
					ImageView moleImgView = new ImageView(moleImg);
					moleImgView.setFitHeight(40);
					moleImgView.setPreserveRatio(true);
					mole.setGraphic(moleImgView);
					
					// give button a random position
					int randomPosX = randomObject.nextInt(700); // Number between 0 and 450.
					int randomPosY = randomObject.nextInt(700); // Number between 0 and 450.
					
					mole.setLayoutX(randomPosX); // Position randomly
					mole.setLayoutY(randomPosY); // Position
					
					// Mole button event.
					
					mole.setOnMouseClicked(event -> { // If a mole is right clicked, you lose points! (equivalent of clicking a bomb)
						
						Button moleClicked = (Button) event.getTarget(); // type check for Button.
						MouseButton clickType = event.getButton();
						
						if (clickType == MouseButton.PRIMARY) {
							root_pane.getChildren().remove(moleClicked);
							MoleClicked(root_pane);
						
						} else {
							root_pane.getChildren().remove(moleClicked);
							bombClicked(root_pane, false);
						}
					}); // setOnAction works, but allows Enter to be pressed instead of actually clicking, no cheating! 
					
					root_pane.getChildren().add(mole); // Add button to pane.
				} else {
					// When bombs are created, they have the appearance of a mole but when you hover over them they reveal their TRUE identity...
					Button mole = new Button();
					mole.setMinWidth(40); // Set button size.
					// Give Button mole image!
					
					Image moleImg = new Image("/mole.png");
					ImageView moleImgView = new ImageView(moleImg);
					moleImgView.setFitHeight(40);
					moleImgView.setPreserveRatio(true);
					mole.setGraphic(moleImgView);
					
					// give button a random position
					int randomPosX = randomObject.nextInt(700); // Number between 0 and 450.
					int randomPosY = randomObject.nextInt(700); // Number between 0 and 450.
					
					mole.setLayoutX(randomPosX); // Position randomly
					mole.setLayoutY(randomPosY); // Position
					
					// BOMB button/show event
					
					
					mole.hoverProperty().addListener(observable -> { // Listener for when mole is hovered.
						
						if (mole.hoverProperty().getValue()) { // set img to bomb
							Image bombImg = new Image("/bomb.png");
							ImageView bombImgView = new ImageView(bombImg);
							bombImgView.setFitHeight(40);
							bombImgView.setPreserveRatio(true);
							mole.setGraphic(bombImgView);
						} else { // set back to mole.
							moleImgView.setFitHeight(40);
							moleImgView.setPreserveRatio(true);
							mole.setGraphic(moleImgView);
						}
					});
					
					mole.setOnMouseClicked(event -> { // Right click bombs to get rid of them!
						Button bombClicked = (Button) event.getTarget(); // type check for Button.
						MouseButton clickType = event.getButton();
						
						if (clickType == MouseButton.SECONDARY) {
							root_pane.getChildren().remove(bombClicked);
							bombClicked(root_pane, true);
						
						} else {
							root_pane.getChildren().remove(bombClicked);
							bombClicked(root_pane, false);
						}
					}); // Right click!
					
					root_pane.getChildren().add(mole); // Add button to pane.;;
				}
				
			}
		}
	}
	private static void EndGame() { // Game Over!
		
		Main.isGameActive = false;
		AIUser.AILoop.stop();
		// Display Winner!
		if (points > AIUser.points) {
			Label WinnerLabel = new Label();
			WinnerLabel.setText("Player 1 Wins!!!! ");
			WinnerLabel.setFont(Font.font("Arial", 40));
			WinnerLabel.setTextFill(Color.YELLOW);
			WinnerLabel.setLayoutX(100);
			WinnerLabel.setLayoutY(400);
			root_pane.getChildren().add(WinnerLabel);
		}else if (AIUser.points > points){
			Label WinnerLabel = new Label();
			WinnerLabel.setText("The Computer Wins!!!! ");
			WinnerLabel.setFont(Font.font("Arial", 40));
			WinnerLabel.setTextFill(Color.RED);
			WinnerLabel.setLayoutX(400);
			WinnerLabel.setLayoutY(400);
			root_pane.getChildren().add(WinnerLabel);
		}else {
			Label WinnerLabel = new Label();
			WinnerLabel.setText("It's a tie.");
			WinnerLabel.setFont(Font.font("Arial", 40));
			WinnerLabel.setTextFill(Color.BLUE);
			WinnerLabel.setLayoutX(200);
			WinnerLabel.setLayoutY(400);
		}
	}
	
	public static void AIClicksMole() { // This just ensures that if the AI clicks the last mole, it regenerates (if there are layouts left.)
		squares_remaining -= 1;
		// Perform check for if more moles need to be created.
		
		if (layouts_remaining == 0 && squares_remaining == 0) {
			EndGame();
		}
		if (squares_remaining == 0 && layouts_remaining > 0) {
			// Generate more!!! this time with bombs! (if applicable);
			int layouts_remaining_save = layouts_remaining - 1;
			try {
				setGameStatsBasedOnDifficulty();
			} catch (Exception e) {
				System.out.println("Invalid difficulty!");
				
				e.printStackTrace();
			} // Set game stats!
			
			layouts_remaining = layouts_remaining_save; // Set layouts remaining back to what it was.
			
			for (int mole_Number = 0; mole_Number < squares_remaining; mole_Number++) { // Possible Bombs this time (with a 10% chance);
				
				Random randomObject = new Random();
				int nextInt = randomObject.nextInt(11); // Doesnt include 11
				
				
				if (nextInt <= 7 ) { // Make a mole 70% of the time.
					Button mole = new Button();
					mole.setMinWidth(40); // Set button size.
					// Give Button mole image!
					
					Image moleImg = new Image("/mole.png");
					ImageView moleImgView = new ImageView(moleImg);
					moleImgView.setFitHeight(40);
					moleImgView.setPreserveRatio(true);
					mole.setGraphic(moleImgView);
					
					// give button a random position
					int randomPosX = randomObject.nextInt(700); // Number between 0 and 450.
					int randomPosY = randomObject.nextInt(700); // Number between 0 and 450.
					
					mole.setLayoutX(randomPosX); // Position randomly
					mole.setLayoutY(randomPosY); // Position
					
					// Mole button event.
					
					mole.setOnMouseClicked(event -> {
						Button moleClicked = (Button) event.getTarget(); // type check for Button.
						MouseButton clickType = event.getButton();
						
						if (clickType == MouseButton.PRIMARY) {
							root_pane.getChildren().remove(moleClicked);
							MoleClicked(root_pane);
						
						} else {
							root_pane.getChildren().remove(moleClicked);
							bombClicked(root_pane, false);
						}
					}); // setOnAction works, but allows Enter to be pressed instead of actually clicking, no cheating!
					
					root_pane.getChildren().add(mole); // Add button to pane.
				} else {
					// When bombs are created, they have the appearance of a mole but when you hover over them they reveal their TRUE identity...
					Button mole = new Button();
					mole.setMinWidth(40); // Set button size.
					// Give Button mole image!
					
					Image moleImg = new Image("/mole.png");
					ImageView moleImgView = new ImageView(moleImg);
					moleImgView.setFitHeight(40);
					moleImgView.setPreserveRatio(true);
					mole.setGraphic(moleImgView);
					
					// give button a random position
					int randomPosX = randomObject.nextInt(700); // Number between 0 and 450.
					int randomPosY = randomObject.nextInt(700); // Number between 0 and 450.
					
					mole.setLayoutX(randomPosX); // Position randomly
					mole.setLayoutY(randomPosY); // Position
					
					// BOMB button/show event
					
					
					mole.hoverProperty().addListener(observable -> { // Listener for when mole is hovered.
						
						if (mole.hoverProperty().getValue()) { // set img to bomb
							Image bombImg = new Image("/bomb.png");
							ImageView bombImgView = new ImageView(bombImg);
							bombImgView.setFitHeight(40);
							bombImgView.setPreserveRatio(true);
							mole.setGraphic(bombImgView);
						} else { // set back to mole.
							moleImgView.setFitHeight(40);
							moleImgView.setPreserveRatio(true);
							mole.setGraphic(moleImgView);
						}
					});
					
					mole.setOnMouseClicked(event -> { // Right click bombs to get rid of them!
						Button bombClicked = (Button) event.getTarget(); // type check for Button.
						MouseButton clickType = event.getButton();
						
						if (clickType == MouseButton.SECONDARY) {
							root_pane.getChildren().remove(bombClicked);
							bombClicked(root_pane, true);
						
						} else {
							root_pane.getChildren().remove(bombClicked);
							bombClicked(root_pane, false);
						}
					}); // Right click!
					
					root_pane.getChildren().add(mole); // Add button to pane.
				}
				
			}

		}
	}
	private static void MoleClicked(Pane root_pane) { // Add points
		points += 1;
		squares_remaining -= 1;
		
		
		
		
		userPoints.setText("Player Points: " + points);
		
		if (layouts_remaining == 0 && squares_remaining == 0) {
			EndGame();
		}
		// Perform check for if more moles need to be created.
		if (squares_remaining == 0 && layouts_remaining > 0) {
			// Generate more!!! this time with bombs! (if applicable);
			int layouts_remaining_save = layouts_remaining - 1;
			try {
				setGameStatsBasedOnDifficulty();
			} catch (Exception e) {
				System.out.println("Invalid difficulty!");
				
				e.printStackTrace();
			} // Set game stats!
			
			layouts_remaining = layouts_remaining_save; // Set layouts remaining back to what it was.
			
			for (int mole_Number = 0; mole_Number < squares_remaining; mole_Number++) { // Possible Bombs this time (with a 10% chance);
				
				Random randomObject = new Random();
				int nextInt = randomObject.nextInt(11); // Doesnt include 11
				
				
				if (nextInt <= 7 ) { // Make a mole 70% of the time.
					Button mole = new Button();
					mole.setMinWidth(40); // Set button size.
					// Give Button mole image!
					
					Image moleImg = new Image("/mole.png");
					ImageView moleImgView = new ImageView(moleImg);
					moleImgView.setFitHeight(40);
					moleImgView.setPreserveRatio(true);
					mole.setGraphic(moleImgView);
					
					// give button a random position
					int randomPosX = randomObject.nextInt(700); // Number between 0 and 450.
					int randomPosY = randomObject.nextInt(700); // Number between 0 and 450.
					
					mole.setLayoutX(randomPosX); // Position randomly
					mole.setLayoutY(randomPosY); // Position
					
					// Mole button event.
					
					mole.setOnMouseClicked(event -> {
						Button moleClicked = (Button) event.getTarget(); // type check for Button.
						MouseButton clickType = event.getButton();
						
						if (clickType == MouseButton.PRIMARY) {
							root_pane.getChildren().remove(moleClicked);
							MoleClicked(root_pane);
						
						} else {
							root_pane.getChildren().remove(moleClicked);
							bombClicked(root_pane, false);
						}
					}); // setOnAction works, but allows Enter to be pressed instead of actually clicking, no cheating!
					
					root_pane.getChildren().add(mole); // Add button to pane.
				} else {
					// When bombs are created, they have the appearance of a mole but when you hover over them they reveal their TRUE identity...
					Button mole = new Button();
					mole.setMinWidth(40); // Set button size.
					// Give Button mole image!
					
					Image moleImg = new Image("/mole.png");
					ImageView moleImgView = new ImageView(moleImg);
					moleImgView.setFitHeight(40);
					moleImgView.setPreserveRatio(true);
					mole.setGraphic(moleImgView);
					
					// give button a random position
					int randomPosX = randomObject.nextInt(700); // Number between 0 and 450.
					int randomPosY = randomObject.nextInt(700); // Number between 0 and 450.
					
					mole.setLayoutX(randomPosX); // Position randomly
					mole.setLayoutY(randomPosY); // Position
					
					// BOMB button/show event
					
					
					mole.hoverProperty().addListener(observable -> { // Listener for when mole is hovered.
						
						if (mole.hoverProperty().getValue()) { // set img to bomb
							Image bombImg = new Image("/bomb.png");
							ImageView bombImgView = new ImageView(bombImg);
							bombImgView.setFitHeight(40);
							bombImgView.setPreserveRatio(true);
							mole.setGraphic(bombImgView);
						} else { // set back to mole.
							moleImgView.setFitHeight(40);
							moleImgView.setPreserveRatio(true);
							mole.setGraphic(moleImgView);
						}
					});
					
					mole.setOnMouseClicked(event -> { // Right click bombs to get rid of them!
						Button bombClicked = (Button) event.getTarget(); // type check for Button.
						MouseButton clickType = event.getButton();
						
						if (clickType == MouseButton.SECONDARY) {
							root_pane.getChildren().remove(bombClicked);
							bombClicked(root_pane, true);
						
						} else {
							root_pane.getChildren().remove(bombClicked);
							bombClicked(root_pane, false);
						}
					}); // Right click!
					
					root_pane.getChildren().add(mole); // Add button to pane.
				}
				
			}

		}
	}
	public static void startGame(String difficulty) throws InterruptedException{ // create main game window.
		
		if (Main.isGameActive) {
			System.out.println("Game is already running!");
			return;
		}
		
		points = 0;
		squares_remaining = 0;
		layouts_remaining = 0;
		
		game_difficulty = difficulty;
		Main.isGameActive = true; // let program know game is running!
		
		try {
			setGameStatsBasedOnDifficulty();
		} catch (Exception e) {
			System.out.println("Invalid difficulty!");
			
			e.printStackTrace();
		} // Set game stats!
		
		
		Stage question_Stage = new Stage();
		root_pane = new Pane();
		Scene root_scene = new Scene(root_pane, 800, 800);
		
		
		// Now that we are in the main game, populate the screen with squares! (buttons/moles);
		
		for (int mole_Number = 0; mole_Number < squares_remaining; mole_Number++) { // Possible Bombs this time (with a 10% chance);
			
			Random randomObject = new Random();
			int nextInt = randomObject.nextInt(11); // Doesnt include 11
			
			
			if (nextInt <= 7 ) { // Make a mole 70% of the time.
				Button mole = new Button();
				mole.setMinWidth(40); // Set button size.
				// Give Button mole image!
				
				Image moleImg = new Image("/mole.png");
				ImageView moleImgView = new ImageView(moleImg);
				moleImgView.setFitHeight(40);
				moleImgView.setPreserveRatio(true);
				mole.setGraphic(moleImgView);
				
				// give button a random position
				int randomPosX = randomObject.nextInt(700); // Number between 0 and 450.
				int randomPosY = randomObject.nextInt(700); // Number between 0 and 450.
				
				mole.setLayoutX(randomPosX); // Position randomly
				mole.setLayoutY(randomPosY); // Position
				
				// Mole button event.
				
				mole.setOnMouseClicked(event -> {
					Button moleClicked = (Button) event.getTarget(); // type check for Button.
					MouseButton clickType = event.getButton();
					
					if (clickType == MouseButton.PRIMARY) {
						root_pane.getChildren().remove(moleClicked);
						MoleClicked(root_pane);
					
					} else {
						root_pane.getChildren().remove(moleClicked);
						bombClicked(root_pane, false);
					}
				}); // setOnAction works, but allows Enter to be pressed instead of actually clicking, no cheating!
				
				root_pane.getChildren().add(mole); // Add button to pane.
			} else {
				
				// When bombs are created, they have the appearance of a mole but when you hover over them they reveal their TRUE identity...
				Button mole = new Button();
				mole.setMinWidth(40); // Set button size.
				// Give Button mole image!
				
				Image moleImg = new Image("/mole.png");
				ImageView moleImgView = new ImageView(moleImg);
				moleImgView.setFitHeight(40);
				moleImgView.setPreserveRatio(true);
				mole.setGraphic(moleImgView);
				
				// give button a random position
				int randomPosX = randomObject.nextInt(700); // Number between 0 and 450.
				int randomPosY = randomObject.nextInt(700); // Number between 0 and 450.
				
				mole.setLayoutX(randomPosX); // Position randomly
				mole.setLayoutY(randomPosY); // Position
				
				// BOMB button/show event
				
				
				mole.hoverProperty().addListener(observable -> { // Listener for when mole is hovered.
					
					if (mole.hoverProperty().getValue()) { // set img to bomb
						
						Image bombImg = new Image("/bomb.png");
						ImageView bombImgView = new ImageView(bombImg);
						bombImgView.setFitHeight(40);
						bombImgView.setPreserveRatio(true);
						mole.setGraphic(bombImgView);
					} else { // set back to mole.
						moleImgView.setFitHeight(40);
						moleImgView.setPreserveRatio(true);
						mole.setGraphic(moleImgView);
					}
				});
				
				mole.setOnMouseClicked(event -> { // Right click bombs to get rid of them!
					Button bombClicked = (Button) event.getTarget(); // type check for Button.
					MouseButton clickType = event.getButton();
					
					if (clickType == MouseButton.SECONDARY) {
						root_pane.getChildren().remove(bombClicked);
						bombClicked(root_pane, true);
					
					} else {
						root_pane.getChildren().remove(bombClicked);
						bombClicked(root_pane, false);
					}
				}); // Right click!
				
				root_pane.getChildren().add(mole); // Add button to pane.
			}
		}

		
		
		// Create Points label (keeps track of pointz)
		Label userPointsLabel = new Label();
		userPointsLabel.setText("Player 1 Points: ");
		userPointsLabel.setFont(Font.font("Arial", 18));
		userPointsLabel.setTextFill(Color.WHITE);
		userPointsLabel.setLayoutX(50);
		userPointsLabel.setLayoutY(50);
		root_pane.getChildren().add(userPointsLabel);
		
		userPoints = userPointsLabel;
		
		Label AIpointsLabel = new Label();
		AIpointsLabel.setText("AI Points: ");
		AIpointsLabel.setFont(Font.font("Arial", 18));
		AIpointsLabel.setTextFill(Color.RED);
		AIpointsLabel.setLayoutX(600);
		AIpointsLabel.setLayoutY(50);
		root_pane.getChildren().add(AIpointsLabel);
		
		AIPoints = AIpointsLabel;
		
		
		// Set gray background.
		root_pane.setBackground(new Background(new BackgroundFill(Main.COLOR_GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
			
		question_Stage.setScene(root_scene);
		question_Stage.setTitle("Game");
		question_Stage.show();
		
		// Start AI User (in another thread!!!)
		
		
		AIUser AI = new AIUser(game_difficulty, tricks_Enabled);
		AI.start(); // Run the ai in a new thread.
		
	}
}
