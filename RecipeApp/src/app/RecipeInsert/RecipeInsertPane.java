package app.RecipeInsert;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PopupControl;
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
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.*;

import app.Ingredient;
import app.Recipe;
import app.RecipeDBManager;

public class RecipeInsertPane extends GridPane {
	
	int next_label_row;
	
	public ArrayList<Ingredient> ingredients_to_add = new ArrayList<>();
	
	public RecipeInsertPane() {
		setPadding( new Insets( 10, 10, 10, 10 ) );
		setMinSize( 200,200 );
		setVgap( 5 );
		setHgap( 5 );
		// TITLE
		Text title = new Text( "Title:" );
		TextField title_text = new TextField();
		title_text.setPrefColumnCount( 10 );
		
		// WRITE UP
		Text write_up = new Text( "Description:" );
		TextField write_up_text = new TextField();
		write_up_text.setPrefColumnCount( 10 );
		
		// ADD INGREDIENT
		Button add_ingredient = new Button("Add Ingredient");
		add_ingredient.setOnAction( e -> addIngredient() );
		
		// RECIPE DONE BUTTON
		Button done_button = new Button("DONE");
		done_button.setOnAction(new EventHandler<ActionEvent>() {
			 
            public void handle( ActionEvent event ) {
                System.out.println( "recipe done" );
                Recipe recipe = new Recipe( -1, title_text.getText(), 
                							write_up_text.getText(), 
                							getIngredients() );
                System.out.println( recipe );
                RecipeDBManager db = new RecipeDBManager( "test_db" );
                db.insert_recipe( recipe.name, 
                				  recipe.write_up, 
                				  recipe.ingredients );
                clearPane();
            }
            
        });
		
		// col, row, col span, row span
		add( done_button,    0, 0, 2, 1 );
		add( title,    		 0, 2, 2, 1 );
		add( write_up, 		 0, 4, 2, 1 );
		add( add_ingredient, 0, 6, 2, 1 );
		
		next_label_row = 7;
		
		add( title_text,	2, 2, 1, 1 );
		add( write_up_text, 2, 4, 1, 1 );
	}
	
	public ArrayList<Ingredient> getIngredients() {
		return ingredients_to_add;
	}
	
	public void clearPane() {
		Parent parent = getParent();
		BorderPane pane = ( BorderPane )parent;		
		pane.setCenter( null );
	}
	
	public void addIngredient() {
		IngredientInsert ins = new IngredientInsert(this);
		ins.show();
	}
	
	public void addToLabelRow(Label label, int col) {
		add( label, 0, next_label_row );
		next_label_row++;
	}
		

}
