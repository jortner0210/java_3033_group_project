package app.RecipeInsert;

import app.Ingredient;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IngredientInsert {
	
	double height =	200;
	double width = 350;
	Label label = new Label();
	Stage stage = new Stage();
	RecipeInsertPane callingClass = null;
	
	String ingredientDisplay = "";
	
	// NAME
	Text name = new Text( "Name:" );
	TextField name_text = new TextField();
	//name_text.setAlignment(Pos.CENTER_RIGHT);
			
	// CATEGORY
	Text category = new Text( "Category:" );
	TextField category_text = new TextField();
	
	// QTY
	Text qty = new Text( "QTY:" );
	TextField qty_text = new TextField();
	
	Text qtyError = new Text( "Quantity must be a number");
	
	
	
	String metrics[] = {"cups", "tsp", "Tbsp", "gal", "L", "mL", "oz", "lbs", "Other"};
	ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(metrics));

	// METRIC
	Text metric = new Text( "Metric:" );
	TextField metric_text = new TextField();
			
	// DONE BUTTON
	Button done = new Button("DONE");
	
	public IngredientInsert(RecipeInsertPane callingClass) {
		this.callingClass = callingClass;
	}
	
	public void show() {
		stage.centerOnScreen();
		stage.setWidth(width);
		stage.setHeight(height);
		
		name_text.setPrefColumnCount( 10 );
		category_text.setPrefColumnCount( 10 );
		metric_text.setPrefColumnCount( 10 );
		combo_box.setEditable(true);
		qtyError.setVisible(false);
		qtyError.setFill(Color.RED);
		
		GridPane grid_pane = new GridPane();
		grid_pane.setPadding( new Insets( 0, 10, 10, 10 ) );
		grid_pane.setVgap( 5 );
		grid_pane.setHgap( 5 );
		
			
		ColumnConstraints col0 = new ColumnConstraints();
		col0.setMinWidth(75);
		col0.setMaxWidth(75);
		grid_pane.getColumnConstraints().add(col0);
		
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setMinWidth(100);
		grid_pane.getColumnConstraints().add(col1);
		
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setMinWidth(50);
		col2.setMaxWidth(75);
		grid_pane.getColumnConstraints().add(col2);
		
		
		grid_pane.setAlignment(Pos.CENTER);
		
		
		qty_text.setPrefColumnCount( 10 );
		qty_text.textProperty().addListener((observable, oldVal, newVal) -> {
			try {
				Float.parseFloat(newVal);
				qtyError.setVisible(false);
				done.setDisable(false);
			}
			catch ( Exception e )  {
				if(newVal.length() == 0) {
					qtyError.setVisible(false);
					done.setDisable(false);
				}
				else {
					qtyError.setVisible(true);
					done.setDisable(true);
				}
			}
		});
		
		
		
		done.setOnAction(new EventHandler<ActionEvent>() {
 
            public void handle( ActionEvent event ) {
                Ingredient ingredient = new Ingredient();
            	
            	ingredient.name 	= name_text.getText();
            	ingredient.category = name_text.getText();
            	ingredient.qty 		= Float.parseFloat( qty_text.getText() );
            	ingredient.metric 	= (String) combo_box.getValue();
            	
            	callingClass.ingredients_to_add.add( ingredient );
            	
            	label.setText( "added: " + ingredient.name);
            	ingredientDisplay += "\u2022 ";
            	if(ingredient.qty == Math.ceil(ingredient.qty))
            		ingredientDisplay += (int) ingredient.qty;
            	else
            		ingredientDisplay += ingredient.qty;
            	ingredientDisplay +=  " " + ingredient.metric + " " + ingredient.name;
            	stage.close();
            }
            
        });
		
		
		
		// col, row, col span, row span
		grid_pane.add( name,     0, 1, 1, 1 );
		grid_pane.add( category, 0, 3, 1, 1 );
		grid_pane.add( qty, 	 0, 5, 1, 1 );
		grid_pane.add( qtyError, 0, 6, 1, 1);
		
		grid_pane.add( name_text,	  1, 1, 1, 1 );
		grid_pane.add( category_text, 1, 3, 1, 1 );
		grid_pane.add( qty_text,	  1, 5, 1, 1 );
		grid_pane.add( combo_box, 	  2, 5, 1, 1 );	
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		hbox.getChildren().add(done);
		grid_pane.add(hbox, 1, 7, 3, 1);

		Scene questionScene = new Scene( grid_pane, height, width);
		
		stage.setTitle("Add Ingredient");
		stage.setResizable(false);
		stage.setScene( questionScene );
		stage.showAndWait();
	
		//callingClass.addToLabelRow(label, 0);
		callingClass.addToIngredientsList(ingredientDisplay);
		
	}
	
}

