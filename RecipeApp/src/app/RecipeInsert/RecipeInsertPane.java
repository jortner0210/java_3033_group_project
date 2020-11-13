package app.RecipeInsert;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.*;

import app.AdminPane;
import app.Ingredient;
import app.IngredientListView;
import app.Recipe;
import app.RecipeDBManager;
import app.Events.AddIngredientEvent;
import app.Events.CloseEvent;
import app.Events.DeleteIngredientEvent;
import app.Events.EditIngredientEvent;
import app.Events.InsertRecipeToDatabaseEvent;

public class RecipeInsertPane extends GridPane {
	
	// holds the ingredient insert pane 
	private IngredientInsert ingredientInsert;
	
	// displays the ingredients added
	IngredientListView ingredientsListView = new IngredientListView(800);
	
	// contains the ingredients that will be added to the db
	public ArrayList<Ingredient> ingredients_to_add = new ArrayList<>();
	
	
	
	public RecipeInsertPane() {
		
		// set up some initial formatting
		setPadding( new Insets( 20 ) );
		setMinSize( 500, 500);
		setVgap( 20 );
		setHgap( 20 );
		setAlignment(Pos.TOP_CENTER);
		
		
		// DELETE INGREDIENT EVENT
		ingredientsListView.addEventHandler(DeleteIngredientEvent.DELETE_INGREDIENT, new EventHandler<DeleteIngredientEvent>() {

			@Override
			public void handle(DeleteIngredientEvent event) {
				Ingredient ingredientToDelete = event.getIngredient();
				deleteIngredient(ingredientToDelete);
			}
			
		});
		
		// EDIT INGREDIENT EVENT
		ingredientsListView.addEventHandler(EditIngredientEvent.EDIT_INGREDIENT, new EventHandler<EditIngredientEvent>() {
			@Override
			public void handle(EditIngredientEvent event) {
				Ingredient ingredientToEdit = event.getIngredient();
				editIngredient(ingredientToEdit);
			}
		});
		
		// set up the widths of the columns
		ColumnConstraints col0 = new ColumnConstraints(100);
		ColumnConstraints col1 = new ColumnConstraints(100);
		ColumnConstraints col2 = new ColumnConstraints(600);
		getColumnConstraints().addAll(col0, col1, col2);
		
		// I used this alot so I saved it. This way if we decide to change it, it's easy
		String fontFamily = "Abyssinicia SIL";
		
		// recipe name
		Text title = new Text( "Recipe Name" );
		title.setFont(Font.font(fontFamily,FontWeight.MEDIUM, 25));
		GridPane.setHalignment(title, HPos.CENTER);
		
		// TextField for the recipe name
		TextField title_text = new TextField();
		title_text.setFont(Font.font(fontFamily, 20));
		title_text.setPrefColumnCount( 10 );
		title_text.setMaxWidth(600);
		title_text.setPadding(new Insets(15));
		
		GridPane.setHalignment(title_text, HPos.CENTER);
		GridPane.setMargin(title_text, new Insets(0, 0, 40, 0));
		
		RecipeInfo infoBox = new RecipeInfo();
		
		// label for ingredients
		Text ingredients = new Text( "Ingredients");
		ingredients.setFont(Font.font(fontFamily, FontWeight.MEDIUM, 20));
		ingredients.setTextAlignment(TextAlignment.CENTER);
		
		// add ingredient button
		Button add_ingredient = new Button("+");
		add_ingredient.setFont(Font.font(fontFamily, FontWeight.BOLD, 15));
		add_ingredient.setAlignment(Pos.CENTER_RIGHT);
		GridPane.setHalignment(add_ingredient, HPos.RIGHT);
		GridPane.setMargin(add_ingredient, new Insets(0,20,0,0));
		
		add_ingredient.setOnAction( e -> addIngredient() );
		
		// instructions label
		Text write_up = new Text( "Instructions" );
		write_up.setFont(Font.font(fontFamily, FontWeight.MEDIUM, 20));
		
		// instructs text area
		TextArea write_up_text = new TextArea();
		write_up_text.setPrefColumnCount( 10 );
		write_up_text.setPrefHeight(600);
		// done and cancel buttons
		Button done_button = new Button("Submit");
		Button cancel_button = new Button("Cancel");
		
		// put the buttons in a flow pane for easier formatting
		FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL, 10.0, 10.0);
		flowPane.getChildren().addAll(cancel_button, done_button);
		flowPane.setAlignment(Pos.CENTER_RIGHT);
		GridPane.setHalignment(flowPane, HPos.RIGHT);
		
