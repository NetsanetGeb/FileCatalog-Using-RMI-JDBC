
package filecatalog.rmi.jdbc.client.view;


import filecatalog.rmi.jdbc.client.controller.ClientViewController;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RegisterdView {
   
    
        private Scene scene;
	public RegisterdView(ClientViewController viewManager) {

                BorderPane border = new BorderPane();
		GridPane root = new GridPane();
                HBox tbox= new HBox();
                HBox lbox= new HBox();
                HBox bbox= new HBox();
                VBox rbox= new VBox();
                        tbox.setPadding(new Insets(15, 12, 15, 12));
                        lbox.setPadding(new Insets(15, 12, 15, 12));
                        bbox.setPadding(new Insets(15, 12, 15, 12));
                        rbox.setPadding(new Insets(15, 12, 15, 12)); 
                        tbox.setStyle("-fx-background-color: #336699;");                
                        lbox.setStyle("-fx-background-color: #336699;");
                        bbox.setStyle("-fx-background-color: #336699;");  
                        rbox.setStyle("-fx-background-color: #336699;");   
                border.setTop(tbox);
                border.setLeft(lbox);
                border.setBottom(bbox);
                border.setRight(rbox);
                
                border.setCenter(root);
		root.setAlignment(Pos.CENTER);
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(25, 25, 25, 25));

		Label registredLabel = new Label(" Registered In Catalog");
                Image imageWelcome = new Image(getClass().getResourceAsStream("I1.png"), 110, 150, true, false);
                registredLabel.setGraphic(new ImageView(imageWelcome));
                registredLabel.setFont(new Font("Arial", 16));
                registredLabel.setTextFill(Color.web("#0076a3"));
		root.add(registredLabel, 0, 0);
                
             
                
                
                Button loginButton = new Button("Login");
                 loginButton.setPrefSize(160, 20);
                loginButton.setFont(new Font("Arial", 15));
                loginButton.setTextFill(Color.web("#0076a3"));
		root.add(loginButton, 0, 2);
		loginButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Login login = new Login(viewManager);
				Scene loginScene = login.getScene();
				viewManager.getStage().setScene(loginScene);
			}
		});

                

		Button listFiles = new Button("List File In Catalog");
                listFiles.setPrefSize(160, 20);
                listFiles.setFont(new Font("Arial", 15));
                listFiles.setTextFill(Color.web("#0076a3"));
		root.add(listFiles, 0, 3);
		listFiles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					List<Object[]> filesList = viewManager.getServer().getFiles(viewManager.getServerReader());
					if (filesList == null) {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Access catalog failure");
						alert.setHeaderText(null);
						alert.setContentText(
								"Impossible to access the catalog, make sure you are logged and try again.");
						alert.showAndWait();
					} else {
						ListFileView files = new ListFileView(viewManager, filesList);
						Scene sceneFiles = files.getScene();
						viewManager.getStage().setScene(sceneFiles);
					}
				} catch (Exception exception) {
					System.err.println("Error while getting file list of the catalog.");
				}

			}
		});

                

		Button unregister = new Button("Unregister");
                 unregister.setPrefSize(160, 20);
                unregister.setFont(new Font("Arial", 15));
                unregister.setTextFill(Color.web("#0076a3"));
		root.add(unregister, 0, 4);
		unregister.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					if (viewManager.getServer().unregister(viewManager.getServerReader())) {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Unregister success");
						alert.setHeaderText(null);
						alert.setContentText("You are now unregistered.");
						alert.showAndWait();
					} else {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Unregister failure");
						alert.setHeaderText(null);
						alert.setContentText("Impossible to unregister.\nMake sure you are logged.");
						alert.showAndWait();
					}
				} catch (Exception exception) {
					System.err.println("Unregister request failed.");
				}
			}
		});


		this.scene = new Scene(border, 400, 400);
		this.scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
	}

	public Scene getScene() {
		return this.scene;
	}

}
