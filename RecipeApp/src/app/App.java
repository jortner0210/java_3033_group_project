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
		/*
		 * 
		// get screen size
		Rectangle2D screenBounds = Screen.getPrimary().getBounds();
		
		// convert window size to specified percentage
		double height = screenBounds.getHeight() * heightPercentage;

		double width = height;
		
		*/
		//double minX = width / 2; //(screenBounds.getWidth() - width) / 2;
		//double minY = height / 2; //(screenBounds.getHeight() - height) / 2;
	
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
		//primaryStage.setX(minX);
		//primaryStage.setY(minY);
		
		/*
		
		primaryStage.centerOnScreen();
		primaryStage.setWidth(width);
		primaryStage.setHeight(height);
		primaryStage.setResizable(false);
		MainPane mainPane = new MainPane();
		mainPane.addEventHandler(AddRecipeEvent.ADD_RECIPE, new AddRecipeHandler());
		mainPane.addEventHandler(ShowRecipeEvent.SHOW_RECIPE, new ShowRecipeHandler());
		
		Scene scene = new Scene(mainPane, width, height);
		primaryStage.setScene(scene);
	
		primaryStage.setTitle("Recipe App");
		primaryStage.show();
		
		*/
		Controller controller = new Controller(primaryStage);
		
		
	}
	/*
	private class MyEventHandler implements EventHandler<MyEvent> {
		@Override
		public void handle(MyEvent event) {
			MainPane main = (MainPane) event.getTarget();
			main.hi();
		}
	}*/
	
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}

class Controller {// get screen size
	double heightPercentage = 0.9;
	Rectangle2D screenBounds = Screen.getPrimary().getBounds();
	
	// convert window size to specified percentage
	double height = screenBounds.getHeight() * heightPercentage;

	double width = height;
	Scene scene;
	
	MainPane mainPane;
	Stack<Scene> scenes = new Stack<Scene>();
	Stage primaryStage;
	RecipeDBManager db;
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
	
	private void closeWindow() {
		scenes.pop();
		primaryStage.setScene(scenes.peek());
	}
	
	private void pushWindow(Pane pane) {
		scenes.push(new Scene(pane, width, height));
		primaryStage.setScene(scenes.peek());
	}
	
	private void addRecipe() {
		System.out.println("add recipe");
	}
	
	
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
	
	private class ShowRecipeHandler implements EventHandler<ShowRecipeEvent>
	{
		@Override
		public void handle(ShowRecipeEvent event) {
			Recipe recipe = (Recipe) event.getRecipe();
			System.out.println("Show Recipe Handler asked to show " + recipe.name);
			showRecipe(recipe);
		}
	}
	
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
		/*
		addEventHandler(
				MyEvent.ADD_RECIPE,
				event);
		
		addEventHandler(
				MyEvent.SHOW_RECIPE,
				event);*/
		
		setCenterPane();
		
		
		btAddRecipe.setOnAction(e -> addRecipe());
		
		btEditRecipe.setOnAction(e -> editRecipe());
		
		btDeleteRecipe.setOnAction(e -> deleteRecipe( db ) );
		
	}
	
	public void setCenterPane() {
		pane.getChildren().clear();
		pane.getChildren().add(recipeListView);
		
		setCenter(pane);
		
	}
	
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
	
	
	public void add() {
		System.out.println("Add recipe clicked.");
	}
	
	
}


