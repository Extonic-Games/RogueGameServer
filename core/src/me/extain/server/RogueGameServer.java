package me.extain.server;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;

import Utils.ConsoleLog;
import me.extain.server.Player.Account;
import me.extain.server.Player.Player;
import me.extain.server.network.NetworkHandler;

public class RogueGameServer extends ApplicationAdapter {

	private Server server;

	private boolean isRunning = false;

	private ServerNetworkListener serverNetworkListener;

	private GameObjectManager gameObjectManager;

	private static RogueGameServer instance = null;

	private ServerWorld serverWorld;

	private HashMap<Integer, Account> accounts;
	private HashMap<Integer, Player> players;

	public static RogueGameServer getInstance() {
		if (instance == null) return instance = new RogueGameServer();
		else return instance;
	}

	public void create() {
		isRunning = true;
		instance = this;
		gameObjectManager = new GameObjectManager();
		server = new Server();
		this.serverWorld = new ServerWorld(server);

		accounts = new HashMap<>();
		players = new HashMap<>();

		serverNetworkListener = new ServerNetworkListener(server);
		server.start();

		NetworkHandler.register(server);

		try {
			server.bind(NetworkHandler.port, 5055);
			ConsoleLog.logSuccess("Server is now running...");
		} catch (IOException e) {
			e.printStackTrace();
		}

		server.addListener(serverNetworkListener);
	}

	public void render() {
		update(Gdx.graphics.getRawDeltaTime());
		serverWorld.render(Gdx.graphics.getRawDeltaTime());
	}

	public void update(float delta) {
		serverWorld.update();
	}

	public Server getServer() {
		return server;
	}

	public ServerWorld getServerWorld() {
		return serverWorld;
	}

	public HashMap<Integer, Account> getAccounts() {
		return accounts;
	}

	public HashMap<Integer, Player> getPlayers() {
		return players;
	}

	public void addAcount(int id, Account account) {
		this.accounts.put(id, account);
	}

	public void addPlayer(int id, Player player) {
		this.players.put(id, player);
	}

	public void removeAccount(int id) {
		this.accounts.remove(id);
	}

	public void removePlayer(int id) {
		this.players.remove(id);
	}
}