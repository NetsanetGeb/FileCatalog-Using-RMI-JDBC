package filecatalog.rmi.jdbc.client.controller;

import filecatalog.rmi.jdbc.client.net.ServerConnection;
import java.io.File;



public class Controller {
	private ServerConnection serverConnection;

	public Controller() {
		this.serverConnection = new ServerConnection();
	}

	public ServerConnection getServerConnection() {
		return this.serverConnection;
	}

	public void connect(String host, int port) {
		this.serverConnection.connect(host, port);
	}

	public void disconnect() {
		this.serverConnection.disconnect();
	}

	public void sendMessage(String message) {
		this.serverConnection.sendMessage(message);
	}

	public void sendFile(File file, String outputName) {
		this.serverConnection.sendFile(file, outputName);
	}
}
