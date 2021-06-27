package academy.mindswap.server.singleplayer_commands;

import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;
import academy.mindswap.server.util.Messages;

import java.io.IOException;
import java.util.LinkedList;

public abstract class DifficultyMode {
    private int difficultyScore;
    private int correctAnswers;

    public DifficultyMode(int difficultyScore, int correctAnswers) {
        this.difficultyScore = difficultyScore;
        this.correctAnswers = correctAnswers;
    }

    public void runQuestions(Server.PlayerHandler playerHandler, LinkedList<Question> questions)
            throws IOException, InterruptedException {

        for (Question question : questions) {
            playerHandler.send(question.toString());
            String answer = playerHandler.getIn().readLine();

            if (answer.equals("/menu") || answer.equals("/quit")) {
                playerHandler.dealWithCommand(answer);
                return;
            }
            if (answer.equals("/support")) {
                playerHandler.dealWithCommand(answer);
                continue;
            }
            if (answer.equals(question.getCorrectAnswer())) {
                playerHandler.setPlayerScore(playerHandler.getPlayerScore() + difficultyScore);
                playerHandler.send(Messages.CORRECT_ANSWER);
                correctAnswers++;
            } else {
                if (playerHandler.isCommand(answer)) {
                    playerHandler.dealWithCommand(answer);
                }
                playerHandler.send(Messages.INCORRECT_ANSWER);
            }
        }
        playerHandler.send("Your final score is " + playerHandler.getPlayerScore() + ".\n" + "You got " + correctAnswers
                + " correct answers out of " + questions.size() + "!\n");
        Thread.sleep(3000);
        playerHandler.send(Messages.MAIN_MENU);
    }

    public LinkedList<Question> loadQuestions(String path) throws IOException {
        FilesLoad fileReader = new FilesLoad();
        LinkedList<Question> questions = fileReader.LoadQuestionsFromFile(path);
        return questions;
    }
}