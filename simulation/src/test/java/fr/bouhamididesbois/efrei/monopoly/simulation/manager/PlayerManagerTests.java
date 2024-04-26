package fr.bouhamididesbois.efrei.monopoly.simulation.manager;

import fr.arsenelapostolet.efrei.monopoly.Dices;
import fr.arsenelapostolet.efrei.monopoly.Location;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.LocationImplementation;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.RentLocationImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.util.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerManagerTests {

    private PlayerManager playerManager;

    private final BoardManager boardManager = mock(BoardManager.class);
    private final String player1 = "player1";
    private final String player2 = "player2";
    private final String player3 = "player3";

    @BeforeEach
    public void setUp() {
        // Mocking BoardManager
        List<Location> squareList = new ArrayList<>();
        Location startLocation = new LocationImplementation("Start", Location.LocationKind.START);
        squareList.add(startLocation);
        when(boardManager.getSquareList()).thenReturn(squareList);
        when(boardManager.getSquareByIndex(0)).thenReturn(startLocation);

        // Mocking JailManager
        JailManager jailManager = mock(JailManager.class);

        // Creating PlayerManager
        List<String> playerIds = List.of(player1, player2, player3);
        playerManager = new PlayerManager(playerIds, boardManager, jailManager, mock(Dices.class));
    }


    @Test
    public void testInitializePlayers() {
        assertNotNull(playerManager.getPlayerLocation(player1));
        assertNotNull(playerManager.getPlayerLocation(player2));
        assertNotNull(playerManager.getPlayerLocation(player3));
        assertEquals(BigDecimal.valueOf(1500), playerManager.getPlayerBalance(player1));
        assertEquals(BigDecimal.valueOf(1500), playerManager.getPlayerBalance(player2));
        assertEquals(BigDecimal.valueOf(1500), playerManager.getPlayerBalance(player3));
    }

    @Test
    public void testMovePlayer() {
        playerManager.movePlayer(player1, 1, mock(JailManager.class));
        assertNotNull(playerManager.getPlayerLocation(player1));
    }

    @Test
    public void testSwitchToNextPlayer() {
        assertTrue(playerManager.switchToNextPlayer(player1));
        assertEquals(player2, playerManager.getCurrentPlayer());
    }

    @Test
    public void testMovePlayerWhoNotInGame(){
        assertThrows(IllegalArgumentException.class, () -> playerManager.movePlayer("player10", 1, mock(JailManager.class)));
    }

    @Test
    public void testGetPlayerIds(){
        List<String> playerIds = List.of(player1, player2, player3);
        assertEquals(playerIds, playerManager.getPlayerIds());
    }

    @Test
    public void testGetPlayerBalance(){
        assertEquals(BigDecimal.valueOf(1500), playerManager.getPlayerBalance(player1));
    }

    @Test
    public void testSwitchToNextPlayerWithNullPlayer(){
        playerManager.setCurrentPlayer(null);
        assertFalse(playerManager.switchToNextPlayer(player1));
    }

    @Test
    public void testBuyLocationNotInGame(){
        playerManager.removePlayerFromTheGame(player1);
        assertThrows(IllegalArgumentException.class, () -> playerManager.buyLocation(new LocationImplementation("Location", Location.LocationKind.PROPERTY), player1, BigDecimal.valueOf(100)));
    }

    @Test
    public void testBuyLocation(){
        LocationImplementation location = new LocationImplementation("Location", Location.LocationKind.STATION);
        location.setPrice(BigDecimal.valueOf(50));
        playerManager.buyLocation(location, player1, BigDecimal.valueOf(1500));
        assertEquals(player1, location.getOwner());
        assertEquals(BigDecimal.valueOf(1450), playerManager.getPlayerBalance(player1));
    }

    @Test
    public void testBuyLocationPlayerIsAlreadyInPlayerStation(){
        LocationImplementation location = new LocationImplementation("Location", Location.LocationKind.STATION);
        LocationImplementation location2 = new LocationImplementation("Location2", Location.LocationKind.STATION);
        location.setPrice(BigDecimal.valueOf(50));
        location2.setPrice(BigDecimal.valueOf(50));
        playerManager.buyLocation(location, player1, BigDecimal.valueOf(1500));
        playerManager.buyLocation(location2, player1, BigDecimal.valueOf(1450));
        assertEquals(player1, location2.getOwner());
        assertEquals(BigDecimal.valueOf(1400), playerManager.getPlayerBalance(player1));
    }

    @Test
    public void testVerificationBalanceJoueur(){
        playerManager.getPlayersBalance().replace(player1, BigDecimal.valueOf(-50));
        playerManager.getPlayersBalance().replace(player2, BigDecimal.valueOf(100));
        playerManager.checkPlayerBalance(player1, player2);
        assertFalse(playerManager.getPlayerIds().contains(player1));
        assertEquals(BigDecimal.valueOf(50), playerManager.getPlayerBalance(player2));
    }

    @Test
    public void testCheckIfPlayerIsOnTaxSquare_True(){
        when(boardManager.getSquareByIndex(0)).thenReturn(new LocationImplementation("Tax", Location.LocationKind.TAX, "blue", 100));
        playerManager.getPlayersBalance().replace(player1, BigDecimal.valueOf(1500));
        playerManager.movePlayer(player1, 0, mock(JailManager.class));
        assertEquals(BigDecimal.valueOf(1400), playerManager.getPlayerBalance(player1));
    }

    @Test
    public void testCheckIfPlayerIsOnTaxSquare_False(){
        when(boardManager.getSquareByIndex(0)).thenReturn(new LocationImplementation("Tax", Location.LocationKind.TAX, "blue", 100));

        playerManager.getPlayersBalance().replace(player1, BigDecimal.ZERO);
        playerManager.movePlayer(player1, 0, mock(JailManager.class));
        assertFalse(playerManager.getPlayerIds().contains(player1));
    }
    @Test
    public void testCheckIfPlayerIsGoingToJail_True(){
        when(boardManager.getSquareByIndex(0)).thenReturn(new LocationImplementation("GoToJail", Location.LocationKind.GO_TO_JAIL));
        when(boardManager.getSquareByIndex(10)).thenReturn(new LocationImplementation("Jail", Location.LocationKind.JAIL));
        JailManager jailManager = mock(JailManager.class);
        playerManager.setCurrentPlayer(player1);
        playerManager.movePlayer(player1, 0, jailManager);
        assertEquals(Location.LocationKind.JAIL, playerManager.getPlayerLocation(player1).getKind());
    }


    @Test
    public void testHasPassedThroughTheStartBox(){
        playerManager.getPlayersBalance().replace(player1, BigDecimal.valueOf(1500));
        playerManager.movePlayer(player1, 2, mock(JailManager.class));
        assertEquals(BigDecimal.valueOf(1900), playerManager.getPlayerBalance(player1));
    }


    @Test
    public void testBuildOnSquare_CanBuild(){
        LocationImplementation locationImplementation = new LocationImplementation("Location", Location.LocationKind.PROPERTY, "blue", 100);
        locationImplementation.setOwner(player1);
        int[] levels = {20, 50, 60, 70, 80};
        RentLocationImplementation rentLocationImplementation = new RentLocationImplementation("Location", 50, levels, 10, locationImplementation);
        playerManager.getPlayersBalance().replace(player1, BigDecimal.valueOf(1500));

        when(boardManager.getSquareByName("Rue Victor Hugo")).thenReturn(locationImplementation);
        when(boardManager.getSquareByColor("blue")).thenReturn(List.of(locationImplementation));
        when(boardManager.getSquareRentByLocation(locationImplementation)).thenReturn(rentLocationImplementation);

        Map<Object, String> playerProperties = Map.of("propertyName", "Rue Victor Hugo");

        playerManager.buildOnSquare(playerProperties.get("propertyName"), player1);

        assertEquals(20, rentLocationImplementation.getPrice());
    }

    @Test
    public void testBuildOnSquare_CanNotBuild(){
        LocationImplementation locationImplementation = new LocationImplementation("Location", Location.LocationKind.PROPERTY, "blue", 100);
        locationImplementation.setOwner(player2);
        int[] levels = {20, 50, 60, 70, 80};
        RentLocationImplementation rentLocationImplementation = new RentLocationImplementation("Location", 50, levels, 10, locationImplementation);
        playerManager.getPlayersBalance().replace(player1, BigDecimal.valueOf(1500));

        when(boardManager.getSquareByName("Rue Victor Hugo")).thenReturn(locationImplementation);
        when(boardManager.getSquareByColor("blue")).thenReturn(List.of(locationImplementation));
        when(boardManager.getSquareRentByLocation(locationImplementation)).thenReturn(rentLocationImplementation);

        Map<Object, String> playerProperties = Map.of("propertyName", "Rue Victor Hugo");

        playerManager.buildOnSquare(playerProperties.get("propertyName"), player1);

        assertEquals(10, rentLocationImplementation.getPrice());
    }

    @Test
    public void testGetPlayerLocation(){
        LocationImplementation location = new LocationImplementation("Location", Location.LocationKind.PROPERTY);
        playerManager.getPlayersLocation().replace(player1, location);
        assertEquals(location, playerManager.getPlayerLocation(player1));
    }
}
