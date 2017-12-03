package filecatalog.rmi.jdbc.client.controller;

import filecatalog.rmi.jdbc.common.FileCatalogServer;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientViewController {

	private static final int PORT = 8080;
	private static final String ADDRESS = "localhost";

	private FileCatalogServer server;
	private FileCatalogServer serverReader;
	private Stage stage;
	private Controller controller;

	public ClientViewController(Stage stage) throws MalformedURLException, RemoteException, NotBoundException {
		
		this.server = (FileCatalogServer) Naming.lookup("rmi://localhost/" + FileCatalogServer.SERVER_NAME_IN_REGISTRY);
		this.serverReader = new DataReadController(); 
		this.server.incomingClient(this.serverReader); 
		this.controller = new Controller();
		this.controller.connect(ADDRESS, PORT); 
		this.stage = stage;
		this.initStage();
	}

	public FileCatalogServer getServer() {
		return this.server;
	}

	public FileCatalogServer getServerReader() {
		return this.serverReader;
	}

	public Stage getStage() {
		return this.stage;
	}

	public Controller getController() {
		return this.controller;
	}

	public void initStage() {
		this.stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				try {
					controller.disconnect();
					server.leavingClient(serverReader);
					Platform.exit();
					System.exit(0);
				} catch (Exception exception) {
					System.err.println("Error while client leaving.");
				}

			}
		});
	}

}
