package filecatalog.rmi.jdbc.client.view;


import filecatalog.rmi.jdbc.client.controller.ClientViewController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;


public class NotificationServiceView extends TableCell<FileData, Boolean> {

	private Button notifyButton;
	private ClientViewController viewManager;

	public NotificationServiceView(ClientViewController viewManager) {

		this.viewManager = viewManager;
		this.notifyButton = new Button("Notification");
                this.notifyButton.setFont(new Font("Arial", 10));
               this.notifyButton.setTextFill(Color.web("#0076a3"));

		this.notifyButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent t) {
				try {
					FileData file = getTableView().getItems().get(getIndex());
					String fileName = file.getName();
					if (viewManager.getServer().notifyFile(viewManager.getServerReader(), fileName)) {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Successful Notification");
						alert.setHeaderText(null);
						alert.setContentText("Notified succesfully about the file.");
						alert.showAndWait();
					} else {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error Notification");
						alert.setHeaderText(null);
						alert.setContentText(
								"Impossible to launch the notification.\nYou need to be the owner of the file.");
						alert.showAndWait();
					}
				} catch (Exception e) {
					System.out.println("Impossible to launch Notification");
				}

			}
		});
	}

	@Override
	protected void updateItem(Boolean t, boolean empty) {
		super.updateItem(t, empty);
		if (!empty) {
			this.setGraphic(this.notifyButton);
		}
	}
}
