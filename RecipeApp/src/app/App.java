package app;

import java.util.Stack;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;


// entry point to the application
public class App extends Application {
	
	
	
	@Override
	public void start(Stage primaryStage) {
		// start controller
		// this will manage all the scenes and events
		Controller controller = new Controller(primaryStage);
	}
	
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}

/**
 * Controller class manages all of the data flow from a centralized point.
 * It is responsible for managing multiple scenes that might be
 * active at once but not displayed at the same time.
 * 
 * It handles the event flow from different areas in the application.
 * 
 * @author jlafever
 *
 */
class Controller {
	
	/**
	 * First we determine the user's primary screen height,
	 * then we size our application to a given percentage of that height.
	 */
	double heightPercentage = 0.9;
	Rectangle2D screenBounds = Screen.getPrimary().getBounds();
	double height = screenBounds.getHeight() * heightPercentage;

	// make the application window a square
	double width = height;
	
	// this scene will hold the MainPane (list view of recipes)
	private Scene scene;
	
	// list view of recipes
	private MainPane mainPane;
	
	// this stack is used to manage multiple scenes at once;
	// the one on top will be the one displayed
	private Stack<Scene> scenes = new Stack<Scene>();
	
	// the same stage is used throughout
	private Stage primaryStage;
	
	// single instance of the database manager
	// NOTE: if there are more instances the db file might be locked!
	private RecipeDBManager db;
	
	// constructor starts db, then adds the main pane to it
	public Controller(Stage primaryStage) 
	{
		// connect to database
		db = new RecipeDBManager( "final_db" );
		
		// set stage formatting
		this.primaryStage = primaryStage;
		primaryStage.centerOnScreen();
		primaryStage.setWidth(width);
		primaryStage.setHeight(height);
		primaryStage.setResizable(false);
		
		// instantiate the MainPane that will show the recipes in the database
		mainPane = new MainPane(height, db);
		
		// handle onClick events from MainPane
		mainPane.addEventHandler(AddRecipeEvent.ADD_RECIPE, new AddRecipeHandler());
		mainPane.addEventHandler(ShowRecipeEvent.SHOW_RECIPE, new ShowRecipeHandler());
		
		// instantiate the default scene
		scene = new Scene(mainPane, width, height);
		scenes.push(scene);
		
		// finalize stage formatting
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
			
			/**
			 * note that the db needs to be connected and closed each
			 * time it is accessed in order to keep the db file from 
			 * being locked by another method
			 */
			
			// insert new recipe into database
			db.reconnect();
			db.insert_recipe(recipe.name, recipe.write_up, recipe.prepTime, recipe.cookTime, recipe.totalTime, recipe.yield, recipe.ingredients);
			db.close();
			
			// tell mainPane to refresh list of recipes from database
			db.reconnect();
			mainPane.showNewRecipe();
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

	// nodes managed by this class
	public Button btAddRecipe = new Button("Add Recipe");
	public Button btDeleteRecipe = new Button("Delete Recipe");
	public RecipeListView recipeListView;
	
	
	private StackPane pane = new StackPane();
	
	private double height;
	
	// constructor needs a reference to the db and the height of the parent node
	public MainPane(double height, RecipeDBManager db) {
		this.height = height;
		
		recipeListView = new RecipeListView(height, db);
		
		// use an HBox as a container for the buttons
		HBox hBox = new HBox(15);
		hBox.setPadding(new Insets(15));
		hBox.setStyle("-fx-border-color: black");
		hBox.getChildren().addAll(btAddRecipe, btDeleteRecipe);
		hBox.setAlignment(Pos.CENTER_LEFT);
		setTop(hBox);
		
		// this adds the recipe list view to the StackPane
		setCenterPane();
		
		/**
		 * Set up local event handlers:
		 * 	These will be used to fire events up the dispatch chain
		 */
		btAddRecipe.setOnAction(e -> addRecipe());
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
	
	// deletes recipe after user confirmation
	private void deleteRecipe( RecipeDBManager db ) {
		System.out.println("Delete recipe clicked.");
		db.reconnect();
		try {
			int selected_id = recipeListView.getSelectedRecipeID();
			if ( selected_id > 0 ) {
				// alert user and delete if okay
				Alert alert = new Alert( AlertType.NONE );
				alert.setAlertType( AlertType.CONFIRMATION ); 
				alert.setContentText( "Are you sure you want to delete recipe?" );
				alert.showAndWait().ifPresent( e -> {
				    ButtonType b = alert.getResult();
				    if ( b.getText().equals( "OK" ) ) {
				    	db.delete_recipe( selected_id );
				    	recipeListView.refresh();
						//recipeListView.removeRecipeFromView();
				    }
				});
			}
		}
		catch ( Exception e ){
			System.out.println( e );
		}
		finally {
			db.close();
		}
	}
	
	// triggers list view to refresh list from database
	public void showNewRecipe(  ) {
		System.out.println("main pane new recipe");
		recipeListView.refresh();
	}
}


