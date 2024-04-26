package fr.bouhamididesbois.efrei.monopoly.simulation.parser;

import fr.arsenelapostolet.efrei.monopoly.Location;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.LocationImplementation;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.RentLocationImplementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Parser {

    public Parser() {}

    // Parse CSV to retrieve locations
    public List<Location> retrieveLocation() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/monopoly.csv"))))) {
            return br.lines()
                    .skip(1) // Skip header
                    .map(line -> {
                        String[] values = line.split(",");
                        return (values.length <= 2) ?
                                new LocationImplementation(values[0], Location.LocationKind.valueOf(values[1].toUpperCase())) :
                                new LocationImplementation(values[0], Location.LocationKind.valueOf(values[1].toUpperCase()), values[2], Integer.parseInt(values[3]));
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Parse CSV to retrieve rent locations
    public List<RentLocationImplementation> retrieveRentLocation(List<Location> squareList) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/rent.csv"))))) {
            return br.lines()
                    .skip(1) // Skip header
                    .map(line -> {
                        String[] values = line.split(",");
                        int[] levels = {Integer.parseInt(values[2]), Integer.parseInt(values[3]), Integer.parseInt(values[4]), Integer.parseInt(values[5]), Integer.parseInt(values[6])};
                        return squareList.stream()
                                .filter(location -> location.getName().equals(values[0]))
                                .findFirst()
                                .map(location -> new RentLocationImplementation(values[0], Integer.parseInt(values[1]), levels, Integer.parseInt(values[7]), location))
                                .orElseThrow(() -> new IllegalArgumentException("Location non trouvé: " + values[0]));
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
