package fr.bouhamididesbois.efrei.monopoly.simulation.rentCalculator;

import fr.arsenelapostolet.efrei.monopoly.Location;
import fr.bouhamididesbois.efrei.monopoly.simulation.manager.BoardManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class FactoryTests {


    @Mock
    private BoardManager boardManager;

    @Test
    public void testRentCalculatorFactory() {
        MockitoAnnotations.openMocks(this);
        // Création de la factory
        RentCalculatorFactory factory = new RentCalculatorFactoryImplementation(boardManager);

        // Création de mocks des différents types de locations
        Location property = mock(Location.class);
        when(property.getKind()).thenReturn(Location.LocationKind.PROPERTY);

        Location station = mock(Location.class);
        when(station.getKind()).thenReturn(Location.LocationKind.STATION);

        Location company = mock(Location.class);
        when(company.getKind()).thenReturn(Location.LocationKind.COMPANY);

        // Test de la création des différents types de RentCalculator
        assertNotNull(factory.createRentCalculator(property, new HashMap<>()));
        assertNotNull(factory.createRentCalculator(station, new HashMap<>()));
        assertNotNull(factory.createRentCalculator(company, new HashMap<>()));

        // Vérification que les méthodes de BoardManager sont appelées
        verify(boardManager).getSquareRentList();
        verify(boardManager).getSquareList();
    }
}
