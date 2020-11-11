package app;

import java.util.*;


import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline; 

public class HomeWorkOne extends Application{

	@Override
	public void start ( Stage primaryStage ){
	
		exercise_14_5( primaryStage );
		exercise_14_11( primaryStage );
		exercise_14_18( primaryStage );
		exercise_14_19( primaryStage );
	}
	
	public static void main (String [] args){
	    launch( args );
	}
	
	public static void exercise_14_5( Stage primaryStage ) {
		
		int WIDTH = 500;
		int HEIGHT = 500;
		int RADIUS = 150;
		
		Pane pane = new Pane();
	    pane.setPrefSize( WIDTH, HEIGHT );
	    Scene scene = new Scene( pane, WIDTH, HEIGHT );
	    
	    String text[] = { "W", "E", "L", "C", "O", "E", " ", "T", "O", " ", "J", "A", "V", "A", " " };
	    
	    for ( int i = 0; i < text.length; i++ ) {
	    	double rotation = ( i * 360 / text.length );
	    	//Text welcome_java = new Text( 100.0, 50.0, "WELCOME TO JAVA" );
		    Text welcome_java = new Text( 100.0, 50.0, text[i] );
	    	Font welcome_font = Font.font( "Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 50 );
		    welcome_java.setFont( welcome_font );
		    welcome_java.setRotate( rotation + 90 );
		    welcome_java.setFill( Color.rgb( ( int )( Math.random() * 255 ), 
		    								 ( int )( Math.random() * 255 ),
		    								 ( int )( Math.random() * 255 ) ) );
	
		    welcome_java.setX( ( WIDTH / 2 ) + Math.cos( Math.toRadians( rotation ) ) * RADIUS );
		    welcome_java.setY( ( HEIGHT / 2 ) + Math.sin( Math.toRadians( rotation ) ) * RADIUS );
	    	pane.getChildren().add( welcome_java );
	    }
	    
	    
	    primaryStage.setTitle( "Josh Ortner" );
	    primaryStage.setScene( scene );
	    primaryStage.show();
	}
	
	public static void exercise_14_11( Stage primaryStage ) {
			
		int WIDTH = 500;
		int HEIGHT = 500;
		int RADIUS = 150;
		
		
		Circle circle = new Circle( WIDTH / 2, HEIGHT / 2, RADIUS );
		circle.setFill( Color.TRANSPARENT );
	    circle.setStroke( Color.BLACK );
	    
	    Ellipse left_eye_base = new Ellipse( 200, 200, 40, 30 );
	    left_eye_base.setFill( Color.GREEN );
	    left_eye_base.setStroke( Color.BLACK );
	    
	    Ellipse right_eye_base = new Ellipse( 300, 200, 40, 30 );
	    right_eye_base.setFill( Color.GREEN );
	    right_eye_base.setStroke( Color.BLACK );
	    
	    Circle left_eye = new Circle( 300, 200, 15 );
	    Circle right_eye = new Circle( 200, 200, 15 );
	    
	    Polygon nose = new Polygon();
	    nose.getPoints().addAll( 
            WIDTH / 2.0, ( HEIGHT / 2.0 ) - 20,
            ( WIDTH / 2.0 ) - 40.0, ( HEIGHT / 2.0 ) + 50.0,
            ( WIDTH / 2.0 ) + 40.0, ( HEIGHT / 2.0 ) + 50.0
        );
	    nose.setFill( Color.BLUE );
	    nose.setStroke( Color.BLACK );
	    
	    Arc smile = new Arc( WIDTH / 2.0, ( HEIGHT / 2.0 ) + 55, 80, 45, -162, 150 );
	    smile.setFill( Color.WHITE );
	    smile.setType( ArcType.OPEN );
	    smile.setStroke( Color.BLACK );
		
		Group group = new Group( circle, right_eye_base, left_eye_base, left_eye, right_eye, nose, smile );

		Scene scene = new Scene( group, WIDTH, HEIGHT );
		
		primaryStage.setScene( scene );
		    
		primaryStage.setTitle( "Josh Ortner" );
		primaryStage.show();
	}
	
	public static void exercise_14_18( Stage primaryStage ) {
		
		int WIDTH = 500;
		int HEIGHT = 500;
		int RADIUS = 150;
		
		Pane pane = new Pane();
	    pane.setPrefSize( WIDTH, HEIGHT );
	    
	    Line x = new Line(); 
	    x.setStartX( 5 ); 
	    x.setStartY( ( HEIGHT / 2 ) + 70 ); 
	    x.setEndX( WIDTH - 5 ); 
	    x.setEndY( ( HEIGHT / 2 ) + 70 );
	    
	    Line y = new Line(); 
	    y.setStartX( ( WIDTH / 2 ) ); 
	    y.setStartY( 25 ); 
	    y.setEndX( ( WIDTH / 2 ) ); 
	    y.setEndY( HEIGHT - 25 );   
	    
	    Arc graph = new Arc( WIDTH / 2.0, ( HEIGHT / 2.0 ) - 330, 175, 400, -160, 140 );
	    graph.setFill( Color.WHITE );
	    graph.setType( ArcType.OPEN );
	    graph.setStroke( Color.BLACK );
	    
	    Text x_label = new Text( WIDTH - 20, ( HEIGHT / 2 ) + 65, "X" );
	    Text y_label = new Text( ( WIDTH / 2 ) + 10, 25, "Y" );
   
        Group group = new Group( graph, x, y, x_label, y_label );

		Scene scene = new Scene( group, WIDTH, HEIGHT );
		
		primaryStage.setScene( scene );
		    
		primaryStage.setTitle( "Josh Ortner" );
		//primaryStage.setScene( scene );
		primaryStage.show();
	}
	
	public static void exercise_14_19( Stage primaryStage ) {
		
		int WIDTH = 500;
		int HEIGHT = 500;
		int RADIUS = 150;
		
		Pane pane = new Pane();
	    pane.setPrefSize( WIDTH, HEIGHT );
	   
	    Polyline sine = new Polyline( );
	    Polyline cosine = new Polyline( );

	    int start = ( WIDTH / 2 ) * -1;
	    
        for (int i = 0; i < ( WIDTH ); i++) {
        	sine.getPoints().addAll( (double)i, 50 * Math.sin( Math.toRadians( start ) ) + ( WIDTH / 2) );
        	cosine.getPoints().addAll( (double)i, 50 * Math.cos( Math.toRadians( start ) ) + ( WIDTH / 2) );
        	System.out.println( Math.sin( Math.toRadians( start ) ) + ( WIDTH / 2) );
        	//System.out.println( i );
        	start += 3;
        }
        
        sine.setStroke( Color.RED );
        cosine.setStroke( Color.BLUE );
	    
        Group group = new Group( sine, cosine );

		Scene scene = new Scene( group, WIDTH, HEIGHT );
		
		primaryStage.setScene( scene );
		    
		primaryStage.setTitle( "Josh Ortner" );
		//primaryStage.setScene( scene );
		primaryStage.show();
	}
	
	
}