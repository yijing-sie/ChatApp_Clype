package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * A class that designs the popup window with an error message
 * 
 * @author Yijing
 *
 */
public class ErrorPopup {
	private String errorMessage; // Error message to be displayed
	public ErrorPopup(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public void display()
	{
		Stage popupwindow=new Stage();
	      
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("ERROR!!!");
		
	    Label label = new Label(errorMessage); // Display the errormessage
	    label.setWrapText(true);

	    Button close= new Button("Close"); // Close the popup window
	    close.setOnAction(new EventHandler<ActionEvent>() {
	    	@Override
	    	public void handle(ActionEvent ac) {
	    		popupwindow.close();
	    	}
	    	
	    });

	    VBox layout= new VBox(10);
	    layout.setPadding(new Insets(10,10,0,10));
	    layout.getChildren().addAll(label, close);
	    layout.setAlignment(Pos.CENTER);

	    Scene scene= new Scene(layout, 300, 110);
	    popupwindow.setScene(scene);
	    popupwindow.showAndWait();
	       
	}

}
