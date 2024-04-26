package fr.bouhamididesbois.efrei.monopoly.simulation.implementation;

import fr.arsenelapostolet.efrei.monopoly.Location;

import java.math.BigDecimal;

public class LocationImplementation implements Location {
    private final String name;
    private final LocationKind kind;
    private String owner;
    private final String color;
    private BigDecimal price;

    public LocationImplementation(String name, LocationKind kind, String color, int price) {
        this.name = name;
        this.kind = kind;
        this.color = color;
        this.price = new BigDecimal(price);
        this.owner = null;
    }

    public LocationImplementation(String name, LocationKind kind){
        this.name = name;
        this.kind = kind;
        this.color = null;
        this.price = null;
        this.owner = null;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getColor(){ return color; }

    public BigDecimal getPrice(){ return price;}

    @Override
    public LocationKind getKind() {
        return kind;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

}
