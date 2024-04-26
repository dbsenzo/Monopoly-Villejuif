package fr.bouhamididesbois.efrei.monopoly.simulation.implementation;

import fr.arsenelapostolet.efrei.monopoly.BaseMonopolyTests;
import fr.arsenelapostolet.efrei.monopoly.Dices;
import fr.arsenelapostolet.efrei.monopoly.Monopoly;

import java.util.List;

public class MonopolyTests extends BaseMonopolyTests {

    @Override
    public Monopoly createMonopoly(Dices dices, List<String> playerIds){

        return new MonopolyImplementation(playerIds, dices);
    }
}