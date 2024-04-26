package fr.bouhamididesbois.efrei.monopoly.simulation.manager;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class JailManagerTests {

    private JailManager jailManager;
    private final String player1 = "player1";
    @BeforeEach
    public void setUp() {
        jailManager = new JailManager();
    }

    @Test
    public void payPrison_PlayerHasMoney_PlayerPaysPrison() {

        jailManager.sendPlayerToJail(player1);
        Map<String, BigDecimal> playerBalance = new HashMap<>();
        playerBalance.put(player1, BigDecimal.valueOf(100));
        jailManager.decreaseTurnsInJail(player1);


        assertTrue(jailManager.payPrison(player1, playerBalance));
        assertEquals(BigDecimal.valueOf(50), playerBalance.get(player1));
    }
    @Test
    public void sendPlayerToJail_PlayerSentToJail_PlayerAddedToJailList() {
        // Act
        jailManager.sendPlayerToJail(player1);

        // Assert
        assertTrue(jailManager.isPlayerInJail(player1));
    }

    @Test
    public void isPlayerInJail_PlayerNotInJail_ReturnsFalse() {
        // Assert
        assertFalse(jailManager.isPlayerInJail(player1));
    }

    @Test
    public void decreaseTurnsInJail_PlayerInJail_TurnsDecreased() {
        // Arrange
        jailManager.sendPlayerToJail(player1);

        // Act
        jailManager.decreaseTurnsInJail(player1);

        // Assert
        assertEquals(2, jailManager.getPlayersInJail().get(player1));
    }

    @Test
    public void releasePlayerFromJail_PlayerReleased_PlayerRemovedFromJailList() {
        // Arrange
        jailManager.sendPlayerToJail(player1);

        // Act
        jailManager.releasePlayerFromJail(player1);

        // Assert
        assertFalse(jailManager.isPlayerInJail(player1));
    }

    @Test
    public void payPrison_PlayerNotInJail_ReturnsFalse() {
        // Arrange
        Map<String, BigDecimal> playerBalance = Map.of(player1, BigDecimal.valueOf(100));

        // Assert
        assertFalse(jailManager.payPrison(player1, playerBalance));
    }

    @Test
    public void payPrison_PlayerHasNoMoney_ReturnsFalse() {
        // Arrange
        jailManager.sendPlayerToJail(player1);
        Map<String, BigDecimal> playerBalance = Map.of(player1, BigDecimal.ZERO);
        jailManager.decreaseTurnsInJail(player1);

        // Assert
        assertFalse(jailManager.payPrison(player1, playerBalance));
    }

    @Test
    public void payPrison_PlayerHasTooMuchTurnsInJail_ReturnsFalse() {
        // Arrange
        jailManager.sendPlayerToJail(player1);
        Map<String, BigDecimal> playerBalance = Map.of(player1, BigDecimal.valueOf(100));

        // Assert
        assertFalse(jailManager.payPrison(player1, playerBalance));
    }

}
