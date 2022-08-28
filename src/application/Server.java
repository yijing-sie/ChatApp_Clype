package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import main.ClypeServer;

/**
 * A class that designs the chat room for the users
 * 
 * @author Yijing
 *
 */
public class Server extends Application {
    private ClypeServer clypeServer;
    private Label label;

    public class ServerListener implements Runnable{
        ClypeServer clypeServer;
        Stage stage;
        // Stage stage;
        public ServerListener(ClypeServer clypeServer, Stage stage) {
            this.clypeServer = clypeServer;
            this.stage = stage;
        }

        @Override
        public void run(){
            String m = clypeServer.start();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (!m.equals("socket closed")) {
                        ErrorPopup ep = new ErrorPopup("SERVER : " + m);
                        ep.display();
                    }            
                	stage.close();
					Platform.exit();
					System.exit(0);
                }
           });
        }
    }



    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ClypeServer");
        ServerPopup serverPopup = new ServerPopup();
		int port = serverPopup.display();
        if (port < 1024) {
            primaryStage.close();
            Platform.exit();
            System.exit(0);
        }
        clypeServer = new ClypeServer(port, this);
        ServerListener serverListener = new ServerListener(clypeServer, primaryStage);
        Thread serverThread = new Thread(serverListener);
        serverThread.start();
        try{
            VBox root = new VBox();
            root.setPadding(new Insets(20, 20, 20, 20));
            root.setSpacing(10);
            root.setAlignment(Pos.CENTER);

            label = new Label();
            label.setText("Server running...");
            label.setAlignment(Pos.TOP_LEFT);
            label.getStyleClass().add("display");
            label.setPadding(new Insets(3, 3, 3, 3));
            label.prefHeightProperty().bind(root.heightProperty().multiply(0.9));
            label.prefWidthProperty().bind(root.widthProperty().subtract(40));
            ScrollPane scrollPane = new ScrollPane();
			scrollPane.setContent(label);	
			scrollPane.setFitToWidth(true);

            Button close = new Button();
            close.setText("Close Connection");
            close.setWrapText(true);
            // close.setMaxSize(70, 60);
            root.getChildren().addAll(scrollPane, close);

            close.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent ac) {
                    System.out.println("close connection button hit");          
                    clypeServer.closeClypeServer();
                    // primaryStage.close();
                    // Platform.exit();
                    // System.exit(0);
                }
            });

            Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

    /**
     * Update the information on the server window with new `message`
     * @param message new information to be added
     */
    public void setLabel(String message) {
        Platform.runLater(new Runnable() {
         @Override
         public void run(){
            String oldLabel = label.getText();
            label.setText(oldLabel + "\n" + message);
         }   
        });
    }

    /**
     * Event handler for button `close connection`
     * Close all the connection from the server side and the server socket safely
     */
    @Override
	public void stop() {
        // System.exit(0);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {            
                clypeServer.closeClypeServer();
            }
       });
	}

	public static void main(String[] args) {
		launch(args);
	}
}