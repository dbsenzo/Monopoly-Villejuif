package fr.arsenelapostolet.efrei.monopoly;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseMultiplayerMonopolyGameTests {

    private final Dices fakeDices = mock(Dices.class);
    public abstract GameServer createServer();
    public abstract GameClient createClient();

    @Test
    public void startGameAndPlayOneTurn() throws IOException, InterruptedException {
        // Given
        when(fakeDices.throwTwoSixSidedDices())
                .thenReturn(3);

        final var player1outPut = new StringWriter();
        final var player2outPut = new StringWriter();

        final var server = createServer();
        server.start(2, 5000, fakeDices);

        final var client1 = createClient();
        final var client2 = createClient();

        client1.start("localhost", 5000, new PrintWriter(player1outPut));
        client2.start("localhost", 5000, new PrintWriter(player2outPut));

        Thread.sleep(2000);

        // When
        client1.sendTextToServer("player1");
        Thread.sleep(500);
        client2.sendTextToServer("player2");
        Thread.sleep(500);

        client1.sendTextToServer("buy");
        Thread.sleep(500);
        client2.sendTextToServer("idle");
        Thread.sleep(500);

        client1.sendTextToServer("idle");
        Thread.sleep(500);
        client2.sendTextToServer("idle");
        Thread.sleep(500);


        // Then
        final var player1outPutResult = player1outPut.toString();
        final var player2outPutResult = player2outPut.toString();

        assertThat(player1outPutResult).isNotEmpty();
        assertThat(player2outPutResult).isNotEmpty();
        assertThat(player1outPutResult).isEqualTo(player2outPutResult);

        final var expectedOutput = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/expected_player_output.txt"))
        )
                .lines()
                .collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator();
        assertThat(player1outPutResult).isEqualTo(expectedOutput);
        assertThat(player2outPutResult).isEqualTo(expectedOutput);
    }

}
