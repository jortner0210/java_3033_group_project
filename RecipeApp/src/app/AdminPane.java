package app;

import app.RecipeInsert.RecipeInsertPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

public class AdminPane extends BorderPane {
	public Button btAddRecipe = new Button("Add Recipe");
	public Button btDeleteRecipe = new Button("Delete Recipe");
	public Button btEditRecipe = new Button("Edit Recipe");
	
	public AdminPane() {
		HBox hBox = new HBox(15);
		hBox.setPadding(new Insets(15));
		hBox.setStyle("-fx-border-color: black");
		hBox.getChildren().addAll(btAddRecipe, btDeleteRecipe, btEditRecipe);
		hBox.setAlignment(Pos.CENTER_LEFT);
		setTop(hBox);
		
		StackPane pane = new StackPane();
		
		Label helloWorld = new Label("Admin pane");
		helloWorld.setAlignment(Pos.CENTER);

		helloWorld.setFont(new Font("Arial", 30));
		pane.getChildren().add(helloWorld);
		
		setCenter(pane);
		
		btAddRecipe.setOnAction(e -> addRecipe());
		
		btEditRecipe.setOnAction(e -> editRecipe());
		
		btDeleteRecipe.setOnAction(e -> deleteRecipe());
	}
	
	private void addRecipe() {
		System.out.println("Add recipe clicked.");
		RecipeInsertPane pane = new RecipeInsertPane();		
		setCenter( pane );
	}
	
	private void deleteRecipe() {
		System.out.println("Delete recipe clicked.");
	}
	
	private void editRecipe() {
		System.out.println("Edit recipe clicked.");
	}
	
	
}
