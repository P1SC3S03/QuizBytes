package academy.mindswap.server.singleplayer_commands;

import academy.mindswap.server.Server;

import java.io.IOException;

public class HardDifficultyMode extends DifficultyMode implements CommandHandler {

    public HardDifficultyMode() {
        super(10, 0);
    }

    @Override
    public void execute(Server.PlayerHandler playerHandler) throws IOException, InterruptedException {
        runQuestions(playerHandler, loadQuestions("resources/hard.txt"));
    }
}


