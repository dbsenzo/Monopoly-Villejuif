package fr.bouhamididesbois.efrei.monopoly.simulation.implementation;

import org.junit.jupiter.api.Test;

import java.util.random.RandomGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DiceTest {

    @Test
    public void testDice() {
        RandomGenerator randomMock = mock(RandomGenerator.class);
        when(randomMock.nextInt(6)).thenReturn(5);
        DiceImplementation d = new DiceImplementation(randomMock);
        assertEquals(10, d.throwTwoSixSidedDices());
    }
}
