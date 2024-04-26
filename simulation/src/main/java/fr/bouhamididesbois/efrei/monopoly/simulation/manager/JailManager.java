package fr.bouhamididesbois.efrei.monopoly.simulation.manager;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class JailManager {
    private final Map<String, Integer> playersInJail;

    public JailManager() {
        this.playersInJail = new HashMap<>();
    }

    // Méthode pour mettre un joueur en prison
    public void sendPlayerToJail(String player) {
        playersInJail.put(player, 3); // Le joueur commence avec 3 tours de prison

    }

    public Map<String, Integer> getPlayersInJail(){
        return playersInJail;
    }

    // Méthode pour vérifier si un joueur est en prison
    public boolean isPlayerInJail(String player) {
        return playersInJail.containsKey(player);
    }

    // Méthode pour réduire le nombre de tours restants en prison pour un joueur
    public void decreaseTurnsInJail(String player) {
        if (playersInJail.containsKey(player)) {
            int remainingTurns = playersInJail.get(player);
            if (remainingTurns > 0) {
                playersInJail.put(player, remainingTurns - 1);
            }
        }
    }

    // Méthode pour libérer un joueur de prison
    public void releasePlayerFromJail(String player) {
        playersInJail.remove(player);
    }

    // Méthode pour payer la prison, return un boolean pour valider ou non le paiement
    public boolean payPrison(String playerName, Map<String, BigDecimal> playerBalance) {
        if (!playersInJail.containsKey(playerName)) {
            return false; // Le joueur n'est pas en prison
        }

        if (playersInJail.get(playerName) > 2) {
            return false; // Le joueur a dépassé le nombre maximum de tours en prison
        }

        BigDecimal prisonFee = new BigDecimal(50); // Frais de prison
        BigDecimal playerMoney = playerBalance.get(playerName);
        if (playerMoney.compareTo(prisonFee) < 0) {
            return false; // Le joueur n'a pas assez d'argent pour payer la prison
        }

        // Paiement de la prison
        playerBalance.put(playerName, playerMoney.subtract(prisonFee));
        releasePlayerFromJail(playerName);
        return true; // Le joueur a payé la prison et est libéré
    }
}
