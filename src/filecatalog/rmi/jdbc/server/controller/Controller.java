package filecatalog.rmi.jdbc.server.controller;

import filecatalog.rmi.jdbc.common.FileCatalogServer;
import filecatalog.rmi.jdbc.common.exceptions.IOException;
import filecatalog.rmi.jdbc.server.integration.FileCatalogDAO;
import filecatalog.rmi.jdbc.server.net.FileTransferConnectionHandler;
import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class Controller extends UnicastRemoteObject implements FileCatalogServer {

	private final FileCatalogDAO catalog;
	private Map<FileCatalogServer, String> loggedUsers;
	private Map<FileCatalogServer, FileTransferConnectionHandler> clientHandlers;
	private Map<String, String> notifications;
	private FileCatalogServer serverWaitingForSocket;

	public synchronized void newClientHandler(FileTransferConnectionHandler clientHandler) {
		if (this.serverWaitingForSocket != null) {
			this.clientHandlers.put(this.serverWaitingForSocket, clientHandler);
			this.serverWaitingForSocket = null;
		} else {
			throw new IOException("Error while associating CatalogServer and ClientHandler.");
		}
	}

	public Controller() throws RemoteException {
		super();
		this.catalog = new FileCatalogDAO();
		this.loggedUsers = new HashMap<>();
		this.clientHandlers = new HashMap<>();
		this.notifications = new HashMap<>();
		this.serverWaitingForSocket = null;
	}



	@Override
	public synchronized boolean login(FileCatalogServer catalogServer, String username, String password)
			throws RemoteException {
		if (this.loggedUsers.containsKey(catalogServer)) {
			// Client already logged, need to logout
			return false;
		}
		if (this.loggedUsers.containsValue(username)) {
			// User already logged somewhere
			return false;
		}
		if (this.catalog.isAccount(username, password)) {
			// Ok
			this.loggedUsers.put(catalogServer, username);
			return true;
		} else {
			// Wrong username or password
			return false;
		}
	}

	@Override
	public synchronized boolean register(FileCatalogServer catalogServer, String username, String password)
			throws RemoteException {
		if (this.loggedUsers.containsKey(catalogServer)) {
			return false;
		}
		return this.catalog.createAccount(username, password);
	}

	@Override
	public synchronized boolean unregister(FileCatalogServer catalogServer) throws RemoteException {
		if (!this.loggedUsers.containsKey(catalogServer)) {
			
			return false;
		}
		if (this.catalog.deleteAccount(this.loggedUsers.get(catalogServer))) {
			
			this.removeFilesLinkedToUser(this.loggedUsers.get(catalogServer)); 																
			this.loggedUsers.remove(catalogServer); 
			return true;
		} else {
			
			return false;
		}
	}

	@Override
	public synchronized boolean logout(FileCatalogServer catalogServer) throws RemoteException {
		if (this.loggedUsers.containsKey(catalogServer)) {
			// Ok
			this.removeFilesLinkedToUser(this.loggedUsers.get(catalogServer));
			this.loggedUsers.remove(catalogServer); // Logout
			return true;
		} else {
			// Client not logged
			return false;
		}
	}

	@Override
	public synchronized boolean addFile(FileCatalogServer catalogServer, String name, long size, String access,
			String action) throws RemoteException {
		return this.catalog.addFile(name, size, this.loggedUsers.get(catalogServer), access, action);
	}

	@Override
	public synchronized List<Object[]> getFiles(FileCatalogServer catalogServer) throws RemoteException {
		if (!this.loggedUsers.containsKey(catalogServer)) {
			// Client must be logged to access the catalog
			return null;
		} else {
			// Ok
			return this.catalog.getFiles(this.loggedUsers.get(catalogServer));
		}
	}

	@Override
	public synchronized boolean downloadFile(FileCatalogServer catalogServer, String fileName) throws RemoteException {
		this.clientHandlers.get(catalogServer).sendFile(fileName);
		this.checkNotification(fileName, this.loggedUsers.get(catalogServer), FileCatalogServer.ACTION_DOWNLOAD);
		return true;
	}

	@Override
	public synchronized boolean removeFile(FileCatalogServer catalogServer, String fileName) throws RemoteException {
		boolean dataDeleted = this.catalog.deleteFile(fileName, this.loggedUsers.get(catalogServer)); // File
																										// removed
																										// from
																										// the
																										// database
		boolean fileDeleted = false;
		if (dataDeleted) {
			
			File fileToDelete = new File(FileTransferConnectionHandler.FILES_DIRECTORY + fileName);
			fileDeleted = fileToDelete.delete();
		}
		if (fileDeleted && dataDeleted) {
			
			this.checkNotification(fileName, this.loggedUsers.get(catalogServer), FileCatalogServer.ACTION_REMOVE);
			return true;
		} else {
			// Problem while removing file
			return false;
		}
	}

	@Override
	public synchronized boolean updateFile(FileCatalogServer catalogServer, String fileName, long length, String access,
			String action) throws RemoteException {
		if (this.catalog.updateFile(fileName, length, this.loggedUsers.get(catalogServer), access, action)) {
			// Update in database ok
			this.checkNotification(fileName, this.loggedUsers.get(catalogServer), FileCatalogServer.ACTION_UPDATE);
			return true;
		} else {
			// Problem while updating database
			return false;
		}
	}

	@Override
	public synchronized boolean notifyFile(FileCatalogServer catalogServer, String fileName) throws RemoteException {
		if (this.catalog.controlNotify(this.loggedUsers.get(catalogServer), fileName)) {
			// Ok if client owner of the file
			this.notifications.put(fileName, this.loggedUsers.get(catalogServer));
			return true;
		} else {
			return false;
		}
	}

	private synchronized FileCatalogServer getCatalogServer(String username) {
		for (Entry<FileCatalogServer, String> association : this.loggedUsers.entrySet()) {
			if (association.getValue().equals(username)) {
				return association.getKey();
			}
		}
		return null;
	}

	private synchronized void removeFilesLinkedToUser(String username) {
		for (Entry<String, String> association : new HashSet<Entry<String, String>>(this.notifications.entrySet())) {
			if (association.getValue().equals(username)) {
				this.notifications.remove(association.getKey());
			}
		}
	}

	private synchronized void checkNotification(String fileName, String username, String action) {
		for (Entry<String, String> association : this.notifications.entrySet()) {
			if (association.getKey().equals(fileName) && !association.getValue().equals(username)) {
				try {
					this.getCatalogServer(association.getValue()).receiveNotification(fileName, username, action);
					return;
				} catch (RemoteException e) {
					System.err.println("Error while sending notification.");
				}
			}
		}
	}

        
        @Override
	public synchronized void incomingClient(FileCatalogServer catalogServer) throws RemoteException {
		this.serverWaitingForSocket = catalogServer;
	}

	@Override
	public synchronized void leavingClient(FileCatalogServer catalogServer) throws RemoteException {
		this.loggedUsers.remove(catalogServer);
		this.clientHandlers.remove(catalogServer);
	}
	/** From server to client **/

	@Override
	public void receiveNotification(String fileName, String username, String action) throws RemoteException {
	}

}
