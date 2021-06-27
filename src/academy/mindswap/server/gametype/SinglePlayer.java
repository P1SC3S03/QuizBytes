package academy.mindswap.server.gametype;

import academy.mindswap.server.Server;
import academy.mindswap.server.singleplayer_commands.Command;
import academy.mindswap.server.util.Messages;

import java.io.IOException;

public class SinglePlayer {

    public void play(Server.PlayerHandler playerHandler) throws IOException, InterruptedException {
        playerHandler.send(Messages.SINGLEPLAYER_MENU);
        String mode = playerHandler.getIn().readLine();

        Command command = Command.getCommandFromMode(mode);

        if (command == null) {
            playerHandler.send(Messages.INVALID_OPERATION_OR_COMMAND);   //RETURN????
        }
        command.getHandler().execute(playerHandler);
    }
}