package filecatalog.rmi.jdbc.client.startup;


import filecatalog.rmi.jdbc.client.controller.ClientViewController;
import filecatalog.rmi.jdbc.client.view.Login;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientMain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		ClientViewController viewManager = new ClientViewController(primaryStage);		
                Login login = new Login(viewManager);
	       Scene loginScene = login.getScene();
		primaryStage.setScene(loginScene);
		primaryStage.show();
	}

}
