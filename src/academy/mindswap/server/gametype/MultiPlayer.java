package academy.mindswap.server.gametype;

import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;
import academy.mindswap.server.util.Messages;
import academy.mindswap.server.util.Timer;


import java.io.IOException;
import java.util.LinkedList;

/**
 * MultiPlayer Class
 */
public class MultiPlayer {
    private LinkedList<Question> questions;
    private FilesLoad fileReader;
    private int difficultyScore;
    private final int maxEasyCorrectScore = 3;
    private final int maxHardCorrectScore = 6;
    private int answeredQuestions;
    private int correctAnsweredQuestions;
    private boolean didHitMe;
    private boolean didSteal;

    public MultiPlayer() {
        questions = new LinkedList<>();
        fileReader = new FilesLoad();
    }

    /**
     * Creates a thread for the timer. Depending on the player score, loads the correct level of difficulty list of
     * questions and calls for the askQuestions method, verifying the level change conditions.
     *
     * @param server        - server needed in the askQuestions method.
     * @param playerHandler - player handler needed to send messages to the player and get its score.
     * @throws IOException          If an input or output exception occurs
     * @throws InterruptedException If an interruption exception occurs
     */
    public void play(Server server, Server.PlayerHandler playerHandler) throws IOException, InterruptedException {

        Timer timer = new Timer(1, playerHandler.getPlayerSocket());
        Thread timerThread = new Thread(timer);
        timerThread.start();

        while (timer.getSeconds() > 0) {

            if (playerHandler.getPlayerScore() < maxEasyCorrectScore) {
                loadEasyQuestions();
                difficultyScore = 1;
                askQuestions(timer, server, playerHandler, difficultyScore, maxEasyCorrectScore);
            } else if (playerHandler.getPlayerScore() < maxHardCorrectScore) {
                loadHardQuestions();
                difficultyScore = 2;
                playerHandler.send(Messages.LEVEL_CHANGE);
                Thread.sleep(5000);
                timer.setSeconds(timer.getSeconds() + 5);
                askQuestions(timer, server, playerHandler, difficultyScore, maxHardCorrectScore);
            } else {
                loadInsaneQuestions();
                difficultyScore = 5;
                int insaneScore = Integer.MAX_VALUE;
                playerHandler.send(Messages.INSANE_MODE);
                Thread.sleep(7000);
                timer.setSeconds(timer.getSeconds() + 7);
                askQuestions(timer, server, playerHandler, difficultyScore, insaneScore);
            }
        }
        gameOver(server, playerHandler);
    }

    /**
     * Asks the player a given set of questions and deals with both answers [correct and incorrect] and commands.
     *
     * @param timer           - timer needed to get the time left in the game and set timer in proportion to the thread sleep.
     * @param server          - needed to the header method.
     * @param playerHandler   - needed to send messages to the player and deal with commands.
     * @param difficultyScore - number of points for each correct answer giving in accordance to the difficulty of that
     *                        same question.
     * @param maxCorrectScore - maximum amount of correct points needed to change difficulty level of questions.
     * @throws IOException          If an input or output exception occurs
     * @throws InterruptedException If an interruption exception occurs
     */
    public void askQuestions(Timer timer, Server server, Server.PlayerHandler playerHandler, int difficultyScore,
                             int maxCorrectScore) throws IOException, InterruptedException {
        for (Question question : questions) {

            while (timer.getSeconds() > 0) {

                String questionBuild = question.toString() + "\n";
                header(timer, server, playerHandler);
                playerHandler.send(questionBuild);

                String answer = playerHandler.getIn().readLine();
                Thread.sleep(10);
                answeredQuestions++;

                switch (answer) {
                    case "/quit":
                        didHitMe = false;
                        didSteal = false;
                        playerHandler.dealWithCommand(answer);
                        return;
                    case "/support":
                        playerHandler.dealWithCommand(answer);
                        timer.setSeconds(timer.getSeconds() + 3);
                        break;
                    case "/hit me":
                        if (!didHitMe) {
                            playerHandler.dealWithCommand(answer);
                            timer.setSeconds(timer.getSeconds() + 5);
                            playerHandler.setPlayerScore(playerHandler.getPlayerScore() + randomBonusPoints());
                            didHitMe = true;
                        } else {
                            playerHandler.send(Messages.GREEDY);
                            Thread.sleep(3000);
                            timer.setSeconds(timer.getSeconds() + 3);
                        }
                        break;
                    case "/steal":
                        if (!didSteal) {
                            playerHandler.dealWithCommand(answer);
                            timer.setSeconds(timer.getSeconds() + 3);
                            playerHandler.setPlayerScore(playerHandler.getPlayerScore()
                                    + server.stealFromRandomPlayer(playerHandler));
                            didSteal = true;
                            if (playerHandler.getPlayerScore() >= maxCorrectScore) {
                                return;
                            }
                        } else {
                            playerHandler.send(Messages.GREEDY);
                            Thread.sleep(3000);
                            timer.setSeconds(timer.getSeconds() + 3);
                        }
                        break;
                }

                if (answer.equals(question.getCorrectAnswer())) {
                    playerHandler.setPlayerScore(playerHandler.getPlayerScore() + difficultyScore);
                    correctAnsweredQuestions++;
                    playerHandler.send(Messages.CORRECT_ANSWER);
                    Thread.sleep(1000);
                    timer.setSeconds(timer.getSeconds() + 1);

                    if (playerHandler.getPlayerScore() >= maxCorrectScore) {
                        return;
                    }
                } else {
                    if (!answer.equals("") && !answer.equals("/steal") && !answer.equals("/hit me")
                            && !answer.equals("/support")) {
                        playerHandler.send(Messages.INCORRECT_ANSWER);
                        Thread.sleep(1000);
                        timer.setSeconds(timer.getSeconds() + 1);
                    }
                }
                break;
            }
        }
    }

