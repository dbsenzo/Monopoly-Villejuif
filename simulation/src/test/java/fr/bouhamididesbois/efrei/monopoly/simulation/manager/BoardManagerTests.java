package fr.bouhamididesbois.efrei.monopoly.simulation.manager;

import fr.arsenelapostolet.efrei.monopoly.Location;
import fr.bouhamididesbois.efrei.monopoly.simulation.parser.Parser;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.LocationImplementation;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.RentLocationImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class BoardManagerTests {

    @Mock
    private Parser parser;

    private BoardManager boardManager;

    private LocationImplementation locationImplementation;

    private RentLocationImplementation rentLocationImplementation;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        List<Location> squareList = new ArrayList<>();
        this.locationImplementation = new LocationImplementation("Location", Location.LocationKind.PROPERTY, "blue", 10);
        squareList.add(this.locationImplementation);
        when(parser.retrieveLocation()).thenReturn(squareList);

        List<RentLocationImplementation> squareRentList = new ArrayList<>();
        this.rentLocationImplementation = new RentLocationImplementation("Location", 100, new int[]{10, 20}, 5, locationImplementation);
        squareRentList.add(this.rentLocationImplementation);
        when(parser.retrieveRentLocation(squareList)).thenReturn(squareRentList);

        boardManager = new BoardManager(parser);
    }

    @Test
    public void testGetSquareList() {
        List<Location> squareList = boardManager.getSquareList();
        assertNotNull(squareList);
        assertEquals(1, squareList.size());
        assertEquals("Location", squareList.getFirst().getName());
    }

    @Test
    public void testGetSquareRentByLocation() {
        RentLocationImplementation rentLocation = boardManager.getSquareRentByLocation(this.locationImplementation);
        assertNotNull(rentLocation);
        assertEquals(rentLocation, this.rentLocationImplementation);
    }

    @Test
    public void testGetSquareRentList() {
        List<RentLocationImplementation> squareRentList = boardManager.getSquareRentList();
        assertNotNull(squareRentList);
        assertEquals(1, squareRentList.size());
        assertEquals("Location", squareRentList.getFirst().getProperty());
    }

    @Test
    public void testGetSquareByIndex() {
        Location square = boardManager.getSquareByIndex(0);
        assertNotNull(square);
        assertEquals(0, boardManager.getSquareList().indexOf(square));
    }

    @Test
    public void testGetSquareByName() {
        Location square = boardManager.getSquareByName("Location");
        assertNotNull(square);
        assertEquals("Location", square.getName());
    }

    @Test
    public void testGetSquareByColor() {
        List<LocationImplementation> squareList = boardManager.getSquareByColor("blue");
        assertNotNull(squareList);
        assertEquals("blue", squareList.getFirst().getColor());
    }

    @Test
    public void testGetSquareByNameNotFound() {
        assertThrows(IllegalArgumentException.class, () -> boardManager.getSquareByName("NotFound"));
    }

    @Test
    public void testGetSquareByIndexNotFound(){
        assertThrows(IllegalArgumentException.class, () -> boardManager.getSquareByIndex(34));
    }
}
