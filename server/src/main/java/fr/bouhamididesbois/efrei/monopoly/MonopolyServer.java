package fr.bouhamididesbois.efrei.monopoly;

import fr.arsenelapostolet.efrei.monopoly.Dices;
import fr.arsenelapostolet.efrei.monopoly.GameServer;
import fr.bouhamididesbois.efrei.monopoly.ClientHandlerFactory.ClientHandler;
import fr.bouhamididesbois.efrei.monopoly.GameStateManager.GameStateManager;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.MonopolyImplementation;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MonopolyServer implements GameServer {
    private int connectedPlayers = 0;
    private final List<ClientHandler> clientHandlers = new ArrayList<>();
    private final List<String> playerIds = new ArrayList<>();
    private final List<Socket> sockets = new ArrayList<>();
    private final List<BufferedReader> readers = new ArrayList<>();
    private final List<PrintWriter> writers = new ArrayList<>();

    public MonopolyServer() {
    }

    @Override
    public void start(int numberOfPlayers, int port, Dices dices) {
        new Thread(() -> startServer(numberOfPlayers, port, dices)).start();
    }

    private void startServer(int numberOfPlayers, int port, Dices dices) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serveur demarre sur le port : " + port);
            acceptPlayerConnections(serverSocket, numberOfPlayers, sockets, readers, writers, playerIds);
            initializeGame(playerIds, dices, writers);
        } catch (IOException e) {
            System.err.println("Le port n'est pas utilisable : " + port);
        }
    }

    private void acceptPlayerConnections(ServerSocket serverSocket, int numberOfPlayers, List<Socket> tempSockets, List<BufferedReader> tempReaders, List<PrintWriter> tempWriters, List<String> tempPlayerIds) throws IOException {
        while (connectedPlayers < numberOfPlayers) {
            Socket connectedClient = serverSocket.accept();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connectedClient.getInputStream()));
            PrintWriter writer = new PrintWriter(connectedClient.getOutputStream(), true);

            String playerId = reader.readLine();
            tempPlayerIds.add(playerId);
            tempSockets.add(connectedClient);
            tempReaders.add(reader);
            tempWriters.add(writer);

            connectedPlayers++;
            //notifyAllPlayers(connectedPlayers + "/" + numberOfPlayers + " joueurs connectés.", tempWriters);
        }
    }


    private void startClientHandlers(List<Socket> sockets, List<BufferedReader> readers, List<PrintWriter> writers, List<String> playerIds, GameStateManager gameStateManager) {
        for (int i = 0; i < sockets.size(); i++) {
            ClientHandler handler = new ClientHandler(sockets.get(i), readers.get(i), writers.get(i), playerIds.get(i), gameStateManager);
            new Thread(handler).start();
            clientHandlers.add(handler);
        }
    }

    private void initializeGame(List<String> playerIds, Dices dices, List<PrintWriter> writers) {
        MonopolyImplementation monopolyImplementation = new MonopolyImplementation(playerIds, dices);
        GameStateManager gameStateManager = new GameStateManager(monopolyImplementation);
        //notifyAllPlayers("La partie est complète, le jeu va commencer.", writers);
        startClientHandlers(sockets, readers, writers, playerIds, gameStateManager);
        gameStateManager.updateGameState(null, null); // Met à jour l'état initial
    }


    private void notifyAllPlayers(String message, List<PrintWriter> writers) {
        for (PrintWriter writer : writers) {
            writer.println(message);
            writer.flush(); // Assurez-vous que le message est immédiatement envoyé
        }
    }
}
