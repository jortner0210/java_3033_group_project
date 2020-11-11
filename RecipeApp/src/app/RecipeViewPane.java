package app;

import java.util.ArrayList;
import java.util.List;

import app.Events.CloseRecipeEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class RecipeViewPane extends BorderPane {
	
	// this will be the center
	ScrollPane scrollPane = new ScrollPane();
	
	// this holds all the main content
	VBox mainPane = new VBox();
	
	// connect to db
	RecipeDBManager db = new RecipeDBManager( "test_db" );
	Recipe recipe;
	
	// all the ingredients used in the recipe
	ArrayList<Text> ingredientsTexts = new ArrayList<Text>();
	
	double height = 100;
	double width = height;
	
	// I used this alot so I saved it. This way if we decide to change it, it's easy
	String fontFamily = "Abyssinicia SIL";
	
	public RecipeViewPane(Recipe recipe ) {
		
		// some set up
		scrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		mainPane.setMinSize(900,1000);
		mainPane.setAlignment(Pos.TOP_CENTER);

	
		// get recipe
		this.recipe = recipe;
		
		// convert ingredients into strings in the right format and then into Texts
		for ( Ingredient ingredient : recipe.ingredients ) {
			
			String ingredientString = "";
        	if(ingredient.qty == Math.ceil(ingredient.qty))
        		ingredientString += (int) ingredient.qty + " ";
        	else
        		ingredientString += ingredient.qty + " ";
			
			if(ingredient.metric != null)
				ingredientString += ingredient.metric + " ";
			ingredientString += ingredient.name;
			ingredientsTexts.add(new Text(ingredientString));
				
		}
		
		// recipe name node
		RecipeNameView recipeName = new RecipeNameView();
		
		// recipe info node
		RecipeInfoView infoView = new RecipeInfoView();
		infoView.maxWidthProperty().bind(widthProperty().multiply(0.75));
		
		// bottom pane contains directions and ingredients
		BottomPane bottomPane = new BottomPane();
		mainPane.setMargin(bottomPane, new Insets(50,0,0,0));
		mainPane.getChildren().addAll(recipeName, infoView, bottomPane);
		
		scrollPane.setContent(mainPane);
		scrollPane.setFitToWidth(true);
		setCenter(scrollPane);
		
		// close button
		Button close = new Button("close");
		
		close.setOnAction(e -> {
			Event closeRecipe = new CloseRecipeEvent(recipe);
			this.fireEvent(closeRecipe);
			
			
		});
		
		// format the bottom button
		HBox buttonBox = new HBox();
		
		buttonBox.getChildren().add(close);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		buttonBox.setMargin(close, new Insets(10));
		setBottom(buttonBox);
		
	}
	
	
	class RecipeNameView extends VBox {
		RecipeNameView() {
			Text recipeName = new Text(recipe.name);
			recipeName.maxWidth(200);
			recipeName.setFont(Font.font(fontFamily,FontWeight.BOLD, 35));
			getChildren().add(recipeName);
			setAlignment(Pos.CENTER);
			this.setMinHeight(150);
		}
	}
	
	
	class RecipeInfoView extends HBox {
		Text prepTimeLabel = new Text("PREP TIME");
		Text cookTimeLabel = new Text("COOK TIME");
		Text totalTimeLabel = new Text("TOTAL TIME");
		Text yieldLabel =  new Text("YIELD");
		ArrayList<Text> labels = new ArrayList<Text>();
		ArrayList<Text> values = new ArrayList<Text>();
		
		double spacing = 5;
		
		RecipeInfoView( ) {
			labels.add(prepTimeLabel);
			labels.add(cookTimeLabel);
			labels.add(totalTimeLabel);
			labels.add(yieldLabel);
			
			values.add(new Text(recipe.prepTime));
			values.add(new Text(recipe.cookTime));
			values.add(new Text(recipe.totalTime));
			values.add(new Text(recipe.yield));
			
			for ( Text label : labels ) {
				label.setFont(Font.font(fontFamily,FontWeight.BOLD, 20));
			}
			
			for ( Text value : values ) {
				value.setFont(Font.font(fontFamily,FontWeight.MEDIUM, 20));
			}
			
			VBox prepTime = new VBox(spacing);
			prepTime.getChildren().addAll(prepTimeLabel, values.get(0));
			
			prepTime.setStyle("-fx-border-style: hidden solid hidden hidden;" + "-fx-border-color: lightgray;"); 
			
			VBox cookTime = new VBox(spacing);
			cookTime.getChildren().addAll(cookTimeLabel, values.get(1));
			cookTime.setStyle("-fx-border-style: hidden solid hidden hidden;" + "-fx-border-color: lightgray;"); 
			
			VBox totalTime = new VBox(spacing);
			totalTime.getChildren().addAll(totalTimeLabel, values.get(2));
			totalTime.setStyle("-fx-border-style: hidden solid hidden hidden;" + "-fx-border-color: lightgray;"); 
			
			VBox yield = new VBox(spacing);
			yield.getChildren().addAll(yieldLabel, values.get(3));
			
			ArrayList<VBox> vBoxes = new ArrayList<VBox>();
			vBoxes.add(prepTime);
			vBoxes.add(cookTime);
			vBoxes.add(totalTime);
			vBoxes.add(yield);
			
			for ( VBox vbox : vBoxes ) {
				vbox.prefWidthProperty().bind(widthProperty().divide(4));
				vbox.setAlignment(Pos.CENTER);
				vbox.setSpacing(40);
			}
			
			getChildren().addAll(prepTime, cookTime, totalTime, yield);
			setSpacing(spacing);
			setAlignment(Pos.CENTER);
			setMinHeight(150);
			this.setBorder(new Border(new BorderStroke(Color.rgb(100,100,100), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.5, 5,5, 0.5))));
		}
		
	}
	
	class RecipeIngredientsView extends VBox {
		Text ingredientsLabel = new Text("Ingredients");
		RecipeIngredientsView() {
			setAlignment(Pos.TOP_CENTER);
			setMargin(ingredientsLabel, new Insets(10, 0, 50, 0));
			ingredientsLabel.setFont(Font.font(fontFamily,FontWeight.BOLD, 25));
			ingredientsLabel.setWrappingWidth(250);
			ingredientsLabel.setTextAlignment(TextAlignment.CENTER);
			
			getChildren().add(ingredientsLabel);
			
			VBox ingredientsBox = new VBox(15);
			
			for(Text ingredientText : ingredientsTexts) {
				ingredientText.setTextAlignment(TextAlignment.LEFT);
				ingredientText.setWrappingWidth(275);
				ingredientText.setFont(Font.font(fontFamily,FontWeight.NORMAL, 20));
				ingredientsBox.getChildren().add(ingredientText);
			}
			ingredientsBox.setAlignment(Pos.TOP_LEFT);
			setMargin(ingredientsBox, new Insets(0,15,30,15));
			getChildren().add(ingredientsBox);
		}
	}
	
	class RecipeDirectionsView extends VBox {
		
		Text directionsLabel = new Text("Directions");
		Text writeUp = new Text(recipe.write_up);
		
		RecipeDirectionsView() {
			setAlignment(Pos.TOP_CENTER);
			directionsLabel.setFont(Font.font(fontFamily,FontWeight.BOLD, 25));
			
			setMargin(directionsLabel,  new Insets(10, 0, 50, 0));
			
			writeUp.setFont(Font.font(fontFamily,FontWeight.NORMAL, 20));
			writeUp.setWrappingWidth(500);
			setMargin(writeUp, new Insets(0,15,15,15));
			getChildren().addAll(directionsLabel, writeUp);
			
			setStyle("-fx-border-style: hidden hidden hidden solid;");
		}
		
	}
	
	class BottomPane extends HBox {
		BottomPane() {
			RecipeIngredientsView ingredientsView = new RecipeIngredientsView();
			RecipeDirectionsView directionsView = new RecipeDirectionsView();
			getChildren().addAll(ingredientsView, directionsView);
			setAlignment(Pos.CENTER);
			setMaxWidth(30);
			setHgrow(ingredientsView, Priority.NEVER);
			setHgrow(directionsView, Priority.NEVER);
			ingredientsView.setMaxWidth(300);
			ingredientsView.setPrefWidth(300);
			ingredientsView.setMinWidth(300);
		}
	}

	
	// for testing purposes until the db is changed!!!
	private String getPrepTime() { return "30 mins"; }
	private String getCookTime() { return "1 hr"; }
	private String getTotalTime() { return "1 hr 30 mins"; }
	private String getYield() { return "8 servings"; }
}
