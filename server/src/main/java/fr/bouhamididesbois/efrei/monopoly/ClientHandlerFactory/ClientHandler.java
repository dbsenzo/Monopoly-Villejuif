package fr.bouhamididesbois.efrei.monopoly.ClientHandlerFactory;

import fr.arsenelapostolet.efrei.monopoly.OrderKind;
import fr.bouhamididesbois.efrei.monopoly.GameStateManager.GameStateManager;
import fr.bouhamididesbois.efrei.monopoly.GameStateManager.GameStateObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable, GameStateObserver {
    private final Socket clientSocket;
    private final BufferedReader reader;
    private final PrintWriter writer;
    private final String playerId;
    private final GameStateManager gameStateManager;

    public ClientHandler(Socket clientSocket, BufferedReader reader, PrintWriter writer, String playerId, GameStateManager gameStateManager) {
        this.clientSocket = clientSocket;
        this.reader = reader;
        this.writer = writer;
        this.playerId = playerId;
        this.gameStateManager = gameStateManager;
        gameStateManager.addObserver(this);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message = reader.readLine();
                if (message == null) break; // Joueur déconnecté
                OrderKind orderKind = OrderKind.valueOf(message.toUpperCase());

                // Demander une mise à jour de l'état du jeu au GameStateManager.
                gameStateManager.updateGameState(playerId, orderKind);
            }
        } catch (IOException e) {
            System.err.println("Error handling client #" + playerId);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                // Une erreur
            }
        }
    }

    public void update(String gameState) {
        writer.println(gameState);
        writer.flush();
    }
}

