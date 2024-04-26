package fr.bouhamididesbois.efrei.monopoly.simulation.StrategySubmitOrder;

import fr.arsenelapostolet.efrei.monopoly.Dices;
import fr.bouhamididesbois.efrei.monopoly.simulation.manager.JailManager;
import fr.bouhamididesbois.efrei.monopoly.simulation.manager.PlayerManager;

import java.util.Map;

public class IdleOrderStrategy implements OrderStrategy {
    @Override
    public void execute(String playerName, PlayerManager playerManager, JailManager jailManager, Dices dices, Map<String, Object> orderParameters) {
        if (jailManager.isPlayerInJail(playerName)) {
            jailManager.decreaseTurnsInJail(playerName);
        }
    }
}