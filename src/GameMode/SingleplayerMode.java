package GameMode;

public class SingleplayerMode implements GameMode {

    private Server.PlayerHandler playerHandler;

    public SingleplayerMode(Server.PlayerHandler playerHandler){
        this.playerHandler = playerHandler;
    }

    @Override
    public void execute() {

    }
}
