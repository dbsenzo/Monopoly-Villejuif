package fr.bouhamididesbois.efrei.monopoly.GameStateManager;

public interface GameStateObserver {
    // Méthode appelée pour notifier l'observateur d'une mise à jour de l'état du jeu
    void update(String gameState);
}
