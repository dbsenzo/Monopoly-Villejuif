package fr.bouhamididesbois.efrei.monopoly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ApplicationClient {

    public static void main(String[] args) {
        String[] serverDetails = processArguments(args);
        if (serverDetails == null) {
            return;
        }

        String ip = serverDetails[0];
        int port = Integer.parseInt(serverDetails[1]);

        try {
            startClient(ip, port);
        } catch (Exception e) {
            System.err.println("Impossible de se connecter au serveur: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String[] processArguments(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java ApplicationClient <IP> <Port>");
            return null;
        }

        String ip = args[0];
        String portStr = args[1];

        try {
            Integer.parseInt(portStr); // Vérifie si le port est un entier valide
        } catch (NumberFormatException e) {
            System.err.println("Le port doit être un nombre entier.");
            return null;
        }

        return new String[]{ip, portStr};
    }

    private static void startClient(String ip, int port) throws IOException, InterruptedException {
        MonopolyClient client = new MonopolyClient();
        client.start(ip, port, new PrintWriter(System.out, true));
        System.out.println("Connecté au serveur " + ip + " sur le port " + port);

        // Envoi du nom du joueur au serveur
        sendPlayerName(client);

        // Attente que le jeu commence
        waitForGameToStart(client);

        // Lecture et envoi des commandes de jeu
        handleGameCommands(client);
    }

    private static void sendPlayerName(MonopolyClient client) throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Entrez votre nom de joueur: ");
        String playerName = consoleReader.readLine();
        client.sendTextToServer(playerName);
    }

    private static void waitForGameToStart(MonopolyClient client) throws InterruptedException {
        while (!client.canPlay()) {
            System.out.println("En attente que la partie commence...");
            Thread.sleep(2000);
        }
    }

    private static void handleGameCommands(MonopolyClient client) throws IOException {
        System.out.println("La partie commence ! Entrez vos commandes.");
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String inputLine;
        while ((inputLine = consoleReader.readLine()) != null && client.canPlay()) {
            client.sendTextToServer(inputLine);
        }
    }

}
