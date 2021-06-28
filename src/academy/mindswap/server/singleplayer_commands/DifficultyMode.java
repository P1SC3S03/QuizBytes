package academy.mindswap.server.singleplayer_commands;

import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;
import academy.mindswap.server.util.Messages;

import java.io.IOException;
import java.util.LinkedList;

/**
 * Abstract Class that is to be extended from all the Difficulty Mode classes.
 */
public abstract class DifficultyMode {
    private int difficultyScore;
    private int correctAnswers;

    /**
     * Constructor method for the Difficulty Mode Class
     *
     * @param difficultyScore - points that are going defined in the different sub classes
     * @param correctAnswers  - amount of the player's correct answers
     */
    public DifficultyMode(int difficultyScore, int correctAnswers) {
        this.difficultyScore = difficultyScore;
        this.correctAnswers = correctAnswers;
    }

    /**
     * Implements the logic of the question and answer and different commands in the different modes
     *
     * @param playerHandler - player handler to be used to be the connecting entity between the game and the player
     * @param questions     - list of questions to be used
     * @throws IOException          If an input or output exception occurs
     * @throws InterruptedException If an interruption exception occurs
     */
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
            if (answer.equals("/steal") || answer.equals("/hit me")) {
                playerHandler.send(Messages.SINGLEPLAYER_COMMAND_RESTRICTION);
                Thread.sleep(5000);
                continue;
            }
            if (answer.equals(question.getCorrectAnswer())) {
                playerHandler.setPlayerScore(playerHandler.getPlayerScore() + difficultyScore);
                playerHandler.send(Messages.CORRECT_ANSWER);
                Thread.sleep(500);
                correctAnswers++;
            } else{
                playerHandler.send(Messages.INCORRECT_ANSWER);
                Thread.sleep(500);
            }
        }
        playerHandler.send("Your final score is " + playerHandler.getPlayerScore() + ".\n" + "You got " + correctAnswers
                + " correct answers out of " + questions.size() + "!\n");
        Thread.sleep(3000);
        playerHandler.send(Messages.MAIN_MENU);
    }

    /**
     * Loads the question list from the Files Load class using a given file path and returns a list of questions.
     *
     * @param path - file path
     * @return the list of questions
     * @throws IOException If an input or output exception occurs
     */
    public LinkedList<Question> loadQuestions(String path) throws IOException {
        FilesLoad fileReader = new FilesLoad();
        LinkedList<Question> questions = fileReader.LoadQuestionsFromFile(path);
        return questions;
    }
}