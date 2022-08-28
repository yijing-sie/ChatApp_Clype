package application;

// import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A class that designs the popup window for user to show the text file content
 * 
 * @author Yijing
 *
 */
public class PopupText {
	public String user = "Anon"; // Default user name
	final int window_width = 700;
	final int window_height = 700;
	/**
	 * Disply the `text` on a popup window
	 * @param text A String of the content from the text file
	 */
	public void display(String text) {
		Stage popupwindow=new Stage();
	      
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle(user + " sent a text file");

		VBox layout= new VBox(10);
	    layout.setPadding(new Insets(10,10,10,10));
		layout.setAlignment(Pos.CENTER);
		// Label to show the text
		Label labelText = new Label();
		labelText.setStyle("-fx-background-color: #FFFFFF");
		labelText.setPadding(new Insets(3, 3, 3, 3));
		labelText.setText(text);
		labelText.setWrapText(true);
		labelText.setAlignment(Pos.TOP_LEFT);
		labelText.prefWidthProperty().bind(layout.widthProperty().subtract(20));
		labelText.prefHeightProperty().bind(layout.heightProperty().subtract(60));
		// ScrollPane to scroll on the `labelText`
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(labelText);
		scrollPane.setFitToWidth(true);
		// Button to close the image
	    Button close = new Button("Close");
		close.setWrapText(true);
		close.maxHeight(60);
		/**
		 * Event handle for `Close` button
		 */
	    close.setOnAction(new EventHandler<ActionEvent>() {
	    	@Override
	    	public void handle(ActionEvent ac) {
	    		popupwindow.close();
	    	}
	    	
	    });

	    layout.getChildren().addAll(scrollPane,close);
	    
		Scene scene;
		scene = new Scene(layout, window_width, window_height);
	    popupwindow.setScene(scene);
	    popupwindow.showAndWait();
	       
	}

}
