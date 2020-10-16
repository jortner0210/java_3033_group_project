package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PopupControl;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.*;

public class RecipeInsertPane extends GridPane {
	
	int next_label_row;
	
	ArrayList<Ingredient> ingredients_to_add = new ArrayList<>();
	
	public RecipeInsertPane() {
		setPadding( new Insets( 10, 10, 10, 10 ) );
		setMinSize( 300, 300 );
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
		
		Label label = new Label();
	
		Stage popupWindow = new Stage();
		
		popupWindow.centerOnScreen();
		popupWindow.setWidth(300);
		popupWindow.setHeight(300);
		
		GridPane grid_pane = new GridPane();
		grid_pane.setPadding( new Insets( 10, 10, 10, 10 ) );
		grid_pane.setMinSize( 300, 300 );
		grid_pane.setVgap( 5 );
		grid_pane.setHgap( 5 );
		
		// NAME
		Text name = new Text( "Name:" );
		TextField name_text = new TextField();
		name_text.setPrefColumnCount( 10 );
				
		// CATEGORY
		Text category = new Text( "Category:" );
		TextField category_text = new TextField();
		category_text.setPrefColumnCount( 10 );
		
		// QTY
		Text qty = new Text( "QTY:" );
		TextField qty_text = new TextField();
		qty_text.setPrefColumnCount( 10 );
				
		// METRIC
		Text metric = new Text( "Metric:" );
		TextField metric_text = new TextField();
		metric_text.setPrefColumnCount( 10 );
				
		// DONE BUTTON
		Button done = new Button("DONE");
		done.setOnAction(new EventHandler<ActionEvent>() {
 
            public void handle( ActionEvent event ) {
                Ingredient ingredient = new Ingredient();
            	
            	ingredient.name 	= name_text.getText();
            	ingredient.category = name_text.getText();
            	ingredient.qty 		= Float.parseFloat( qty_text.getText() );
            	ingredient.metric 	= metric_text.getText();
            	
            	ingredients_to_add.add( ingredient );
            	
            	label.setText( "added: " + ingredient.name );
            	
            	popupWindow.close();
            }
            
        });
				
		// col, row, col span, row span
		grid_pane.add( name,     0, 0, 2, 1 );
		grid_pane.add( category, 0, 2, 2, 1 );
		grid_pane.add( qty, 	 0, 4, 2, 1 );
		grid_pane.add( metric,   0, 6, 2, 1 );
		grid_pane.add( done, 	 0, 8, 2, 1 );
		
		grid_pane.add( name_text,	  2, 0, 1, 1 );
		grid_pane.add( category_text, 2, 2, 1, 1 );
		grid_pane.add( qty_text,	  2, 4, 1, 1 );
		grid_pane.add( metric_text,   2, 6, 1, 1 );
		
		Scene questionScene = new Scene( grid_pane, 300, 300 );
		
		popupWindow.setScene( questionScene );
		popupWindow.showAndWait();
		
		
		add( label, 0, next_label_row );
		next_label_row++;
	}
	

}
