package fr.bouhamididesbois.efrei.monopoly.simulation.manager;

import fr.arsenelapostolet.efrei.monopoly.Location;
import fr.bouhamididesbois.efrei.monopoly.simulation.parser.Parser;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.LocationImplementation;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.RentLocationImplementation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BoardManager {
    private final List<Location> squareList;
    private final List<RentLocationImplementation> squareRentList;

    public BoardManager(Parser parser) {
        this.squareList = parser.retrieveLocation();
        this.squareRentList = parser.retrieveRentLocation(squareList);
    }

    // Méthode pour obtenir la liste des cases sur le plateau
    public List<Location> getSquareList() {
        return squareList;
    }

    public RentLocationImplementation getSquareRentByLocation(LocationImplementation loc) {
        return squareRentList.stream()
                .filter(rentLoc -> rentLoc.getLocation().equals(loc))
                .findFirst()
                .orElse(null);
    }


    // Méthode pour obtenir la liste des cases à louer sur le plateau
    public List<RentLocationImplementation> getSquareRentList() {
        return squareRentList;
    }

    // Méthode pour obtenir une case spécifique par son index
    public Location getSquareByIndex(int index) {
        if (index < 0 || index >= squareList.size()) {
            throw new IllegalArgumentException("Invalid square index");
        }
        return squareList.get(index);
    }

    // Méthode pour obtenir une case spécifique par son nom
    public Location getSquareByName(String name) {
        for (Location location : squareList) {
            if (location.getName().equals(name)) {
                return location;
            }
        }
        throw new IllegalArgumentException("Location not found: " + name);
    }

    public List<LocationImplementation> getSquareByColor(String color) {
        return squareList.stream()
                .filter(location -> location instanceof LocationImplementation)
                .map(location -> (LocationImplementation) location)
                .filter(location -> Objects.equals(location.getColor(), color))
                .collect(Collectors.toList());
    }

}


