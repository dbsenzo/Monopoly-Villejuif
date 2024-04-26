package fr.bouhamididesbois.efrei.monopoly.simulation.rentCalculator;

import fr.arsenelapostolet.efrei.monopoly.Location;

import java.math.BigDecimal;
import java.util.Map;

public class StationRentCalculator implements RentCalculator {
    private final Map<String, Integer> playerStation;
    public StationRentCalculator(Map<String, Integer> playerStation){
        this.playerStation = playerStation;
    }
    @Override
    public BigDecimal calculatePrice(Location location, String owner, int score) {
        int numberOfStation = playerStation.get(owner);
        return new BigDecimal(Math.pow(2, numberOfStation-1) * 25);
    }
}
