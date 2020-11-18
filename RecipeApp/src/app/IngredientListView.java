package app;

import java.util.ArrayList;

//import javax.script.Bindings;

import app.Events.DeleteIngredientEvent;
import app.Events.EditIngredientEvent;
import app.Events.ShowRecipeEvent;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

public class IngredientListView extends BorderPane {
	
	private ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
	private ObservableList<Node> observableList = FXCollections.<Node>observableArrayList();
	private ArrayList<IngredientListViewItem> ingredientItems = new ArrayList<IngredientListViewItem>();
	private IngredientListViewItem selectedIngredient;
	private VBox vBox = new VBox();
	private Button editBtn;
	private Button deleteBtn;
	private HBox buttonBox;
	private double height;
	
	public IngredientListView(double height) {
		javafx.beans.binding.Bindings.bindContentBidirectional(observableList, vBox.getChildren());
		this.height = height;
		//vBox.setStyle("-fx-border-style: solid hidden hidden hidden;");
		vBox.setMaxHeight(height * 0.8);
		vBox.setAlignment(Pos.TOP_CENTER);
		
		
		
		for(Ingredient ingredient : ingredients) {
			IngredientListViewItem item = new IngredientListViewItem(ingredient);
			ingredientItems.add(item);
			item.setPrefHeight(50);
			vBox.getChildren().add(item);
		}
		this.addEventHandler(SelectedIngredientEvent.SELECTED_INGREDIENT, 
				new EventHandler<SelectedIngredientEvent>() {
					@Override
					public void handle(SelectedIngredientEvent event) {
						if ( selectedIngredient != null )
						{
							selectedIngredient.unselect();
						}
						
						selectedIngredient = (IngredientListViewItem) event.getTarget();
						System.out.println(selectedIngredient.getIngredient().name + " selected ");
						showButtons();
						
					}
			
		});
		
		
			
		
		setTop(vBox);
	}
	
	public void addIngredient(Ingredient ingredient) {
		vBox.setStyle("-fx-border-style: solid hidden hidden hidden;");
		ingredients.add(ingredient);
		IngredientListViewItem item = new IngredientListViewItem(ingredient);
		observableList.add(item);
		/*
		item.ingredientText.textProperty().addListener((obs, oldVal, newVal) -> {
			if(oldVal != newVal) {
				System.out.println("bruh");
			}
		});*/
		/*
		item.ingredientText.textProperty().addListener(ChangeListener<Text>() {
			@Override
			public void changed(ObservableValue<? extends Text> observable,
					Text oldValue, Text newValue) {
				
				System.out.println("bruhhh");;
			}
		});*/
		//ingredientItems.add(item);
		item.setPrefHeight(50);
		//vBox.getChildren().add(item);
	}
	
	public void updateIngredient(Ingredient ingredient) {
		IngredientListViewItem toDelete = null;
		
		for(Node node: observableList) {
			IngredientListViewItem item = (IngredientListViewItem) node;
			if(item.getIngredient() == ingredient) {
				//observableList.remove(item);
				//deleteIngredient(item);
				//addIngredient(ingredient);
				toDelete = item;
				//observableList.add(item);
			}
		}
		if(toDelete != null) {
			deleteIngredient(toDelete);
			addIngredient(ingredient);
			resetStyles();
		}
		
		
		/*
		IngredientListViewItem itemToUpdate = findIngredientItem(ingredient);
		if(itemToUpdate != null) {
			itemToUpdate.setIngredientText(ingredient);
			vBox.getChildren().set(0, itemToUpdate);
		}*/
	}
	
	private IngredientListViewItem findIngredientItem(Ingredient ingredient) {
		IngredientListViewItem foundItem = null;
		for( IngredientListViewItem item: ingredientItems ) {
			if(item.getIngredient() == ingredient) {
				foundItem = item;
			}
		}
		return foundItem;
	}
	
