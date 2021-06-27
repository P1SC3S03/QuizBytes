package academy.mindswap.server.singleplayer_commands;

import academy.mindswap.server.Server;

import java.io.IOException;

/**
 * Command Handler Interface
 * Created so that the current and future difficulty modes within the SinglePlayer can be used in a polymorphic way.
 */
public interface CommandHandler {
    /**
     * Forces the implementation of the execute method by the implementing classes.
     * Those currently are:
     * EasyDifficultyMode and HardDifficultyMode
     *
     * @param playerHandler - Player Handler who needs to execute the modes.
     * @throws IOException          If an input or output exception occurs
     * @throws InterruptedException If an interruption exception occurs
     */
    void execute(Server.PlayerHandler playerHandler) throws IOException, InterruptedException;
}

