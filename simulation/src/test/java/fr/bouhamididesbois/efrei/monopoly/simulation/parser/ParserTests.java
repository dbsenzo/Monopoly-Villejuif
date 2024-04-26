package fr.bouhamididesbois.efrei.monopoly.simulation.parser;


import fr.arsenelapostolet.efrei.monopoly.Location;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.LocationImplementation;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.RentLocationImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ParserTests {
    @Mock
    private Parser parser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveLocation() {
        String csvData = "location1,PROPERTY\nlocation2,STATION\n";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        when(parser.retrieveLocation()).thenReturn(
                List.of(
                        new LocationImplementation("location1", Location.LocationKind.PROPERTY),
                        new LocationImplementation("location2", Location.LocationKind.STATION)
                )
        );

        List<Location> locations = parser.retrieveLocation();
        assertEquals(2, locations.size());
        assertEquals("location1", locations.get(0).getName());
        assertEquals(Location.LocationKind.PROPERTY, locations.get(0).getKind());
        assertEquals("location2", locations.get(1).getName());
        assertEquals(Location.LocationKind.STATION, locations.get(1).getKind());
    }

    @Test
    void testRetrieveRentLocation() {
        String csvData = "location1,100,1,2,3,4,5,6\nlocation2,200,1,2,3,4,5,6\n";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
        when(parser.retrieveRentLocation(any())).thenAnswer(invocation -> {
            List<Location> locations = invocation.getArgument(0);
            return locations.stream()
                    .map(location -> {
                        if (location.getName().equals("location1")) {
                            return new RentLocationImplementation("location1", 100, new int[]{1, 2, 3, 4, 5}, 6, null);
                        } else if (location.getName().equals("location2")) {
                            return new RentLocationImplementation("location2", 200, new int[]{1, 2, 3, 4, 5}, 6, null);
                        }
                        return null;
                    })
                    .collect(Collectors.toList());
        });

        List<Location> locations = List.of(
                new LocationImplementation("location1", Location.LocationKind.PROPERTY),
                new LocationImplementation("location2", Location.LocationKind.STATION)
        );

        List<RentLocationImplementation> rentLocations = parser.retrieveRentLocation(locations);

        assertEquals(2, rentLocations.size());
        assertEquals("location1", rentLocations.get(0).getProperty());
        assertEquals(100, rentLocations.get(0).getCost());
        assertEquals("location2", rentLocations.get(1).getProperty());
        assertEquals(200, rentLocations.get(1).getCost());
    }

}
