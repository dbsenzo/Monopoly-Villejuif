package fr.arsenelapostolet.efrei.monopoly;

import java.io.IOException;
import java.io.PrintWriter;

public interface GameClient {

    void start(String ipAddress, int port, PrintWriter output) throws IOException;
    void sendTextToServer(String text);
}
