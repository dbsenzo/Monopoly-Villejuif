package fr.bouhamididesbois.efrei.monopoly.simulation.implementation;

import fr.arsenelapostolet.efrei.monopoly.*;
import fr.bouhamididesbois.efrei.monopoly.simulation.parser.Parser;
import fr.bouhamididesbois.efrei.monopoly.simulation.StrategySubmitOrder.*;
import fr.bouhamididesbois.efrei.monopoly.simulation.manager.BoardManager;
import fr.bouhamididesbois.efrei.monopoly.simulation.manager.JailManager;
import fr.bouhamididesbois.efrei.monopoly.simulation.manager.PlayerManager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class MonopolyImplementation implements Monopoly {
    private final PlayerManager playerManager;

    private final BoardManager boardManager;
    private final JailManager jailManager;
    private final Dices dices;

    public MonopolyImplementation(List<String> playerIds, Dices dices) {
        this.boardManager = new BoardManager(new Parser());
        this.jailManager = new JailManager();
        this.dices = dices;
        this.playerManager = new PlayerManager(playerIds, boardManager, jailManager, dices);
    }

    @Override
    public void submitOrder(String playerName, OrderKind order, Map<String, Object> orderParameters) {
        OrderStrategy strategy;

        if (playerManager.getPlayerIds().size() < 2){
            throw new GameFinishedException();
        }

        if(!playerManager.switchToNextPlayer(playerName)){
            return;
        }

        strategy = switch (order) {
            case IDLE -> new IdleOrderStrategy();
            case BUY -> new BuyOrderStrategy();
            case PAY_PRISON -> new PayPrisonOrderStrategy();
            case BUILD -> new BuildOrderStrategy();
        };

        strategy.execute(playerName, playerManager, jailManager, dices, orderParameters);

        if (!jailManager.isPlayerInJail(playerManager.getCurrentPlayer())) {
            playerManager.movePlayer(playerManager.getCurrentPlayer(), dices.throwTwoSixSidedDices(), jailManager); // Bouge le joueur suivant
        }else if ((jailManager.getPlayersInJail().get(playerManager.getCurrentPlayer())-1 == 0) && (jailManager.payPrison(playerManager.getCurrentPlayer(), playerManager.getPlayersBalance()))) {
            playerManager.movePlayer(playerManager.getCurrentPlayer(), dices.throwTwoSixSidedDices(), jailManager);
        }
    }

    @Override
    public Map<String, Location> getPlayersLocation() {
        return playerManager.getPlayersLocation();
    }

    @Override
    public Map<String, BigDecimal> getPlayersBalance() {
        return playerManager.getPlayersBalance();
    }

    @Override
    public List<Location> getBoard() {
        return boardManager.getSquareList();
    }



}
