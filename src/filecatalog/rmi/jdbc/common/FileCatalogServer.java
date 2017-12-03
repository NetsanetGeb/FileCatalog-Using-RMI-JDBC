package filecatalog.rmi.jdbc.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FileCatalogServer extends Remote {

	public static final String SERVER_NAME_IN_REGISTRY = "CATALOG_SERVER";

	public static final String ACTION_DOWNLOAD = "downloaded";
	public static final String ACTION_REMOVE = "removed";
	public static final String ACTION_UPDATE = "updated";

	/** From client to server **/

	void incomingClient(FileCatalogServer catalogServer) throws RemoteException;

	void leavingClient(FileCatalogServer catalogServer) throws RemoteException;

	boolean login(FileCatalogServer catalogServer, String username, String password) throws RemoteException;

	boolean register(FileCatalogServer catalogServer, String username, String password) throws RemoteException;

	boolean unregister(FileCatalogServer catalogServer) throws RemoteException;

	boolean logout(FileCatalogServer catalogServer) throws RemoteException;

	boolean addFile(FileCatalogServer catalogServer, String name, long size, String access, String action)
			throws RemoteException;

	List<Object[]> getFiles(FileCatalogServer catalogServer) throws RemoteException;

	boolean downloadFile(FileCatalogServer catalogServer, String fileName) throws RemoteException;

	boolean removeFile(FileCatalogServer catalogServer, String fileName) throws RemoteException;

	boolean updateFile(FileCatalogServer catalogServer, String fileName, long length, String access, String action)
			throws RemoteException;

	boolean notifyFile(FileCatalogServer catalogServer, String fileName) throws RemoteException;

	/** From server to client **/

	void receiveNotification(String fileName, String username, String action) throws RemoteException;

}
