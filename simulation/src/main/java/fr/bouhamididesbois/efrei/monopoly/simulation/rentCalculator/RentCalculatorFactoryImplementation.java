package fr.bouhamididesbois.efrei.monopoly.simulation.rentCalculator;

import fr.arsenelapostolet.efrei.monopoly.Location;
import fr.bouhamididesbois.efrei.monopoly.simulation.manager.BoardManager;

import java.util.Map;

public class RentCalculatorFactoryImplementation implements RentCalculatorFactory {
    private final BoardManager boardManager;

    public RentCalculatorFactoryImplementation(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    @Override
    public RentCalculator createRentCalculator(Location location, Map<String, Integer> playerStation) {
        return switch (location.getKind()) {
            case PROPERTY -> new PropertyRentCalculator(boardManager.getSquareRentList());
            case STATION -> new StationRentCalculator(playerStation);
            case COMPANY -> new CompanyRentCalculator(boardManager.getSquareList());
            default -> null;
        };
    }

}
