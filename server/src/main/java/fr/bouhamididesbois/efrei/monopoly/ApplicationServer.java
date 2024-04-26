package fr.bouhamididesbois.efrei.monopoly;


import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.DiceImplementation;

import java.util.Random;

public class ApplicationServer {
    public static void main(String[] args) {
        // Définition de valeurs par défaut
        int numberOfPlayers = 3;
        int port = 3000;

        // Vérification et attribution des arguments de la ligne de commande
        if (args.length > 0) {
            try {
                numberOfPlayers = Integer.parseInt(args[0]);
                if (args.length > 1) {
                    port = Integer.parseInt(args[1]);
                }
            } catch (NumberFormatException e) {
                System.out.println("Erreur: Assurez-vous que le nombre de joueurs et le port sont des nombres valides.");
                return;
            }
        }

        // Démarrage du serveur avec les paramètres configurés
        MonopolyServer ms = new MonopolyServer();
        ms.start(numberOfPlayers, port, new DiceImplementation(new Random()));

        System.out.println("Serveur Monopoly démarré sur le port " + port + " pour " + numberOfPlayers + " joueurs.");
    }

}
