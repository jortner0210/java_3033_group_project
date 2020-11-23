package app;

import java.util.ArrayList;

import app.Events.ShowRecipeEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * This class gets the list of all recipes residing in the database, converts them into RecipeListViewItems,
 * and displays them. It includes functionality for selection, deselection, and the
 * dynamic display of action buttons.
 * 
 * @author jlafever
 *
 */
public class RecipeListView extends BorderPane {
	
	// holds reference to db passed in
	private RecipeDBManager db; 
	
	// holds the recipe objects
	private ArrayList<Recipe> recipes;
	
	// holds a reference to the currently selected recipe, or null
	private RecipeListViewItem selectedRecipe;
	
	// this will hold all the RecipeListViewItems
	private VBox vBox = new VBox();
	
	// both of these are for the Show Recipe button
	private Button showRecipeBtn;
	private HBox buttonBox;
	
	// passed in parameter
	private double height;
	
	// this will be the center
	ScrollPane scrollPane = new ScrollPane();
	
	public RecipeListView(double height, RecipeDBManager db) {
		
		// set up scroll pane
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		
		// set instance variables
		this.db = db;
		this.height = height;
		
		/**
		 *  retrieve list of recipes from db ;
		 *  they are coming in as Recipe objects and
		 *  therefore must be converted into RecipeListViewItems
		 */
		recipes = db.get_all_recipes();
		
		// this VBox manages the RecipeListViewItems and is set as the 
		// displayed node for the scroll pane
		vBox.setMinSize(900,1000);
		vBox.setMaxHeight(height * 0.8);
		vBox.setAlignment(Pos.TOP_CENTER);
		
		// iteratively instantiate list view items from the list of recipes
		for(Recipe recipe : recipes) {
			RecipeListViewItem item = new RecipeListViewItem(recipe);
			item.setPrefHeight(100);
			vBox.getChildren().add(item);
		}
		
		// this event handler manages the currently selected recipe;
		this.addEventHandler(SelectedRecipeEvent.SELECTED_RECIPE, 
				new EventHandler<SelectedRecipeEvent>() {
					@Override
					public void handle(SelectedRecipeEvent event) {
						// if a recipe is selected, and there was already
						// a selected recipe, we need to unselect that one
						// to avoid multiple recipes looking like they
						// are selected
						if ( selectedRecipe != null )
						{
							selectedRecipe.unselect();
						}
						
						// a RecipeListViewItem will fire an Event when clicked
						// we need to determine which one fired the event
						selectedRecipe = (RecipeListViewItem) event.getTarget();
						System.out.println(selectedRecipe.getID() + " selected ");
						
						// only show the buttons if a recipe is selected
						if(selectedRecipe.isSelected()) {
							showRecipeButton();
						}
						else {
							buttonBox = null;
							setBottom(null);
							selectedRecipe = null;
						}
							
					}
			
		});
		
		// final set-up of scroll pane
		scrollPane.setContent(vBox);
		scrollPane.setFitToWidth(true);
		setCenter(scrollPane);
	}
	
	// clears all components and retrieves the updated list
	// from the database
	public void refresh() {
		recipes = db.get_all_recipes();
		vBox.getChildren().clear();
		
		// same process as was in the constructor -> convert Recipe objects to RecipeListViewItem objects
		for(Recipe recipe : recipes) {
			RecipeListViewItem item = new RecipeListViewItem(recipe);
			item.setPrefHeight(100);
			vBox.getChildren().add(item);
		}
		
		// hide buttons
		buttonBox = null;
		setBottom(null);
		
	}
	
	// displays the buttons when a recipe is selected
	private void showRecipeButton() {
		// we only want to do this if the buttons are not currently displayed
		if ( buttonBox == null ) {
			
			System.out.println("show");
			
			// formatting
			buttonBox = new HBox(10);
			buttonBox.setPadding(new Insets(20));
			showRecipeBtn = new Button("View Recipe");
			showRecipeBtn.setFont(Font.font(
					"Abyssinicia SIL", 
					FontWeight.BOLD,
					20));
			
			// add buttons to HBox
			buttonBox.getChildren().add(showRecipeBtn);
			buttonBox.setAlignment(Pos.TOP_RIGHT);
			
			// this will fire an event up the chain that will be handled
			// by the controller
			showRecipeBtn.setOnAction((event) -> {
				System.out.println("button clicked");
				Event showRecipe = new ShowRecipeEvent(selectedRecipe.getRecipe());
				this.fireEvent(showRecipe);
			});
			
			setBottom(buttonBox);
		}
	}
	
	
	// getters
	
