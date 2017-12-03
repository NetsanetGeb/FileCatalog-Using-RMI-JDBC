
package filecatalog.rmi.jdbc.client.view;


import filecatalog.rmi.jdbc.client.controller.ClientViewController;
import filecatalog.rmi.jdbc.common.Constants;
import java.util.List;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class LoggedInView {
    
        private Scene scene;
	public LoggedInView(ClientViewController viewManager) {

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

		Label welcomeLabel = new Label(" to the catalog");
                Image imageWelcome = new Image(getClass().getResourceAsStream("wel.png"), 120, 140, true, false);
                welcomeLabel.setGraphic(new ImageView(imageWelcome));
                welcomeLabel.setFont(new Font("Arial", 22));
                welcomeLabel.setTextFill(Color.web("#0076a3"));
		root.add(welcomeLabel, 0, 0);


		Button listFiles = new Button("List File In Catalog");
                listFiles.setPrefSize(170, 20);
                listFiles.setFont(new Font("Arial", 15));
                listFiles.setTextFill(Color.web("#0076a3"));
		root.add(listFiles, 0, 1);
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
                
                Button uploadButton = new Button("Upload file to catalog");
                 uploadButton.setPrefSize(170, 20);
                uploadButton.setFont(new Font("Arial", 15));
                 uploadButton.setTextFill(Color.web("#0076a3"));
		root.add(uploadButton, 0, 2);
		uploadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Choose the file to add to the catalog.");
					File file = fileChooser.showOpenDialog(viewManager.getStage());
					String accessValue = null;
					String actionValue = null;
					if (file != null) {
						List<String> dialogData = new ArrayList<>();
						dialogData.add(Constants.ACCESS_PUBLIC);
						dialogData.add(Constants.ACCESS_PRIVATE);
						ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
						dialog.setTitle("Access permission");
						dialog.setHeaderText("Select the access permission.");
						Optional<String> result = dialog.showAndWait();
						if (result.isPresent()) {
							accessValue = result.get();
						}
					}
					boolean actionValid = true;
					if (accessValue != null && accessValue.equals(Constants.ACCESS_PUBLIC)) {
						actionValid = false;
						List<String> dialogData = new ArrayList<>();
						dialogData.add(Constants.ACTION_READ);
						dialogData.add(Constants.ACTION_WRITE);
						ChoiceDialog dialog = new ChoiceDialog(dialogData.get(0), dialogData);
						dialog.setTitle("Action permission");
						dialog.setHeaderText("Select the action permission for the other users.");
						Optional<String> result = dialog.showAndWait();
						if (result.isPresent()) {
							actionValue = result.get();
							actionValid = true;
						}
					}
					if (file != null && accessValue != null && actionValid) {
						if (viewManager.getServer().addFile(viewManager.getServerReader(), file.getName(),
								file.length(), accessValue, actionValue)) {
							viewManager.getController().sendFile(file, file.getName());
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Upload success");
							alert.setHeaderText(null);
							alert.setContentText("The file has been added to the catalog.");
							alert.showAndWait();
							List<Object[]> filesList = viewManager.getServer().getFiles(viewManager.getServerReader());
							ListFileView listUpdated = new ListFileView(viewManager, filesList);
							Scene sceneUpdated = listUpdated.getScene();
							viewManager.getStage().setScene(sceneUpdated);
						} else {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Upload failure");
							alert.setHeaderText(null);
							alert.setContentText(
									"Impossible to add the file to the catalog.\nMake sure you are logged and try again with another name.");
							alert.showAndWait();
						}
					}
				} catch (Exception exception) {
					exception.printStackTrace();
					System.err.println("File adding to the catalog failed.");
				}
			}
		});

                

		Button unregister = new Button("Unregister");
                 unregister.setPrefSize(170, 20);
                unregister.setFont(new Font("Arial", 15));
                unregister.setTextFill(Color.web("#0076a3"));
		root.add(unregister, 0, 3);
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

		Button logout = new Button("Logout");
                logout.setPrefSize(170, 20);
                logout.setFont(new Font("Arial", 15));
                logout.setTextFill(Color.web("#0076a3"));
		root.add(logout, 0, 4);
		logout.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					if (viewManager.getServer().logout(viewManager.getServerReader())) {
						Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Logout success");
						alert.setHeaderText(null);
						alert.setContentText("You are now logged out.");
						alert.showAndWait();
                                                Login login = new Login(viewManager);
				                Scene loginScene = login.getScene();
				                viewManager.getStage().setScene(loginScene);
					} else {
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setTitle("Logout failure");
						alert.setHeaderText(null);
						alert.setContentText("Impossible to logout.\nMake sure you are logged.");
						alert.showAndWait();
					}
				} catch (Exception exception) {
					System.err.println("Error while trying to log out.");
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