		// actions for these buttons
		
		// FIRE RECIPE INSERT EVENT
		done_button.setOnAction((e) -> {
			System.out.println( "recipe done" );
			
            Recipe recipe = new Recipe( -1, title_text.getText(), 
            							write_up_text.getText(), 
            							infoBox.getPrepTime(), 
            							infoBox.getCookTime(), 
            							infoBox.getTotalTime(), 
            							infoBox.getYield(),
            							getIngredients() );
            System.out.println( recipe );
            Event insertRecipe = new InsertRecipeToDatabaseEvent(recipe);
            this.fireEvent(insertRecipe);
            
		});
		
		// FIRE CLOSE EVENT
		cancel_button.setOnAction((e)-> {
			Event event = new CloseEvent();
			fireEvent(event);
		});
		
		
		
		// col, row, col span, row span
		add( title,    		 	0, 0, 3, 1 );
		add( title_text,		0, 1, 3, 1 );
		add( infoBox,			0, 2, 3, 1 );
		add( ingredients,	 	0, 3, 1, 1 );	
		add( add_ingredient, 	1, 3, 1, 1 );
		add( ingredientsListView, 	0, 4, 2, 1 );
		add( write_up, 		 	2, 3, 1, 1 );
		add( write_up_text, 	2, 4, 1, 1 );
		add( flowPane,			2, 5, 1, 1 );
		
	}
	
	public ArrayList<Ingredient> getIngredients() {
		return ingredients_to_add;
	}
	
	// displays ingredient insert window, fires event when closed
	public void addIngredient() {
		IngredientInsert ins = new IngredientInsert();
		ins.show();
		
		ins.setOnHiding((event) -> {
			System.out.println("hidden");
			Ingredient ingredient = ins.getIngredient();
			if(ingredient != null) {
				ingredientsListView.addIngredient(ingredient);
				ingredients_to_add.add(ingredient);
			}
		});
		
	}
	
	// opens the ingredient insert pane but with an existing ingredient
	public void editIngredient(Ingredient editIngredient) {
		IngredientInsert ins = new IngredientInsert(editIngredient);
		ins.show();
		
		ins.setOnHiding((event) -> {
			System.out.println("hidden");
			Ingredient ingredient = ins.getIngredient();
			if(ingredient != null) {
				if(ins.getIngredientChanged()) {
					ingredientsListView.updateIngredient(ingredient);
					ingredients_to_add.add(ingredient);
				}
				
			}
		});
	}
	
	// deletes ingredient from ingredients being passed to db
	public void deleteIngredient(Ingredient deleteIngredient)
	{
		ingredients_to_add.remove(deleteIngredient);
	}
	
	class RecipeInfo extends GridPane {
		
		private String prepTime;
		private Label prepLabel = new Label("Prep Time:");
		private TextField prepText = new TextField();
		
		private String cookTime;
		private Label cookLabel = new Label("Cook Time:");
		private TextField cookText = new TextField();
		
		private String totalTime;
		private Label totalLabel = new Label("Total Time:");
		private TextField totalText = new TextField();
		
		private String yield;
		private Label yieldLabel = new Label("Yield:");
		private TextField yieldText = new TextField();
		private String fontFamily = "Abyssinicia SIL";
		public RecipeInfo() {
			

			prepLabel.setFont(Font.font(fontFamily,FontWeight.MEDIUM, 15));
			prepText.setFont(Font.font(fontFamily,FontWeight.MEDIUM, 15));
			cookLabel.setFont(Font.font(fontFamily,FontWeight.MEDIUM, 15));
			cookText.setFont(Font.font(fontFamily,FontWeight.MEDIUM, 15));
			totalLabel.setFont(Font.font(fontFamily,FontWeight.MEDIUM, 15));
			totalText.setFont(Font.font(fontFamily,FontWeight.MEDIUM, 15));
			yieldLabel.setFont(Font.font(fontFamily,FontWeight.MEDIUM, 15));
			yieldText.setFont(Font.font(fontFamily,FontWeight.MEDIUM, 15));
			
			this.setAlignment(Pos.CENTER);
			this.setVgap(15);
			this.setHgap(15);
			
			add( prepLabel, 	0, 0, 1, 1 );
			add( prepText,  	1, 0, 1, 1 );
			add( cookLabel, 	2, 0, 1, 1 );
			add( cookText,  	3, 0, 1, 1 );
			add( totalLabel,	0, 1, 1, 1 );
			add( totalText, 	1, 1, 1, 1 );
			add( yieldLabel,	2, 1, 1, 1 );
			add( yieldText,     3, 1, 1, 1 );
			
		}
		
		public String getPrepTime() { return prepText.getText(); }
		public String getCookTime() { return cookText.getText(); }
		public String getTotalTime() { return totalText.getText(); }
		public String getYield() { return yieldText.getText(); }
	}
}


