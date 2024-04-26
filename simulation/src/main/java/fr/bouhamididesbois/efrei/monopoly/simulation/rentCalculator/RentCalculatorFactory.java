package fr.bouhamididesbois.efrei.monopoly.simulation.rentCalculator;

import fr.arsenelapostolet.efrei.monopoly.Location;

import java.util.Map;

public interface RentCalculatorFactory {
    RentCalculator createRentCalculator(Location location, Map<String, Integer> playerStation);
}
