package fr.bouhamididesbois.efrei.monopoly.simulation.rentCalculator;

import fr.arsenelapostolet.efrei.monopoly.Location;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class CompanyRentCalculator implements RentCalculator {
    private final List<Location> squareList;

    public CompanyRentCalculator(List<Location> squareList){
        this.squareList = squareList;

    }

    @Override
    public BigDecimal calculatePrice(Location location, String owner, int score) {
        long companyCount = squareList.stream()
                .filter(square -> square.getKind() == Location.LocationKind.COMPANY && Objects.equals(square.getOwner(), owner))
                .count();
        return companyCount == 2 ? new BigDecimal(score * 10) : new BigDecimal(score * 4);
    }
}

