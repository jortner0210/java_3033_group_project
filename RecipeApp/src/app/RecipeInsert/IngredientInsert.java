package app.RecipeInsert;

import app.Ingredient;
import app.Events.AddIngredientEvent;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
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

// window for adding an ingredient
public class IngredientInsert extends Stage {
	
	double height =	200;
	double width = 400;
	Label label = new Label();
	
	// NAME
	Text name = new Text( "Name:" );
	TextField name_text = new TextField();
			
	// CATEGORY
	Text category = new Text( "Category:" );
	TextField category_text = new TextField();
	
	// QTY
	Text qty = new Text( "QTY:" );
	TextField qty_text = new TextField();
	
	// Error message if invalid qty
	Text qtyError = new Text( "Quantity must be a number");
	
	

	// METRIC
	Text metric = new Text( "Metric:" );
	TextField metric_text = new TextField();
	// combo box for the different types of metrics
	String metrics[] = {"cups", "tsp", "Tbsp", "gal", "L", "mL", "oz", "lbs"};
	ComboBox combo_box = new ComboBox(FXCollections.observableArrayList(metrics));
			
	// DONE BUTTON
	Button done = new Button("DONE");
	
	Ingredient ingredient;
	GridPane grid_pane;
	Scene scene;
	
	boolean ingredientChanged = false;
	
