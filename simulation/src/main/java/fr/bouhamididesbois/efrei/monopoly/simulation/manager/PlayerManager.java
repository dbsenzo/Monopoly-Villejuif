package fr.bouhamididesbois.efrei.monopoly.simulation.manager;

import fr.arsenelapostolet.efrei.monopoly.Dices;
import fr.arsenelapostolet.efrei.monopoly.Location;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.LocationImplementation;
import fr.bouhamididesbois.efrei.monopoly.simulation.implementation.RentLocationImplementation;
import fr.bouhamididesbois.efrei.monopoly.simulation.rentCalculator.*;

import java.math.BigDecimal;
import java.util.*;

public class PlayerManager {
    private final Map<String, Location> playerLocation;
    private final Map<String, BigDecimal> playerBalance;
    private final Map<String, Integer> playerStation;
    private final List<String> playerIds;
    private String currentPlayer;
    private final BoardManager boardManager;
    private final RentCalculatorFactory rentCalculatorFactory;

    public PlayerManager(List<String> playerIds, BoardManager boardManager, JailManager jailManager, Dices dices) {
        this.playerLocation = new HashMap<>();
        this.playerBalance = new HashMap<>();
        this.playerStation = new HashMap<>();
        this.playerIds = new ArrayList<>(playerIds);
        this.currentPlayer = null;
        this.boardManager = boardManager;
        this.rentCalculatorFactory = new RentCalculatorFactoryImplementation(boardManager);
        initializePlayers();
        movePlayer(currentPlayer, dices.throwTwoSixSidedDices(), jailManager);
    }

    // Méthode pour initialiser les joueurs avec leur solde et leur position de départ
    public void initializePlayers() {
        for (String playerId : playerIds) {
            playerLocation.put(playerId, boardManager.getSquareByIndex(0)); // Position de départ
            playerBalance.put(playerId, BigDecimal.valueOf(1500)); // Solde initial de 1500
        }
        currentPlayer = playerIds.getFirst(); // Le premier joueur commence
    }


    // Méthode pour déplacer un joueur sur le plateau en fonction du nombre de cases
    public void movePlayer(String player, int numberOfSquares, JailManager jailManager) {
        if (!playerLocation.containsKey(player)) {
            throw new IllegalArgumentException("Player not found: " + player);
        }

        Location currentPlayerLocation = playerLocation.get(player);
        int currentIndex = boardManager.getSquareList().indexOf(currentPlayerLocation);

        int newIndex = (currentIndex + numberOfSquares) % boardManager.getSquareList().size();
        Location newLocation = boardManager.getSquareByIndex(newIndex);
        playerLocation.put(player, newLocation);

        hasPassedTroughTheStartBox(currentIndex, numberOfSquares);
        checkIfPlayerIsOnTaxSquare((LocationImplementation) newLocation);
        checkIfPlayerIsOnSquareGoToJail(newLocation, jailManager, numberOfSquares);
    }

    private void checkIfPlayerIsOnTaxSquare(LocationImplementation newLocation) {
        BigDecimal playerMoney = getPlayerBalance(currentPlayer);
        if(newLocation.getKind().equals(Location.LocationKind.TAX)){
            if(playerMoney.compareTo(newLocation.getPrice()) >= 0){
                playerBalance.replace(currentPlayer, playerBalance.get(currentPlayer).subtract(newLocation.getPrice()));
            }else{
                removePlayerFromTheGame(currentPlayer);
            }
        }
    }

    private void checkIfPlayerIsOnSquareGoToJail(Location newLocation, JailManager jailManager, int numberOfSquares){
        // Vérification pour voir si le joueur va en prison
        if (newLocation.getKind().equals(Location.LocationKind.GO_TO_JAIL)) {
            jailManager.sendPlayerToJail(currentPlayer);
            playerLocation.put(currentPlayer, boardManager.getSquareByIndex(10));
        } else if(newLocation.getOwner() != null){
            payRentLocation(currentPlayer, newLocation, numberOfSquares);
        }
    }

    private void hasPassedTroughTheStartBox(int currentIndex, int numberOfSquare) {
       if (currentIndex + numberOfSquare > boardManager.getSquareList().size()){
           BigDecimal bonus = new BigDecimal((currentIndex + numberOfSquare) / boardManager.getSquareList().size());
           playerBalance.replace(currentPlayer, playerBalance.get(currentPlayer).add(bonus.multiply(BigDecimal.valueOf(200))));
       }
    }

