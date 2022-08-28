package application;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A class that designs the popup window for user to show the received image
 * 
 * @author Yijing
 *
 */
public class PopupImage {
	public String user = "Anon"; // Default user name
	final int window_width = 1000;
	final int window_height = 700;
	/**
	 * Disply the `picture_file` on a popup window
	 * @param picture_file the picture file received from the server 
	 */
	public void display(File picture_file) {
		Stage popupwindow=new Stage();
	      
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle(user + " sent a picture");
		// Label to show the image
		Label labelImage = new Label();
		Image picture = new Image(picture_file.toURI().toString());
		ImageView imageView = new ImageView(picture);
		if (picture.getHeight() > window_height || picture.getWidth() > window_width) {
			if (picture.getWidth() > window_width) {
				imageView.setFitWidth(window_width);
			}
			if (picture.getHeight() > window_height) {
				imageView.setFitHeight(window_height);
			}
			imageView.preserveRatioProperty();
		}
	    labelImage.setGraphic(imageView);
		// Button to close the image
	    Button close = new Button("Close");
		// Event hadler for the `Close` button
	    close.setOnAction(new EventHandler<ActionEvent>() {
	    	@Override
	    	public void handle(ActionEvent ac) {
	    		popupwindow.close();
	    	}
	    	
	    });

	    VBox layout= new VBox(10);

	    layout.setPadding(new Insets(10,10,0,10));
	    layout.getChildren().addAll(labelImage,close);
	    layout.setAlignment(Pos.CENTER);

		Scene scene;
		scene = new Scene(layout, window_width, window_height);

	    popupwindow.setScene(scene);
	    popupwindow.showAndWait();
	       
	}

}