/*package app.RecipeInsert;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.*;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;

import java.util.*;

import app.AdminPane;
import app.Ingredient;
import app.IngredientListView;
import app.Recipe;
import app.RecipeDBManager;
import app.Events.AddIngredientEvent;
import app.Events.CloseEvent;
import app.Events.DeleteIngredientEvent;
import app.Events.EditIngredientEvent;
import app.Events.InsertRecipeToDatabaseEvent;

public class RecipeInsertPane extends GridPane {
	
	// holds the ingredient insert pane 
	private IngredientInsert ingredientInsert;
	
	// displays the ingredients added
	IngredientListView ingredientsListView = new IngredientListView(800);
	
	// contains the ingredients that will be added to the db
	public ArrayList<Ingredient> ingredients_to_add = new ArrayList<>();
	
	
	
	public RecipeInsertPane() {
		
		// set up some initial formatting
		setPadding( new Insets( 20 ) );
		setMinSize( 500, 500);
		setVgap( 20 );
		setHgap( 20 );
		setAlignment(Pos.TOP_CENTER);
		
		
		// DELETE INGREDIENT EVENT
		ingredientsListView.addEventHandler(DeleteIngredientEvent.DELETE_INGREDIENT, new EventHandler<DeleteIngredientEvent>() {

			@Override
			public void handle(DeleteIngredientEvent event) {
				Ingredient ingredientToDelete = event.getIngredient();
				deleteIngredient(ingredientToDelete);
			}
			
		});
		
		// EDIT INGREDIENT EVENT
		ingredientsListView.addEventHandler(EditIngredientEvent.EDIT_INGREDIENT, new EventHandler<EditIngredientEvent>() {
			@Override
			public void handle(EditIngredientEvent event) {
				Ingredient ingredientToEdit = event.getIngredient();
				editIngredient(ingredientToEdit);
			}
		});
		
		// set up the widths of the columns
		ColumnConstraints col0 = new ColumnConstraints(100);
		ColumnConstraints col1 = new ColumnConstraints(100);
		ColumnConstraints col2 = new ColumnConstraints(600);
		getColumnConstraints().addAll(col0, col1, col2);
		
		// I used this alot so I saved it. This way if we decide to change it, it's easy
		String fontFamily = "Abyssinicia SIL";
		
		// recipe name
		Text title = new Text( "Recipe Name" );
		title.setFont(Font.font(fontFamily,FontWeight.MEDIUM, 25));
		GridPane.setHalignment(title, HPos.CENTER);
		
		// TextField for the recipe name
		TextField title_text = new TextField();
		title_text.setFont(Font.font(fontFamily, 20));
		title_text.setPrefColumnCount( 10 );
		title_text.setMaxWidth(600);
		title_text.setPadding(new Insets(15));
		
		GridPane.setHalignment(title_text, HPos.CENTER);
		GridPane.setMargin(title_text, new Insets(0, 0, 40, 0));
		
		// label for ingredients
		Text ingredients = new Text( "Ingredients");
		ingredients.setFont(Font.font(fontFamily, FontWeight.MEDIUM, 20));
		ingredients.setTextAlignment(TextAlignment.CENTER);
		
		// add ingredient button
		Button add_ingredient = new Button("+");
		add_ingredient.setFont(Font.font(fontFamily, FontWeight.BOLD, 15));
		add_ingredient.setAlignment(Pos.CENTER_RIGHT);
		GridPane.setHalignment(add_ingredient, HPos.RIGHT);
		GridPane.setMargin(add_ingredient, new Insets(0,20,0,0));
		
		add_ingredient.setOnAction( e -> addIngredient() );
		
		// instructions label
		Text write_up = new Text( "Instructions" );
		write_up.setFont(Font.font(fontFamily, FontWeight.MEDIUM, 20));
		
		// instructs text area
		TextArea write_up_text = new TextArea();
		write_up_text.setPrefColumnCount( 10 );
		write_up_text.setPrefHeight(600);
		// done and cancel buttons
		Button done_button = new Button("Submit");
		Button cancel_button = new Button("Cancel");
		
		// put the buttons in a flow pane for easier formatting
		FlowPane flowPane = new FlowPane(Orientation.HORIZONTAL, 10.0, 10.0);
		flowPane.getChildren().addAll(cancel_button, done_button);
		flowPane.setAlignment(Pos.CENTER_RIGHT);
		GridPane.setHalignment(flowPane, HPos.RIGHT);
		
		// actions for these buttons
		
		// FIRE RECIPE INSERT EVENT
		done_button.setOnAction((e) -> {
			System.out.println( "recipe done" );
            Recipe recipe = new Recipe( -1, title_text.getText(), 
            							write_up_text.getText(), 
            							"prep", "cook", "total_time", "yield",
            							getIngredients() );
            System.out.println( recipe );
            Event insertRecipe = new InsertRecipeToDatabaseEvent(recipe);
            this.fireEvent(insertRecipe);
            
		});
		
		// FIRE CLOSE EVENT
		cancel_button.setOnAction((e)-> {
			Event event = new CloseEvent();
			fireEvent(event);
		});
		
		
		
		// col, row, col span, row span
		add( title,    		 	0, 0, 3, 1 );
		add( title_text,		0, 1, 3, 1 );
		add( ingredients,	 	0, 2, 1, 1 );	
		add( add_ingredient, 	1, 2, 1, 1 );
		add( ingredientsListView, 	0, 3, 2, 1 );
		add( write_up, 		 	2, 2, 1, 1 );
		add( write_up_text, 	2, 3, 1, 1 );
		add( flowPane,			2, 4, 1, 1 );
		
	}
	
	public ArrayList<Ingredient> getIngredients() {
		return ingredients_to_add;
	}
	
	// displays ingredient insert window, fires event when closed
	public void addIngredient() {
		IngredientInsert ins = new IngredientInsert();
		ins.show();
		
		ins.setOnHiding((event) -> {
			System.out.println("hidden");
			Ingredient ingredient = ins.getIngredient();
			if(ingredient != null) {
				ingredientsListView.addIngredient(ingredient);
				ingredients_to_add.add(ingredient);
			}
		});
		
	}
	
	// opens the ingredient insert pane but with an existing ingredient
	public void editIngredient(Ingredient editIngredient) {
		IngredientInsert ins = new IngredientInsert(editIngredient);
		ins.show();
		
		ins.setOnHiding((event) -> {
			System.out.println("hidden");
			Ingredient ingredient = ins.getIngredient();
			if(ingredient != null) {
				if(ins.getIngredientChanged()) {
					ingredientsListView.updateIngredient(ingredient);
					ingredients_to_add.add(ingredient);
				}
				
			}
		});
	}
	
	// deletes ingredient from ingredients being passed to db
	public void deleteIngredient(Ingredient deleteIngredient)
	{
		ingredients_to_add.remove(deleteIngredient);
	}
	
}

*/