	public Recipe getSelectedRecipe() { return selectedRecipe.getRecipe(); }
	
	public int getSelectedRecipeID() { return selectedRecipe.getID(); }
}

/**
 * 
 * This class creates a clickable HBox that displays the recipe name,
 * allows for selection and dynamic changing of background colors
 * 
 * @author jlafever
 *
 */
class RecipeListViewItem extends HBox {
	
	// each item is assigned to one recipe
	private Recipe recipe;
	
	// these will be used internally
	private Text recipeName;
	private int id;
	
	// used for the coloring of the backgrounds
	private static int count = 0;
	
	// default selected status 
	private boolean selected = false;
	
	// potential backgrounds to be used; dependent on selection status
	// and position in list
	private Background lightGreyBackground = new Background(
			new BackgroundFill(
					Color.LIGHTGRAY,
					CornerRadii.EMPTY,
					Insets.EMPTY));
	private Background mediumGreyBackground = new Background(
			new BackgroundFill(
					Color.grayRgb(180),
					CornerRadii.EMPTY,
					Insets.EMPTY));
	private Background darkGreyBackground = new Background(
			new BackgroundFill(
					Color.GRAY,
					CornerRadii.EMPTY,
					Insets.EMPTY));
	private Background whiteBackground = new Background(
			new BackgroundFill(
					Color.WHITE,
					CornerRadii.EMPTY,
					Insets.EMPTY));
			
	// constructor
	public RecipeListViewItem(Recipe recipe) {
		// assign local recipe
		this.recipe = recipe;
		
		// formatting
		this.setAlignment(Pos.CENTER_LEFT);
		this.setPadding(new Insets(0, 0, 0, 15));
		
		// this essentially just sets style sheets,
		// but it uses some logic to determine which styles to apply,
		// and sets the proper background
		setStyle();
		
		// Text to be displayed
		recipeName = new Text(recipe.name);
		recipeName.setFont(
				Font.font(
						"Abyssinicia SIL", 
						FontWeight.BOLD,
						20));
		
		// store our original background
		// this is necessary to revert back to because clicking or hovering on an
		// item will temporarily change its background
		Background originalBackground = this.getBackground();
		
		// on hover, change to medium grey
		this.setOnMouseEntered((e) -> {
			if(!selected)
				this.setBackground(mediumGreyBackground);
		});
		
		// when no longer hovering, change back to either 
		// original background, or if currently selected,
		// change to the background used for selected items
		this.setOnMouseExited((e) -> {
			if(selected) {
				this.setBackground(darkGreyBackground);
			}
			else 
				this.setBackground(originalBackground);	
		});
		
		// manages selection status
		// fires event to RecipeListView to
		// notify it to change its selectedRecipe variable
		this.setOnMouseClicked((e) -> {
			if(selected) {
				unselect();
			}
			if(!selected) {
				setSelected();
				Event recipeSelected = new SelectedRecipeEvent();
				this.fireEvent(recipeSelected);
			}
			
		});
		
		
		id = recipe.id;
		this.getChildren().add(recipeName);
		
		// STATIC VARIABLE; important for background coloring
		count++;
	}
	
	/**
	 * This method calls the javaFX method of the same name
	 * after some modifications to the background have been made
	 */
	public void setStyle() {
		String style = "";
		if ( count % 2 == 0) 
			this.setBackground(lightGreyBackground);
		else
			this.setBackground(whiteBackground);
		
		style += "-fx-border-color: black;";
		style += "-fx-border-style: hidden solid solid solid;";
		
		setStyle(style);
	}
	
	// count incrementer that is used when list is modified
	public void updateCount()
	{
		count++;
		setStyle();
	}
	
	// used when list is modified
	public void resetCount() { count = 0; }
	
	// getters
	
	public boolean isSelected() { return selected; }
	
	public int getID() { return id; }
	
	public Recipe getRecipe() { return recipe; }
	
	/** MANAGE SELECTION STATE **/
	
	// resets style to original background 
	public void unselect() { 
		selected = false;
		setStyle();
	}
	
	private void setSelected() {
		System.out.println(id + "selected item");
		selected = true;
		this.setBackground(darkGreyBackground);
	}
	
	
}

// EventType that will be fired upon selection to result in buttons being displayed
class SelectedRecipeEvent extends Event {
	public static final EventType<SelectedRecipeEvent> SELECTED_RECIPE =
			new EventType<>(Event.ANY, "SELECTED_RECIPE");
	
	public SelectedRecipeEvent() {
		super(SELECTED_RECIPE);
	}
}




