    /**
     * Sends the final message of the game containing the final score, the number of correct answers and the total
     * answers that he was given during the time set.
     * Adds final players score to the score list and sets the highest score, resets booleans and removes the given
     * player handler from the multiplayer list and prompts it back to the main menu.
     *
     * @param server        - server needed to add the player's score to the score list, define highscore and remove
     *                      player handler from the multiplayer list.
     * @param playerHandler - player handler needed to prompt user back with main menu.
     * @throws InterruptedException If an interruption exception occurs
     */
    public void gameOver(Server server, Server.PlayerHandler playerHandler) throws InterruptedException {
        playerHandler.send("Your final score is " + playerHandler.getPlayerScore() + ".\n" + "You got "
                + correctAnsweredQuestions + " correct answers out of " + answeredQuestions + "!\n");
        Thread.sleep(3000);
        server.addScoreToList(playerHandler);
        server.defineHighestScore();
        didHitMe = false;
        didSteal = false;
        server.removePlayerHandlerFromMultiPlayerList(playerHandler);
        playerHandler.send(Messages.MAIN_MENU);
    }

    /**
     * Creates a header with a graphical interface for each question containing the player's score, the time left to the
     * end of the multiplayer game and the top/highest player score.
     *
     * @param timer         - timer needed to get the time left in the game
     * @param server        - server needed to get the top player's score
     * @param playerHandler - player handler needed to get the player's score
     */
    public void header(Timer timer, Server server, Server.PlayerHandler playerHandler) {
        playerHandler.send("--------------------------------------------------------------------------");
        playerHandler.send("Your Score: " + playerHandler.getPlayerScore() + "         Time left: " + timer.getSeconds()
                + "         Top Player Score: " + server.getHighestScore());
        playerHandler.send("--------------------------------------------------------------------------");
    }

    /**
     * Loads a list of easy questions from a file path and returns it.
     *
     * @return a list of easy questions
     * @throws IOException If an input or output exception occurs
     */
    private LinkedList loadEasyQuestions() throws IOException {
        questions = fileReader.LoadQuestionsFromFile("resources/easy.txt");
        return questions;
    }

    /**
     * Loads a list of hard questions from a file path and returns it.
     *
     * @return a list of hard questions
     * @throws IOException If an input or output exception occurs
     */
    private LinkedList loadHardQuestions() throws IOException {
        questions = fileReader.LoadQuestionsFromFile("resources/hard.txt");
        return questions;
    }

    /**
     * Loads a list of insane questions from a file path and returns it.
     *
     * @return a list of insane questions
     * @throws IOException If an input or output exception occurs
     */
    private LinkedList loadInsaneQuestions() throws IOException {
        questions = fileReader.LoadQuestionsFromFile("resources/insane.txt");
        return questions;
    }

    /**
     * Creates a random number of points using the Math random method
     *
     * @return a random number of points from 1 to 5
     */
    private int randomBonusPoints() {
        int min = 1;
        int max = 6;

        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
















