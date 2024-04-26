package fr.bouhamididesbois.efrei.monopoly.simulation.rentCalculator;

import fr.arsenelapostolet.efrei.monopoly.Location;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.RentLocationImplementation;

import java.math.BigDecimal;
import java.util.List;

public class PropertyRentCalculator implements RentCalculator {
    private final List<RentLocationImplementation> squareRentList;

    public PropertyRentCalculator(List<RentLocationImplementation> squareRentList) {
        this.squareRentList = squareRentList;
    }

    @Override
    public BigDecimal calculatePrice(Location location, String owner, int score) {
        BigDecimal priceBD = null;
        for (RentLocationImplementation rentLocation : squareRentList) {
            if (rentLocation.getLocation() == location) {
                priceBD = new BigDecimal(rentLocation.getPrice());
            }
        }
        return priceBD;
    }
}

