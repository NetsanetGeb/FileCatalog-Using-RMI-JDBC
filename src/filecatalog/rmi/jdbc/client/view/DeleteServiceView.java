package filecatalog.rmi.jdbc.client.view;

import filecatalog.rmi.jdbc.client.controller.ClientViewController;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DeleteServiceView extends TableCell<FileData, Boolean> {

	private Button deleteButton;
	private ClientViewController viewManager;

	public DeleteServiceView(ClientViewController viewManager) {

		this.viewManager = viewManager;

		
		this.deleteButton = new Button("Delete");
                this.deleteButton.setFont(new Font("Arial", 10));
                this.deleteButton.setTextFill(Color.web("#0076a3"));

		this.deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				try {
					FileData file = getTableView().getItems().get(getIndex());
					String fileName = file.getName();
					if (viewManager.getServer().removeFile(viewManager.getServerReader(), fileName)) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Successful Delete");
						alert.setHeaderText(null);
						alert.setContentText("The file on the catalog has been deleted.");
						alert.showAndWait();
						List<Object[]> filesList = viewManager.getServer().getFiles(viewManager.getServerReader());
						ListFileView listUpdated = new ListFileView(viewManager, filesList);
						Scene sceneUpdated = listUpdated.getScene();
						viewManager.getStage().setScene(sceneUpdated);
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Failure while Deleting");
						alert.setHeaderText(null);
						alert.setContentText(
								"Impossible to delete the file on the catalog, please make sure you have the rights and try again.");
						alert.showAndWait();
					}
				} catch (Exception e) {
					System.out.println("Error while deleting the file on the server.");
				}

			}
		});
	}

	@Override
	protected void updateItem(Boolean t, boolean empty) {
		super.updateItem(t, empty);
		if (!empty) {
			this.setGraphic(this.deleteButton);
		}
	}
}
