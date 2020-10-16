package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
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
		double width = 500; //screenBounds.getWidth() * widthPercentage;
		double height = 500; //screenBounds.getHeight() * heightPercentage;
		double minX = width / 2; //(screenBounds.getWidth() - width) / 2;
		double minY = height / 2; //(screenBounds.getHeight() - height) / 2;
		/*
		// pane to hold everything for now
		BorderPane pane = new BorderPane();
		
		// buttons
		Button btAddRecipe = new Button("Add Recipe");
		Button btDeleteRecipe = new Button("Delete Recipe");
		Button btEditRecipe = new Button("Edit Recipe");
		HBox hBox = new HBox(15);
		hBox.getChildren().addAll(btAddRecipe, btDeleteRecipe, btEditRecipe);
		hBox.setAlignment(Pos.CENTER);
		pane.setCenter(hBox);
		
		btAddRecipe.setOnAction(e -> System.out.println("Add recipe button clicked."));
		btDeleteRecipe.setOnAction(e -> System.out.println("Delete recipe button clicked."));
		btEditRecipe.setOnAction(e -> System.out.println("Edit recipe button clicked."));
		
		// create label
		Label helloWorld = new Label("Welcome to the recipe app.");
		helloWorld.setAlignment(Pos.CENTER);
		helloWorld.setTranslateY(50);
		helloWorld.setMaxWidth(width);
		helloWorld.setFont(new Font("Airal", 30));
		pane.setTop(helloWorld);
		
		
		// create scene
		Scene scene = new Scene(pane, width, height);

		// set stage 
		primaryStage.setScene(scene);*/
		
		/*
		AdminPane admin = new AdminPane();
		Scene scene = new Scene(admin, width, height);*/
		
		/*
		UserPane user = new UserPane();
		Scene scene = new Scene(user, width, height);
		
		primaryStage.setScene(scene);*/
		primaryStage.setX(minX);
		primaryStage.setY(minY);
		primaryStage.setWidth(width);
		primaryStage.setHeight(height);
		
		primaryStage.setTitle("Recipe App");
		primaryStage.show();
		
		Stage popupWindow = new Stage();
		
		popupWindow.centerOnScreen();
		popupWindow.setWidth(300);
		popupWindow.setHeight(300);
		
		Label label = new Label("Admin or User?");
		Button btAdmin = new Button("Admin");
		Button btUser = new Button("User");
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPadding(new Insets(15));
		flowPane.setHgap(5);
		flowPane.setVgap(5);
		flowPane.getChildren().addAll(label, btAdmin, btUser);
		
		Scene questionScene = new Scene(flowPane, 300, 300);
		
		popupWindow.setScene(questionScene);
		popupWindow.show();
		
		btAdmin.setOnAction(e -> {
			popupWindow.close();
			AdminPane admin = new AdminPane();
			Scene scene = new Scene(admin, width, height);
			primaryStage.setScene(scene);
		});
		
		btUser.setOnAction(e -> {
			popupWindow.close();
			UserPane user = new UserPane();
			Scene scene = new Scene(user, width, height);
			primaryStage.setScene(scene);
		});
		
		
		
	}
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}


