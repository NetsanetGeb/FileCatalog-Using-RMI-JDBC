package filecatalog.rmi.jdbc.client.view;

import filecatalog.rmi.jdbc.client.controller.ClientViewController;
import filecatalog.rmi.jdbc.common.Constants;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class EditServiceView extends TableCell<FileData, Boolean> {

	private Button updateButton;
	private ClientViewController viewManager;

	public EditServiceView(ClientViewController viewManager) {

		this.viewManager = viewManager;

		
		this.updateButton = new Button("Update");
                this.updateButton.setFont(new Font("Arial", 10));
                this.updateButton.setTextFill(Color.web("#0076a3"));
		this.updateButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				try {
					FileData file = getTableView().getItems().get(getIndex());
					String fileName = file.getName();
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Choose the file to update to the catalog.");
					File fileToUpdate = fileChooser.showOpenDialog(viewManager.getStage());
					if (fileToUpdate != null) {
						String accessValue = null;
						String actionValue = null;
						if (fileToUpdate != null) {
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
						if (fileToUpdate != null && accessValue != null && actionValid) {
							if (viewManager.getServer().updateFile(viewManager.getServerReader(), fileName,
									fileToUpdate.length(), accessValue, actionValue)) {
								viewManager.getController().sendFile(fileToUpdate, fileName);
								Alert alert = new Alert(AlertType.INFORMATION);
								alert.setTitle("Successfull Update");
								alert.setHeaderText(null);
								alert.setContentText("File on the catalog server has been updated.");
								alert.showAndWait();
								List<Object[]> filesList = viewManager.getServer()
										.getFiles(viewManager.getServerReader());
								ListFileView listUpdated = new ListFileView(viewManager, filesList);
								Scene sceneUpdated = listUpdated.getScene();
								viewManager.getStage().setScene(sceneUpdated);
							} else {
								Alert alert = new Alert(AlertType.ERROR);
								alert.setTitle("Update failure");
								alert.setHeaderText(null);
								alert.setContentText(
										"Impossible to update the file on the catalog, please make sure you have access permission rights and try again.");
								alert.showAndWait();
							}
						}
					}
				} catch (Exception e) {
					System.err.println("Error while updating file on server.");
				}
			}
		});
	}

	@Override
	protected void updateItem(Boolean t, boolean empty) {
		super.updateItem(t, empty);
		if (!empty) {
			this.setGraphic(this.updateButton);
		}
	}
}
