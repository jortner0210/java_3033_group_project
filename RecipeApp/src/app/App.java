package app;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class App extends Application {
	
	private final double heightPercentage = 0.8;
	private final double widthPercentage = 0.8;
	
	
	@Override
	public void start(Stage primaryStage) {
		// get screen size
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		
		// convert window size to specified percentage
		double width = screenBounds.getWidth() * widthPercentage;
		double height = screenBounds.getHeight() * heightPercentage;
		double minX = (screenBounds.getWidth() - width) / 2;
		double minY = (screenBounds.getHeight() - height) / 2;
		
		// create label
		Label helloWorld = new Label("Welcome to the recipe app.");
		helloWorld.setAlignment(Pos.CENTER);
		helloWorld.setMaxWidth(width);
		helloWorld.setFont(new Font("Airal", 30));
		
		// create scene
		Scene scene = new Scene(helloWorld, width, height);
		
		// set stage 
		primaryStage.setScene(scene);
		primaryStage.setX(minX);
		primaryStage.setY(minY);
		primaryStage.setWidth(width);
		primaryStage.setHeight(height);
		
		primaryStage.setTitle("Recipe App");
		primaryStage.show();
		
	}
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
