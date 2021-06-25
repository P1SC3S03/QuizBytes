package academy.mindswap.server.commandsmode;

import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;
import academy.mindswap.server.util.Messages;

import java.io.IOException;
import java.util.LinkedList;

public class EasyMode implements CommandHandler {
    private final int difficultyScore = 1;

    @Override
    public void execute(Server server, Server.PlayerHandler playerHandler) throws IOException {
        runQuestions(server, playerHandler, loadEasyQuestions());
    }

    public LinkedList<Question> loadEasyQuestions() throws IOException {
        FilesLoad fileReader = new FilesLoad();
        LinkedList<Question> questions = fileReader.LoadQuestionsFromFile("resources/easy.txt");
        return questions;
    }

    public void runQuestions(Server server, Server.PlayerHandler playerHandler, LinkedList<Question> questions)
            throws IOException {

        for (Question question : questions) {
            playerHandler.send(question.toString());
            String answer = playerHandler.getIn().readLine();
            if (answer.equals(question.getCorrectAnswer())) {
                playerHandler.setPlayerScore(playerHandler.getPlayerScore() + difficultyScore);
                playerHandler.send(Messages.CORRECT_ANSWER);
                playerHandler.send(("" + playerHandler.getPlayerScore()));
            } else {
                playerHandler.send(Messages.INCORRECT_ANSWER);
            }
        }
        playerHandler.send("Your score is " + playerHandler.getPlayerScore() + " out of"
                + questions.size() + "!");
    }
}
