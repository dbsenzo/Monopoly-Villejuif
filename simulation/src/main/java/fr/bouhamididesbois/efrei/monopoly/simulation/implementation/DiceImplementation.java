package fr.bouhamididesbois.efrei.monopoly.simulation.implementation;
import fr.arsenelapostolet.efrei.monopoly.Dices;

import java.util.Random;
import java.util.random.RandomGenerator;

public class DiceImplementation implements Dices {

    private final RandomGenerator random;
    public DiceImplementation(RandomGenerator random){
        this.random = random;
    }

    @Override
    public int throwTwoSixSidedDices() {
        return random.nextInt(6)+random.nextInt(6);
    }
}
