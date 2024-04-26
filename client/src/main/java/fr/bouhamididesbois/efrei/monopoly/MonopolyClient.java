package fr.bouhamididesbois.efrei.monopoly;

import fr.arsenelapostolet.efrei.monopoly.GameClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MonopolyClient implements GameClient {
    private PrintWriter serverOutput;
    private BufferedReader messagesFromServerStream;
    private PrintWriter messagesToServerStream;
    private volatile boolean canPlay = false;



    public MonopolyClient() {}

    @Override
    public void start(String ipAddress, int port, PrintWriter output) throws IOException {
        this.serverOutput = output;

        Socket socket = new Socket(ipAddress, port);

        messagesFromServerStream = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        messagesToServerStream = new PrintWriter(
                socket.getOutputStream(),
                true
        );

        new Thread(this::listenForMessages).start();
    }

    public boolean canPlay() {
        return canPlay;
    }

    private void listenForMessages() {
        while (true) {
            try {
                final String message = messagesFromServerStream.readLine();
                if (message == null) {
                    break; // Sortie de la boucle si le serveur ferme la connexion
                }
                serverOutput.println(message);
                // Modifier canPlay selon les messages du serveur
//                if ("La partie est complète, le jeu va commencer.".equals(message)) {
//                    canPlay = true;
//                }
                if (message.matches(".*\\|.*:.*")) {
                    canPlay = true;
                }
            } catch (IOException e) {
                serverOutput.println("La connexion avec le serveur a été interrompue.");
                break;
            }
        }
    }

    @Override
    public void sendTextToServer(String text) {
        messagesToServerStream.println(text); // Remplir le buffer avec du texte
    }
}
