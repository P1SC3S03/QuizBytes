package academy.mindswap.server.singleplayer_commands;

import academy.mindswap.server.Server;

import java.io.IOException;

public interface CommandHandler {

    void execute(Server.PlayerHandler playerHandler) throws IOException, InterruptedException;
}