	public IngredientInsert() {
		
		ingredientChanged = true;
		grid_pane = new GridPane();
		grid_pane.setPadding( new Insets( 0, 10, 10, 10 ) );
		grid_pane.setVgap( 5 );
		grid_pane.setHgap( 5 );
		
		// set up column widths
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
		
		// add listener to detect for qty errors
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
					done.setDisable(true);
				}
				else {
					qtyError.setVisible(true);
					done.setDisable(true);
				}
			}
		});
		
		qtyError.setVisible(false);
		done.setDisable(true);
		
		
		// col, row, col span, row span
		grid_pane.add( name,     0, 1, 1, 1 );
		grid_pane.add( category, 0, 3, 1, 1 );
		grid_pane.add( qty, 	 0, 5, 1, 1 );
		grid_pane.add( qtyError, 0, 6, 1, 1);
		
		grid_pane.add( name_text,	  1, 1, 1, 1 );
		grid_pane.add( category_text, 1, 3, 1, 1 );
		grid_pane.add( qty_text,	  1, 5, 1, 1 );
		grid_pane.add( combo_box, 	  2, 5, 1, 1 );	
		
		// adding an hbox for easy alignment of done button
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		hbox.getChildren().add(done);
		grid_pane.add(hbox, 1, 7, 3, 1);
		
		// creates new ingredient and closes window
		done.setOnAction((e) -> {
			ingredient = new Ingredient();
        	
        	ingredient.name 	= name_text.getText();
        	ingredient.category = category_text.getText();
        	ingredient.qty 		= Float.parseFloat( qty_text.getText() );
        	ingredient.metric 	= (String) combo_box.getValue();
        	
        	this.close();
		});

		scene = new Scene( grid_pane, width, height );
		combo_box.setEditable(true);
		setTitle("Add Ingredient");
		setResizable(false);
		setScene( scene );
	}
	
	// constructor for editing an existing ingredient
	public IngredientInsert(Ingredient ingredient) {
		
		this.ingredient = ingredient;
		grid_pane = new GridPane();
		grid_pane.setPadding( new Insets( 0, 10, 10, 10 ) );
		grid_pane.setVgap( 5 );
		grid_pane.setHgap( 5 );
		
		name_text.setText(ingredient.name);
		category_text.setText(ingredient.category);
		qty_text.setText(Float.toString(ingredient.qty));
		
		// if metric is not null and it matches one of the options in the combo box, select that item
		for(int i = 0; i < metrics.length; i++) {
			
			if(ingredient.metric != null && ingredient.metric.equals(metrics[i])) {
				combo_box.getSelectionModel().select(i);
				combo_box.setEditable(true);
				System.out.println("found " + ingredient.metric);
			}
				
		}
		
		
		
		// set up column widths
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
		
		// add listener to detect for qty errors
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
					done.setDisable(true);
				}
				else {
					qtyError.setVisible(true);
					done.setDisable(true);
				}
			}
		});
		
		qtyError.setVisible(false);
		//done.setDisable(true);
		
		
		// col, row, col span, row span
		grid_pane.add( name,     0, 1, 1, 1 );
		grid_pane.add( category, 0, 3, 1, 1 );
		grid_pane.add( qty, 	 0, 5, 1, 1 );
		grid_pane.add( qtyError, 0, 6, 1, 1);
		
		grid_pane.add( name_text,	  1, 1, 1, 1 );
		grid_pane.add( category_text, 1, 3, 1, 1 );
		grid_pane.add( qty_text,	  1, 5, 1, 1 );
		grid_pane.add( combo_box, 	  2, 5, 1, 1 );	
		
		// adding an hbox for easy alignment of done button
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		hbox.getChildren().add(done);
		grid_pane.add(hbox, 1, 7, 3, 1);
		
		done.setOnAction((e) -> {
			
        	String newName = name_text.getText();
        	String newCategory = category_text.getText();
        	float qty = Float.parseFloat( qty_text.getText() );
        	String metric = (String) combo_box.getValue();
        	
        	// detect if the ingredient has actually changed
        	if(!newName.equals(ingredient.name)) 
        		ingredientChanged = true;
        	
        	if(!newCategory.equals(ingredient.category))
        		ingredientChanged = true;
        	
        	if(qty != ingredient.qty)
        		ingredientChanged = true;
        	
        	if(ingredient.metric != null  && !metric.equals(ingredient.metric))
        		ingredientChanged = true;
        	
        	ingredient.name = newName;
        	ingredient.category = newCategory;
        	ingredient.qty = qty;
        	ingredient.metric = metric;
        	
        	this.close();
        	
		});

		scene = new Scene( grid_pane, width, height );
		
		setTitle("Add Ingredient");
		setResizable(false);
		setScene( scene );
	}
	
	public Ingredient getIngredient() { return this.ingredient; }
	
	public boolean getIngredientChanged() { return ingredientChanged; }
}














	/*
	public IngredientInsert(RecipeInsertPane callingClass) {
		this.callingClass = callingClass;
	}*/
	
	/*
	// displays this pane
	public void show() {
		// some formatting
		stage.centerOnScreen();
		stage.setWidth(width);
		stage.setHeight(height);
		
		name_text.setPrefColumnCount( 10 );
		category_text.setPrefColumnCount( 10 );
		metric_text.setPrefColumnCount( 10 );
		combo_box.setEditable(true);
		qtyError.setVisible(false);
		qtyError.setFill(Color.RED);
		
		// put a gridpane in the center
		GridPane grid_pane = new GridPane();
		grid_pane.setPadding( new Insets( 0, 10, 10, 10 ) );
		grid_pane.setVgap( 5 );
		grid_pane.setHgap( 5 );
		
		// set up column widths
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
		
		// add listener to detect for qty errors
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
		*/
		/*
		// submits ingredient
		done.setOnAction(new EventHandler<ActionEvent>() {
 
            public void handle( ActionEvent event ) {
                Ingredient ingredient = new Ingredient();
            	
            	ingredient.name 	= name_text.getText();
            	ingredient.category = name_text.getText();
            	ingredient.qty 		= Float.parseFloat( qty_text.getText() );
            	ingredient.metric 	= (String) combo_box.getValue();
            	
            	*/
            	
            	/*
            	callingClass.ingredients_to_add.add( ingredient );
            	
            	label.setText( "added: " + ingredient.name);
            	ingredientDisplay += "\u2022 ";
            	if(ingredient.qty == Math.ceil(ingredient.qty))
            		ingredientDisplay += (int) ingredient.qty;
            	else
            		ingredientDisplay += ingredient.qty;
            	ingredientDisplay +=  " " + ingredient.metric + " " + ingredient.name;
            	stage.close();*/
           
			/*}
            
        });*/
		
		/*
		
		// col, row, col span, row span
		grid_pane.add( name,     0, 1, 1, 1 );
		grid_pane.add( category, 0, 3, 1, 1 );
		grid_pane.add( qty, 	 0, 5, 1, 1 );
		grid_pane.add( qtyError, 0, 6, 1, 1);
		
		grid_pane.add( name_text,	  1, 1, 1, 1 );
		grid_pane.add( category_text, 1, 3, 1, 1 );
		grid_pane.add( qty_text,	  1, 5, 1, 1 );
		grid_pane.add( combo_box, 	  2, 5, 1, 1 );	
		
		// adding an hbox for easy alignment of done button
		HBox hbox = new HBox(10);
		hbox.setAlignment(Pos.BOTTOM_RIGHT);
		hbox.getChildren().add(done);
		grid_pane.add(hbox, 1, 7, 3, 1);

		Scene questionScene = new Scene( grid_pane, height, width);
		
		stage.setTitle("Add Ingredient");
		stage.setResizable(false);
		stage.setScene( questionScene );
		stage.showAndWait();
	
		callingClass.addToIngredientsList(ingredientDisplay);
		
	}
	*/