    // Méthode pour récupérer l'emplacement actuel d'un joueur
    public Location getPlayerLocation(String player) {
        return playerLocation.get(player);
    }

    // Méthode pour récupérer le solde d'un joueur
    public BigDecimal getPlayerBalance(String player) {
        return playerBalance.get(player);
    }

    public List<String> getPlayerIds(){
        return playerIds;
    }

    public Map<String, BigDecimal> getPlayersBalance(){
        return playerBalance;
    }

    // Méthode pour récupérer l'identifiant du joueur actuel
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    // Méthode pour passer au joueur suivant et en même temps vérifier le tour
    public boolean switchToNextPlayer(String playerName) {
        if (currentPlayer == null) {
            currentPlayer = playerIds.getFirst();
        } else {
            int currentPlayerIndex = playerIds.indexOf(currentPlayer);
            int nextPlayerIndex = (currentPlayerIndex + 1) % playerIds.size();
            String nextPlayer = playerIds.get(nextPlayerIndex);

            if(currentPlayer.equals(playerIds.get(playerIds.indexOf(playerName)))){
                currentPlayer = nextPlayer;
                return true;
            }
        }
        return false;
    }

    //Méthode pour acheter une case
    public void buyLocation(LocationImplementation location, String player, BigDecimal money) {
        if (!playerLocation.containsKey(player)) {
            throw new IllegalArgumentException("Player not found: " + player);
        }

        if ((location.getOwner() == null) && (money.compareTo(location.getPrice()) >= 0)) {
            // Le joueur peut acheter la propriété
            playerBalance.put(player, money.subtract(location.getPrice()));
            location.setOwner(player);
            if (location.getKind() == Location.LocationKind.STATION){
                if(playerStation.containsKey(player)){
                    playerStation.replace(player, playerStation.get(player) + 1);
                }else{
                    playerStation.put(player, 1);
                }
            }
        }
    }

    //Méthode pour payer une location à un autre joueur
    public void payRentLocation(String player, Location location, int score) {
        String owner = location.getOwner();
        RentCalculator rentCalculator = rentCalculatorFactory.createRentCalculator(location, playerStation);

        BigDecimal priceBD = rentCalculator.calculatePrice(location, owner, score);
        playerBalance.replace(player, playerBalance.get(player).subtract(priceBD));
        playerBalance.replace(owner, playerBalance.get(owner).add(priceBD));
        checkPlayerBalance(player, owner);
    }

    // Méthode pour vérifier si le joueur à assez d'argent
    public void checkPlayerBalance(String playerName, String owner){
        BigDecimal moneyPlayer = playerBalance.get(playerName);
        if (moneyPlayer.compareTo(BigDecimal.ZERO) <= 0) {
            playerBalance.replace(owner, playerBalance.get(owner).subtract(playerBalance.get(playerName).abs()));
            removePlayerFromTheGame(playerName);
        }
    }

    public void removePlayerFromTheGame(String playerName){
        playerBalance.remove(playerName);
        playerLocation.remove(playerName);
        playerIds.remove(playerName);
    }

    public void buildOnSquare(Object propertyNameObj, String playerName){
        String propertyName = propertyNameObj.toString();
        boolean canBuild = true;
        LocationImplementation locationToBuild = (LocationImplementation) boardManager.getSquareByName(propertyName);
        List<LocationImplementation> locImplList = boardManager.getSquareByColor(locationToBuild.getColor());
        for (LocationImplementation loc : locImplList) {
            if (!Objects.equals(loc.getOwner(), playerName)) {
                canBuild = false;
                break;
            }
        }
        RentLocationImplementation rentLocationToBuild = boardManager.getSquareRentByLocation(locationToBuild);
        if (canBuild && (playerBalance.get(playerName).intValue() >= rentLocationToBuild.getCost())) {
            boolean upgradeLevel = boardManager.getSquareRentByLocation(locationToBuild).upgradeLevel();
            if (upgradeLevel) {
                playerBalance.replace(playerName, playerBalance.get(playerName).subtract(BigDecimal.valueOf(rentLocationToBuild.getCost())));
                locationToBuild.setPrice(BigDecimal.valueOf(rentLocationToBuild.getPrice()));
            }
        }

    }

    public Map<String, Location> getPlayersLocation() {
        return playerLocation;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}


