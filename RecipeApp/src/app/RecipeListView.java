package app;

import java.util.ArrayList;

import app.Events.ShowRecipeEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class RecipeListView extends BorderPane {
	private RecipeDBManager db; //= new RecipeDBManager( "test_db" );
	private ArrayList<Recipe> recipes;
	private RecipeListViewItem selectedRecipe;
	private VBox vBox = new VBox();
	private Button showRecipeBtn;
	private HBox buttonBox;
	private double height;
	
	public RecipeListView(double height, RecipeDBManager db) {
		this.db = db;
		this.height = height;
		recipes = db.get_all_recipes();
		vBox.setStyle("-fx-border-color: green;");
		vBox.setMaxHeight(height * 0.8);
		vBox.setAlignment(Pos.TOP_CENTER);
		for(Recipe recipe : recipes) {
			RecipeListViewItem item = new RecipeListViewItem(recipe);
			item.setPrefHeight(100);
			vBox.getChildren().add(item);
		}
		this.addEventHandler(SelectedRecipeEvent.SELECTED_RECIPE, 
				new EventHandler<SelectedRecipeEvent>() {
					@Override
					public void handle(SelectedRecipeEvent event) {
						if ( selectedRecipe != null )
						{
							selectedRecipe.unselect();
						}
						
						selectedRecipe = (RecipeListViewItem) event.getTarget();
						System.out.println(selectedRecipe.getID() + " selected ");
						showRecipeButton();
						
					}
			
		});
		
		
		db.close();
		
		setTop(vBox);
	}
	
	private void showRecipeButton() {
		if ( buttonBox == null ) {
			System.out.println("show");
			buttonBox = new HBox(10);
			buttonBox.setPadding(new Insets(20));
			showRecipeBtn = new Button("View Recipe");
			showRecipeBtn.setFont(Font.font(
					"Abyssinicia SIL", 
					FontWeight.BOLD,
					20));
			buttonBox.getChildren().add(showRecipeBtn);
			buttonBox.setAlignment(Pos.TOP_RIGHT);
			
			showRecipeBtn.setOnAction((event) -> {
				System.out.println("button clicked");
				Event showRecipe = new ShowRecipeEvent(selectedRecipe.getRecipe());
				this.fireEvent(showRecipe);
			});
			
			setCenter(buttonBox);
		}
	}
	
	public Recipe getSelectedRecipe() { return selectedRecipe.getRecipe(); }
}


class RecipeListViewItem extends HBox {
	
	private Recipe recipe;
	private Text recipeName;
	private int id;
	private static int count = 0;
	private boolean selected = false;
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
			
	
	public RecipeListViewItem(Recipe recipe) {
		this.recipe = recipe;
		this.setAlignment(Pos.CENTER_LEFT);
		this.setPadding(new Insets(0, 0, 0, 15));
		
		setStyle();
		
		recipeName = new Text(recipe.name);
		recipeName.setFont(
				Font.font(
						"Abyssinicia SIL", 
						FontWeight.BOLD,
						20));
		Background originalBackground = this.getBackground();
		this.setOnMouseEntered(e -> this.setBackground(mediumGreyBackground));
		this.setOnMouseExited((e) -> {
			if(selected) {
				this.setBackground(darkGreyBackground);
			}
			else 
				this.setBackground(originalBackground);	
		});
		
		this.setOnMouseClicked((e) -> {
			setSelected();
			Event recipeSelected = new SelectedRecipeEvent();
			this.fireEvent(recipeSelected);
		});
		
		
		id = recipe.id;
		this.getChildren().add(recipeName);
		count++;
	}
	
	
	public void setStyle() {
		String style = "";
		if ( count % 2 == 0) 
			this.setBackground(lightGreyBackground);
		else
			this.setBackground(whiteBackground);
			//style += "-fx-background-color: #D3D3D3;";
		
		style += "-fx-border-color: black;";
		style += "-fx-border-style: hidden solid solid solid;";
		//else
			//style += "-fx-background-color: green;";
		
		
		setStyle(style);
	}
	
	public int getID() { return id; }
	
	public Recipe getRecipe() { return recipe; }
	
	public void unselect() { 
		selected = false;
		setStyle();
	}
	
	private void setSelected() {
		selected = true;
		this.setBackground(darkGreyBackground);
	}
	
	
}

class SelectedRecipeEvent extends Event {
	public static final EventType<SelectedRecipeEvent> SELECTED_RECIPE =
			new EventType<>(Event.ANY, "SELECTED_RECIPE");
	
	public SelectedRecipeEvent() {
		super(SELECTED_RECIPE);
	}
}




















