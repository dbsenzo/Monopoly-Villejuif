package fr.bouhamididesbois.efrei.monopoly.simulation.rentCalculator;

import fr.arsenelapostolet.efrei.monopoly.Location;

import java.math.BigDecimal;

public interface RentCalculator {
    BigDecimal calculatePrice(Location location, String owner, int score);
}
