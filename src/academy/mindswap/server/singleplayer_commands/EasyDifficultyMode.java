package academy.mindswap.server.singleplayer_commands;

import academy.mindswap.server.Server;

import java.io.IOException;

public class EasyDifficultyMode extends DifficultyMode implements CommandHandler {

    public EasyDifficultyMode() {
        super(1, 0);
    }

    @Override
    public void execute(Server.PlayerHandler playerHandler) throws IOException, InterruptedException {
        runQuestions(playerHandler, loadQuestions("resources/easy.txt"));
    }
}