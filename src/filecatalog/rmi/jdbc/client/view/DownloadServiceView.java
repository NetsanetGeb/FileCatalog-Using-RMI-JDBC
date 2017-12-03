package filecatalog.rmi.jdbc.client.view;

import filecatalog.rmi.jdbc.client.controller.ClientViewController;
import java.io.File;
import java.rmi.RemoteException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

public class DownloadServiceView extends TableCell<FileData, Boolean> {

	private Button downloadButton;
	private ClientViewController viewManager;

	public DownloadServiceView(ClientViewController viewManager) {

		this.viewManager = viewManager;
		this.downloadButton = new Button("Download");
                this.downloadButton.setFont(new Font("Arial", 10));
                this.downloadButton.setTextFill(Color.web("#0076a3"));
		this.downloadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				try {
					FileData file = getTableView().getItems().get(getIndex());
					String fileName = file.getName();
					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Choose folder to place the file.");
					fileChooser.setInitialFileName(fileName);
					File fileToDownload = fileChooser.showSaveDialog(viewManager.getStage());
					if (fileToDownload != null) {
						viewManager.getController().getServerConnection().setDownloadFile(fileToDownload);
						if (viewManager.getServer().downloadFile(viewManager.getServerReader(), fileName)) {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Successful Download");
							alert.setHeaderText(null);
							alert.setContentText("The file has been downloaded from the catalog server.");
							alert.showAndWait();
						} else {
							Alert alert = new Alert(AlertType.ERROR);
							alert.setTitle("Failure while Downloading");
							alert.setHeaderText(null);
							alert.setContentText("Impossible to download the file from the catalog server, please try again.");
							alert.showAndWait();
						}
					}
				} catch (RemoteException e) {
					e.printStackTrace();
					System.err.println("Failure while trying to download file.");
				}
			}
		});
	}

	@Override
	protected void updateItem(Boolean t, boolean empty) {
		super.updateItem(t, empty);
		if (!empty) {
			this.setGraphic(this.downloadButton);
		}
	}
}
