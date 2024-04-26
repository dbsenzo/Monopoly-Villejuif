package fr.bouhamididesbois.efrei.monopoly.simulation.strategySubmitOrder;

import fr.arsenelapostolet.efrei.monopoly.Dices;
import fr.arsenelapostolet.efrei.monopoly.Location;
import fr.bouhamididesbois.efrei.monopoly.simulation.StrategySubmitOrder.BuildOrderStrategy;
import fr.bouhamididesbois.efrei.monopoly.simulation.StrategySubmitOrder.BuyOrderStrategy;
import fr.bouhamididesbois.efrei.monopoly.simulation.StrategySubmitOrder.IdleOrderStrategy;
import fr.bouhamididesbois.efrei.monopoly.simulation.StrategySubmitOrder.PayPrisonOrderStrategy;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.LocationImplementation;
import fr.bouhamididesbois.efrei.monopoly.simulation.manager.JailManager;
import fr.bouhamididesbois.efrei.monopoly.simulation.manager.PlayerManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class StrategyTests {

    @Mock
    private JailManager mockJailManager;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIdleOrderStrategy_PlayerInJail_DecreaseTurnsInJailCalled() {
        IdleOrderStrategy idleOrderStrategy = new IdleOrderStrategy();
        PlayerManager playerManager = mock(PlayerManager.class);
        Dices dices = mock(Dices.class);

        String playerName = "Player1";
        Map<String, Object> orderParameters = new HashMap<>();
        when(mockJailManager.isPlayerInJail(playerName)).thenReturn(true);

        idleOrderStrategy.execute(playerName, playerManager, mockJailManager, dices, orderParameters);

        verify(mockJailManager).decreaseTurnsInJail(playerName);
    }

    @Test
    public void testIdleOrderStrategy_PlayerNotInJail_DecreaseTurnsInJailNotCalled() {

        IdleOrderStrategy idleOrderStrategy = new IdleOrderStrategy();
        PlayerManager playerManager = mock(PlayerManager.class);
        Dices dices = mock(Dices.class);

        String playerName = "Player1";
        Map<String, Object> orderParameters = new HashMap<>();
        when(mockJailManager.isPlayerInJail(playerName)).thenReturn(false);

        idleOrderStrategy.execute(playerName, playerManager, mockJailManager, dices, orderParameters);

        verify(mockJailManager, never()).decreaseTurnsInJail(playerName);
    }

    @Test
    public void testBuyOrderStrategy_CurrentLocationIsLocationImplementation_PlayerManagerBuyLocationCalled() {

        BuyOrderStrategy buyOrderStrategy = new BuyOrderStrategy();
        PlayerManager playerManager = mock(PlayerManager.class);
        JailManager jailManager = mock(JailManager.class);
        Dices dices = mock(Dices.class);

        String playerName = "Player1";
        Map<String, Object> orderParameters = new HashMap<>();
        LocationImplementation locationImplementation = mock(LocationImplementation.class);
        when(playerManager.getPlayerLocation(playerName)).thenReturn(locationImplementation);

        buyOrderStrategy.execute(playerName, playerManager, jailManager, dices, orderParameters);

        verify(playerManager).buyLocation(locationImplementation, playerName, playerManager.getPlayerBalance(playerName));
    }

    @Test
    public void testBuyOrderStrategy_CurrentLocationIsNotLocationImplementation_PlayerManagerBuyLocationNotCalled() {

        BuyOrderStrategy buyOrderStrategy = new BuyOrderStrategy();
        PlayerManager playerManager = mock(PlayerManager.class);
        JailManager jailManager = mock(JailManager.class);
        Dices dices = mock(Dices.class);

        String playerName = "Player1";
        Map<String, Object> orderParameters = new HashMap<>();
        Location location = mock(Location.class);
        when(playerManager.getPlayerLocation(playerName)).thenReturn(location);

        buyOrderStrategy.execute(playerName, playerManager, jailManager, dices, orderParameters);

        verify(playerManager, never()).buyLocation(any(), eq(playerName), BigDecimal.valueOf(anyInt()));
    }

    @Test
    public void testPayPrisonOrderStrategy_PlayerInJail_JailManagerPayPrisonCalled_PlayerManagerMovePlayerCalled() {
        PayPrisonOrderStrategy payPrisonOrderStrategy = new PayPrisonOrderStrategy();
        PlayerManager playerManager = mock(PlayerManager.class);
        JailManager jailManager = mock(JailManager.class);
        Dices dices = mock(Dices.class);

        String playerName = "Player1";
        Map<String, Object> orderParameters = new HashMap<>();
        when(jailManager.payPrison(playerName, playerManager.getPlayersBalance())).thenReturn(true);

        payPrisonOrderStrategy.execute(playerName, playerManager, jailManager, dices, orderParameters);

        verify(jailManager).payPrison(playerName, playerManager.getPlayersBalance());
        verify(playerManager).movePlayer(playerName, dices.throwTwoSixSidedDices(), jailManager);
    }

    @Test
    public void testPayPrisonOrderStrategy_PlayerNotInJail_JailManagerPayPrisonNotCalled_PlayerManagerMovePlayerNotCalled() {

        PayPrisonOrderStrategy payPrisonOrderStrategy = new PayPrisonOrderStrategy();
        PlayerManager playerManager = mock(PlayerManager.class);
        JailManager jailManager = mock(JailManager.class);
        Dices dices = mock(Dices.class);

        String playerName = "Player1";
        Map<String, Object> orderParameters = new HashMap<>();
        when(jailManager.payPrison(playerName, playerManager.getPlayersBalance())).thenReturn(false);

        payPrisonOrderStrategy.execute(playerName, playerManager, jailManager, dices, orderParameters);

        verify(jailManager).payPrison(playerName, playerManager.getPlayersBalance());
        verify(playerManager, never()).movePlayer(anyString(), anyInt(), any());
    }

    @Test
    public void testBuildOrderStrategy_PlayerManagerBuildOnSquareCalledWithCorrectParameters() {

        BuildOrderStrategy buildOrderStrategy = new BuildOrderStrategy();
        PlayerManager playerManager = mock(PlayerManager.class);
        JailManager jailManager = mock(JailManager.class);
        Dices dices = mock(Dices.class);

        String playerName = "Player1";
        String propertyName = "Property1";
        Map<String, Object> orderParameters = new HashMap<>();
        orderParameters.put("propertyName", propertyName);

        buildOrderStrategy.execute(playerName, playerManager, jailManager, dices, orderParameters);

        verify(playerManager).buildOnSquare(propertyName, playerName);
    }
}
