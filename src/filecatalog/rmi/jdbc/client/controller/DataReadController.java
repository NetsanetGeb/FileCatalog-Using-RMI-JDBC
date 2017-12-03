package filecatalog.rmi.jdbc.client.controller;

import filecatalog.rmi.jdbc.common.FileCatalogServer;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;



public class DataReadController extends UnicastRemoteObject implements FileCatalogServer {

	protected DataReadController() throws RemoteException {
	}


        // notification from server to client 
        @Override
	public void receiveNotification(String fileName, String username, String action) throws RemoteException {
		try {
			SystemTray tray = SystemTray.getSystemTray();
			Image image = Toolkit.getDefaultToolkit().getImage("");
			TrayIcon trayIcon = new TrayIcon(image, "Notification");
			trayIcon.setImageAutoSize(true);
			trayIcon.setToolTip("Notification");
			tray.add(trayIcon);
			trayIcon.displayMessage("Notification", username + " has " + action + " the file " + fileName,
					MessageType.INFO);
		} catch (Exception e) {
			System.err.println("Error while displaying notification.");
		}
	}
	@Override
	public boolean login(FileCatalogServer catalogServer, String username, String password) throws RemoteException {
		return false;
	}

	@Override
	public boolean register(FileCatalogServer catalogServer, String username, String password) throws RemoteException {
		return false;
	}

	@Override
	public boolean unregister(FileCatalogServer catalogServer) throws RemoteException {
		return false;
	}

	@Override
	public boolean logout(FileCatalogServer catalogServer) throws RemoteException {
		return false;
	}

	@Override
	public boolean addFile(FileCatalogServer catalogServer, String name, long size, String access, String action)
			throws RemoteException {
		return false;
	}

	@Override
	public List<Object[]> getFiles(FileCatalogServer catalogServer) throws RemoteException {
		return null;
	}

	@Override
	public boolean removeFile(FileCatalogServer catalogServer, String fileName) throws RemoteException {
		return false;
	}

	@Override
	public boolean updateFile(FileCatalogServer catalogServer, String fileName, long length, String access, String action)
			throws RemoteException {
		return false;
	}

	@Override
	public void incomingClient(FileCatalogServer catalogServer) throws RemoteException {
	}

	@Override
	public void leavingClient(FileCatalogServer catalogServer) throws RemoteException {
	}

	@Override
	public boolean downloadFile(FileCatalogServer catalogServer, String fileName) throws RemoteException {
		return false;
	}

	@Override
	public boolean notifyFile(FileCatalogServer catalogServer, String fileName) throws RemoteException {
		return false;
	}

	

	

}
