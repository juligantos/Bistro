package logic;

import java.util.List;

import comms.Api;
import comms.Message;
import entities.Order;
import gui.controllers.ServerConsoleController;
import logic.api.Router;
import logic.api.subjects.ConnectionSubject;
import logic.api.subjects.OrdersSubject;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
import logic.api.subjects.*;

/**
 * BistroServer class that extends AbstractServer to handle client-server
 * communication for a Bistro application.
 */
public class BistroServer extends AbstractServer {

	// ****************************** Instance variables******************************

	private static BistroServer serverInstance;

	private final BistroDataBase_Controller dbController;

	private final Router router;

	private final ServerLogger logger;

	// ******************************** Constructors***********************************

	/**
	 * Constructor for BistroServer class
	 * 
	 * @param port                    The port number for the server to listen on.
	 * @param serverConsoleController The controller for the server console UI.
	 */
	private BistroServer(int port, ServerConsoleController serverConsoleController) {
		super(port);
		this.dbController = BistroDataBase_Controller.getInstance();
		this.router = new Router();
		this.logger = new ServerLogger(serverConsoleController);
		this.dbController.setLogger(this.logger);
		// Register API subjects
		registerHandlers(this.router, this.dbController, this.logger);
	}

	/**
	 * Static method to get the singleton instance of BistroServer.
	 * 
	 * @param port                    The port number for the server to listen on.
	 * 
	 * @param serverConsoleController The controller for the server console UI.
	 * 
	 * @return The singleton instance of BistroServer.
	 */
	public static synchronized BistroServer getInstance(int port, ServerConsoleController serverConsoleController) {
		if (serverInstance == null) {
			serverInstance = new BistroServer(port, serverConsoleController);
		}
		return serverInstance;
	}

	// *************************************Getters and Setters***************************************

	/**
	 * Getter for the BistroDataBase_Controller associated with this server.
	 * 
	 * @return The BistroDataBase_Controller instance.
	 */
	public BistroDataBase_Controller getDBController() {
		return this.dbController;
	}

	/**
	 * Getter for the ServerLogger associated with this server.
	 * 
	 * @return The ServerLogger instance.
	 */
	public ServerLogger getLogger() {
		return this.logger;
	}

	// ****************************** Overridden methods from AbstractServer ******************************
	
	/**
	 * Method to handle messages received from clients.
	 * 
	 * @param msg The message received from the client.
	 * 
	 * @param client The connection to the client that sent the message.
	 */
	@Override
	protected void handleMessageFromClient(Object obj, ConnectionToClient client) {
		if (!(obj instanceof Message)) { // Validate message type
			return;
		}
		Message msg = (Message) obj;
		logger.log("Received message: " + msg.getId() + " from " + client);
		try { // Dispatch message to appropriate handler
			boolean handled = router.dispatch(msg, client);
			if (!handled) { // Unknown command
				client.sendToClient(new Message(Api.REPLY_UNKNOWN_COMMAND, msg.getId()));
				logger.log("Unknown command: " + msg.getId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Method called when the server starts listening for client connections.
	 */
	protected void serverStarted() {
		logger.log("Server started, listening for connections on port " + getPort());
		boolean isConnectToDB = dbController.openConnection();
		if (isConnectToDB) {
			logger.log("Connected to database successfully");
		} else {
			logger.log("Failed to connect to database");
		}
	}

	/**
	 * Method called when the server stops to close the database connection.
	 */
	protected void serverStopped() {
		logger.log("Server stopped");
		dbController.closeConnection();
	}

	/**
	 * Method to receive all client connections from abstract server and display
	 * them on the server console.
	 */
	public void showAllConnections() {
		Thread[] clientList = this.getClientConnections(); // Thread array of all clients
		logger.log("Number of connected clients: " + clientList.length);
		// Display each client's information
		for (Thread client : clientList) {
			logger.log("Client: " + client.toString());
		}
	}

	// ****************************** Instance methods ******************************

	/**
	 * Registers API subjects and their handlers with the router.
	 * 
	 * @param router       The Router instance to register handlers with.
	 * 
	 * @param dbController The database controller for handling data operations.
	 * 
	 * @param logger       The server logger for logging events.
	 */
	private void registerHandlers(Router router, BistroDataBase_Controller dbController, ServerLogger logger) {
		// Register API subjects
		ConnectionSubject.register(router, logger);
		UserSubject.register(router,dbController, logger);
		OrdersSubject.register(router, dbController, logger);
		WaitingListSubject.register(router, dbController, logger);
	}

}
// End of BistroServer.java