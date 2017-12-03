package filecatalog.rmi.jdbc.client.view;

import filecatalog.rmi.jdbc.client.controller.ClientViewController;
import filecatalog.rmi.jdbc.common.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.util.Callback;

public class ListFileView {

	private Scene scene;

	public ListFileView(ClientViewController viewManager, List<Object[]> files) {
            
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

		Label listLabel = new Label("List of files in the catalog");
                listLabel.setFont(new Font("Arial", 30));
                listLabel.setTextFill(Color.web("#0076a3"));
		root.add(listLabel, 0, 0);

		final ObservableList<filecatalog.rmi.jdbc.client.view.FileData> data = FXCollections.observableArrayList();
		for (Object[] file : files) {
			String actionPerm = "";
			if (file[4] != null) {
				actionPerm = file[4].toString();
			}
			filecatalog.rmi.jdbc.client.view.FileData filePrepared = new filecatalog.rmi.jdbc.client.view.FileData(file[0].toString(), file[1].toString(),
					file[2].toString(), file[3].toString(), actionPerm);
			data.add(filePrepared);
		}
		TableView<FileData> table = new TableView<>();
		table.setEditable(false);
		TableColumn name = new TableColumn("Name");
		name.setCellValueFactory(new PropertyValueFactory<FileData, String>("name"));
		TableColumn size = new TableColumn("Size");
		size.setCellValueFactory(new PropertyValueFactory<FileData, String>("size"));
		TableColumn owner = new TableColumn("Owner");
		owner.setCellValueFactory(new PropertyValueFactory<FileData, String>("owner"));
		TableColumn access = new TableColumn("Access");
		access.setCellValueFactory(new PropertyValueFactory<FileData, String>("access"));
		TableColumn action = new TableColumn("Action");
		action.setCellValueFactory(new PropertyValueFactory<FileData, String>("action"));
		TableColumn download = new TableColumn("Download");
		download.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<FileData, Boolean>, ObservableValue<Boolean>>() {
					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<FileData, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});
		download.setCellFactory(new Callback<TableColumn<FileData, Boolean>, TableCell<FileData, Boolean>>() {
			@Override
			public TableCell<FileData, Boolean> call(TableColumn<FileData, Boolean> p) {
				return new DownloadServiceView(viewManager);
			}
		});
		TableColumn update = new TableColumn("Update");
		update.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<FileData, Boolean>, ObservableValue<Boolean>>() {
					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<FileData, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});
		update.setCellFactory(new Callback<TableColumn<FileData, Boolean>, TableCell<FileData, Boolean>>() {
			@Override
			public TableCell<FileData, Boolean> call(TableColumn<FileData, Boolean> p) {
				return new EditServiceView(viewManager);
			}
		});
		TableColumn delete = new TableColumn("Delete");
		delete.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<FileData, Boolean>, ObservableValue<Boolean>>() {
					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<FileData, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});
		delete.setCellFactory(new Callback<TableColumn<FileData, Boolean>, TableCell<FileData, Boolean>>() {
			@Override
			public TableCell<FileData, Boolean> call(TableColumn<FileData, Boolean> p) {
				return new DeleteServiceView(viewManager);
			}
		});
		TableColumn notify = new TableColumn("Notify");
		notify.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<FileData, Boolean>, ObservableValue<Boolean>>() {
					@Override
					public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<FileData, Boolean> p) {
						return new SimpleBooleanProperty(p.getValue() != null);
					}
				});
		notify.setCellFactory(new Callback<TableColumn<FileData, Boolean>, TableCell<FileData, Boolean>>() {
			@Override
			public TableCell<FileData, Boolean> call(TableColumn<FileData, Boolean> p) {
				return new NotificationServiceView(viewManager);
			}
		});
		table.setItems(data);
		table.getColumns().addAll(name, size, owner, access, action, download, update, delete, notify);
		table.setMinWidth(780);
		root.add(table, 0, 2);

		Button uploadButton = new Button("Upload File");
                uploadButton.setFont(new Font("Arial", 15));
                uploadButton.setTextFill(Color.web("#0076a3"));
		root.add(uploadButton, 0, 3);
		uploadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Choose file to upload to catalog.");
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
							alert.setTitle("Successful Upload");
							alert.setHeaderText(null);
							alert.setContentText("The file has been uploaded to the catalog.");
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
									"Impossible to upload file to the catalog.\nMake sure you are logged in and try with other file name.");
							alert.showAndWait();
						}
					}
				} catch (Exception exception) {
					exception.printStackTrace();
					System.err.println("File uploading failed.");
				}
			}
		});

		
		Button backButton = new Button("Back");
                backButton.setFont(new Font("Arial", 15));
                backButton.setTextFill(Color.web("#0076a3"));
		root.add(backButton, 0, 5);
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				LoggedInView loggedin = new LoggedInView(viewManager);
			        Scene loggedinScene = loggedin.getScene();
			        viewManager.getStage().setScene(loggedinScene);
			}
		});

		this.scene = new Scene(border, 900, 500);
		this.scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

	}

	public Scene getScene() {
		return this.scene;
	}

}
