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
import main.ClypeClient;

/**
 * A class that designs the popup window for user to type in connection information (user name, host name, port number)
 * It only closes when a valid username, a host name, and a port number (>= 1024) is entered
 * @author Yijing
 *
 */
public class Popup {
	ClypeClient client = null;

	/** 
	 * Displaying the pop-up winsow for the user to enter and return the typed-in username
	 * @return A ClypeCliet with the given user name, host name, and a port number
	 */
	public ClypeClient display()
	{
		Stage popupwindow=new Stage();
	      
		popupwindow.initModality(Modality.APPLICATION_MODAL);
		popupwindow.setTitle("Welcome");

		VBox root = new VBox();
		Scene scene= new Scene(root, 750, 200);
		root.setPadding(new Insets(10, 20, 10, 20));
		root.setAlignment(Pos.CENTER);
		root.setSpacing(20);
		root.prefWidthProperty().bind(scene.widthProperty().multiply(0.8));


		HBox hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);
		hbox.setSpacing(30);
		hbox.prefWidthProperty().bind(root.widthProperty());

		// VBox for the Label and the TextField indicating user to type in a user name 
		VBox userBox = new VBox();
		userBox.setAlignment(Pos.CENTER);
		userBox.setSpacing(20);
		userBox.prefWidthProperty().bind(hbox.widthProperty().multiply(0.3));
		// VBox for the Label and the TextField indicating user to type in a host name
		VBox hostBox = new VBox();
		hostBox.setSpacing(20);
		hostBox.setAlignment(Pos.CENTER);
		hostBox.prefWidthProperty().bind(hbox.widthProperty().multiply(0.3));
		// VBox for the Label and the TextField indicating user to type in a port number 
		VBox portBox = new VBox();
		portBox.setAlignment(Pos.CENTER);
		portBox.setSpacing(20);
		portBox.maxWidth(200);
		portBox.prefWidthProperty().bind(hbox.widthProperty().multiply(0.3));
		// HBox for `Finish` button and `Default` button
		HBox buttons = new HBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.setSpacing(10);
		buttons.prefWidthProperty().bind(hbox.widthProperty());
	      
		TextField userName = new TextField();
		TextField hostName = new TextField();
		TextField portNumber = new TextField();

		userName.prefWidthProperty().bind(userBox.widthProperty());
		userName.setAlignment(Pos.CENTER);
		userName.setPromptText("Anon");

		hostName.prefWidthProperty().bind(hostBox.widthProperty());
		hostName.setAlignment(Pos.CENTER);
		hostName.setPromptText("localhost");

		portNumber.prefWidthProperty().bind(portBox.widthProperty());
		portNumber.setAlignment(Pos.CENTER);
		portNumber.setPromptText("7000");

		Label user_label = new Label("User Name");
		user_label.setWrapText(true);
		user_label.prefWidthProperty().bind(userName.widthProperty());
		user_label.setStyle("-fx-font: 15px 'calibri' ");
		user_label.setAlignment(Pos.CENTER);

		Label host_label = new Label("Host Name / IP");
		host_label.setStyle("-fx-font: 15px 'calibri' ");
		host_label.setWrapText(true);
		host_label.prefWidthProperty().bind(hostName.widthProperty());
		host_label.setAlignment(Pos.CENTER);

		Label port_label = new Label("Port Number (>= 1024)");
		port_label.setStyle("-fx-font: 15px 'calibri' ");
		port_label.setWrapText(true);
		port_label.prefWidthProperty().bind(portNumber.widthProperty());
		port_label.setAlignment(Pos.CENTER);

		// Button to close the popup window after typing
	    Button finish= new Button("Finish");
		finish.setWrapText(true);
		/*
		* Button to show the default connection setting in the TextFields 
		* with user name = Anon, host name = localhost, port number = 7000
		*/ 
		Button Default = new Button("Default");
		Default.setWrapText(true);

		// Event handler for `Finish` button
	    finish.setOnAction(new EventHandler<ActionEvent>() {
			
	    	@Override
	    	public void handle(ActionEvent ac) {
	    		if(!userName.getText().equals("") && 
						!hostName.getText().equals("") && 
						!portNumber.getText().equals("")) {
	    			String user = userName.getText();
					String host = hostName.getText();
					try {
						int port = Integer.parseInt(portNumber.getText());
						// Only close the popup window with a valid port number
						if(port >= 1024) {
							client = new ClypeClient(user, host, port); 
							popupwindow.close();
						}
					} catch (NumberFormatException nfe) {
						System.out.println("Invalid port number input");
					}
	    		}
	    	}
	    	
	    });

		// Event handler for `Default` button
		Default.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ac) {
				userName.setText("Anon");
				hostName.setText("localhost");
				portNumber.setText("7000");
			}
		});

		userBox.getChildren().addAll(user_label, userName);
		hostBox.getChildren().addAll(host_label, hostName);
		portBox.getChildren().addAll(port_label, portNumber);
		buttons.getChildren().addAll(finish, Default);
		hbox.getChildren().addAll(userBox, hostBox, portBox);
		root.getChildren().addAll(hbox, buttons);

	    
	    popupwindow.setScene(scene);
	    popupwindow.showAndWait();
	    return client;
	       
	}

}
