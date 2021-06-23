package GameMode;

public class MultiplayerMode implements GameMode {

    private Server.PlayerHandler playerHandler;

    public MultiplayerMode(Server.PlayerHandler playerHandler){
        this.playerHandler = playerHandler;
    }

    @Override
    public void execute() {

    }
}
