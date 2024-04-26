package fr.bouhamididesbois.efrei.monopoly.GameStateManager;

import fr.arsenelapostolet.efrei.monopoly.OrderKind;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.MonopolyImplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class GameStateManager {
    private final List<GameStateObserver> observers = new ArrayList<>();
    private final MonopolyImplementation monopolyImplementation;

    public GameStateManager(MonopolyImplementation monopolyImplementation){
        this.monopolyImplementation = monopolyImplementation;
    }

    public void addObserver(GameStateObserver observer) {
        observers.add(observer);
    }

    public void updateGameState(String player, OrderKind order) {
        if (player == null && order == null) {
            String gameState = getMonopolyStatus(null, null);
            notifyObservers(gameState);
        } else {
            monopolyImplementation.submitOrder(player, order, null);
            String gameState = getMonopolyStatus(player, order);
            notifyObservers(gameState);
        }
    }


    private void notifyObservers(String gameState) {
        for (GameStateObserver observer : observers) {
            observer.update(gameState);
        }
    }

    public String getMonopolyStatus(String player, OrderKind order) {
        StringJoiner sj = new StringJoiner("|");

        sj.add(formatPlayerAction(player, order));
        sj.add(formatLocations());
        sj.add(formatBalances());
        sj.add(formatBoard());

        return sj.toString();
    }

    private String formatPlayerAction(String player, OrderKind order) {
        // Gérer les cas où player ou order sont null
        String playerStr = player != null ? player : "null";
        String orderStr = order != null ? order.toString() : "null";
        return playerStr + ":" + orderStr;
    }

    private String formatLocations() {
        return monopolyImplementation.getPlayersLocation().entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue().getName())
                .collect(Collectors.joining(","));
    }

    private String formatBalances() {
        return monopolyImplementation.getPlayersBalance().entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(","));
    }

    private String formatBoard() {
        return monopolyImplementation.getBoard().stream()
                .map(location -> location.getName() + ":" + location.getOwner())
                .collect(Collectors.joining(","));
    }

}
