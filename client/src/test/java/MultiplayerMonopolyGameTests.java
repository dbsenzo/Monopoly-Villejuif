import fr.arsenelapostolet.efrei.monopoly.BaseMultiplayerMonopolyGameTests;
import fr.arsenelapostolet.efrei.monopoly.GameClient;
import fr.arsenelapostolet.efrei.monopoly.GameServer;
import fr.bouhamididesbois.efrei.monopoly.MonopolyClient;
import fr.bouhamididesbois.efrei.monopoly.MonopolyServer;

public class MultiplayerMonopolyGameTests extends BaseMultiplayerMonopolyGameTests {
    @Override
    public GameServer createServer() {
        return new MonopolyServer();
    }

    @Override
    public GameClient createClient() {
        return new MonopolyClient();
    }
}