	public void deleteIngredient(IngredientListViewItem ingredientItem) {
		ingredientItem.resetCount();
		vBox.getChildren().remove(ingredientItem);
		if(observableList.size() == 0) {
			vBox.setStyle("-fx-border-style: hidden;");
		}
		
		resetStyles();
		buttonBox = null;
		setCenter(null);
	}
	
	private void resetStyles() {
		for(Node node : observableList) {
			IngredientListViewItem item = (IngredientListViewItem) node;
			item.updateCount();
		}
	}
	
	private void showButtons() {
		if ( buttonBox == null ) {
			System.out.println("show");
			buttonBox = new HBox(10);
			buttonBox.setPadding(new Insets(20));
			
			deleteBtn = new Button("Delete");
			deleteBtn.setFont(Font.font(
					"Abyssinicia SIL", 
					FontWeight.BOLD,
					15));
			buttonBox.getChildren().add(deleteBtn);
			
			editBtn = new Button("Edit");
			editBtn.setFont(Font.font(
					"Abyssinicia SIL", 
					FontWeight.BOLD,
					15));
			buttonBox.getChildren().add(editBtn);
			buttonBox.setAlignment(Pos.TOP_RIGHT);
			
		
			editBtn.setOnAction((event) -> {
				Event editIngredient = new EditIngredientEvent(selectedIngredient.getIngredient());
				fireEvent(editIngredient);
			});
			
			deleteBtn.setOnAction((event) -> {
				deleteIngredient(selectedIngredient);
				Event deleteIngredient = new DeleteIngredientEvent(selectedIngredient.getIngredient());
				fireEvent(deleteIngredient);
			});
			
			setCenter(buttonBox);
		}
	}
	
	public Ingredient getSelectedIngredient() { return selectedIngredient.getIngredient(); }
}


class IngredientListViewItem extends HBox {
	
	private Ingredient ingredient;
	public Text ingredientText;
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
	
	public IngredientListViewItem() {

		setStyle();
		count++;
	}
	
	public void resetCount() { count = 0; }
			
	
	public IngredientListViewItem(Ingredient ingredient) {
		this.ingredient = ingredient;
		this.setAlignment(Pos.CENTER_LEFT);
		this.setPadding(new Insets(10));
		
		setStyle();
		
		setIngredientText(this.ingredient);
		
		
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
			Event ingredientSelected = new SelectedIngredientEvent();
			this.fireEvent(ingredientSelected);
		});
		
		
		
		
		this.getChildren().add(ingredientText);
		count++;
	}
	
	public void setIngredientText(Ingredient ingredient) {
		
		String ingredientString = "\u2022 ";
    	if(ingredient.qty == Math.ceil(ingredient.qty))
    		ingredientString += (int) ingredient.qty + " ";
    	else
    		ingredientString += ingredient.qty + " ";

		if(ingredient.metric != null)
			ingredientString += ingredient.metric;
    	ingredientString += " " + ingredient.name;
		ingredientText = new Text(ingredientString);
		ingredientText.setFont(
				Font.font(
						"Abyssinicia SIL", 
						FontWeight.MEDIUM,
						15));
		ingredientText.setWrappingWidth(150);
		System.out.println("Ingredient  changed to " + ingredientString);
		this.ingredient = ingredient;
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
	
	public void updateCount()
	{
		System.out.println("ingredientlistviewitem " + ingredientText.getText() + " count " + count);
		count++;
		setStyle();
	}
	
	
	public Ingredient getIngredient() { return ingredient; }
	
	
	public void unselect() { 
		selected = false;
		setStyle();
	}
	
	private void setSelected() {
		selected = true;
		this.setBackground(darkGreyBackground);
	}
	
	
}

class SelectedIngredientEvent extends Event {
	public static final EventType<SelectedIngredientEvent> SELECTED_INGREDIENT =
			new EventType<>(Event.ANY, "SELECTED_INGREDIENT");
	
	public SelectedIngredientEvent() {
		super(SELECTED_INGREDIENT);
	}
}