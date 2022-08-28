package application;
	



import java.io.File;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Pair;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import main.ClypeClient;

/**
 * A class that designs the chat room for the users
 * 
 * @author Yijing
 *
 */
public class Main extends Application {
	public static Label display; // Display the messages exchanged between users
	public static Label friendList; // A list of users online
	private ClypeClient client;



	
	public class ClientSideServerListener implements Runnable{
	    ClypeClient client; // Representing a client that calls this class to make a threaded Runnable object.
	    Stage stage;
	    /**
	     * Set the ClypeClient instance variable `client` 
	     * to the value of the `client` that fed as a parameter to it.
	     * @param client ClypeClient that being fed in this constructor
	     */
	    public ClientSideServerListener(ClypeClient client, Stage stage) {
	        this.client = client;
	        this.stage = stage;
	    }
		/**
		 * Override run() for the Runnable
		 */
	    @Override
	    public void run() {
	        while(!client.closeConnection) {
	            client.receiveData(); // Receive data from the server
	            Pair<Boolean, String> data = client.printData(); // Get the message content
				Pair<String, String> textFile = client.printText(); // Get the text file content
	            Pair<File, String> picture_file = client.printPicture(); // Get the picture content
				// Update the information received from the server via another thread at some unspecified time in the future.
	            Platform.runLater(new Runnable() {
	                @Override
	                public void run() {
						if (data != null) {
							if (data.getKey()) {
								synchronized(Main.friendList){
									friendList.setText("  ONLINE USERS\n*------------------*\n" + data.getValue());
								}      
							} else {
								synchronized(Main.display){
									display.setText(display.getText() + "\n"+ data.getValue());
								}        	
							}
						}
						if (textFile != null) {
							PopupText popupText = new PopupText();
							popupText.user = textFile.getValue(); // Update the username for the popup window
							popupText.display(textFile.getKey());
						}
						if(picture_file!=null) {
							PopupImage popupImage = new PopupImage();
							popupImage.user = picture_file.getValue(); // Update the username for the popup window
							popupImage.display(picture_file.getKey());
						}
					}
	                
	            });
	        } // close the connection for the client after server closed connection at some unspecified time in the future.
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
					ErrorPopup ep = new ErrorPopup("Server Closed Connection!!!");
					ep.display();
                	stage.close();
					Platform.exit();
					System.exit(0);
                }
           });
	    }

	}

	/**
	 * Override start() for Application
	 */
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Clype");
    	FileChooser pictureChooser = new FileChooser(); 
		FileChooser fileChooser = new FileChooser();
		// Add extensions for picture files
    	pictureChooser.getExtensionFilters().add(
    	         new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
		// Add extensions for text files
		fileChooser.getExtensionFilters().add(
				 new ExtensionFilter("Text Files", "*.txt"));
		Popup popup = new Popup();
		client = popup.display();
		// If client closes the popup without entering, close the stage
		if (client == null) {
			primaryStage.close();
			Platform.exit();
			System.exit(0);
		}
		// If client fails connection, close the stage
		boolean fail_connection = client.start();
		if (fail_connection) {
			primaryStage.close();
			Platform.exit();
			System.exit(0);
		}
        ClientSideServerListener clientListener = new ClientSideServerListener(client, primaryStage);
        Thread clientThread = new Thread(clientListener);
		// Use clientThread to handle interaction between the client and the server
        clientThread.start();
		try {
			HBox root = new HBox();
			VBox left = new VBox();
			VBox left_buttons = new VBox();
			VBox right = new VBox();
			// Vbox for `Share Picture` and `Share Text File` botton
			left_buttons.setAlignment(Pos.CENTER);
			left_buttons.setSpacing(5);
			//Vbox for friendlist and `Leave` button
			right.setPadding(new Insets(10, 0, 20, 0));
			right.setAlignment(Pos.CENTER);
			right.getStyleClass().add("friednList");
			right.prefWidthProperty().bind(root.widthProperty().multiply(0.15));
			
			left.setPadding(new Insets(10,10,30,10));
			left.getStyleClass().add("left");
			left.prefWidthProperty().bind(root.widthProperty().multiply(0.85));
			left.setAlignment(Pos.CENTER);
			// Label for displaying messages
			display = new Label();
			display.getStyleClass().add("display");
			display.setAlignment(Pos.TOP_LEFT);
			display.setPadding(new Insets(0,10,10,10));
			display.setWrapText(true);
			display.prefHeightProperty().bind(left.heightProperty().multiply(0.9));
			display.prefWidthProperty().bind(left.widthProperty().subtract(20));
			// ScrollPane to scroll on the label content
			ScrollPane scrollPane = new ScrollPane();
			scrollPane.setContent(display);	
			scrollPane.setFitToWidth(true);

			HBox bottom = new HBox();
			bottom.setStyle("-fx-spacing: 10px;");
			// bottom.setPadding(new Insets(20,0,0,0));//Insets(top,left,bottom,right)
			bottom.prefWidthProperty().bind(left.widthProperty().subtract(20));
			bottom.prefHeightProperty().bind(left.heightProperty().multiply(0.1));
			bottom.setAlignment(Pos.CENTER);

			Button pictureButton = new Button();
			// pictureButton.setMaxSize(90,40);
			pictureButton.setText("Share Picture");
			pictureButton.setWrapText(true);

			Button fileButton = new Button();
			// fileButton.setMaxSize(90,40);
			fileButton.setText("Share Text File");
			fileButton.setWrapText(true);
			// TextField for user to type in
			TextField text = new TextField();
			text.prefWidthProperty().bind(left.widthProperty().subtract(180));
			text.prefHeightProperty().bind(bottom.heightProperty());

			Button sendButton = new Button();
			sendButton.setText("SEND");
			sendButton.setWrapText(true);

			Button leaveButton = new Button();
			leaveButton.setText("Leave");
			leaveButton.setWrapText(true);

			friendList = new Label();
			friendList.setText("  ONLINE USERS:\n");
			friendList.getStyleClass().add("friednList");
			friendList.setPadding(new Insets(10,10,10,10));
			friendList.setAlignment(Pos.TOP_LEFT);
			friendList.setWrapText(true);
			friendList.prefHeightProperty().bind(right.heightProperty().subtract(leaveButton.getHeight() + 20));
			friendList.prefWidthProperty().bind(right.widthProperty());

			left_buttons.getChildren().addAll(pictureButton, fileButton);
			bottom.getChildren().add(left_buttons);
			bottom.getChildren().add(text);
			bottom.getChildren().add(sendButton);
			left.getChildren().addAll(scrollPane);
			left.getChildren().add(bottom);
			right.getChildren().addAll(friendList, leaveButton);

			VBox.setMargin(scrollPane, new Insets(0, 0, 5, 0));
			VBox.setMargin(bottom, new Insets(5, 0, 0, 0));

			root.getChildren().add(left);
			root.getChildren().add(right);
			
			/**
			 * Event handler for `Leave` button to close the connection
			 */
			leaveButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent ac) {
					client.closeClientConnection();
					primaryStage.close();
					Platform.exit();
					System.exit(0);
				}
			});

			/**
			 * Event handler to send a picture to all the users in the chat room
			 */
			pictureButton.setOnAction(new EventHandler<ActionEvent>() {
		        @Override
		        public void handle(ActionEvent ac) {
		             File file = pictureChooser.showOpenDialog(primaryStage);
					 if (file != null) {
						client.sendPictureToServer(file);
					 }	
						
		        }
		    });
			
			/**
			 * Event handler to send a text file to all the users in the chat room
			 */
			fileButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent ac) {
					File file = fileChooser.showOpenDialog(primaryStage);	
					// client.sendPictureToServer(file);
					if (file != null) {
						client.sendFileToServer(file);
					}
				}
			});

			/**
			 * Event handler to send a message typed in the `text` TextField to all the users in the chat room
			 */
			sendButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent ac) {
					String message = text.getText();
					if (!message.equals("")) {
						client.readClientMessage(message);
						client.sendData();
						text.setText("");
					}
			     }
				});

			Scene scene = new Scene(root,900,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Overrde stop() for Application to close the connection and notify the server beforng leaving
	 */
	@Override
	public void stop(){
		Platform.runLater(new Runnable() {
			@Override
			public void run () {
				client.closeClientConnection();
			}
		});
		System.out.println("Stage is closing");
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
	
}
