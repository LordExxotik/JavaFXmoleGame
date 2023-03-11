package application;
import java.util.Random;
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
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;

public class AIUser extends Thread { // AI User
	// Responsible for clicking squares, performing tricks, and trying to beat you!
	// May occasionally add squares to the game.

	static private double squareClickTimeDelay;
	static public int points; // AI wants to keep track of his own points!
	static public Timeline AILoop;
	
	public AIUser(String game_difficulty, boolean performTricks) throws InterruptedException { // Utilizes sleep, so it may be interrupted.
		
		
		switch (game_difficulty){ // Set amount of squares per layout, layout amount, tricks enabled, etc.
		
		case "God.": 
			squareClickTimeDelay = Math.random() + .25; // 
			break;
		
		case "Expert":
			squareClickTimeDelay = Math.random() + .5; // 
			break;
		
		case "Hard":
			squareClickTimeDelay = 1; // 
			break;
			
		
		case "Medium":
			squareClickTimeDelay = 2; // 
			break;
			
		
		case "Easy":
			squareClickTimeDelay = 3; // Convert to long.
			break;
			
		}
		// The AI user will now answer at this interval for the duration of the game.
		System.out.println(squareClickTimeDelay);
		
		Timeline timeLine = new Timeline(new KeyFrame(Duration.seconds((double) squareClickTimeDelay), // Computer plays the game!
			    e -> {
			    	if (MainGame.squares_remaining > 0) {
						Button selected = null;
						
						// If there is a square remaining, click it and remove it (remove it) and add ai points.
						ObservableList<?> children = MainGame.root_pane.getChildren();
						for (int x = 0; x < children.size(); x++) {
							java.lang.Object randomObjectInList = children.get(x);
							if (randomObjectInList instanceof Button) { // checks if this object is a button.
								selected = (Button) randomObjectInList; // cast to button (even tho it is a button xd java moment)
								points += 1;
								MainGame.AIClicksMole();
								MainGame.root_pane.getChildren().remove(x);
								MainGame.AIPoints.setText("AI Points: " + points);
								break;
							}
						}
					}
			    	
			    }   
		));
		
		AIUser.AILoop = timeLine; // MainGame will end this loop when the game is over.
		timeLine.setCycleCount(Timeline.INDEFINITE);
		timeLine.play();
			
		
		
		
		
	}
	
}
