package academy.mindswap.server.commands;

import academy.mindswap.server.Server;

import java.io.IOException;

public interface CommandHandler {

    void execute(Server server, Server.PlayerHandler playerHandler) throws IOException, InterruptedException;
}
