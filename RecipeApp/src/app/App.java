package app;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Stack;

//import org.sqlite.core.DB;

import app.Events.AddRecipeEvent;
import app.Events.CloseEvent;
import app.Events.CloseRecipeEvent;
import app.Events.InsertRecipeToDatabaseEvent;
import app.Events.ShowRecipeEvent;
import app.RecipeInsert.RecipeInsertPane;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;



public class App extends Application {
	
	private final double heightPercentage = 0.9;
	private final double widthPercentage = 0.8;
	
	
	@Override
	public void start(Stage primaryStage) {
		// start controller
		Controller controller = new Controller(primaryStage);
	}
	
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}

// manages the screens
class Controller {
	
	double heightPercentage = 0.9;
	
	// get screen size
	Rectangle2D screenBounds = Screen.getPrimary().getBounds();
	
	// convert window size to specified percentage
	double height = screenBounds.getHeight() * heightPercentage;

	double width = height;
	Scene scene;
	
	MainPane mainPane;
	Stack<Scene> scenes = new Stack<Scene>();
	Stage primaryStage;
	RecipeDBManager db;
	
	// constructor starts db, then adds the main pane to it
	public Controller(Stage primaryStage) 
	{
		db = new RecipeDBManager( "test_db" );
		this.primaryStage = primaryStage;
		primaryStage.centerOnScreen();
		primaryStage.setWidth(width);
		primaryStage.setHeight(height);
		primaryStage.setResizable(false);
		mainPane = new MainPane(height, db);
		mainPane.addEventHandler(AddRecipeEvent.ADD_RECIPE, new AddRecipeHandler());
		mainPane.addEventHandler(ShowRecipeEvent.SHOW_RECIPE, new ShowRecipeHandler());
		
		scene = new Scene(mainPane, width, height);
		scenes.push(scene);
		primaryStage.setScene(scene);
	
		primaryStage.setTitle("Recipe App");
		primaryStage.show();
	}
	
	// closes a scene and resets it
	private void closeWindow() {
		scenes.pop();
		primaryStage.setScene(scenes.peek());
	}
	
	// opens a new scene
	private void pushWindow(Pane pane) {
		scenes.push(new Scene(pane, width, height));
		primaryStage.setScene(scenes.peek());
	}
	
	// handler for ADD_RECIPE event; tells it to change to the recipe insert pane
	private class AddRecipeHandler implements EventHandler<AddRecipeEvent>
	{
		@Override
		public void handle(AddRecipeEvent event) {
			RecipeInsertPane insertPane = new RecipeInsertPane();
			insertPane.addEventHandler(CloseEvent.CLOSE, new EventHandler<CloseEvent>() {
				@Override
				public void handle(CloseEvent arg0) {
					System.out.println("app got close");
					closeWindow();
				}
				
			});
			insertPane.addEventHandler(InsertRecipeToDatabaseEvent.INSERT_RECIPE, new InsertRecipeHandler());
			pushWindow(insertPane);
		}
	}
	
	// handler for a INSERT_RECIPE event; actually inserts the recipe into database
	private class InsertRecipeHandler implements EventHandler<InsertRecipeToDatabaseEvent>
	{
		@Override
		public void handle(InsertRecipeToDatabaseEvent event) {
			System.out.println("main insertrecipehandler");
			Recipe recipe = (Recipe) event.getRecipe();
			db.reconnect();
			db.insert_recipe(recipe.name, recipe.write_up, recipe.prepTime, recipe.cookTime, recipe.totalTime, recipe.yield, recipe.ingredients);
			db.close();
			closeWindow();
		}
	}
	
	// handler for SHOW_RECIPE event
	private class ShowRecipeHandler implements EventHandler<ShowRecipeEvent>
	{
		@Override
		public void handle(ShowRecipeEvent event) {
			Recipe recipe = (Recipe) event.getRecipe();
			System.out.println("Show Recipe Handler asked to show " + recipe.name);
			showRecipe(recipe);
		}
	}
	
	// method to show the recipe view pane
	private void showRecipe(Recipe recipe) {
		System.out.println("show recipe");
		RecipeViewPane viewPane = new RecipeViewPane(recipe);
		viewPane.addEventHandler(CloseRecipeEvent.CLOSE_RECIPE, new EventHandler<CloseRecipeEvent>() {

			@Override
			public void handle(CloseRecipeEvent arg0) {
				closeWindow();
			}
			
		});
		
		scenes.push(new Scene(viewPane, width, height));
		primaryStage.setScene(scenes.peek());
	}
}

// this holds the recipe list view and the buttons
class MainPane extends BorderPane {

	public Button btAddRecipe = new Button("Add Recipe");
	public Button btDeleteRecipe = new Button("Delete Recipe");
	public Button btEditRecipe = new Button("Edit Recipe");
	public RecipeListView recipeListView;
	
	StackPane pane = new StackPane();
	
	private double height;
	public MainPane(double height, RecipeDBManager db) {
		this.height = height;
		recipeListView = new RecipeListView(height, db);
		
		HBox hBox = new HBox(15);
		hBox.setPadding(new Insets(15));
		hBox.setStyle("-fx-border-color: black");
		hBox.getChildren().addAll(btAddRecipe, btDeleteRecipe, btEditRecipe);
		hBox.setAlignment(Pos.CENTER_LEFT);
		setTop(hBox);
		
		setCenterPane();
		
		
		btAddRecipe.setOnAction(e -> addRecipe());
		
		btEditRecipe.setOnAction(e -> editRecipe());
		
		btDeleteRecipe.setOnAction(e -> deleteRecipe( db ) );
		
	}
	
	// adds recipe list view
	public void setCenterPane() {
		pane.getChildren().clear();
		pane.getChildren().add(recipeListView);
		
		setCenter(pane);
		
	}
	
	// tells controller to switch scenes
	private void addRecipe() {
		Event addRecipeEvent = new AddRecipeEvent();
		this.fireEvent(addRecipeEvent);
	}
	
	private void deleteRecipe( RecipeDBManager db ) {
		System.out.println("Delete recipe clicked.");
		try {
			int selected_id = recipeListView.getSelectedRecipeID();
			if ( selected_id > 0 ) {
				System.out.println( "id is fine - " + selected_id );
				db.delete_recipe( selected_id );
			}
		}
		catch ( Exception e ){
			System.out.println( e );
		}
	}
	
	private void editRecipe() {
		System.out.println("Edit recipe clicked.");
	}
	
	
}


