package academy.mindswap.server.gametype;

import academy.mindswap.server.Server;
import academy.mindswap.server.questions.Question;
import academy.mindswap.server.util.FilesLoad;
import academy.mindswap.server.util.Messages;
import academy.mindswap.server.util.Timer;


import java.io.IOException;
import java.util.LinkedList;

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

    public void askQuestions(Timer timer, Server server, Server.PlayerHandler playerHandler, int difficultyScore, int maxCorrectScore) throws IOException, InterruptedException {
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
    //TODO definir quantas questões
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

    public void header(Timer timer, Server server, Server.PlayerHandler playerHandler) {
        playerHandler.send("--------------------------------------------------------------------------");
        playerHandler.send("Your Score: " + playerHandler.getPlayerScore() + "          Time left: " + timer.getSeconds() + "          Top Player Score: " + server.getHighestScore());
        playerHandler.send("--------------------------------------------------------------------------");
    }

    private LinkedList loadEasyQuestions() throws IOException {
        questions = fileReader.LoadQuestionsFromFile("resources/easy.txt");
        return questions;
    }

    private LinkedList loadHardQuestions() throws IOException {
        questions = fileReader.LoadQuestionsFromFile("resources/hard.txt");
        return questions;
    }

    private LinkedList loadInsaneQuestions() throws IOException {
        questions = fileReader.LoadQuestionsFromFile("resources/insane.txt");
        return questions;
    }

    private int randomBonusPoints() {
        int min = 1;
        int max = 6;

        return (int) (Math.random() * (max - min + 1)) + min;
    }
}
















