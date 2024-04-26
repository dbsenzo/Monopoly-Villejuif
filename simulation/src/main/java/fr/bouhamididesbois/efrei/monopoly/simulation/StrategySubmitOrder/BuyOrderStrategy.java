package fr.bouhamididesbois.efrei.monopoly.simulation.StrategySubmitOrder;

import fr.arsenelapostolet.efrei.monopoly.Dices;
import fr.arsenelapostolet.efrei.monopoly.Location;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.LocationImplementation;
import fr.bouhamididesbois.efrei.monopoly.simulation.manager.JailManager;
import fr.bouhamididesbois.efrei.monopoly.simulation.manager.PlayerManager;

import java.util.Map;

public class BuyOrderStrategy implements OrderStrategy{
    @Override
    public void execute(String playerName, PlayerManager playerManager, JailManager jailManager, Dices dices, Map<String, Object> orderParameters) {
        Location currentLocation = playerManager.getPlayerLocation(playerName);
        if (currentLocation instanceof LocationImplementation) {
            playerManager.buyLocation((LocationImplementation) currentLocation, playerName, playerManager.getPlayerBalance(playerName));
        }
    }
}
