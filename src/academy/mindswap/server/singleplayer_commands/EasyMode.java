package academy.mindswap.server.singleplayer_commands;

import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;
import academy.mindswap.server.util.Messages;

import java.io.IOException;
import java.util.LinkedList;

public class EasyMode implements CommandHandler {
    private final int difficultyScore = 1;
    private int correctAnswers;

    @Override
    public void execute(Server server, Server.PlayerHandler playerHandler) throws IOException, InterruptedException {
        runQuestions(server, playerHandler, loadEasyQuestions());
    }

    public LinkedList<Question> loadEasyQuestions() throws IOException {
        FilesLoad fileReader = new FilesLoad();
        LinkedList<Question> questions = fileReader.LoadQuestionsFromFile("resources/easy.txt");
        return questions;
    }

    public void runQuestions(Server server, Server.PlayerHandler playerHandler, LinkedList<Question> questions)
            throws IOException, InterruptedException {

        for (Question question : questions) {
            playerHandler.send(question.toString());
            String answer = playerHandler.getIn().readLine();

            if(answer.equals("/menu") || answer.equals("/quit")){
                playerHandler.dealWithCommand(answer);
                return;
            }
            if (answer.equals("/support")){
                playerHandler.dealWithCommand(answer);
                continue;
            }
            if (answer.equals(question.getCorrectAnswer())) {
                playerHandler.setPlayerScore(playerHandler.getPlayerScore() + difficultyScore);
                playerHandler.send(Messages.CORRECT_ANSWER);
                correctAnswers++;
            } else {

                playerHandler.send(Messages.INCORRECT_ANSWER);
            }
        }
        playerHandler.send("Your final score is " + playerHandler.getPlayerScore() + ".\n" + "You got " + correctAnswers
                + " correct answers out of " + questions.size() + "!");
    }
}