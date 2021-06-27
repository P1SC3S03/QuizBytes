package academy.mindswap.server.singleplayer_commands;

import academy.mindswap.server.Server;

import java.io.IOException;

/**
 * The Hard Difficulty Mode is a Sub class of Difficulty Mode and implements the Command Handler interface.
 */
public class HardDifficultyMode extends DifficultyMode implements CommandHandler {
    /**
     * Constructor for the Hard Difficulty Mode
     * Sets the difficulty score of the easy mode to 10 and starts the amount of correct answers with 0
     */
    public HardDifficultyMode() {
        super(10, 0);
    }

    /**
     * Implements the execute method from the interface and defines the path for the list of questions to be prompted
     * to the player using a given file path.
     *
     * @param playerHandler - Player Handler who needs to execute the modes.
     * @throws IOException          If an input or output exception occurs
     * @throws InterruptedException If an interruption exception occurs
     */
    @Override
    public void execute(Server.PlayerHandler playerHandler) throws IOException, InterruptedException {
        runQuestions(playerHandler, loadQuestions("resources/hard.txt"));
    }
}


