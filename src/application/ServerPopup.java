package application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * A class that designs the popup window for the server to get information from the client
 * 
 * @author Yijing
 *
 */
public class ServerPopup {
	// Default port number 
	private static int port = 0;

	/**
	 *  Display the pop-up window for the user to enter and return the typed-in port number
	 *  It only closes when a valid port number  (>= 1024) is entered
	 *  @return port number
	 */
	public int display()
	{
		Stage popupwindow=new Stage();
	      
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("Welcome");

	    Label label = new Label("Please enter your port number here (>= 1024)");
		label.setStyle("-fx-font: 15px 'calibri' ");
		label.setWrapText(true);
	      
		TextField portNumber= new TextField();
		portNumber.setPromptText("7000");
		portNumber.maxWidth(200);
		portNumber.setAlignment(Pos.CENTER);
		// Button to close the popup
	    Button finish= new Button("Finish");
		// Button the show the default connection setting in the TextFields
		Button Default = new Button("Default");
		/**
		 * Event handler for `Finish` button
		 */
	    finish.setOnAction(new EventHandler<ActionEvent>() {
	    	@Override
	    	public void handle(ActionEvent ac) {
	    		if(!portNumber.getText().equals("")) {
				// Update the port number
					try {
						port = Integer.parseInt(portNumber.getText());
						if (port >= 1024) {
							popupwindow.close(); // Only close the popup with a valid port number input
						}
					} catch (NumberFormatException nfe) {
						System.out.println("Invalid port number");
					}
	    		}

	    	}
	    });
		/**
		 * Event handler for `Default` button
		 */
		Default.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ac) {
				portNumber.setText("7000");
			}
		});
		// VBox for Label and TextField indicating user to type in port number
	    VBox layout= new VBox(10);
		layout.setPadding(new Insets(10,10,0,10));
		layout.setAlignment(Pos.CENTER);
		// HBox for buttons
		HBox buttons = new HBox(5);
		buttons.setAlignment(Pos.CENTER);

		buttons.getChildren().addAll(finish, Default);
	    layout.getChildren().addAll(label,portNumber, buttons);
	    
	    Scene scene= new Scene(layout, 300, 110);
	    popupwindow.setScene(scene);
	    popupwindow.showAndWait();
	    return port;
	}

}
