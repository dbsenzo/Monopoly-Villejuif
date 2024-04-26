package fr.bouhamididesbois.efrei.monopoly.simulation.implementation;

import fr.arsenelapostolet.efrei.monopoly.Location;

public class RentLocationImplementation {
    private final String property;

    private final int cost;
    private final int[] levels;
    private int currentLevel;
    private int price;

    private final Location location;

    public RentLocationImplementation(String property, int cost, int[] levels, int bare, Location location){
        this.property = property;
        this.cost = cost;
        this.levels = levels;
        this.location = location;
        this.currentLevel = 0;
        this.price = bare;
    }

    public String getProperty() {
        return property;
    }

    public int getCost() {
        return cost;
    }

    public Location getLocation(){
        return this.location;
    }

    public int getPrice(){
        return price;
    }

    public boolean upgradeLevel(){
        if (currentLevel < 5){
            currentLevel++;
            price = levels[currentLevel-1];
            return true;
        }else{
            return false;
        }
    }

}
